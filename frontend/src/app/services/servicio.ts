import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Servicio } from '../models/servicio';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable({ providedIn: 'root' })
export class ServicioService {

  private url = `${environment.apiUrl}/servicios`;

  constructor(private http: HttpClient) {}

  getAll(): Observable<Servicio[]> {
    return this.http.get<Servicio[]>(this.url);
  }

  getById(id: number): Observable<Servicio> {
    return this.http.get<Servicio>(`${this.url}/${id}`);
  }

  create(servicio: Servicio) {
  return this.http.post<Servicio>(this.url, servicio);
}

update(id: number, servicio: Servicio) {
  return this.http.put<Servicio>(`${this.url}/${id}`, servicio);
}

delete(id: number) {
  return this.http.delete(`${this.url}/${id}`);
}
}