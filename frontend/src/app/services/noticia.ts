import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Noticia } from '../models/noticia';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable({ providedIn: 'root' })
export class NoticiaService {

  private url = `${environment.apiUrl}/noticias`;

  constructor(private http: HttpClient) {}

  getAll(): Observable<Noticia[]> {
    return this.http.get<Noticia[]>(this.url);
  }

  getById(id: number): Observable<Noticia> {
    return this.http.get<Noticia>(`${this.url}/${id}`);
  }

  create(noticia: Noticia) {
    return this.http.post<Noticia>(this.url, noticia);
  }

  update(id: number, noticia: Noticia) {
    return this.http.put<Noticia>(`${this.url}/${id}`, noticia);
  }

  delete(id: number) {
    return this.http.delete(`${this.url}/${id}`);
  }
}