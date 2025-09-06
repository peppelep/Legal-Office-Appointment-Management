export interface Appuntamento {
    id : number;
    tipo : String;
    ora : Date;
    data : Date;
    dettagli : String;
    nomeAvvocato : String;
    postiDisponibili : number;
    utentiInAttesa : number;
}
