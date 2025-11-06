package controller; // El paquete es 'controller', no 'controller.java'

import dto.UsuarioDTO;
import dto.UsuarioCreateDTO;
import services.UsuarioService; // Corregido a 'services' (plural)
import java.util.List;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/usuarios")
public class UsuarioController {

    // Asegúrate de que tu clase UsuarioService esté en el paquete 'services'
    private UsuarioService usuarioService = new UsuarioService(); 

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTodosLosUsuarios() {
        List<UsuarioDTO> usuarios = usuarioService.getTodosLosUsuarios();
        return Response.ok(usuarios).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsuarioPorId(@PathParam("id") int id) {
        UsuarioDTO dto = usuarioService.getUsuarioPorId(id);
        if (dto != null) {
            return Response.ok(dto).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response crearUsuario(UsuarioCreateDTO createDTO) {
        // Asumiendo que tu servicio devuelve el DTO del usuario creado
        UsuarioDTO nuevoUsuario = usuarioService.crearUsuario(createDTO);
        return Response.status(Response.Status.CREATED).entity(nuevoUsuario).build();
    }

    // ... Aquí irían tus métodos PUT y DELETE ...
}