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
      nombre: [''],
      apellidos: [''],
      email: [''],
      contrasena: [''], // Importante para cuando creamos uno nuevo
      activo: ['true'], // Por defecto el usuario se crea activo
      perfilId: ['']    // Aquí pondremos 1 (Admin), 2 (Monitor) o 3 (Cliente)
    });

    this.load();
  }

  load() {
    this.service.getAll().subscribe(data => this.usuarios = data);
  }

 submit() {
    const usuarioData = {
      nombre: this.form.value.nombre,
      apellidos: this.form.value.apellidos,
      email: this.form.value.email,
      contrasena: this.form.value.contrasena,
      activo: this.form.value.activo === 'true' || this.form.value.activo === true,
      
      // === LA SOLUCIÓN ===
      // Enviamos ÚNICAMENTE idPerfil, que es lo que existe en Perfil.java
      perfil: { 
        idPerfil: Number(this.form.value.perfilId) 
      }
    };

    if (this.editingId) {
      this.service.update(this.editingId, usuarioData as any).subscribe({
        next: () => { this.reset(); this.load(); },
        error: (err) => alert('Error al actualizar: ' + err.message)
      });
    } else {
      this.service.create(usuarioData as any).subscribe({
        next: () => { 
          alert('¡USUARIO CREADO CON ÉXITO!');
          this.reset(); 
          this.load(); 
        },
        error: (err) => {
          console.error('Error:', err);
          alert('Error: Revisa si el email ya existe en la base de datos.');
        }
      });
    }
  }

  edit(usuario: Usuario) {
    this.editingId = usuario.idUsuario!;
    
    // Rellenamos el formulario con los datos del usuario que hemos clicado
    this.form.patchValue({
      nombre: usuario.nombre,
      apellidos: usuario.apellidos,
      email: usuario.email,
      activo: usuario.activo ? 'true' : 'false',
      // Extraemos el ID del perfil usando $any lógico
      perfilId: usuario.perfil?.id || usuario.perfil?.id_perfil || usuario.perfil?.idPerfil
    });
  }

  delete(id: number) {
    if (!confirm('¿Eliminar usuario?')) return;
    this.service.delete(id).subscribe(() => this.load());
  }

  reset() {
    this.form.reset({ activo: 'true' });
    this.editingId = null;
  }
}