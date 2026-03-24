import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ServicioService } from '../../services/servicio';
import { Servicio } from '../../models/servicio';

@Component({
  selector: 'app-admin-servicios',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './admin-servicios.html',
  styleUrl: './admin-servicios.css'
})
export class AdminServiciosComponent implements OnInit {

  servicios: Servicio[] = [];
  form!: FormGroup;
  editingId: number | null = null;

  constructor(private service: ServicioService, private fb: FormBuilder) {}

  ngOnInit() {
    this.form = this.fb.group({
      nombre:      ['', Validators.required],
      descripcion: ['', Validators.required],
      categoria:   ['CLASE', Validators.required]  // 'CLASE' o 'SALUD'
    });

    this.load();
  }

  load() {
    this.service.getAll().subscribe({
      next: data => this.servicios = data,
      error: err => console.error('Error cargando servicios', err)
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

  edit(servicio: Servicio) {
    this.editingId = servicio.idServicio;
    this.form.patchValue({
      nombre: servicio.nombre,
      descripcion: servicio.descripcion,
      categoria: servicio.categoria
    });
  }

  delete(id: number) {
    if (!confirm('¿Eliminar este servicio?')) return;
    this.service.delete(id).subscribe({
      next: () => this.load(),
      error: err => console.error('Error eliminando', err)
    });
  }

  reset() {
    this.editingId = null;
    this.form.reset({ nombre: '', descripcion: '', categoria: 'CLASE' });
  }

  /** Traduce la categoría a texto legible para la tabla */
  getCategoriaLabel(categoria: string): string {
    if (categoria === 'CLASE') return 'Clase grupal';
    if (categoria === 'SALUD') return 'Salud individual';
    return categoria;
  }
}