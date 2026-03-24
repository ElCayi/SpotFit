import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { UsuarioService } from '../../services/usuario';
import { Usuario } from '../../models/usuario';

@Component({
  selector: 'app-admin-usuarios',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './admin-usuarios.html',
  styleUrl: './admin-usuarios.css'
})
export class AdminUsuariosComponent implements OnInit {

  usuarios: Usuario[] = [];
  form!: FormGroup;
  editingId: number | null = null;

  constructor(private service: UsuarioService, private fb: FormBuilder) {}

  ngOnInit() {
    this.form = this.fb.group({
      nombre:    ['', Validators.required],
      apellidos: ['', Validators.required],
      email:     ['', [Validators.required, Validators.email]],
      contrasena:['', Validators.required],
      activo:    [true],
      perfilId:  [3, Validators.required]   // 1=Admin, 2=Monitor, 3=Cliente
    });

    this.load();
  }

  load() {
    this.service.getAll().subscribe({
      next: data => this.usuarios = data,
      error: err => console.error('Error cargando usuarios', err)
    });
  }

  submit() {
    if (this.form.invalid) return;

    const v = this.form.value;

    // Construimos el objeto tal como lo espera el backend (entidad Usuario de Spring)
    // El backend necesita perfil: { idPerfil: N }, no un string "ROLE_ADMIN"
    const payload: any = {
      nombre: v.nombre,
      apellidos: v.apellidos,
      email: v.email,
      activo: v.activo,
      perfil: { idPerfil: Number(v.perfilId) }
    };

    // Solo mandamos contraseña si se ha escrito algo (al editar es opcional)
    if (v.contrasena && v.contrasena.trim() !== '') {
      payload.contrasena = '{noop}' + v.contrasena;
    }

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

  edit(usuario: Usuario) {
    this.editingId = usuario.idUsuario;

    // Al editar, la contraseña pasa a ser opcional (no la queremos cambiar siempre)
    this.form.get('contrasena')?.clearValidators();
    this.form.get('contrasena')?.updateValueAndValidity();

    this.form.patchValue({
      nombre: usuario.nombre,
      apellidos: usuario.apellidos,
      email: usuario.email,
      contrasena: '',
      activo: usuario.activo,
      perfilId: this.rolToPerfilId(usuario.rol)
    });
  }

  delete(id: number) {
    if (!confirm('¿Eliminar este usuario?')) return;
    this.service.delete(id).subscribe({
      next: () => this.load(),
      error: err => console.error('Error eliminando', err)
    });
  }

  reset() {
    this.editingId = null;
    this.form.reset({
      nombre: '', apellidos: '', email: '',
      contrasena: '', activo: true, perfilId: 3
    });

    // Restaurar validador de contraseña (obligatoria solo al crear)
    this.form.get('contrasena')?.setValidators(Validators.required);
    this.form.get('contrasena')?.updateValueAndValidity();
  }

  /** Traduce el string del DTO a texto legible para la tabla */
  getRolLabel(rol: string): string {
    if (rol === 'ROLE_ADMIN')   return 'Administrador';
    if (rol === 'ROLE_MONITOR') return 'Monitor';
    if (rol === 'ROLE_CLIENTE') return 'Cliente';
    return rol;
  }

  /** Traduce el string del DTO al idPerfil que necesita el <select> */
  private rolToPerfilId(rol: string): number {
    if (rol === 'ROLE_ADMIN')   return 1;
    if (rol === 'ROLE_MONITOR') return 2;
    return 3;
  }
}