import { Component } from '@angular/core';
import {Appuntamento} from "../model/appuntamento";
import {Router} from "@angular/router";
import {AppuntamentoService} from "../../service/appuntamento.service";
import {DomSanitizer} from "@angular/platform-browser";
import {EmailService} from "../../service/email.service";
import {authCodeFlowConfig} from "../sso-config";
import {JwksValidationHandler} from "angular-oauth2-oidc";

@Component({
  selector: 'app-rimuovi-appuntamento',
  templateUrl: './rimuovi-appuntamento.component.html',
  styleUrls: ['./rimuovi-appuntamento.component.css']
})
export class RimuoviAppuntamentoComponent {
  appuntamenti: Appuntamento[] = [];

  constructor(
    private router: Router,
    private appuntamentoService: AppuntamentoService,
    private sanitizer: DomSanitizer,
    private emailService : EmailService,
  ) {}

  ngOnInit(): void {
    this.getAllAppuntamenti();
  }


  getAllAppuntamenti(): void {
    this.appuntamentoService.getAllAppuntamenti().subscribe(
      (appuntamenti: Appuntamento[]) => {
        const now = new Date();
        this.appuntamenti = appuntamenti.filter(app => new Date(app.data) >= now);
      },
      (error) => {
        console.log(error);
      }
    );
  }

  removeAppuntamento(appuntamentoId: number): void {
    this.appuntamentoService.removeAppuntamento(appuntamentoId).subscribe(
      () => {
        console.log(`Appuntamento con id ${appuntamentoId} rimosso.`);
        this.appuntamenti = this.appuntamenti.filter(a => a.id !== appuntamentoId);
      },
      (error) => {
        console.error(`Errore durante la rimozione dell'appuntamento con id ${appuntamentoId}:`, error);
      }
    );
  }
}
