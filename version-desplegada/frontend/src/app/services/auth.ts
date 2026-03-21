import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { Observable, tap } from 'rxjs';
import { UsuarioLogin } from '../models/usuario-login';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private baseUrl = `${environment.apiUrl}`;

  constructor(private http: HttpClient) {}

  login(data: UsuarioLogin): Observable<any> {
    return this.http.post<any>(`${this.baseUrl}/login`, data)
      .pipe(
        tap(response => {
          localStorage.setItem('token', response.token);
          localStorage.setItem('usuario', JSON.stringify({
            id: response.id,
            nombre: response.nombre,
            email: response.email,
            rol: response.rol
          }));
        })
      );
  }

  logout(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('usuario');
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }

  getUsuario(): any {
    const usuario = localStorage.getItem('usuario');
    return usuario ? JSON.parse(usuario) : null;
  }

  isLogged(): boolean {
    return !!this.getToken();
  }

  getRol(): string | null {
    const usuario = this.getUsuario();
    return usuario ? usuario.rol : null;
  }

  register(data: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/register`, data);
  }
}