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
      url: ['']
    });

    this.load();
  }

  load() {
    this.service.getAll().subscribe(data => this.videos = data);
  }

  submit() {
    if (this.editingId) {
      this.service.update(this.editingId, this.form.value)
        .subscribe(() => { this.reset(); this.load(); });
    } else {
      this.service.create(this.form.value)
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