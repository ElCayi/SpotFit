import { Component, signal, inject, effect, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ServicioService } from '../../services/servicio';
import { Servicio } from '../../models/servicio';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';

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
      nombre: [''],
      descripcion: [''],
      duracion: ['']
    });

    this.load();
  }

  load() {
    this.service.getAll().subscribe(data => this.servicios = data);
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

  edit(servicio: Servicio) {
    this.editingId = servicio.idServicio!;
    this.form.patchValue(servicio);
  }

  delete(id: number) {
    if (!confirm('¿Eliminar servicio?')) return;
    this.service.delete(id).subscribe(() => this.load());
  }

  reset() {
    this.form.reset();
    this.editingId = null;
  }
}