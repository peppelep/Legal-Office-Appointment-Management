import { Component } from '@angular/core';
import { JwksValidationHandler, OAuthService } from "angular-oauth2-oidc";
import { authCodeFlowConfig } from "../../sso-config";
import { EmailService } from 'src/service/email.service';


@Component({
  selector: 'app-welcome',
  templateUrl: './welcome.component.html',
  styleUrls: ['./welcome.component.css']
})
export class WelcomeComponent {
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

  registrazione(name2 : String) {
    console.log(name2)
    this.emailService.registrazione(this.name).subscribe(
      (response: any) => {
        console.log("Registrazione effettuata con successo");
        this.isRegistrato = true; // Imposto lo stato di registrazione
      },
      (error) => {
        console.error('Errore durante la registrazione:', error);
      }
    );
  }


}
