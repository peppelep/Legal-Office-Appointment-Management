import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { OAuthService } from 'angular-oauth2-oidc';
import { AppuntamentoService } from 'src/service/appuntamento.service';
import { PrenotazioneService } from 'src/service/prenotazione.service';
import { Appuntamento } from '../../model/appuntamento';
import { Prenotazione, StatoPrenotazione } from '../../model/prenotazione';
import { DocumentiService } from 'src/service/documenti.service';
import { MatCard } from '@angular/material/card';

@Component({
  selector: 'app-prenota-appuntamento',
  templateUrl: './prenota-appuntamento.component.html',
  styleUrls: ['./prenota-appuntamento.component.css']
})
export class PrenotaAppuntamentoComponent implements OnInit {
  appuntamentoId: number = 0;
  appuntamento: Appuntamento | null = null;

  alreadyBooked: boolean = false;

  prenotazione: Prenotazione = {
    id: 0,
    appuntamento: 0,
    usernameCliente: '',
    documento:'',
    stato: StatoPrenotazione.CONFERMATA, // Puoi impostare lo stato iniziale come preferisci
    tipoAppuntamento : '',
    ora : new Date(),
    data : new Date(),
    nomeAvvocato : '',
    postoInCoda : 0
  };

  errorMessage: string = '';
  selectedFile: File | null = null; // Campo per il file selezionato
  selectedFile2: File | null = null; // Campo per il file selezionato
  selectedFile3: File | null = null; // Campo per il file selezionato
  selectedFile4: File | null = null; // Campo per il file selezionato



  constructor(
    private route: ActivatedRoute,
    private appuntamentoService: AppuntamentoService,
    private oauthService: OAuthService,
    private prenotazioniService: PrenotazioneService ,
    private documentiService: DocumentiService
  ) {}

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.appuntamentoId = +params['id'];
      this.getProductDetails();
    });
  }

  getProductDetails() {
    this.appuntamentoService.getAppuntamentoById(this.appuntamentoId).subscribe(
      (appuntamento: Appuntamento) => {
        this.appuntamento = appuntamento;
        let claims: any = this.oauthService.getIdentityClaims();
        let name = claims.email;
        this.prenotazione.usernameCliente = name;
        this.prenotazione.appuntamento = this.appuntamentoId;
        this.prenotazione.tipoAppuntamento = this.appuntamento.tipo ;
        this.prenotazione.ora = appuntamento.ora ;
        this.prenotazione.data = appuntamento.data ;
        this.prenotazione.nomeAvvocato = appuntamento.nomeAvvocato ;
        //this.prenotazione.postoInCoda = appuntamento.utentiInAttesa+1 ;
        this.checkIfAlreadyBooked(name, this.appuntamentoId); // Chiama il metodo per controllare la prenotazione
      },
      (error) => {
        console.log(error);
      }
    );
  }

  checkIfAlreadyBooked(username: string, appuntamentoId: number) {
    this.prenotazioniService.getPrenotazioniByAppuntamento(appuntamentoId).subscribe(
      (prenotazioni: Prenotazione[]) => {
        this.alreadyBooked = prenotazioni.some(p => p.usernameCliente === username);
      },
      (error) => {
        console.error('Errore durante il controllo della prenotazione:', error);
      }
    );
  }

  prenota() {
    if (this.appuntamento) {
      const now = new Date();
      const appuntamentoData = new Date(this.appuntamento.data);
      if (appuntamentoData < now) {
        this.errorMessage = 'Non è possibile prenotare un appuntamento scaduto.';
        return;
      }

      this.prenotazioniService.addPrenotazione(this.prenotazione).subscribe(
        (response) => {
          this.carica();
          console.log('Prenotazione effettuata con successo', response);
        },
        (error) => {
          console.log(error);
        }
      );
    } else {
      this.errorMessage = 'Dettagli dell\'appuntamento non disponibili.';
    }
  }

  // Nuovo metodo per gestire l'azione di accodamento
  accoda() {
    if (this.appuntamento) {
      // Implementa la logica per l'accodamento
      console.log('Accodamento effettuato con successo');
      // Puoi aggiungere ulteriori azioni come notifiche o salvataggio dello stato
    } else {
      this.errorMessage = 'Dettagli dell\'appuntamento non disponibili.';
    }
  }

  // Metodo per gestire il caricamento del documento
  onFileSelected(event: any) {
    const fileList: FileList | null = event.target.files;

    if (fileList && fileList.length > 0) {
      this.selectedFile = fileList[0];
      this.prenotazione.documento = this.selectedFile.name;
    } else {
      // Gestisci il caso in cui non sia stato selezionato alcun file
      console.error("Nessun file selezionato.");
    }
  }

  // Metodo per gestire il caricamento del documento
  onFileSelected2(event: any) {
    const fileList: FileList | null = event.target.files;

    if (fileList && fileList.length > 0) {
      this.selectedFile2 = fileList[0];
      this.prenotazione.documento = this.selectedFile2.name;
    } else {
      // Gestisci il caso in cui non sia stato selezionato alcun file
      console.error("Nessun file selezionato.");
    }
  }

  onFileSelected3(event: any) {
    const fileList: FileList | null = event.target.files;

    if (fileList && fileList.length > 0) {
      this.selectedFile3 = fileList[0];
      this.prenotazione.documento = this.selectedFile3.name;
    } else {
      // Gestisci il caso in cui non sia stato selezionato alcun file
      console.error("Nessun file selezionato.");
    }
  }

  onFileSelected4(event: any) {
    const fileList: FileList | null = event.target.files;

    if (fileList && fileList.length > 0) {
      this.selectedFile4 = fileList[0];
      this.prenotazione.documento = this.selectedFile4.name;
    } else {
      // Gestisci il caso in cui non sia stato selezionato alcun file
      console.error("Nessun file selezionato.");
    }
  }

  carica() {
    // Assicurati di avere un file caricato prima di inviare la richiesta al servizio
    if (this.selectedFile) {
      this.documentiService.carica(this.selectedFile,this.prenotazione.usernameCliente,this.prenotazione.tipoAppuntamento).subscribe(() => {
        console.log("File caricato con successo!");
      }, (error: any) => {
        console.error("Errore durante il caricamento del file:", error);
      });
    } else {
      console.error("Nessun file caricato.");
    }

    if (this.selectedFile2) {
      this.documentiService.carica(this.selectedFile2,this.prenotazione.usernameCliente,this.prenotazione.tipoAppuntamento).subscribe(() => {
        console.log("File caricato con successo!");
      }, (error: any) => {
        console.error("Errore durante il caricamento del file:", error);
      });
    } else {
      console.error("Nessun file caricato.");
    }

    if (this.selectedFile3) {
      this.documentiService.carica(this.selectedFile3,this.prenotazione.usernameCliente,this.prenotazione.tipoAppuntamento).subscribe(() => {
        console.log("File caricato con successo!");
      }, (error: any) => {
        console.error("Errore durante il caricamento del file:", error);
      });
    } else {
      console.error("Nessun file caricato.");
    }

    if (this.selectedFile4) {
      this.documentiService.carica(this.selectedFile4,this.prenotazione.usernameCliente,this.prenotazione.tipoAppuntamento).subscribe(() => {
        console.log("File caricato con successo!");
      }, (error: any) => {
        console.error("Errore durante il caricamento del file:", error);
      });
    } else {
      console.error("Nessun file caricato.");
    }
  }


}
