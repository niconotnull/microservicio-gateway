package com.springboot.gateway.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Component
public class GlobalFilters implements GlobalFilter, Ordered {


    private static final Logger log = LoggerFactory.getLogger(GlobalFilters.class);

    /**
     *  Se implementa la configuración de filtros globales PRE Y POST PARA EL API-GATEWAY de manera global para
     *  todos los servicios de nuestro ecosistema.
     *
     *  chain:  se puede acceder al request y al response y nos permite de igual manera modificarlos o bien
     *  realizar validaciones y según los parámetros que se envíen en la cabecera, parámetros del request
     *  que se cumplan con cierta condición se podría permitir o rechazar el acceso  a nuestros servicios, por
     *  ejemplo por motivos de seguridad
     *
     *  Para modificar el request deberá ir en el PRE, si que requiere modificar el response deberá de ir en el POST
     *
     *
     */

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("Ejecutando el filtro pre");
        // PERMITE MODIFICAR EL REQUEST
        // Pasamos el valor token desde el request al response
        exchange.getRequest().mutate().headers(h->h.add("token", "12345") );

        return chain.filter(exchange).then(Mono.fromRunnable(()->{
            log.info("Ejecutando el filtro post");
            // Obtenemos el valor token desde el request y los pasamos al response
            Optional.ofNullable(exchange.getRequest().getHeaders().getFirst("token")).ifPresent(valor ->{
                exchange.getResponse().getHeaders().add("token",valor );
            });
            exchange.getResponse().getCookies().add("color", ResponseCookie.from("color", "rojo").build());
//            exchange.getResponse().getHeaders().setContentType(MediaType.TEXT_PLAIN);
        }));
    }

    @Override
    public int getOrder() {
        return 10;
    }
}
