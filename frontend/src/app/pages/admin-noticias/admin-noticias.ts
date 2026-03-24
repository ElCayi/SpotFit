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
    // Definimos el formulario con los nombres exactos que espera el Backend
    this.form = this.fb.group({
      titulo: [''],
      contenido: [''],
      urlImagen: [''], 
      fechaPublicacion: [''] 
    });

    this.load();
  }

  load() {
    this.service.getAll().subscribe(data => {
      this.noticias = data;
    });
  }

  submit() {
    // Creamos un objeto limpio para enviar al servidor.
    // IMPORTANTE: No enviamos 'fechaPublicacion' para que MySQL 
    // use su valor por defecto (NOW()) y no nos dé error de formato.
    const noticiaData = {
      titulo: this.form.value.titulo,
      contenido: this.form.value.contenido,
      urlImagen: this.form.value.urlImagen || 'https://via.placeholder.com/150'
    };

    if (this.editingId) {
      // Si estamos editando, enviamos los datos limpios al update
      this.service.update(this.editingId, noticiaData as Noticia)
        .subscribe(() => { 
          this.reset(); 
          this.load(); 
        });
    } else {
      // Si estamos creando, enviamos los datos limpios al create
      this.service.create(noticiaData as Noticia)
        .subscribe(() => { 
          this.reset(); 
          this.load(); 
        });
    }
  }

  edit(noticia: Noticia) {
    this.editingId = noticia.idNoticia!;
    // Cargamos los datos de la noticia en el formulario
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