import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth';

@Component({
  selector: 'app-signup',
  standalone: true,
  templateUrl: './signup.html',
  styleUrls: ['./signup.css'],
  imports: [CommonModule, FormsModule, RouterLink]
})
export class SignupComponent {

  nombre = '';
  apellido = '';
  email = '';
  telefono = '';
  contrasena = '';
  confirmarContrasena = '';
  acepta = false;

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  onSignup() {

    if (this.contrasena !== this.confirmarContrasena) {
      alert('Las contraseñas no coinciden');
      return;
    }

    if (!this.acepta) {
      alert('Debes aceptar los términos');
      return;
    }

    const data = {
      nombre: this.nombre,
      apellido: this.apellido,
      email: this.email,
      telefono: this.telefono,
      contrasena: this.contrasena
    };

    this.authService.register(data).subscribe({
      next: () => {
        alert('Cuenta creada correctamente');
        this.router.navigate(['/login']);
      },
      error: (err) => {
        console.error(err);
        alert('Error al crear la cuenta');
      }
    });
  }
}
