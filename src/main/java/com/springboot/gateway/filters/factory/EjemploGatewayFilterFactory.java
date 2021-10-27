package com.springboot.gateway.filters.factory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class EjemploGatewayFilterFactory extends AbstractGatewayFilterFactory<EjemploGatewayFilterFactory.Configuracion> {

    private static final Logger log = LoggerFactory.getLogger(EjemploGatewayFilterFactory.class);

    public EjemploGatewayFilterFactory() {
        super(Configuracion.class);
    }

    /**
     *  Se implementa la configuración de filtros personalizado PRE Y POST, solo se aplica para el
     *  microservicio de cursos se configura en el application.yml.
     *
     *
     * GatewayFilterFactory:  prefijo indispensable en el nombre de la clase para
     * ser reconocido como un filter del api-gateway
     *
     * Este GatewayFilterFactory ha sido configurado en el application.yml como un filtro
     *
     *   filters:
     *       - name: Ejemplo    este el prefijo de la clase EjemploGatewayFilterFactory
     *         args:
     *            mensaje: Hola mi mensaje personalizado
     *            cookieNombre: usuario
     *            cookieValor: NicolasHayden
     *
     *
     *
     *            new OrderedGatewayFilter nos permite definir un orden de prioridad si no se requiere
     *            un orden no es necesario colocarlo, es decir, para la clase GlobalFiletes tiene un orden
     *            de prioridad 1 y nuestra filtro personalizado Ejemplo tiene el orden dos es decir
     *            primero aparecerán las cookies del filtro global y después las del filtro personalizado
     */



    @Override
    public GatewayFilter apply(Configuracion config) {
        return new OrderedGatewayFilter((exchange, chain) -> {
            // Ejecutando el filtro PRE
            log.info("Ejecutando PRE gateway filter factory: " + config.mensaje);
            return chain.filter(exchange).then(Mono.fromRunnable(() -> { // Ejecutando el filtro POST
                Optional.ofNullable(config.cookieValor).ifPresent(cookie->{
                    exchange.getResponse().addCookie(ResponseCookie.from(config.cookieNombre, cookie).build());
                });
                log.info("Ejecutando POST gateway filter factory: "+config.mensaje);
            }));
        },2);
    }

    /**
     * Nos permite Perzonalizar el nombre del filtro ya que por defecto se toma el nombre de prefijo de la
     * clase es decir "Ejemplo"
     */
//    @Override
//    public String name() {
//        return "EjemploCookie";
//    }

    /**
     * Este método nos permite configurar el orden cuando se configura un orden especifico solo cuando se utiliza
     * esta configuración en   application.yml
     * filters:
     *   Ejemplo=Hola mi mensaje perzonalizado, usuario, NicolasHayden

     */
    public List<String> shortcutFieldOrder() {
        return Arrays.asList("mensaje","cookieNombre","cookieValor");
    }

    //Se Crear un clase de configuración
    public static class Configuracion {
        private String mensaje;
        private String cookieValor;
        private String cookieNombre;

        public String getMensaje() {
            return mensaje;
        }
        public String getCookieValor() {
            return cookieValor;
        }
        public String getCookieNombre() {
            return cookieNombre;
        }
        public void setMensaje(String mensaje) {
            this.mensaje = mensaje;
        }
        public void setCookieValor(String cookieValor) {
            this.cookieValor = cookieValor;
        }
        public void setCookieNombre(String cookieNombre) {
            this.cookieNombre = cookieNombre;
        }
    }

}
