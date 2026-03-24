package spotfit.modelo.entities;

import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor  
@NoArgsConstructor   
@Data                
@Builder             


// ANOTACIONES DE JPA

@Entity              // Indica que esta clase es una tabla en la base de datos
@Table(name="usuarios")  // Nombre de la tabla en MySQL


// IMPLEMENTACIÓN DE UserDetails

// Al implementar UserDetails, Spring Security puede usar esta clase
// para gestionar la autenticación y autorización de usuarios.
// Spring Security llamará a los métodos de esta interfaz durante el login.

public class Usuario implements UserDetails {
    

    // CAMPOS DE LA ENTIDAD (columnas de BD)

    
	private static final long serialVersionUID = 1L;

	/**
     * Identificador único del usuario (clave primaria)
     * - @Id: marca este campo como clave primaria
     * - @GeneratedValue: el valor se genera automáticamente (autoincremental)
     * - @Column(name="id"): mapea con la columna "id" de la tabla usuarios
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int idUsuario;
    
    /**
     * Nombre del usuario
     */
    private String nombre;
    
    /**
     * Apellidos del usuario
     */
    private String apellidos;
    
    /**
     * Email del usuario (usado como username en Spring Security)
     * - @Column(unique = true): no puede haber dos usuarios con el mismo email
     * - Este campo es el "username" que Spring Security usará para identificar usuarios
     */
    @Column(unique = true)
    private String email;
    
    /**
     * Contraseña del usuario
     * - Almacenada con prefijo {noop} para indicar texto plano
     * - Spring Security comparará esta contraseña durante el login
     */
    private String contrasena;
    
    /**
     * Indica si el usuario está activo o dado de baja
     * - true: puede hacer login
     * - false: no puede hacer login (cuenta deshabilitada)
     * - Spring Security llamará al método isEnabled() y verificará este campo
     */
    private boolean activo;
    
    // RELACIÓN CON PERFIL (UN SOLO ROL)
    
    /**
     * Relación Many-to-One con la tabla Perfil
     * 
     * - Muchos usuarios pueden tener el mismo perfil
     * - Cada usuario tiene UN SOLO perfil
     * 
     */
    @ManyToOne
    @JoinColumn(name="perfil_id")
    private Perfil perfil;  
    
  
    // MÉTODOS DE UserDetails (Spring Security)
    
    /**
     * Devuelve las autoridades (permisos/roles) del usuario.
     * 
     * IMPORTANTE:
     * - perfil.getNombre() devuelve "ROLE_CLIENTE" (con prefijo ROLE_)
     * - SimpleGrantedAuthority envuelve ese string en el formato que Spring entiende
     * - List.of(...) crea una lista inmutable con un solo elemento
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(perfil.getNombre()));
    }
    
    
    @Override
    public String getPassword() {
        return contrasena;
    }
    
    /**
     * Devuelve el "username" del usuario.
     * 
     */
    @Override
    public String getUsername() {
        return email;
    }
    
    /**
     * ¿La cuenta del usuario ha expirado?
     * 
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    
    /**
     * ¿La cuenta del usuario está bloqueada?
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    
    /**
     * ¿Las credenciales (contraseña) del usuario han expirado?
     * 
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    
    /**
     * ¿El usuario está habilitado/activo?
     * 
     * Devolvemos el valor del campo "activo" de la base de datos.
     * 
     */
    @Override
    public boolean isEnabled() {
        return activo;
    }
}