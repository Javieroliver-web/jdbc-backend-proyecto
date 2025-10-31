import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;
import org.glassfish.jersey.server.ResourceConfig; // <-- NUEVO IMPORT
import org.glassfish.jersey.internal.inject.AbstractBinder; // <-- NUEVO IMPORT
import org.glassfish.jersey.media.json.jackson.JacksonFeature; // <-- NUEVO IMPORT (para JSON)

public class Main {

    public static void main(String[] args) {

        // --- PASO 1: INICIALIZAR JPA ---
        System.out.println("Inicializando conexión con la base de datos (JPA)...");
        final EntityManagerFactory emf; // <-- La hacemos 'final' para pasarla
        try {
            emf = Persistence.createEntityManagerFactory("miUnidadDePersistencia");
            System.out.println("¡Conexión a la base de datos (JPA) inicializada!");
        } catch (Exception e) {
            System.out.println("¡¡ERROR FATAL!! No se pudo inicializar JPA.");
            e.printStackTrace();
            return;
        }

        // --- PASO 2: CONFIGURAR JERSEY (LA API) ---

        // Creamos la configuración de Jersey
        ResourceConfig config = new ResourceConfig();
        // Le decimos que escanee el paquete 'controller'
        config.packages("controller");
        // Le decimos que use "Jackson" para convertir objetos Java a JSON
        config.register(JacksonFeature.class);

        // Le enseñamos a Jersey cómo "inyectar" el EMF
        config.register(new AbstractBinder() {
            @Override
            protected void configure() {
                bind(emf).to(EntityManagerFactory.class);
            }
        });

        // --- PASO 3: INICIAR EL SERVIDOR WEB (JETTY) ---
        System.out.println("Iniciando servidor de la API...");
        try {
            Server server = new Server(8080);
            ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
            context.setContextPath("/");
            server.setHandler(context);

            // Le pasamos la configuración de Jersey (en vez de los paquetes)
            ServletHolder jerseyServlet = new ServletHolder(new ServletContainer(config));
            context.addServlet(jerseyServlet, "/api/*");
            jerseyServlet.setInitOrder(0);

            server.start();
            System.out.println("¡Servidor iniciado! Escuchando en http://localhost:8080");
            server.join();

        } catch (Exception e) {
            System.out.println("Error al iniciar el servidor Jetty.");
            e.printStackTrace();
        }
    }
}