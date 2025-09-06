import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { OAuthService } from 'angular-oauth2-oidc';
import {jwtDecode} from 'jwt-decode';

@Injectable({
  providedIn: 'root'
})
export class AdminGuard implements CanActivate {

  constructor(private oAuthService: OAuthService, private router: Router) {}

  canActivate(): boolean {
    const token = this.oAuthService.getAccessToken();
    if (token) {
      try {
        const decodedToken: any = jwtDecode(token);
        const roles: string[] = decodedToken['realm_access']['roles'];
        if (roles.includes('admin')) {
          return true;
        }
      } catch (error) {
        console.error('Error decoding access token:', error);
      }
    }
    this.router.navigate(['/welcome']);
    return false;
  }
}
