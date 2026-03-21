import { Servicio } from './servicio';
import { Usuario } from './usuario';
import { Sala } from './sala';

export interface Sesion {
  idSesion?: number;
  
  // Campos del DTO
  idServicio?: number;
  nombreServicio?: string;
  idMonitor?: number;
  nombreMonitor?: string;
  idSala?: number;
  nombreSala?: string;
  
  fechaInicio: string;
  fechaFin: string;
  aforoMaximo: number;

  // Campos anidados (para crear/editar)
  servicio?: any;
  monitor?: any;
  sala?: any;
}