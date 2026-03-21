import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Video } from '../models/video';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable({ providedIn: 'root' })
export class VideoService {

  private url = `${environment.apiUrl}/videos`;

  constructor(private http: HttpClient) {}

  getAll(): Observable<Video[]> {
    return this.http.get<Video[]>(this.url);
  }

  create(video: Video) {
  return this.http.post<Video>(this.url, video);
}

update(id: number, video: Video) {
  return this.http.put<Video>(`${this.url}/${id}`, video);
}

delete(id: number) {
  return this.http.delete(`${this.url}/${id}`);
}
}