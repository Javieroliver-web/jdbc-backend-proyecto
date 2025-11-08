package com.proyecto;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;

public class Main {
    public static void main(String[] args) {
        Server server = new Server(8080);
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);

        // Configurar el Servlet de Jersey
        ServletHolder holder = context.addServlet(ServletContainer.class, "/api/*");
        holder.setInitOrder(1);

        // Escanear el paquete 'controller' para encontrar todos los endpoints
        holder.setInitParameter("jersey.config.server.provider.packages", "controller");
        
        // Usar GSON para manejar JSON
        holder.setInitParameter("jersey.config.server.provider.classnames", 
            "org.glassfish.jersey.media.json.gson.GsonFeature");

        try {
            server.start();
            System.out.println("╔════════════════════════════════════════════════════════╗");
            System.out.println("║   🚀 Servidor iniciado en http://localhost:8080/api  ║");
            System.out.println("╚════════════════════════════════════════════════════════╝");
            System.out.println();
            System.out.println("📚 Endpoints disponibles:");
            System.out.println("   - POST   /api/auth/register");
            System.out.println("   - POST   /api/auth/login");
            System.out.println("   - GET    /api/auth/me");
            System.out.println("   - POST   /api/auth/logout");
            System.out.println();
            System.out.println("   - GET    /api/usuarios");
            System.out.println("   - POST   /api/usuarios");
            System.out.println("   - GET    /api/usuarios/{id}");
            System.out.println();
            System.out.println("   - GET    /api/proyectos");
            System.out.println("   - POST   /api/proyectos");
            System.out.println("   - GET    /api/proyectos/{id}");
            System.out.println("   - PUT    /api/proyectos/{id}");
            System.out.println("   - DELETE /api/proyectos/{id}");
            System.out.println();
            System.out.println("   - GET    /api/tareas/{id}");
            System.out.println("   - GET    /api/tareas/proyecto/{proyectoId}");
            System.out.println("   - POST   /api/tareas");
            System.out.println("   - PUT    /api/tareas/{id}");
            System.out.println("   - DELETE /api/tareas/{id}");
            System.out.println("   - POST   /api/tareas/{id}/asignar");
            System.out.println("   - POST   /api/tareas/{id}/favorito");
            System.out.println();
            System.out.println("   - GET    /api/notificaciones/usuario/{usuarioId}");
            System.out.println("   - GET    /api/notificaciones/usuario/{usuarioId}/no-leidas");
            System.out.println("   - POST   /api/notificaciones");
            System.out.println("   - PUT    /api/notificaciones/{id}/leer");
            System.out.println("   - DELETE /api/notificaciones/{id}");
            System.out.println();
            System.out.println("   - GET    /api/archivos/proyecto/{proyectoId}");
            System.out.println("   - POST   /api/archivos");
            System.out.println("   - DELETE /api/archivos/{id}");
            System.out.println();
            System.out.println("Presiona Ctrl+C para detener el servidor");
            
            server.join();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("❌ Error al iniciar el servidor: " + e.getMessage());
        } finally {
            server.destroy();
        }
    }
}