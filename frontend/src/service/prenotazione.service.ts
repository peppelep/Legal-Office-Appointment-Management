import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {EMPTY, finalize, Observable} from 'rxjs';
import { Prenotazione } from 'src/app/model/prenotazione';

@Injectable({
  providedIn: 'root'
})
export class PrenotazioneService {
  private baseUrl = 'http://3.81.235.81:8081';
  private loadingProduct = false;

  constructor(private http: HttpClient) { }

  getAllPrenotazioni(): Observable<Prenotazione[]> {
    return this.http.get<Prenotazione[]>(`${this.baseUrl}/all`);
  }

  addPrenotazione(prenotazione: Prenotazione): Observable<Prenotazione> {
    return this.http.post<Prenotazione>(`${this.baseUrl}/add`,prenotazione);
  }

  removePrenotazione(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/remove/${id}`);
  }

  getPrenotazioniByUsername(name: string): Observable<any[]> {
    const url = `${this.baseUrl}/get/${name}`;
    return this.http.get<Prenotazione[]>(url);
  }

  getPrenotazioniByAppuntamento(appuntamentoId: number): Observable<Prenotazione[]> {
    const url = `${this.baseUrl}/byAppuntamento/${appuntamentoId}`;
    return this.http.get<Prenotazione[]>(url);
  }

}
