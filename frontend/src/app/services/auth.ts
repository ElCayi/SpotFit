// src/app/services/auth.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { Observable, tap } from 'rxjs';
import { UsuarioLogin } from '../models/usuario-login';
import { Usuario } from '../models/usuario';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private baseUrl = `${environment.apiUrl}`;

  constructor(private http: HttpClient) {}

  login(data: UsuarioLogin): Observable<Usuario> {
    return this.http.post<Usuario>(`${this.baseUrl}/login`, data)
      .pipe(
        tap(usuario => this.guardarUsuario(usuario))
      );
  }

  logout(): void {
    localStorage.removeItem('usuario');
  }

  guardarUsuario(usuario: Usuario): void {
    localStorage.setItem('usuario', JSON.stringify(usuario));
  }

  getUsuario(): Usuario | null {
    const usuario = localStorage.getItem('usuario');
    return usuario ? JSON.parse(usuario) : null;
  }

  isLogged(): boolean {
    return !!this.getUsuario();
  }

  register(data: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/register`, data);
  }
}