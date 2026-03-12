import { Servicio } from './servicio';
import { Usuario } from './usuario';
import { Sala } from './sala';

export interface Sesion {
  idSesion: number;

  idServicio: number;
  nombreServicio: string;

  idMonitor: number;
  nombreMonitor: string;

  idSala: number;
  nombreSala: string;

  fechaInicio: string;
  fechaFin: string;

  aforoMaximo: number;
}