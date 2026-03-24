import { Routes } from '@angular/router';
import { IndexComponent } from './pages/index/index';
import { AdminComponent } from './pages/admin/admin';
import { LoginComponent } from './pages/login/login';
import { SignupComponent } from './pages/signup/signup';
import { BookingComponent } from './pages/booking/booking';
import { adminRoleGuard } from './guards/admin-role.guard';

export const routes: Routes = [
  { path: '', component: IndexComponent },          
  {
    path: 'admin',
    component: AdminComponent,
    canActivate: [adminRoleGuard],
    children: [
      {
        path: 'usuarios',
        loadComponent: () => import('./pages/admin-usuarios/admin-usuarios').then(m => m.AdminUsuariosComponent)
      },
      {
        path: 'servicios',
        loadComponent: () => import('./pages/admin-servicios/admin-servicios').then(m => m.AdminServiciosComponent)
      },
      {
        path: 'salas',
        loadComponent: () => import('./pages/admin-salas/admin-salas').then(m => m.AdminSalasComponent)
      },
      {
        path: 'sesiones',
        loadComponent: () => import('./pages/admin-sesiones/admin-sesiones').then(m => m.AdminSesionesComponent)
      },
      {
        path: 'reservas',
        loadComponent: () => import('./pages/admin-reservas/admin-reservas').then(m => m.AdminReservasComponent)
      },
      {
        path: 'noticias',
        loadComponent: () => import('./pages/admin-noticias/admin-noticias').then(m => m.AdminNoticiasComponent)
      },
      {
        path: 'videos',
        loadComponent: () => import('./pages/admin-videos/admin-videos').then(m => m.AdminVideosComponent)
      }
    ]
  },
  { path: 'login', component: LoginComponent },   
  { path: 'signup', component: SignupComponent },    
  {
    path: 'booking',
    loadComponent: () => import('./pages/booking/booking').then(m => m.BookingComponent)
  },
  {
    path: 'fisioterapia',
    loadComponent: () =>
      import('./pages/fisioterapia/fisioterapia')
        .then(m => m.FisioterapiaComponent)
  },
  {
    path: 'nutricion',
    loadComponent: () =>
      import('./pages/nutricion/nutricion')
        .then(m => m.NutricionComponent)
  },
  {
    path: 'clasesdirigidas',
    loadComponent: () =>
      import('./pages/clasesdirigidas/clasesdirigidas')
        .then(m => m.ClasesdirigidasComponent)
  },
  {
    path: 'comunidad',
    loadComponent: () =>
      import('./pages/comunidad/comunidad')
        .then(m => m.ComunidadComponent)
  },
  { path: '**', redirectTo: '' },           
];
