import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import {EMPTY, finalize, Observable} from 'rxjs';
import { Prenotazione } from 'src/app/model/prenotazione';

@Injectable({
  providedIn: 'root'
})
export class EmailService {
  private baseUrl = 'http://3.81.235.81:8081/email';
  constructor(private http: HttpClient) {}

  registrazione(email: string): Observable<void> {
    return this.http.post<void>(`${this.baseUrl}/registrazione`, email);
  }

  isSubscribed(email: string): Observable<boolean> {
    return this.http.get<boolean>(`${this.baseUrl}/isSubscribed`, {
      params: { email: email }
    });
  }

  registrazioneSeS(email: string): Observable<void> {
    return this.http.post<void>(`${this.baseUrl}/registrazioneSeS`, email);
  }

  isSubscribedSeS(email: string): Observable<boolean> {
    return this.http.get<boolean>(`${this.baseUrl}/isSubscribedSeS`, {
      params: { email: email }
    });
  }

}
