import { Component , OnInit } from '@angular/core';

import {OAuthService} from "angular-oauth2-oidc" ;
import { JwksValidationHandler } from 'angular-oauth2-oidc-jwks';
import {authCodeFlowConfig} from "../../sso-config";
import {Router} from "@angular/router";

import { UsernameService } from 'src/service/username.service';

@Component({
  selector: 'app-pagina-iniziale',
  templateUrl: './pagina-iniziale.component.html',
  styleUrls: ['./pagina-iniziale.component.css']
})
export class PaginaInizialeComponent implements OnInit{

  username: String = '';

  constructor(private oautService: OAuthService , private router: Router , private usernameService: UsernameService) {}

  ngOnInit(): void {
    this.configureSingleSignOn();

  }

  configureSingleSignOn(){
    this.oautService.configure(authCodeFlowConfig);
    this.oautService.tokenValidationHandler = new JwksValidationHandler();
    this.oautService.loadDiscoveryDocumentAndTryLogin();
  }

  login(){
    this.oautService.initCodeFlow();
  }

  logout(){
    this.oautService.logOut();
  }

  get token(){
    let claims: any = this.oautService.getIdentityClaims();
    return claims ? claims :null;
  }

}

