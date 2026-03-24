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
      fechaReserva: [''],
      estado: ['CONFIRMADA']
    });

    this.load();
  }

  load() {
    this.service.getAll().subscribe(data => this.reservas = data);
  }

  submit() {
    // Construimos la "Matrioska" para Java
    const reservaData = {
      fechaReserva: this.form.value.fechaReserva || new Date().toISOString(),
      estado: this.form.value.estado || 'CONFIRMADA',
      
      usuario: {
        idUsuario: this.form.value.usuarioId // Coincide con tu Usuario.java
      },
      sesion: {
        idSesion: this.form.value.sesionId // Coincide con tu Sesion.java
      }
    };

    if (this.editingId) {
      this.service.update(this.editingId, reservaData as any).subscribe(() => {
        this.reset();
        this.load();
      });
    } else {
      this.service.create(reservaData as any).subscribe(() => {
        this.reset();
        this.load();
      });
    }
  }

  edit(reserva: any) {
    this.editingId = reserva.idReserva;
    this.form.patchValue({
      usuarioId: reserva.usuario?.idUsuario,
      sesionId: reserva.sesion?.idSesion,
      // Recortamos la fecha para que se adapte al input de HTML
      fechaReserva: reserva.fechaReserva ? reserva.fechaReserva.substring(0, 16) : '',
      estado: reserva.estado
    });
  }

  delete(id: number) {
    if (!confirm('¿Eliminar reserva?')) return;
    this.service.delete(id).subscribe(() => this.load());
  }

  reset() {
    this.form.reset({ estado: 'CONFIRMADA' });
    this.editingId = null;
  }
}