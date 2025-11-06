package com.proyecto;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;

public class Main {
    public static void main(String[] args) {
        Server server = new Server(8080); // Inicia el servidor en el puerto 8080
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);

        // Configura el Servlet de Jersey
        // Esto le dice a Jersey que maneje todas las peticiones bajo /api/*
        ServletHolder holder = context.addServlet(ServletContainer.class, "/api/*");
        holder.setInitOrder(1);

        // --- ESTA ES LA SOLUCIÓN A TU ERROR ---
        // 1. Le decimos a Jersey que escanee el paquete 'controller' para encontrar tus Endpoints
        holder.setInitParameter("jersey.config.server.provider.packages", "controller");
        
        // 2. Le decimos a Jersey que use GSON para manejar JSON
        // (En lugar de 'JsonFeature' de Jackson, usamos 'GsonFeature' de Gson)
        holder.setInitParameter("jersey.config.server.provider.classnames", 
            "org.glassfish.jersey.media.json.gson.GsonFeature");

        try {
            server.start();
            System.out.println("Servidor iniciado en http://localhost:8080/api");
            server.join();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error al iniciar el servidor: " + e.getMessage());
        } finally {
            server.destroy();
        }
    }
}