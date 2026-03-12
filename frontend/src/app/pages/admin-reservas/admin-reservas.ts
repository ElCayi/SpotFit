import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup } from '@angular/forms';
import { ReservaService } from '../../services/reserva';
import { Reserva } from '../../models/reserva';

@Component({
  selector: 'app-admin-reservas',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './admin-reservas.html',
  styleUrls: ['./admin-reservas.css']

})
export class AdminReservasComponent implements OnInit {

  reservas: Reserva[] = [];
  form!: FormGroup;
  editingId: number | null = null;

  constructor(private service: ReservaService, private fb: FormBuilder) {}

  ngOnInit() {
    this.form = this.fb.group({
      usuarioId: [''],
      sesionId: [''],
      fecha: [''],
      estado: ['']
    });

    this.load();
  }

  load() {
    this.service.getAll().subscribe(data => this.reservas = data);
  }

  submit() {
    if (this.editingId) {
      this.service.update(this.editingId, this.form.value).subscribe(() => {
        this.reset();
        this.load();
      });
    } else {
      this.service.create(this.form.value).subscribe(() => {
        this.reset();
        this.load();
      });
    }
  }

  edit(reserva: Reserva) {
    this.editingId = reserva.idReserva!;
    this.form.patchValue(reserva);
  }

  delete(id: number) {
    if (!confirm('¿Eliminar reserva?')) return;

    this.service.delete(id).subscribe(() => this.load());
  }

  reset() {
    this.form.reset();
    this.editingId = null;
  }
}