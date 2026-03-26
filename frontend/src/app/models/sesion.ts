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
  reservasActuales: number; 
}
