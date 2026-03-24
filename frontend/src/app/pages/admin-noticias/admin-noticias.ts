import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { NoticiaService } from '../../services/noticia';
import { Noticia } from '../../models/noticia';

@Component({
  selector: 'app-admin-noticias',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './admin-noticias.html',
  styleUrl: './admin-noticias.css'
})
export class AdminNoticiasComponent implements OnInit {

  noticias: Noticia[] = [];
  form!: FormGroup;
  editingId: number | null = null;

  constructor(private service: NoticiaService, private fb: FormBuilder) {}

  ngOnInit() {
    this.form = this.fb.group({
      titulo:    ['', Validators.required],
      contenido: ['', Validators.required],
      urlImagen: [''],
      fecha:     ['', Validators.required]
    });

    this.load();
  }

  load() {
    this.service.getAll().subscribe({
      next: data => this.noticias = data,
      error: err => console.error('Error cargando noticias', err)
    });
  }

  submit() {
    if (this.form.invalid) return;

    const v = this.form.value;

    // El backend espera fechaPublicacion como LocalDateTime (formato ISO)
    // El input date devuelve "2025-06-15", le añadimos la hora para que Spring lo parsee
    const payload: any = {
      titulo: v.titulo,
      contenido: v.contenido,
      urlImagen: v.urlImagen || null,
      fechaPublicacion: v.fecha + 'T00:00:00'
    };

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

  edit(noticia: Noticia) {
    this.editingId = noticia.idNoticia;

    // fechaPublicacion viene como "2025-06-15T00:00:00", el input date necesita solo "2025-06-15"
    const fechaCorta = noticia.fechaPublicacion
      ? noticia.fechaPublicacion.substring(0, 10)
      : '';

    this.form.patchValue({
      titulo: noticia.titulo,
      contenido: noticia.contenido,
      urlImagen: noticia.urlImagen || '',
      fecha: fechaCorta
    });
  }

  delete(id: number) {
    if (!confirm('¿Eliminar esta noticia?')) return;
    this.service.delete(id).subscribe({
      next: () => this.load(),
      error: err => console.error('Error eliminando', err)
    });
  }

  reset() {
    this.editingId = null;
    this.form.reset({ titulo: '', contenido: '', urlImagen: '', fecha: '' });
  }
}