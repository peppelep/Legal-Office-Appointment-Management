import { Component, ElementRef, ViewChild } from '@angular/core';
import {authCodeFlowConfig} from "../../sso-config";
import {JwksValidationHandler, OAuthService} from "angular-oauth2-oidc";
import {Router} from "@angular/router";
import {Appuntamento} from "../../model/appuntamento";
import {AppuntamentoService} from "../../../service/appuntamento.service";
import {jwtDecode} from 'jwt-decode';
import {DomSanitizer, SafeResourceUrl} from "@angular/platform-browser";
import { FormGroup, FormBuilder, Validators } from '@angular/forms';

@Component({
  selector: 'app-inserisci-appuntamento',
  templateUrl: './inserisci-appuntamento.component.html',
  styleUrls: ['./inserisci-appuntamento.component.css']
})
export class InserisciAppuntamentoComponent {
  @ViewChild('tipoInput') tipoInput!: ElementRef;
  @ViewChild('oraInput') oraInput!: ElementRef;
  @ViewChild('dataInput') dataInput!: ElementRef;
  @ViewChild('dettagliInput') dettagliInput!: ElementRef;
  @ViewChild('nomeAvvocatoInput') nomeAvvocatoInput!: ElementRef;
  @ViewChild('postiDisponibiliInput') postiDisponibiliInput!: ElementRef;

  appuntamentotForm: FormGroup;
  formSubmitted = false;

  constructor(private formBuilder: FormBuilder, private appuntamentoServie: AppuntamentoService) {
    this.appuntamentotForm = this.formBuilder.group({
      tipo: ['', Validators.required],
      ora: ['', Validators.required],
      data: ['', Validators.required],
      dettagli: ['', Validators.required],
      nomeAvvocato: ['', Validators.required],
      postiDisponibili: ['']
    });
  }

  addAppuntamento() {
    this.formSubmitted = true;

    if (this.appuntamentotForm.valid) {
      const appuntamento: Appuntamento = {
        id: 0,
        tipo: this.appuntamentotForm.value.tipo,
        ora: this.appuntamentotForm.value.ora,
        data: this.appuntamentotForm.value.data,
        dettagli: this.appuntamentotForm.value.dettagli,
        nomeAvvocato: this.appuntamentotForm.value.nomeAvvocato,
        postiDisponibili: this.appuntamentotForm.value.postiDisponibili,
        utentiInAttesa : 0
      };

      this.appuntamentoServie.addProduct(appuntamento).subscribe(
        (response) => {
          console.log(appuntamento);
        },
        (error) => {
          console.log(error);
        }
      );
    }
  }

}
