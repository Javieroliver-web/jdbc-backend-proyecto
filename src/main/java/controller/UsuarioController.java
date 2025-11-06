package controller;

import dto.UsuarioDTO;
import dto.UsuarioCreateDTO;
import services.UsuarioService; // Corregido a 'services'
import java.util.List;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/usuarios")
@Produces(MediaType.APPLICATION_JSON) // Ponerlo aquí aplica a todos los métodos
public class UsuarioController {

    private UsuarioService usuarioService = new UsuarioService();

    @GET
    public Response getTodosLosUsuarios() {
        List<UsuarioDTO> usuarios = usuarioService.getTodosLosUsuarios();
        return Response.ok(usuarios).build();
    }

    @GET
    @Path("/{id}")
    public Response getUsuarioPorId(@PathParam("id") int id) {
        UsuarioDTO dto = usuarioService.getUsuarioPorId(id);
        if (dto != null) {
            return Response.ok(dto).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    /**
     * ¡ESTA ES LA CORRECCIÓN!
     * Añadimos @Consumes para que acepte JSON.
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON) // <-- ¡¡AQUÍ ESTÁ LA MAGIA!!
    public Response crearUsuario(UsuarioCreateDTO createDTO) {
        UsuarioDTO nuevoUsuario = usuarioService.crearUsuario(createDTO);
        return Response.status(Response.Status.CREATED).entity(nuevoUsuario).build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON) // <-- ¡También lo necesitas aquí!
    public Response actualizarUsuario(@PathParam("id") int id, UsuarioDTO usuarioDTO) {
        // Lógica de actualización...
        return Response.ok(usuarioDTO).build();
    }

    @DELETE
    @Path("/{id}")
    public Response eliminarUsuario(@PathParam("id") int id) {
        // Lógica de eliminación...
        return Response.noContent().build();
    }
}