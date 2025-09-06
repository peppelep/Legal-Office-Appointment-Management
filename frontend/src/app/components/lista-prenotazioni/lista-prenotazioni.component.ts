import { Component } from '@angular/core';
import { Prenotazione, StatoPrenotazione } from 'src/app/model/prenotazione';
import { authCodeFlowConfig } from "../../sso-config";
import { JwksValidationHandler, OAuthService } from "angular-oauth2-oidc";
import { Router } from "@angular/router";
import { DomSanitizer } from "@angular/platform-browser";
import { PrenotazioneService } from "../../../service/prenotazione.service";
import { DocumentiService } from 'src/service/documenti.service';


@Component({
  selector: 'app-lista-prenotazioni',
  templateUrl: './lista-prenotazioni.component.html',
  styleUrls: ['./lista-prenotazioni.component.css']
})

export class ListaPrenotazioniComponent {
  name: string = "";
  prenotazioni: Prenotazione[] = [];

  constructor(private oAuthService: OAuthService, private router: Router,private documentoService : DocumentiService , private prenotazioneService: PrenotazioneService, private sanitizer: DomSanitizer) { }

  ngOnInit(): void {
    this.configureSingleSignOn();
    const userClaims: any = this.oAuthService.getIdentityClaims();
    this.name = userClaims.email ? userClaims.email : "";
    this.getPrenotazioni(this.name);
  }

  configureSingleSignOn() {
    this.oAuthService.configure(authCodeFlowConfig);
    this.oAuthService.tokenValidationHandler = new JwksValidationHandler();
    this.oAuthService.loadDiscoveryDocumentAndTryLogin();
  }

  getPrenotazioni(name: string) {
    this.prenotazioneService.getPrenotazioniByUsername(this.name).subscribe(
      (response: any[]) => {
        const now = new Date();
        this.prenotazioni = response.filter(p =>
          (new Date(p.data) > now ||
          (new Date(p.data).getDate() === now.getDate() && new Date(p.data).getHours() >= now.getHours())) &&
          p.stato !== StatoPrenotazione.IN_ATTESA
        );
      },
      (error) => {
        console.error('Errore durante il recupero delle prenotazioni:', error);
      }
    );
  }

  deletePrenotazione(id: number): void {
    this.elimina(id) ;

    this.prenotazioneService.removePrenotazione(id).subscribe(() => {
      console.log(`Prenotazione con id ${id} rimossa`);

      this.prenotazioni = this.prenotazioni.filter(prenotazione => prenotazione.id !== id);
    }, error => {
      console.error(`Errore nella rimozione della prenotazione con id ${id}:`, error);
    });
  }

  elimina(id : number) {
    this.documentoService.elimina(id).subscribe(() => {
      console.log("File eliminato con successo!");
      }, (error: any) => {
        console.error("Errore durante il caricamento del file:", error);
      });
  }

}
