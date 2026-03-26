export interface Reserva {
  idReserva: number;
  idUsuario: number;
  nombreUsuario: string;
  idSesion: number;
  nombreServicio: string;
  fechaReserva: string;
  estado: string;
}

export interface ReservaPayload {
  usuario: { idUsuario: number };
  sesion: { idSesion: number };
  fechaReserva: string;
  estado: string;
}
