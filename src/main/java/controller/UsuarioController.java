package controller;

import dao.UsuarioDAO; // <-- IMPORTA TU DAO
import entity.Usuario; // <-- IMPORTA TU ENTIDAD
import jakarta.inject.Inject; // <-- IMPORT para Inyección
import jakarta.persistence.EntityManagerFactory;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.List; // <-- IMPORT para Listas

@Path("/usuarios")
public class UsuarioController {

    private final EntityManagerFactory emf;
    private final UsuarioDAO usuarioDAO;

    // 1. Jersey "inyectará" el EMF que registramos en Main.java
    @Inject
    public UsuarioController(EntityManagerFactory emf) {
        this.emf = emf;
        this.usuarioDAO = new UsuarioDAO(emf); // Creamos el DAO
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Usuario> getUsuarios() {
        // 2. Usamos el DAO para buscar todos los usuarios
        return usuarioDAO.findAll();
    }
}