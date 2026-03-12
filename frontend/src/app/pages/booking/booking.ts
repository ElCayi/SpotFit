import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { SesionService } from '../../services/sesion';
import { ReservaService } from '../../services/reserva';
import { Sesion } from '../../models/sesion';
import { Reserva } from '../../models/reserva';

@Component({
  selector: 'app-booking',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './booking.html',
  styleUrls: ['./booking.css']
})
export class BookingComponent implements OnInit {

  sesiones: Sesion[] = [];

  // filtros
  selectedDay: 'hoy' | 'manana' | null = 'hoy';
  selectedCategory: 'TODAS' | 'YOGA' | 'BODY' | 'CICLO' = 'TODAS';
  dateFrom: string | null = null;
  dateTo: string | null = null;

  // créditos de ejemplo
  creditos = 4;

  constructor(
    private sesionService: SesionService,
    private reservaService: ReservaService
  ) {}

  ngOnInit(): void {
    this.cargarSesiones();
  }

  cargarSesiones(): void {
    this.sesionService.getAll().subscribe({
      next: data => {
        this.sesiones = data;
      },
      error: err => {
        console.error('Error cargando sesiones', err);
      }
    });
  }

  get sesionesFiltradas(): Sesion[] {
    return this.sesiones
      .filter(s => this.filtrarPorDia(s))
      .filter(s => this.filtrarPorRangoFecha(s))
      .filter(s => this.filtrarPorCategoria(s));
  }

  private filtrarPorDia(sesion: Sesion): boolean {
    if (!this.selectedDay) return true;

    const hoy = new Date();
    const fechaSesion = new Date(sesion.fechaInicio);

    if (this.selectedDay === 'hoy') {
      return fechaSesion.toDateString() === hoy.toDateString();
    }

    if (this.selectedDay === 'manana') {
      const manana = new Date();
      manana.setDate(hoy.getDate() + 1);
      return fechaSesion.toDateString() === manana.toDateString();
    }

    return true;
  }

  private filtrarPorRangoFecha(sesion: Sesion): boolean {
    if (!this.dateFrom && !this.dateTo) return true;

    const fecha = new Date(sesion.fechaInicio);
    if (this.dateFrom) {
      const from = new Date(this.dateFrom);
      if (fecha < from) return false;
    }
    if (this.dateTo) {
      const to = new Date(this.dateTo);
      to.setHours(23, 59, 59, 999);
      if (fecha > to) return false;
    }
    return true;
  }

  private filtrarPorCategoria(sesion: Sesion): boolean {
    if (this.selectedCategory === 'TODAS') return true;

    const nombre = (sesion.nombreServicio || '').toLowerCase();

    if (this.selectedCategory === 'YOGA') {
      return nombre.includes('yoga');
    }
    if (this.selectedCategory === 'BODY') {
      return nombre.includes('body');
    }
    if (this.selectedCategory === 'CICLO') {
      return nombre.includes('ciclo');
    }

    return true;
  }

  setDayFilter(day: 'hoy' | 'manana'): void {
    this.selectedDay = day;
  }

  setCategory(cat: 'TODAS' | 'YOGA' | 'BODY' | 'CICLO'): void {
    this.selectedCategory = cat;
  }

  getDuracionMinutos(sesion: Sesion): number {
    const inicio = new Date(sesion.fechaInicio).getTime();
    const fin = new Date(sesion.fechaFin).getTime();
    const diffMs = fin - inicio;
    return Math.max(0, Math.round(diffMs / 60000));
  }

  reservar(sesion: Sesion): void {
    const reserva: Reserva = {
      idReserva: 0,
      idUsuario: 1,        
      nombreUsuario: 'Usuario',
      idSesion: sesion.idSesion,
      nombreServicio: sesion.nombreServicio,
      fechaReserva: new Date().toISOString(),
      estado: 'RESERVADA'
    };

    this.reservaService.create(reserva).subscribe({
      next: () => {
        alert('Reserva realizada correctamente');
      },
      error: err => {
        console.error('Error al reservar', err);
        alert('Ha ocurrido un error al realizar la reserva');
      }
    });
  }
}
