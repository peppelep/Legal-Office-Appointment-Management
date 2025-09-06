import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import {EMPTY, finalize, Observable} from 'rxjs';
import { Prenotazione } from 'src/app/model/prenotazione';

@Injectable({
  providedIn: 'root'
})
export class DocumentiService {
  private baseUrl = 'http://3.81.235.81:8081';

  constructor(private http: HttpClient) {}

  carica(file: File,nome:String,appuntamento:String): Observable<void> {
    const formData: FormData = new FormData();
    formData.append('file', file);
    formData.append('nome',nome.toString());
    formData.append('appuntamento',appuntamento.toString());

    // Imposta le intestazioni per indicare che si tratta di un upload di file
    const headers = new HttpHeaders();
    headers.append('Content-Type', 'multipart/form-data');
    headers.append('Accept', 'application/json');

    return this.http.post<void>(`${this.baseUrl}/documenti/carica`, formData, { headers: headers });
  }

  elimina(id : Number): Observable<void> {
    const formData: FormData = new FormData();
    formData.append('id',id.toString());

    // Imposta le intestazioni per indicare che si tratta di un upload di file
    //const headers = new HttpHeaders();
    //headers.append('Content-Type', 'multipart/form-data');
    //headers.append('Accept', 'application/json');

    //return this.http.post<void>(`${this.baseUrl}/documenti/carica`, formData, { headers: headers });
    return this.http.post<void>(`${this.baseUrl}/documenti/eliminaDocumenti`, formData);

  }

}
