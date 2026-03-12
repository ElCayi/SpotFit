import { Component, OnInit, signal, inject, effect } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup } from '@angular/forms';
import { SesionService } from '../../services/sesion';
import { Sesion } from '../../models/sesion';


@Component({
  selector: 'app-admin-sesiones',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './admin-sesiones.html',
  styleUrl: '../admin-reservas/admin-reservas.css'
})
export class AdminSesionesComponent implements OnInit {

  sesiones: Sesion[] = [];
  form!: FormGroup;
  editingId: number | null = null;

  constructor(
    private sesionService: SesionService,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.initForm();
    this.loadSesiones();
  }

  initForm() {
    this.form = this.fb.group({
      titulo: [''],
      descripcion: [''],
      fecha: [''],
      hora: [''],
      salaId: [''],
      servicioId: ['']
    });
  }

  loadSesiones() {
    this.sesionService.getAll().subscribe(data => {
      this.sesiones = data;
    });
  }

  submit() {
    const data = this.form.value;

    if (this.editingId) {
      this.sesionService.update(this.editingId, data)
        .subscribe(() => {
          this.resetForm();
          this.loadSesiones();
        });
    } else {
      this.sesionService.create(data)
        .subscribe(() => {
          this.resetForm();
          this.loadSesiones();
        });
    }
  }

  edit(sesion: Sesion) {
    this.editingId = sesion.idSesion!;
    this.form.patchValue(sesion);
  }

  delete(id: number) {
    if (!confirm('Eliminar sesión?')) return;

    this.sesionService.delete(id).subscribe(() => {
      this.loadSesiones();
    });
  }

  resetForm() {
    this.form.reset();
    this.editingId = null; 
  }
}