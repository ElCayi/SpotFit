import { Usuario } from './usuario';
import { Sesion } from './sesion';

export interface Reserva {
  idReserva?: number;
  fechaReserva: string;
  estado: string;

  // En lugar de IDs sueltos, usamos los objetos (igual que en Java)
  usuario?: Usuario;
  sesion?: Sesion;
}