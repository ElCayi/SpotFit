export interface Reserva {
  idReserva: number;

  idUsuario: number;
  nombreUsuario: string;

  idSesion: number;
  nombreServicio: string;

  fechaReserva: string;
  estado: string;
}