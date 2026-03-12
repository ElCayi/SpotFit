package spotfit.modelo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// El UsuarioLoginDto es un "contenedor" diseñado exclusivamente para transportar datos 
//desde el cliente hacia el servidor durante el proceso de inicio de sesión.

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UsuarioLoginDto {
    private String email;    
    private String password;
}

//FLUJO:
//
//El Usuario escribe sus credenciales en el login.
//El Frontend envía un JSON: {"email": "user@mail.com", "password": "123"}.
//Spring Boot recibe ese JSON y lo "mapea" automáticamente a este objeto UsuarioLoginDto.
//Tu lógica de negocio toma ese DTO, busca el email en la base de datos y verifica si la contraseña coincide.