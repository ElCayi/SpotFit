import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Sesion } from '../models/sesion';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable({ providedIn: 'root' })
export class SesionService {

  private url = `${environment.apiUrl}/sesiones`;

  constructor(private http: HttpClient) {}

  getAll(): Observable<Sesion[]> {
    return this.http.get<Sesion[]>(this.url);
  }

  getById(id: number): Observable<Sesion> {
    return this.http.get<Sesion>(`${this.url}/${id}`);
  }

  create(sesion: Sesion): Observable<Sesion> {
    return this.http.post<Sesion>(this.url, sesion);
  }

  update(id: number, sesion: Sesion): Observable<Sesion> {
    return this.http.put<Sesion>(`${this.url}/${id}`, sesion);
  }

  delete(id: number) {
    return this.http.delete(`${this.url}/${id}`);
  }

  getSesionesHoy() { 
    return this.http.get<any[]>(`${this.url}/hoy`); }
}