import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { SesionService } from '../../services/sesion';
import { ServicioService } from '../../services/servicio';
import { SalaService } from '../../services/sala';
import { UsuarioService } from '../../services/usuario';
import { Sesion } from '../../models/sesion';

@Component({
  selector: 'app-admin-sesiones',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './admin-sesiones.html',
  styleUrl: './admin-sesiones.css'
})
export class AdminSesionesComponent implements OnInit {

  sesiones: Sesion[] = [];
  form!: FormGroup;
  editingId: number | null = null;

  // Datos para los selects (se cargan del backend)
  servicios: any[] = [];
  salas: any[] = [];
  monitores: any[] = [];

  constructor(
    private sesionService: SesionService,
    private servicioService: ServicioService,
    private salaService: SalaService,
    private usuarioService: UsuarioService,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.form = this.fb.group({
      servicioId:  ['', Validators.required],
      monitorId:   ['', Validators.required],
      salaId:      ['', Validators.required],
      fechaInicio: ['', Validators.required],
      fechaFin:    ['', Validators.required]
    });

    this.load();
    this.loadServicios();
    this.loadSalas();
    this.loadMonitores();
  }

  // ── Cargar datos ──

  load() {
    this.sesionService.getAll().subscribe({
      next: data => this.sesiones = data,
      error: err => console.error('Error cargando sesiones', err)
    });
  }

  loadServicios() {
    this.servicioService.getAll().subscribe({
      next: data => this.servicios = data,
      error: err => console.error('Error cargando servicios', err)
    });
  }

  loadSalas() {
    this.salaService.getAll().subscribe({
      next: data => this.salas = data,
      error: err => console.error('Error cargando salas', err)
    });
  }

  loadMonitores() {
    // Cargamos todos los usuarios y filtramos los que pueden impartir sesiones
    this.usuarioService.getAll().subscribe({
      next: data => {
        this.monitores = data.filter(
          u => u.rol === 'ROLE_MONITOR' || u.rol === 'ROLE_ADMIN'
        );
      },
      error: err => console.error('Error cargando monitores', err)
    });
  }

  // ── CRUD ──

  submit() {
    if (this.form.invalid) return;

    const v = this.form.value;

    // El backend espera la entidad Sesion con objetos anidados
    // El aforoMaximo lo calcula el backend según la categoría del servicio
    const payload: any = {
      servicio: { idServicio: Number(v.servicioId) },
      monitor:  { idUsuario: Number(v.monitorId) },
      sala:     { idSala: Number(v.salaId) },
      fechaInicio: v.fechaInicio + ':00',  // datetime-local da "2025-06-15T10:00", añadimos segundos
      fechaFin:    v.fechaFin + ':00',
      aforoMaximo: 0  // el backend lo recalcula si es <= 0
    };

    if (this.editingId) {
      this.sesionService.update(this.editingId, payload).subscribe({
        next: () => { this.reset(); this.load(); },
        error: err => console.error('Error actualizando', err)
      });
    } else {
      this.sesionService.create(payload).subscribe({
        next: () => { this.reset(); this.load(); },
        error: err => console.error('Error creando', err)
      });
    }
  }

  edit(sesion: Sesion) {
    this.editingId = sesion.idSesion;

    // fechaInicio viene como "2025-06-15T10:00:00"
    // datetime-local necesita "2025-06-15T10:00" (sin segundos)
    const inicioCorto = sesion.fechaInicio ? sesion.fechaInicio.substring(0, 16) : '';
    const finCorto    = sesion.fechaFin ? sesion.fechaFin.substring(0, 16) : '';

    this.form.patchValue({
      servicioId: sesion.idServicio,
      monitorId:  sesion.idMonitor,
      salaId:     sesion.idSala,
      fechaInicio: inicioCorto,
      fechaFin:    finCorto
    });
  }

  delete(id: number) {
    if (!confirm('¿Eliminar esta sesión?')) return;
    this.sesionService.delete(id).subscribe({
      next: () => this.load(),
      error: err => console.error('Error eliminando', err)
    });
  }

  reset() {
    this.editingId = null;
    this.form.reset({
      servicioId: '', monitorId: '', salaId: '',
      fechaInicio: '', fechaFin: ''
    });
  }
}
