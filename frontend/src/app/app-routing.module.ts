import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {HomeComponent} from "./components/home/home.component";
import {WelcomeComponent} from "./components/welcome/welcome.component";
import {AuthGuard} from "./auth.guard";
import {RingraziamentiComponent} from "./components/ringraziamenti/ringraziamenti.component";
import {Ringraziamenti2Component} from "./components/ringraziamenti2/ringraziamenti2.component";
import { ListaPrenotazioniComponent } from './components/lista-prenotazioni/lista-prenotazioni.component';
import {PrenotaAppuntamentoComponent} from "./components/prenota-appuntamento/prenota-appuntamento.component";
import {InserisciAppuntamentoComponent} from "./components/inserisci-appuntamento/inserisci-appuntamento.component";
import { RimuoviAppuntamentoComponent } from './rimuovi-appuntamento/rimuovi-appuntamento.component';
import { PrenotazioniInAttesaComponent } from './components/prenotazioni-in-attesa/prenotazioni-in-attesa.component';
import { InformazioniComponent } from './components/informazioni/informazioni.component';

import { AdminGuard } from './admin.guard';





const routes: Routes = [
  {path: "home",component: HomeComponent , canActivate: [AuthGuard]},
  {path: "welcome",component: WelcomeComponent},
  { path: 'lista-prenotazioni', component: ListaPrenotazioniComponent , canActivate: [AuthGuard]},
  { path: 'lista-prenotazioni-in-attesa', component: PrenotazioniInAttesaComponent , canActivate: [AuthGuard]},
  { path: 'prenota-appuntamento/:id', component: PrenotaAppuntamentoComponent , canActivate: [AuthGuard]},
  { path: 'inserisci-appuntamento', component: InserisciAppuntamentoComponent , canActivate: [AuthGuard,AdminGuard]},
  { path: 'rimuovi-appuntamento', component: RimuoviAppuntamentoComponent , canActivate: [AuthGuard,AdminGuard]},
  { path: 'ringraziamenti', component: RingraziamentiComponent , canActivate: [AuthGuard] },
  { path: 'ringraziamenti2', component: Ringraziamenti2Component , canActivate: [AuthGuard] },
  { path: 'informazioni', component: InformazioniComponent  },

  {path: "",redirectTo:"welcome",pathMatch:"full"},
  {path: "**",redirectTo:"welcome",pathMatch:"full"}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
