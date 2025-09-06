import { Component, OnInit } from '@angular/core';
import { OAuthService } from "angular-oauth2-oidc";
import { JwksValidationHandler } from 'angular-oauth2-oidc-jwks';
import { authCodeFlowConfig } from "../../sso-config";
import { Router } from "@angular/router";
import { UsernameService } from 'src/service/username.service';
import {jwtDecode} from 'jwt-decode';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {
  username: String = '';
  roles: string[] = [];
  accessTokenPayload: any = {};

  constructor(private oautService: OAuthService, private router: Router, private usernameService: UsernameService) {}

  ngOnInit(): void {
    this.configureSingleSignOn();
    this.extractRoles();
    this.decodeAccessToken();
  }

  configureSingleSignOn() {

    //Questa linea configura l'OAuthService utilizzando la configurazione authCodeFlowConfig.
    //La configurazione contiene le informazioni necessarie per stabilire il flusso di autorizzazione OAuth2, come l'URL del provider di identità,
    // le credenziali client, e altre impostazioni specifiche.
    this.oautService.configure(authCodeFlowConfig);

    //Qui viene impostato il tokenValidationHandler dell'OAuthService con un'istanza di JwksValidationHandler.
    // Questo handler è responsabile della validazione dei token JWT (JSON Web Token) utilizzando JSON Web Key Set (JWKS).
    // JWKS contiene le chiavi pubbliche utilizzate per verificare la firma del token JWT emesso dal provider di identità.
    this.oautService.tokenValidationHandler = new JwksValidationHandler();

    this.oautService.loadDiscoveryDocumentAndTryLogin().then(() => {
      this.extractRoles();  //estrae e salva i ruoli dell'utente autenticato
    });
  }

  login() {
    this.oautService.initCodeFlow();
  }

  logout() {
    this.oautService.logOut();
  }

  get token() {
    let claims: any = this.oautService.getIdentityClaims();
    return claims ? claims : null;
  }

  private extractRoles() {
    let claims: any = this.oautService.getIdentityClaims();
    if (claims && claims['realm_access'] && claims['realm_access']['roles']) {
      this.roles = claims['realm_access']['roles'];
    }
  }

  isUserAdmin(): boolean {  //verifica dal token se l'utente ha il ruolo di admin
    let token: any = this.oautService.getAccessToken();
    if (token) {
      try {
        let decodedToken: any = jwtDecode(token);
        return decodedToken['realm_access']['roles'].includes('admin');
      } catch (error) {
        console.error('Error decoding access token:', error);
        return false;
      }
    }
    return false;
  }

  private decodeAccessToken() {
    let token: any = this.oautService.getAccessToken();
    if (token) {
      try {
        this.accessTokenPayload = jwtDecode(token);
        console.log('Access Token Payload:', this.accessTokenPayload);
      } catch (error) {
        console.error('Error decoding access token:', error);
      }
    }
  }
}
