import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { EMPTY, finalize, Observable } from 'rxjs';
import { Appuntamento } from 'src/app/model/appuntamento';

@Injectable({
  providedIn: 'root'
})
export class AppuntamentoService {
  private baseUrl = 'http://3.81.235.81:8081/appuntamenti';
  private loadingProduct = false;

  constructor(private http: HttpClient) { }

  getAllAppuntamenti(): Observable<Appuntamento[]> {
    return this.http.get<Appuntamento[]>(`${this.baseUrl}/all`);
  }

  addProduct(product: Appuntamento): Observable<Appuntamento> {
    return this.http.post<Appuntamento>(`${this.baseUrl}/add`, product);
  }

  removeAppuntamento(appuntamentoId: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/remove/${appuntamentoId}`);
  }

  getAppuntamentoById(appuntamentoId: number): Observable<Appuntamento> {
    if (this.loadingProduct) {
      return EMPTY;
    }
    this.loadingProduct = true;

    return this.http.get<Appuntamento>(`${this.baseUrl}/get/${appuntamentoId}`).pipe(
      finalize(() => {
        this.loadingProduct = false;
      })
    );
  }
}
