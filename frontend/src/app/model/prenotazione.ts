// prenotazione.model.ts

export interface Prenotazione {
  id: number;
  appuntamento: number;
  usernameCliente: string;
  documento: string;
  stato: StatoPrenotazione;
  tipoAppuntamento : String;
  ora : Date;
  data : Date;
  nomeAvvocato : String;
  postoInCoda : number ;
}

export enum StatoPrenotazione {
  CONFERMATA = "CONFERMATA",
  IN_ATTESA = "IN_ATTESA"
}
