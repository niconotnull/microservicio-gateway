###API GATEWAY SPRING-CLOUD

Gateway es un servidor de enrutamiento dinámico, compuesto por filtros cada unos de los cuales está
enfocado en una tarea en específico, por ejemplo: autorización, seguridad, monitorización o análisis
estadístico, ya se integran filtros por defectos. El filtro principal es el enrutamiento de los microservicios,
pero también se pueden construir filtros personalizados por ejemplo para implementar multi-lenguaje o
modificar una respuesta, manejo de errores, modificar las cabeceras del request, modificar los parámetros 
que se están enviando, interceptar el acceso a una ruta en particular y modificar su enrutamiento.

Spring-cloud-gateway que es propio de spring y es el que se recomienda utilizar, ya que actualmente el
servidor de enrutamiento de Netflix Zuul se encuentra en mantenimiento y ya no es recomendable utilizarlo, por 
otra parte Spring-cloud-gateway utiliza programación reactiva por lo tanto los flujos de comunicación son
mucho más rápidos no se bloquean, trabajan con procesos paralelos independientes, comunicación asíncrona.

Al API-GATEWAY también se le conoce como puerta de enlace o servidor perimetral de acceso centralizado en 
la arquitectura de microservicios y será el encargado  de enrutar cada unos de los servicios a una url base
y apartir de esta ruta base se puede acceder a las distintas rutas individuales de cada servicio, tambien se
integra el balanceo de carga.

Al pasar todas estas peticiones del ecosistema de nuestros micro-servicios a través del api-gateway este se
puede encargar de realizar tarea en común como el tema de seguridad de autorización, el manejo de CORS, se puede
manejar todo el tema de seguridad a través del api gateway no es necesario implementar el tema de seguridad en cada
micro-servicio sino que se centraliza todo en la puerta de enlace.

Cada vez que se levanta una  instancia de un micro-servicio primero va y se registra en el servidor de nombre(EUREKA)
la comunicación se realiza a través del balanceo de carga la comunicación entre los micro-servicio se realiza a
través del API-REST pero el acceso a cada uno de ellos es a través del GATEWAY ya sea de ZUUL o  Spring-cloud-gateway 
cualquier aplicación cliente que se conecta a nuestro ecosistema de microservicios siempre pasa por el Gateway