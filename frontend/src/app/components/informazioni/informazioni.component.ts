import { Component } from '@angular/core';
import { JwksValidationHandler, OAuthService } from "angular-oauth2-oidc";
import { authCodeFlowConfig } from "../../sso-config";
import { EmailService } from 'src/service/email.service';


@Component({
  selector: 'app-informazioni',
  templateUrl: './informazioni.component.html',
  styleUrls: ['./informazioni.component.css']
})
export class InformazioniComponent {
  name: string = "";
  isRegistrato: boolean = false;



  constructor(private emailService : EmailService , private oAuthService: OAuthService) {}

  ngOnInit(): void {
    this.configureSingleSignOn();
    const userClaims: any = this.oAuthService.getIdentityClaims();
    this.name = userClaims.email ? userClaims.email : "";

    //this.utente.email = this.name;
  }

  configureSingleSignOn() {
    this.oAuthService.configure(authCodeFlowConfig);
    this.oAuthService.tokenValidationHandler = new JwksValidationHandler();
    this.oAuthService.loadDiscoveryDocumentAndTryLogin();
  }

}
