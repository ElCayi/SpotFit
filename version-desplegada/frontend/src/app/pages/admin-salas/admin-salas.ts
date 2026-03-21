import { Component, signal, inject, effect, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SalaService } from '../../services/sala';
import { Sala } from '../../models/sala';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'app-admin-salas',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './admin-salas.html',
  styleUrl: './admin-salas.css'
})

export class AdminSalasComponent implements OnInit {

  salas: Sala[] = [];
  form!: FormGroup;
  editingId: number | null = null;

  constructor(private service: SalaService, private fb: FormBuilder) {}

  ngOnInit() {
    this.form = this.fb.group({
      nombre: [''],
      capacidad: ['']
    });

    this.load();
  }

  load() {
    this.service.getAll().subscribe(data => this.salas = data);
  }

  submit() {
    if (this.editingId) {
      this.service.update(this.editingId, this.form.value)
        .subscribe(() => { this.reset(); this.load(); });
    } else {
      this.service.create(this.form.value)
        .subscribe(() => { this.reset(); this.load(); });
    }
  }

  edit(sala: Sala) {
    this.editingId = sala.idSala!;
    this.form.patchValue(sala);
  }

  delete(id: number) {
    if (!confirm('¿Eliminar sala?')) return;
    this.service.delete(id).subscribe(() => this.load());
  }

  reset() {
    this.form.reset();
    this.editingId = null;
  }
}