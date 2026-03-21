import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Sala } from '../models/sala';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable({ providedIn: 'root' })
export class SalaService {

  private url = `${environment.apiUrl}/salas`;

  constructor(private http: HttpClient) {}

  getAll(): Observable<Sala[]> {
    return this.http.get<Sala[]>(this.url);
  }

  getById(id: number): Observable<Sala> {
    return this.http.get<Sala>(`${this.url}/${id}`);
  }
  create(sala: Sala) {
  return this.http.post<Sala>(this.url, sala);
}

update(id: number, sala: Sala) {
  return this.http.put<Sala>(`${this.url}/${id}`, sala);
}

delete(id: number) {
  return this.http.delete(`${this.url}/${id}`);
}
}