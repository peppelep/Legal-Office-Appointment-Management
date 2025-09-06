import { Component, OnInit } from '@angular/core';
import { authCodeFlowConfig } from "../../sso-config";
import { JwksValidationHandler, OAuthService } from "angular-oauth2-oidc";
import { Router } from "@angular/router";
import { Appuntamento } from "../../model/appuntamento";
import { AppuntamentoService } from "../../../service/appuntamento.service";
import {jwtDecode} from 'jwt-decode';
import { DomSanitizer } from "@angular/platform-browser";
import { EmailService } from 'src/service/email.service';
@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  name: string = "";
  appuntamenti: Appuntamento[] = [];

  iscrittoSnS: boolean = false;
  iscrittoSeS : boolean = false ;


  constructor(
    private OAuthService: OAuthService,
    private router: Router,
    private appuntamentoService: AppuntamentoService,
    private sanitizer: DomSanitizer,
    private emailService : EmailService,
  ) {}

  ngOnInit(): void {
    this.configureSingleSignOn();
    const userClaims: any = this.OAuthService.getIdentityClaims();
    this.name = userClaims.email ? userClaims.email : "";
    this.isSubscribedSnS();
    this.isSubscribedSeS();
    this.getAllAppuntamenti();

  }

  configureSingleSignOn(): void {
    this.OAuthService.configure(authCodeFlowConfig);
    this.OAuthService.tokenValidationHandler = new JwksValidationHandler();
    this.OAuthService.loadDiscoveryDocumentAndTryLogin();

  }

  isSubscribedSnS() : void {
    this.emailService.isSubscribed(this.name).subscribe(
      (isSubscribed: boolean) => {
        this.iscrittoSnS = isSubscribed;
        console.log(`Is user ${this.name} subscribed: ${isSubscribed}`);
      },
      (error) => {
        console.error(`Errore durante la verifica dell'iscrizione per l'utente ${this.name}:`, error);
      }
    );
  }

  isSubscribedSeS() : void {
    this.emailService.isSubscribedSeS(this.name).subscribe(
      (isSubscribedSeS: boolean) => {
        this.iscrittoSeS = isSubscribedSeS;
        console.log(`Is user ${this.name} subscribed: ${isSubscribedSeS}`);
      },
      (error) => {
        console.error(`Errore durante la verifica dell'iscrizione per l'utente ${this.name}:`, error);
      }
    );
  }

  getAllAppuntamenti(): void {
    this.appuntamentoService.getAllAppuntamenti().subscribe(
      (appuntamenti: Appuntamento[]) => {
        const now = new Date();
        this.appuntamenti = appuntamenti.filter(app => {
          const appDate = new Date(app.data);
          const isExpired = appDate < now;
          if (isExpired) {
            this.removeAppuntamento(app.id);
          }
          return !isExpired;
        });
      },
      (error) => {
        console.log(error);
      }
    );
  }

  removeAppuntamento(appuntamentoId: number): void {
    console.log(this.name)
    this.appuntamentoService.removeAppuntamento(appuntamentoId).subscribe(
      () => {
        console.log(`Appuntamento con id ${appuntamentoId} rimosso.`);
      },
      (error) => {
        console.error(`Errore durante la rimozione dell'appuntamento con id ${appuntamentoId}:`, error);
      }
    );
  }

  registrazioneSnS() {
    this.emailService.registrazione(this.name).subscribe(
      (response: any) => {
        console.log("Registrazione effettuata con successo");
      },
      (error) => {
        console.error('Errore durante la registrazione:', error);
      }
    );
  }

  registrazioneSeS() {
    this.emailService.registrazioneSeS(this.name).subscribe(
      (response: any) => {
        console.log("Registrazione effettuata con successo");
      },
      (error) => {
        console.error('Errore durante la registrazione:', error);
      }
    );
  }

}
