import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Reserva, ReservaPayload } from '../models/reserva';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable({ providedIn: 'root' })
export class ReservaService {

  private url = `${environment.apiUrl}/reservas`;

  constructor(private http: HttpClient) {}

  getAll(): Observable<Reserva[]> {
    return this.http.get<Reserva[]>(this.url);
  }

  getById(id: number): Observable<Reserva> {
    return this.http.get<Reserva>(`${this.url}/${id}`);
  }

  create(reserva: ReservaPayload): Observable<Reserva> {
    return this.http.post<Reserva>(this.url, reserva);
  }

  update(id: number, reserva: ReservaPayload): Observable<Reserva> {
    return this.http.put<Reserva>(`${this.url}/${id}`, reserva);
  }

  delete(id: number) {
    return this.http.delete(`${this.url}/${id}`);
  }
}
