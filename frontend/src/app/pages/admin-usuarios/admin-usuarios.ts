import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup } from '@angular/forms';
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
      email: [''],
      rol: ['']
    });

    this.load();
  }

  load() {
    this.service.getAll().subscribe(data => this.usuarios = data);
  }

  submit() {
    if (this.editingId) {
      this.service.update(this.editingId, this.form.value).subscribe(() => {
        this.reset();
        this.load();
      });
    } else {
      this.service.create(this.form.value).subscribe(() => {
        this.reset();
        this.load();
      });
    }
  }

  edit(usuario: Usuario) {
    this.editingId = usuario.idUsuario!;
    this.form.patchValue(usuario);
  }

  delete(id: number) {
    if (!confirm('¿Eliminar usuario?')) return;

    this.service.delete(id).subscribe(() => this.load());
  }

  reset() {
    this.form.reset();
    this.editingId = null;
  }
}