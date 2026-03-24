import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { SesionService } from '../../services/sesion';
import { ReservaService } from '../../services/reserva';
import { NoticiaService } from '../../services/noticia';
import { VideoService } from '../../services/video';
import { AuthService } from '../../services/auth';
import { Sesion } from '../../models/sesion';
import { Noticia } from '../../models/noticia';
import { Video } from '../../models/video';
import { Reserva } from '../../models/reserva';

type BookingSection = 'reservas' | 'citas' | 'noticias' | 'videos';
type ReservasTab = 'disponibles' | 'reservadas';
type CitasTab = 'disponibles' | 'reservadas';

@Component({
  selector: 'app-booking',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './booking.html',
  styleUrls: ['./booking.css']
})
export class BookingComponent implements OnInit {

  sesiones: Sesion[] = [];
  reservas: Reserva[] = [];
  noticias: Noticia[] = [];
  videos: Video[] = [];
  activeSection: BookingSection = 'reservas';
  activeReservasTab: ReservasTab = 'disponibles';
  activeCitasTab: CitasTab = 'disponibles';

  // filtros
  selectedDay: 'hoy' | 'manana' | null = null;
  selectedCategory: 'TODAS' | 'YOGA' | 'BODY' | 'CICLO' = 'TODAS';
  selectedCitaType: 'TODAS' | 'FISIOTERAPIA' | 'NUTRICION' = 'TODAS';
  dateFrom: string | null = null;
  dateTo: string | null = null;

  // créditos de ejemplo
  creditos = 4;

  constructor(
    private sesionService: SesionService,
    private reservaService: ReservaService,
    private noticiaService: NoticiaService,
    private videoService: VideoService,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.cargarSesiones();
    this.cargarReservas();
    this.cargarNoticias();
    this.cargarVideos();
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

  cargarNoticias(): void {
    this.noticiaService.getAll().subscribe({
      next: data => {
        this.noticias = data;
      },
      error: err => {
        console.error('Error cargando noticias', err);
      }
    });
  }

  cargarVideos(): void {
    this.videoService.getAll().subscribe({
      next: data => {
        this.videos = data;
      },
      error: err => {
        console.error('Error cargando videos', err);
      }
    });
  }

  cargarReservas(): void {
    this.reservaService.getAll().subscribe({
      next: data => {
        this.reservas = data;
      },
      error: err => {
        console.error('Error cargando reservas', err);
      }
    });
  }

  get headerTitle(): string {
    switch (this.activeSection) {
      case 'citas':
        return 'Citas de salud';
      case 'noticias':
        return 'Noticias SpotFit';
      case 'videos':
        return 'Biblioteca de videos';
      default:
        return 'Reservas de clases';
    }
  }

  get headerSubtitle(): string {
    switch (this.activeSection) {
      case 'citas':
        return 'Consulta las sesiones disponibles de nutricion y fisioterapia.';
      case 'noticias':
        return 'Mantente al dia de las novedades del gimnasio.';
      case 'videos':
        return 'Accede a los videos disponibles en la plataforma.';
      default:
        return 'Reserva tus clases dirigidas para hoy y los proximos dias.';
    }
  }

  get clasesFiltradas(): Sesion[] {
    return this.sesiones
      .filter(s => !this.esSesionDeCita(s))
      .filter(s => !this.estaReservadaPorUsuario(s.idSesion))
      .filter(s => this.filtrarPorDia(s))
      .filter(s => this.filtrarPorRangoFecha(s))
      .filter(s => this.filtrarPorCategoria(s));
  }

  get misReservasDeClases(): Reserva[] {
    const usuario = this.authService.getUsuario();

    if (!usuario?.id) {
      return [];
    }

    return this.reservas
      .filter(reserva => reserva.idUsuario === usuario.id)
      .filter(reserva => !this.esReservaDeCita(reserva))
      .filter(reserva => reserva.estado !== 'CANCELADA');
  }

  get citasFiltradas(): Sesion[] {
    return this.sesiones
      .filter(s => this.esSesionDeCita(s))
      .filter(s => !this.estaCitaReservadaPorUsuario(s.idSesion))
      .filter(s => this.filtrarPorDia(s))
      .filter(s => this.filtrarPorRangoFecha(s))
      .filter(s => this.filtrarPorTipoCita(s));
  }

  get misReservasDeCitas(): Reserva[] {
    const usuario = this.authService.getUsuario();

    if (!usuario?.id) {
      return [];
    }

    return this.reservas
      .filter(reserva => reserva.idUsuario === usuario.id)
      .filter(reserva => this.esReservaDeCita(reserva))
      .filter(reserva => reserva.estado !== 'CANCELADA');
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

  private filtrarPorTipoCita(sesion: Sesion): boolean {
    if (this.selectedCitaType === 'TODAS') return true;

    const nombre = (sesion.nombreServicio || '').toLowerCase();

    if (this.selectedCitaType === 'FISIOTERAPIA') {
      return nombre.includes('fisioterapia');
    }

    if (this.selectedCitaType === 'NUTRICION') {
      return nombre.includes('nutric');
    }

    return true;
  }

  private esSesionDeCita(sesion: Sesion): boolean {
    const nombre = (sesion.nombreServicio || '').toLowerCase();
    return nombre.includes('fisioterapia') || nombre.includes('nutric');
  }

  private esReservaDeCita(reserva: Reserva): boolean {
    const nombre = (reserva.nombreServicio || '').toLowerCase();
    return nombre.includes('fisioterapia') || nombre.includes('nutric');
  }

  private estaReservadaPorUsuario(idSesion: number): boolean {
    return this.misReservasDeClases.some(reserva => reserva.idSesion === idSesion);
  }

  private estaCitaReservadaPorUsuario(idSesion: number): boolean {
    return this.misReservasDeCitas.some(reserva => reserva.idSesion === idSesion);
  }

  setSection(section: BookingSection): void {
    this.activeSection = section;
  }

  setReservasTab(tab: ReservasTab): void {
    this.activeReservasTab = tab;
  }

  setCitasTab(tab: CitasTab): void {
    this.activeCitasTab = tab;
  }

  setDayFilter(day: 'hoy' | 'manana' | null): void {
    this.selectedDay = day;
  }

  setCategory(cat: 'TODAS' | 'YOGA' | 'BODY' | 'CICLO'): void {
    this.selectedCategory = cat;
  }

  setCitaType(type: 'TODAS' | 'FISIOTERAPIA' | 'NUTRICION'): void {
    this.selectedCitaType = type;
  }

  getDuracionMinutos(sesion: Sesion): number {
    const inicio = new Date(sesion.fechaInicio).getTime();
    const fin = new Date(sesion.fechaFin).getTime();
    const diffMs = fin - inicio;
    return Math.max(0, Math.round(diffMs / 60000));
  }

  reservar(sesion: Sesion): void {
    const usuario = this.authService.getUsuario();

    if (!usuario?.id) {
      alert('No se ha podido identificar al usuario logueado');
      return;
    }

    const reservaPayload = {
      usuario: { idUsuario: usuario.id },
      sesion: { idSesion: sesion.idSesion },
      fechaReserva: new Date().toISOString(),
      estado: 'CONFIRMADA'
    };

    this.reservaService.create(reservaPayload as any).subscribe({
      next: () => {
        this.cargarReservas();
        this.cargarSesiones();  // Recargar para actualizar el contador de plazas
        alert('Reserva realizada correctamente');
      },
      error: err => {
        console.error('Error al reservar', err);
        alert('Ha ocurrido un error al realizar la reserva');
      }
    });
  }

  cancelarReserva(reserva: Reserva): void {
    const usuario = this.authService.getUsuario();

    if (!usuario?.id) {
      alert('No se ha podido identificar al usuario logueado');
      return;
    }

    const reservaPayload = {
      usuario: { idUsuario: usuario.id },
      sesion: { idSesion: reserva.idSesion },
      fechaReserva: reserva.fechaReserva,
      estado: 'CANCELADA'
    };

    this.reservaService.update(reserva.idReserva, reservaPayload as any).subscribe({
      next: () => {
        this.cargarReservas();
        this.cargarSesiones();
        alert('Reserva cancelada correctamente');
      },
      error: err => {
        console.error('Error al cancelar la reserva', err);
        alert('No se ha podido cancelar la reserva');
      }
    });
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
