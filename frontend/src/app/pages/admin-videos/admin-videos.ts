import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { VideoService } from '../../services/video';
import { Video } from '../../models/video';

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
      titulo:    ['', Validators.required],
      urlVideo:  ['', Validators.required],
      categoria: ['', Validators.required]
    });

    this.load();
  }

  load() {
    this.service.getAll().subscribe({
      next: data => this.videos = data,
      error: err => console.error('Error cargando videos', err)
    });
  }

  submit() {
    if (this.form.invalid) return;

    const payload = this.form.value;

    if (this.editingId) {
      this.service.update(this.editingId, payload).subscribe({
        next: () => { this.reset(); this.load(); },
        error: err => console.error('Error actualizando', err)
      });
    } else {
      this.service.create(payload).subscribe({
        next: () => { this.reset(); this.load(); },
        error: err => console.error('Error creando', err)
      });
    }
  }

  edit(video: Video) {
    this.editingId = video.idVideo;
    this.form.patchValue({
      titulo: video.titulo,
      urlVideo: video.urlVideo,
      categoria: video.categoria
    });
  }

  delete(id: number) {
    if (!confirm('¿Eliminar este video?')) return;
    this.service.delete(id).subscribe({
      next: () => this.load(),
      error: err => console.error('Error eliminando', err)
    });
  }

  reset() {
    this.editingId = null;
    this.form.reset({ titulo: '', urlVideo: '', categoria: '' });
  }
}