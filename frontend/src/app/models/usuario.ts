export interface Usuario {
  idUsuario?: number;
  nombre: string;
  apellidos: string;
  email: string;
  contrasena?: string;
  activo: boolean;
  perfil?: any;
  rol?: string;  
}