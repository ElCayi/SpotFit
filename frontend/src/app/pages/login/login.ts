import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../services/auth';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  standalone: true,
  templateUrl: './login.html',
  styleUrls: ['./login.css'],
  imports: [CommonModule, FormsModule]
})
export class LoginComponent {

  email = '';
  password = '';

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  onLogin() {
    const data = {
      email: this.email,
      password: this.password
    };

    this.authService.login(data).subscribe({
      next: (usuario) => {
        console.log('Login correcto:', usuario);

        if (usuario.rol === 'ROLE_ADMIN') {
          this.router.navigate(['/admin']);
        } else {
          this.router.navigate(['/booking']);
        }
      },
      error: (err) => {
        console.error('Error en login:', err);
        alert('Credenciales incorrectas');
      }
    });
  }
}