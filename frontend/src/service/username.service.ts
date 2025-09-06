import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class UsernameService {
  private username: String = '';

  setUsername(username: String): void {
    this.username = username;
  }

  getUsername(): String {
    return this.username;
  }
}
