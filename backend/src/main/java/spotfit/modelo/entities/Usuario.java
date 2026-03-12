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
     * ¿Cuándo se llama?
     * Spring Security llama a este método después de autenticar al usuario
     * para saber qué permisos tiene.
     * 
     * ¿Qué devuelve?
     * Una colección de objetos GrantedAuthority.
     * En nuestro caso, devolvemos una lista con UN SOLO elemento:
     * el nombre del perfil del usuario.
     * 
     * Ejemplo de flujo completo:
     * 1. Usuario "pedro@mail.com" hace login
     * 2. Spring Security carga el usuario de BD
     * 3. Pedro tiene perfil = Perfil{nombre="ROLE_CLIENTE"}
     * 4. Este método devuelve [SimpleGrantedAuthority("ROLE_CLIENTE")]
     * 5. Spring Security compara esto con las rutas protegidas
     * 6. Si la ruta requiere .hasRole("CLIENTE"), Pedro puede acceder
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
    
    /**
     * Devuelve la contraseña del usuario.
     * 
     * Spring Security llama a este método durante el login para
     * comparar la contraseña ingresada con la almacenada en BD.
     * 
     * Retorna el campo "contrasena" que incluye el prefijo {noop}
     * Ejemplo: "{noop}1234"
     */
    @Override
    public String getPassword() {
        return contrasena;
    }
    
    /**
     * Devuelve el "username" del usuario.
     * 
     * En sistemas tradicionales, esto devolvería un campo "username".
     * En nuestro caso, usamos el EMAIL como identificador único.
     * 
     * Por eso cuando hagas login con HTTP Basic Auth o JWT,
     * usarás el email como username:
     * - Username: pedro@mail.com
     * - Password: 1234
     * 
     * Spring Security usará este método internamente para identificar usuarios.
     */
    @Override
    public String getUsername() {
        return email;
    }
    
    /**
     * ¿La cuenta del usuario ha expirado?
     * 
     * Devolvemos siempre TRUE porque no gestionamos expiración de cuentas.
     * 
     * Si en el futuro quisieras implementar cuentas temporales
     * (por ejemplo, suscripciones que caducan), aquí verificarías
     * una fecha de expiración y devolverías true/false según corresponda.
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    
    /**
     * ¿La cuenta del usuario está bloqueada?
     * 
     * Devolvemos siempre TRUE (= cuenta NO bloqueada).
     * 
     * Si en el futuro quisieras bloquear usuarios tras varios intentos
     * fallidos de login, aquí verificarías un campo "bloqueado" y
     * devolverías el valor correspondiente.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    
    /**
     * ¿Las credenciales (contraseña) del usuario han expirado?
     * 
     * Devolvemos siempre TRUE (= credenciales NO expiradas).
     * 
     * Si en el futuro quisieras forzar cambios de contraseña periódicos
     * (por ejemplo, cada 90 días), aquí verificarías la fecha del último
     * cambio de contraseña y devolverías true/false.
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
     * Flujo completo:
     * 1. Usuario intenta hacer login
     * 2. Spring Security verifica usuario y contraseña correctos
     * 3. Spring Security llama a isEnabled()
     * 4. Si devuelve FALSE, rechaza el login aunque la contraseña sea correcta
     * 5. Si devuelve TRUE, permite el acceso
     * 
     * Esto permite "dar de baja" usuarios sin borrarlos de la base de datos.
     * Ejemplo: Carlos (id=7) tiene activo=false, no podrá hacer login.
     */
    @Override
    public boolean isEnabled() {
        return activo;
    }
}