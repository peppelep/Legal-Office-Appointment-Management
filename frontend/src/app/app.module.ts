import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NavbarComponent } from './components/navbar/navbar.component';
import { HomeComponent } from './components/home/home.component';
import { WelcomeComponent } from './components/welcome/welcome.component';
import {HttpClient, HttpClientModule} from "@angular/common/http";
import {OAuthModule} from "angular-oauth2-oidc";

import { MatCardModule } from '@angular/material/card';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import { RingraziamentiComponent } from './components/ringraziamenti/ringraziamenti.component';
import {MatButtonModule} from "@angular/material/button";
import {MatIconModule} from "@angular/material/icon";
import { PaginaInizialeComponent } from './components/pagina-iniziale/pagina-iniziale.component';
import { ListaPrenotazioniComponent } from './components/lista-prenotazioni/lista-prenotazioni.component';
import { PrenotaAppuntamentoComponent } from './components/prenota-appuntamento/prenota-appuntamento.component';
import { InserisciAppuntamentoComponent } from './components/inserisci-appuntamento/inserisci-appuntamento.component';
import { AuthGuard } from './auth.guard';
import { AdminGuard } from './admin.guard';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { PrenotazioniInAttesaComponent } from './components/prenotazioni-in-attesa/prenotazioni-in-attesa.component';
import { InformazioniComponent } from './components/informazioni/informazioni.component';
import { Ringraziamenti2Component } from './components/ringraziamenti2/ringraziamenti2.component';
import { RimuoviAppuntamentoComponent } from './rimuovi-appuntamento/rimuovi-appuntamento.component';


@NgModule({
  declarations: [
    AppComponent,
    NavbarComponent,
    HomeComponent,
    WelcomeComponent,
    RingraziamentiComponent,
    PaginaInizialeComponent,
    ListaPrenotazioniComponent,
    PrenotaAppuntamentoComponent,
    InserisciAppuntamentoComponent,
    PrenotazioniInAttesaComponent,
    InformazioniComponent,
    Ringraziamenti2Component,
    RimuoviAppuntamentoComponent,
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    OAuthModule.forRoot(),
    AppRoutingModule,

    MatCardModule,
    FormsModule,
    ReactiveFormsModule,
    MatButtonModule,
    MatIconModule,
    BrowserAnimationsModule,
  ],
  providers: [AuthGuard, AdminGuard],
  bootstrap: [AppComponent]
})
export class AppModule { }
