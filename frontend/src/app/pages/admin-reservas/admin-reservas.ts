import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReservaService } from '../../services/reserva';
import { Reserva } from '../../models/reserva';

@Component({
  selector: 'app-admin-reservas',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './admin-reservas.html',
  styleUrl: './admin-reservas.css'
})
export class AdminReservasComponent implements OnInit {

  reservas: Reserva[] = [];
  filtro: 'TODAS' | 'CONFIRMADA' | 'CANCELADA' = 'TODAS';

  constructor(private service: ReservaService) {}

  ngOnInit() {
    this.load();
  }

  load() {
    this.service.getAll().subscribe({
      next: data => this.reservas = data,
      error: err => console.error('Error cargando reservas', err)
    });
  }

  get reservasFiltradas(): Reserva[] {
    if (this.filtro === 'TODAS') return this.reservas;
    return this.reservas.filter(r => r.estado === this.filtro);
  }

  setFiltro(filtro: 'TODAS' | 'CONFIRMADA' | 'CANCELADA') {
    this.filtro = filtro;
  }

  cancelar(reserva: Reserva) {
    if (!confirm('¿Cancelar la reserva de ' + reserva.nombreUsuario + '?')) return;

    // El backend espera la entidad Reserva con los objetos anidados
    const payload: any = {
      usuario: { idUsuario: reserva.idUsuario },
      sesion:  { idSesion: reserva.idSesion },
      fechaReserva: reserva.fechaReserva,
      estado: 'CANCELADA'
    };

    this.service.update(reserva.idReserva, payload).subscribe({
      next: () => this.load(),
      error: err => console.error('Error cancelando reserva', err)
    });
  }

  eliminar(id: number) {
    if (!confirm('¿Eliminar esta reserva permanentemente?')) return;
    this.service.delete(id).subscribe({
      next: () => this.load(),
      error: err => console.error('Error eliminando', err)
    });
  }

  get totalConfirmadas(): number {
    return this.reservas.filter(r => r.estado === 'CONFIRMADA').length;
  }

  get totalCanceladas(): number {
    return this.reservas.filter(r => r.estado === 'CANCELADA').length;
  }
}