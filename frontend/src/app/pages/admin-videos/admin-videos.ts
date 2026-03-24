import { Component, signal, inject, effect, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { VideoService } from '../../services/video';
import { Video } from '../../models/video';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'app-admin-videos',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './admin-videos.html',
  styleUrl: './admin-videos.css'
})
export class AdminVideosComponent implements OnInit {

  videos: Video[] = [];
  form!: FormGroup;
  editingId: number | null = null;

  constructor(private service: VideoService, private fb: FormBuilder) {}

  ngOnInit() {
    this.form = this.fb.group({
      titulo: [''],
      urlVideo: [''], // Cambiado de 'url' a 'urlVideo'
      categoria: [''] // Añadido para que coincida con tu entidad
    });

    this.load();
  }

  load() {
    this.service.getAll().subscribe(data => this.videos = data);
  }

  submit() {
    // Creamos el objeto limpio para asegurar que los nombres sean correctos
    const videoData = {
      titulo: this.form.value.titulo,
      urlVideo: this.form.value.urlVideo,
      categoria: this.form.value.categoria || 'General'
    };

    if (this.editingId) {
      this.service.update(this.editingId, videoData as Video)
        .subscribe(() => { this.reset(); this.load(); });
    } else {
      this.service.create(videoData as Video)
        .subscribe(() => { this.reset(); this.load(); });
    }
  }

  edit(video: Video) {
    this.editingId = video.idVideo!;
    this.form.patchValue(video);
  }

  delete(id: number) {
    if (!confirm('¿Eliminar video?')) return;
    this.service.delete(id).subscribe(() => this.load());
  }

  reset() {
    this.form.reset();
    this.editingId = null;
  }
}