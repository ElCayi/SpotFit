import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SesionService } from '../../services/sesion';
import { NoticiaService } from '../../services/noticia';
import { VideoService } from '../../services/video';
import { AuthService } from '../../services/auth';
import { Router } from '@angular/router';
import { Sesion } from '../../models/sesion';
import { Noticia } from '../../models/noticia';
import { Video } from '../../models/video';

type MonitorSection = 'sesiones' | 'noticias' | 'videos';

@Component({
  selector: 'app-monitor',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './monitor.html',
  styleUrl: './monitor.css'
})
export class MonitorComponent implements OnInit {

  monitorName = '';
  activeSection: MonitorSection = 'sesiones';

  sesiones: Sesion[] = [];
  noticias: Noticia[] = [];
  videos: Video[] = [];

  constructor(
    private sesionService: SesionService,
    private noticiaService: NoticiaService,
    private videoService: VideoService,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit() {
    const usuario = this.authService.getUsuario();
    this.monitorName = usuario?.nombre ?? 'Monitor';

    this.cargarSesiones();
    this.cargarNoticias();
    this.cargarVideos();
  }

  cargarSesiones() {
    this.sesionService.getAll().subscribe({
      next: data => this.sesiones = data,
      error: err => console.error('Error cargando sesiones', err)
    });
  }

  cargarNoticias() {
    this.noticiaService.getAll().subscribe({
      next: data => this.noticias = data,
      error: err => console.error('Error cargando noticias', err)
    });
  }

  cargarVideos() {
    this.videoService.getAll().subscribe({
      next: data => this.videos = data,
      error: err => console.error('Error cargando videos', err)
    });
  }

  setSection(section: MonitorSection) {
    this.activeSection = section;
  }

  get headerTitle(): string {
    switch (this.activeSection) {
      case 'noticias': return 'Noticias SpotFit';
      case 'videos': return 'Biblioteca de videos';
      default: return 'Mis sesiones';
    }
  }

  get headerSubtitle(): string {
    switch (this.activeSection) {
      case 'noticias': return 'Mantente al día de las novedades del gimnasio.';
      case 'videos': return 'Accede a los videos disponibles en la plataforma.';
      default: return 'Consulta las sesiones programadas y tu agenda de trabajo.';
    }
  }

  getDuracionMinutos(sesion: Sesion): number {
    const inicio = new Date(sesion.fechaInicio).getTime();
    const fin = new Date(sesion.fechaFin).getTime();
    return Math.max(0, Math.round((fin - inicio) / 60000));
  }

  getOcupacionPorcentaje(sesion: Sesion): number {
    if (!sesion.aforoMaximo) return 0;
    return Math.round((sesion.reservasActuales / sesion.aforoMaximo) * 100);
  }

  logout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}