import jakarta.persistence.EntityManagerFactory; // <-- IMPORT NUEVO
import jakarta.persistence.Persistence;        // <-- IMPORT NUEVO
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;

public class Main {
    public static void main(String[] args) throws Exception {

        // --- INICIO DE LO NUEVO ---
        // Forzamos la inicialización de JPA (Hibernate)
        // Esto leerá tu persistence.xml y creará las tablas
        System.out.println("Inicializando conexión con la base de datos (JPA)...");
        EntityManagerFactory emf;
        try {
            // "miUnidadDePersistencia" debe coincidir con el nombre en tu persistence.xml
            emf = Persistence.createEntityManagerFactory("miUnidadDePersistencia");
            System.out.println("¡Conexión a la base de datos (JPA) inicializada!");

            // NOTA: Más adelante, necesitarás pasar este 'emf' a tus DAOs 
            // para que puedan hacer consultas. Por ahora, solo con crearlo
            // forzaremos la creación de las tablas.

        } catch (Exception e) {
            System.out.println("¡¡ERROR FATAL!! No se pudo inicializar JPA.");
            e.printStackTrace();
            return; // Detenemos la aplicación si la BD falla
        }
        // --- FIN DE LO NUEVO ---


        System.out.println("Iniciando servidor de la API...");

        // 1. Crear el Servidor
        // (El resto de tu código de Jetty sigue igual)
        Server server = new Server(8080);
        // ... (etc)

        // ... (el resto de tu método main)
    }
}