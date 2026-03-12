import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { SesionService } from '../../services/sesion';
import { AuthService } from '../../services/auth';
import { Sesion } from '../../models/sesion';

@Component({
  selector: 'app-admin',
  standalone: true,
  templateUrl: './admin.html',
  styleUrls: ['./admin.css'],
  imports: [CommonModule, RouterModule]
})
export class AdminComponent {

  adminName = '';
  sesionesHoy: {
    hora: string;
    servicio: string;
    monitor: string;
    sala: string;
    ocupacion: number;
    aforo: number;
    porcentaje: number;
  }[] = [];

  constructor(
    private sesionService: SesionService,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit() {
    const usuario = this.authService.getUsuario();
    this.adminName = usuario?.nombre ?? 'Admin';

    this.cargarSesionesHoy();
  }

  cargarSesionesHoy() {
    this.sesionService.getSesionesHoy().subscribe({
      next: (data: any[]) => {
        // Adaptamos lo que venga del backend al formato de la tabla
        this.sesionesHoy = data.map(s => {
          const porcentaje = s.aforoMaximo
            ? Math.round((s.ocupacionActual / s.aforoMaximo) * 100)
            : 0;

          return {
            hora: s.hora || s.fechaInicio?.substring(11, 16),
            servicio: s.nombreServicio,
            monitor: s.nombreMonitor,
            sala: s.nombreSala,
            ocupacion: s.ocupacionActual,
            aforo: s.aforoMaximo,
            porcentaje
          };
        });
      },
      error: (err) => console.error(err)
    });
  }

  logout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
