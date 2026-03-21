import { Component, OnInit } from '@angular/core';
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

  constructor(private sesionService: SesionService, private fb: FormBuilder) {}

  ngOnInit() {
    this.initForm();
    this.loadSesiones();
  }

  initForm() {
    this.form = this.fb.group({
      fecha: [''],
      hora: [''],
      salaId: [''],
      servicioId: [''],
      monitorId: [''], // <-- ¡ESTO ES VITAL! Si no está aquí, el formulario no lo lee
      aforoMaximo: ['20']
    });
  }

  loadSesiones() {
    this.sesionService.getAll().subscribe(data => {
      this.sesiones = data;
      // ⬇️ ESTO NOS DIRÁ LA VERDAD ⬇️
      if (data.length > 0) {
        console.log('ESTRUCTURA REAL DE UNA SESIÓN:', data[0]);
      }
    });
  }

  submit() {
  const fechaStr = `${this.form.value.fecha}T${this.form.value.hora}:00`;

  const sesionData = {
    fechaInicio: fechaStr,
    fechaFin: fechaStr,
    aforoMaximo: Number(this.form.value.aforoMaximo),
    
    // ✅ CORREGIDO: Usar los nombres correctos de los campos Java
    servicio: { idServicio: Number(this.form.value.servicioId) },
    sala:     { idSala: Number(this.form.value.salaId) },
    monitor:  { idUsuario: Number(this.form.value.monitorId) }
  };

  console.log('Enviando datos...', sesionData);

  if (this.editingId) {
    this.sesionService.update(this.editingId, sesionData as any).subscribe({
      next: () => { this.resetForm(); this.loadSesiones(); },
      error: () => { this.loadSesiones(); this.resetForm(); }
    });
  } else {
    this.sesionService.create(sesionData as any).subscribe({
      next: () => { 
        alert('¡Sesión creada!'); 
        this.resetForm(); 
        this.loadSesiones(); 
      },
      error: (err) => {
        console.error('Error:', err);
        alert('Error al crear la sesión');
      }
    });
  }
}

  edit(sesion: any) {
  this.editingId = sesion.idSesion;
  if (sesion.fechaInicio) {
    const parts = sesion.fechaInicio.split('T');
    this.form.patchValue({
      fecha: parts[0],
      hora: parts[1]?.substring(0, 5) || '',
      salaId: sesion.idSala,
      servicioId: sesion.idServicio,
      monitorId: sesion.idMonitor,
      aforoMaximo: sesion.aforoMaximo
    });
  }
}

  delete(id: number) {
    if (!confirm('¿Eliminar sesión?')) return;
    this.sesionService.delete(id).subscribe(() => this.loadSesiones());
  }

  resetForm() {
    this.form.reset({ aforoMaximo: '20' });
    this.editingId = null; 
  }
}