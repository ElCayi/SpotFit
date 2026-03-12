package spotfit.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

// ============================================================
// ANOTACIONES DE SPRING SECURITY
// ============================================================

/**
 * @Configuration
 * Indica que esta clase contiene definiciones de beans de Spring.
 * Spring la procesará para registrar los beans en el contexto.
 * 
 * @EnableWebSecurity
 * Activa Spring Security en la aplicación.
 * Le dice a Spring que use esta clase para configurar la seguridad web.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    // ============================================================
    // CONFIGURACIÓN PRINCIPAL DE SEGURIDAD
    // ============================================================
    
    /**
     * Define la cadena de filtros de seguridad de Spring Security.
     * 
     * ¿QUÉ ES ESTO?
     * Spring Security funciona como una cadena de filtros que intercepta
     * TODAS las peticiones HTTP antes de que lleguen a los controladores.
     * 
     * Esta cadena verifica:
     * 1. ¿La petición requiere autenticación?
     * 2. ¿El usuario está autenticado?
     * 3. ¿El usuario tiene los permisos necesarios?
     * 
     * FLUJO COMPLETO:
     * Usuario hace petición → Filtros de Spring Security → Controlador
     * 
     * @param http - Objeto para configurar la seguridad HTTP
     * @return SecurityFilterChain configurado
     */
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // ========================================
            // 1. DESHABILITAR CSRF
            // ========================================
            /**
             * CSRF (Cross-Site Request Forgery) es un ataque donde un sitio malicioso
             * hace peticiones a tu API en nombre del usuario autenticado.
             * 
             * ¿Por qué lo deshabilitamos?
             * - Estamos haciendo una API REST, no una web tradicional con formularios.
             * - No usamos cookies de sesión (usamos HTTP Basic Auth).
             * - El frontend estará en un dominio separado (Angular).
             * 
             * En producción con JWT también se deshabilita porque el token
             * no se almacena en cookies.
             */
            .csrf(csrf -> csrf.disable())
            
            // ========================================
            // 2. GESTIÓN DE SESIONES: STATELESS
            // ========================================
            /**
             * SessionCreationPolicy.STATELESS significa:
             * - Spring Security NO crea sesiones HTTP
             * - NO guarda el estado del usuario en el servidor
             * - Cada petición debe autenticarse por sí misma
             * 
             * ¿Por qué STATELESS?
             * - Es el estándar para APIs REST modernas
             * - Facilita escalabilidad horizontal (varios servidores)
             * - Compatible con HTTP Basic Auth y JWT
             * 
             * ¿Cómo funciona?
             * - Cada petición incluye las credenciales (Authorization: Basic ...)
             * - Spring Security verifica las credenciales en CADA petición
             * - No hay "sesión activa" en el servidor
             * 
             * Diferencia con sesiones tradicionales:
             * CON SESIÓN: Login → Servidor guarda sesión → Cookie con ID sesión
             * SIN SESIÓN: Cada petición lleva credenciales → Servidor verifica siempre
             */
            .sessionManagement(ses -> ses.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            
            // ========================================
            // 3. CONFIGURACIÓN CORS
            // ========================================
            /**
             * CORS (Cross-Origin Resource Sharing) permite que un frontend
             * en un dominio diferente pueda hacer peticiones a esta API.
             * 
             * Ejemplo:
             * - Frontend Angular: http://localhost:4200
             * - Backend Spring: http://localhost:8090
             * 
             * Sin CORS, el navegador bloquearía las peticiones.
             * 
             * Customizer.withDefaults() usa la configuración por defecto.
             * Más adelante se puede personalizar para permitir solo dominios específicos.
             */
            .cors(cors -> cors.configurationSource(request -> {
                var config = new org.springframework.web.cors.CorsConfiguration();
                config.setAllowedOrigins(java.util.List.of("https://aitorserrano.com"));
                config.setAllowedMethods(java.util.List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                config.setAllowedHeaders(java.util.List.of("*"));
                config.setAllowCredentials(true);
                return config;
            }))
            
            // ========================================
            // 4. AUTORIZACIÓN DE RUTAS
            // ========================================
            /**
             * Aquí definimos QUÉ RUTAS requieren QUÉ PERMISOS.
             * 
             * Spring Security evalúa las reglas EN ORDEN:
             * - La primera regla que coincida se aplica
             * - Las reglas más específicas deben ir ANTES que las genéricas
             * 
             * Formato:
             * .requestMatchers("/ruta") → Define qué ruta
             * .permitAll() → Permite acceso sin autenticación
             * .hasRole("ADMIN") → Requiere rol ADMIN
             * .hasAnyRole("ADMIN", "MONITOR") → Requiere ADMIN O MONITOR
             * .authenticated() → Requiere estar autenticado (cualquier rol)
             */
            .authorizeHttpRequests(auth -> auth
                
                // ========================================
                // RUTAS PÚBLICAS (sin autenticación)
                // ========================================
                
              
            	.requestMatchers("/", "/login", "/register", "/noticias/**", "/videos/**").permitAll()
                
                // ========================================
                // RUTAS DE ADMINISTRACIÓN (solo ADMIN)
                // ========================================
                /**
                
                 */
                .requestMatchers("/usuarios/**").hasRole("ADMIN")
                .requestMatchers("/salas/**").hasRole("ADMIN")
                .requestMatchers("/servicios/**").hasRole("ADMIN")
                
                // ========================================
                // RUTAS DE SESIONES (ADMIN + MONITOR)
                // ========================================
                
                .requestMatchers(org.springframework.http.HttpMethod.GET, "/sesiones/**").hasAnyRole("ADMIN", "MONITOR", "CLIENTE")
                .requestMatchers("/sesiones/**").hasAnyRole("ADMIN", "MONITOR")
                
                // ========================================
                // RUTAS DE RESERVAS (solo CLIENTE)
                // ========================================
               
                .requestMatchers("/reservas/**").hasRole("CLIENTE")
                
                
                // ========================================
                // CUALQUIER OTRA RUTA
                // ========================================
                /**
                 * Cualquier ruta que NO coincida con las anteriores
                 * requiere estar autenticado.
                 * 
                 * authenticated() = "debe haber hecho login, cualquier rol vale"
                 * 
                 * Esto es una red de seguridad:
                 * - Si añades un nuevo endpoint y olvidas protegerlo,
                 *   al menos requerirá autenticación básica.
                 */
                .anyRequest().authenticated()
            )
            
            // ========================================
            // 5. TIPO DE AUTENTICACIÓN: HTTP BASIC
            // ========================================
            /**
             * HTTP Basic Authentication:
             * - El cliente envía usuario y contraseña en CADA petición
             * - Se envían en la cabecera Authorization codificados en Base64
             * 
             * Formato de la cabecera:
             * Authorization: Basic cGVkcm9AbWFpbC5jb206MTIzNA==
             * 
             * Decodificado:
             * pedro@mail.com:1234
             * 
             * ¿Cómo funciona?
             * 1. Cliente hace petición con cabecera Authorization
             * 2. Spring Security decodifica las credenciales
             * 3. Llama a loadUserByUsername(email)
             * 4. Compara contraseñas
             * 5. Si coincide, permite el acceso
             * 
             * IMPORTANTE:
             * - En producción SIEMPRE usar HTTPS (SSL/TLS)
             * - Sin HTTPS, las credenciales viajan en texto plano (aunque en Base64)
             * 
             * Más adelante se puede migrar a JWT, que es más seguro y flexible.
             */
            .httpBasic(Customizer.withDefaults())
            
            // ========================================
            // 6. DESHABILITAR FORMULARIO DE LOGIN
            // ========================================
            /**
             * Spring Security por defecto crea una página de login HTML.
             * 
             * Como estamos haciendo una API REST (no una web tradicional),
             * deshabilitamos ese formulario.
             * 
             * El login se hará mediante:
             * - HTTP Basic Auth (ahora)
             * - JWT (más adelante, opcional)
             * - Nunca mediante un formulario HTML generado por Spring
             */
            .formLogin(form -> form.disable());
        
        return http.build();
    }
}