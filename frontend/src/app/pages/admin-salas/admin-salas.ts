import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { SalaService } from '../../services/sala';
import { Sala } from '../../models/sala';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-admin-salas',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, MatSnackBarModule],
  templateUrl: './admin-salas.html',
  styleUrl: './admin-salas.css'
})
export class AdminSalasComponent implements OnInit {

  salas: Sala[] = [];
  form!: FormGroup;
  editingId: number | null = null;

  constructor(private service: SalaService, private fb: FormBuilder, private snackBar: MatSnackBar) {}

  ngOnInit() {
    this.form = this.fb.group({
      nombre:    ['', Validators.required],
      capacidad: [null, [Validators.required, Validators.min(1)]]
    });

    this.load();
  }

  load() {
    this.service.getAll().subscribe({
      next: data => this.salas = data,
      error: err => console.error('Error cargando salas', err)
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

  edit(sala: Sala) {
    this.editingId = sala.idSala;
    this.form.patchValue({
      nombre: sala.nombre,
      capacidad: sala.capacidad
    });
  }

  delete(id: number) {
  if (!confirm('¿Eliminar esta sala?')) return;
  this.service.delete(id).subscribe({
    next: () => this.load(),
    error: err => this.snackBar.open('Error al eliminar la sala', 'Cerrar', { duration: 3000, 
  verticalPosition: 'top', 
  horizontalPosition: 'center',
  panelClass: ['snackbar-error'] })
  });
}

  reset() {
    this.editingId = null;
    this.form.reset({ nombre: '', capacidad: null });
  }
}