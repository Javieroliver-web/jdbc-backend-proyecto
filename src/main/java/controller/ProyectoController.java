package controller;

import dto.ProyectoDTO;
import dto.ProyectoCreateDTO;
import services.ProyectoService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/proyectos")
public class ProyectoController {

    private ProyectoService proyectoService = new ProyectoService();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTodosLosProyectos() {
        List<ProyectoDTO> proyectos = proyectoService.getTodosLosProyectos();
        return Response.ok(proyectos).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProyectoPorId(@PathParam("id") int id) {
        ProyectoDTO dto = proyectoService.getProyectoPorId(id);
        if (dto != null) {
            return Response.ok(dto).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response crearProyecto(ProyectoCreateDTO createDTO) {
        ProyectoDTO nuevoProyecto = proyectoService.crearProyecto(createDTO);
        if (nuevoProyecto == null) {
            // Esto sucede si el usuario_id no es válido
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity("El usuario creador no existe.")
                           .build();
        }
        return Response.status(Response.Status.CREATED).entity(nuevoProyecto).build();
    }
}