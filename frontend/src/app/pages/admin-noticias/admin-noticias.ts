import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup } from '@angular/forms';
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
      titulo: [''],
      contenido: [''],
      fecha: ['']
    });

    this.load();
  }

  load() {
    this.service.getAll().subscribe(data => this.noticias = data);
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

  edit(noticia: Noticia) {
    this.editingId = noticia.idNoticia!;
    this.form.patchValue(noticia);
  }

  delete(id: number) {
    if (!confirm('¿Eliminar noticia?')) return;
    this.service.delete(id).subscribe(() => this.load());
  }

  reset() {
    this.form.reset();
    this.editingId = null;
  }
}