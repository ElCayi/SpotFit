import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth';

export const adminRoleGuard: CanActivateFn = () => {
  const authService = inject(AuthService);
  const router = inject(Router);

  return authService.getRol() === 'ROLE_ADMIN'
    ? true
    : router.createUrlTree(['/booking']);
};
