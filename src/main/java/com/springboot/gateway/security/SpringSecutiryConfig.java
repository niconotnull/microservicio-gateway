package com.springboot.gateway.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

// Permite habilitar la seguridad en webFlux,con esta anotación que es de configuración
// se tendrá un método bean SecurityWebFilterChain   que va a registrar un componente para
// configurar la seguridad
@EnableWebFluxSecurity
public class SpringSecutiryConfig {

    /**
     * Clase de configuración para spring-security con web-flux
     *
     * SpringCloud- gateway trabaja con programación reactiva,  la programación reactiva es asíncrona
     * y cada flujo se ejecuta en su propio proceso y eso hace que sea reactivo y no bloqueante,  es decir
     * no bloquea el hilo principal tampoco afecta a los demás flujos es decir se podrían tener varios
     * flujos sobre el proceso principal en su propio hilo sin afectarse uno con otro, este tipo de
     * programación reactiva es ideal para llamadas asíncronas a un servidor.
     *
     * Cuando trabajamos con Angular o React y solicitan una petición al servidor es posible que exista
     * un delay en la petición si se demora un flujo en obtener los datos esa demora no afecta a toda
     * la aplicación y es por eso que SpringCloud- gateway  trabaja con la programación reactiva en web-flux
     * para que la comunicación entre micro-servicios sea más ágil y mucho mas rápida que se pueda utilizar el delay
     * que la demora no afecte a los demás micro-servicios es decir al fina del día todo funciona más rápido, mejor
     * y más optimizado
     *
     *
     * EnableWebFluxSecurity : Permite habilitar la seguridad con webFlux
     * SecurityWebFilterChain :  este filtro permite definir que rutas serán públicas y/o privadas, cuales
     * con permitAll o deshabilitar csrf que un token cuando se utiliza vistas reactivas thymeleaf
     */


    @Autowired
    private JwtAuthenticationFilter authenticationFilter;

    /**
     * Paso 1 : Se configura el filtro que permite definir que rutas serán publicas
     *  .anyExchange().authenticated(): se define que toas las rutas deberán de ser autenticadas
     *  .and().csrf().disable():  se deshabilita el token de tipo csrf
     *  .build(): se construye el objeto
     */
    @Bean
    public SecurityWebFilterChain configure(ServerHttpSecurity http){
        return  http.authorizeExchange()
                .pathMatchers("/api/security/oauth/**").permitAll()
                .pathMatchers(HttpMethod.GET, "/api/alumnos/listar", "/api/examenes/listar", "/api/cursos/paginacion").permitAll()
                .pathMatchers(HttpMethod.GET, "/api/examenes/{id}", "/api/cursos/{id}").hasAnyRole("ADMIN", "USER")
                .pathMatchers("/api/alumnos/**", "/api/examenes/","/api/cursos/**").hasRole("ADMIN")
                .anyExchange().authenticated()
                .and().addFilterAt(authenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .csrf().disable()
                .build();
    }

}
