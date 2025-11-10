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

        ServletHolder holder = context.addServlet(ServletContainer.class, "/api/*");
        holder.setInitOrder(1);

        // Escanear el paquete 'controller'
        holder.setInitParameter("jersey.config.server.provider.packages", "controller");
        
        // CAMBIO IMPORTANTE: Usar Jackson en lugar de GSON
        holder.setInitParameter("jersey.config.server.provider.classnames", 
            "org.glassfish.jersey.jackson.JacksonFeature");

        try {
            server.start();
            System.out.println("╔════════════════════════════════════════════════════════╗");
            System.out.println("║   🚀 Servidor iniciado en http://localhost:8080/api  ║");
            System.out.println("╚════════════════════════════════════════════════════════╝");
            System.out.println();
            System.out.println("📚 Endpoints disponibles:");
            System.out.println("   - POST   /api/auth/register");
            System.out.println("   - POST   /api/auth/login");
            
            server.join();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("❌ Error al iniciar el servidor: " + e.getMessage());
        } finally {
            server.destroy();
        }
    }
}