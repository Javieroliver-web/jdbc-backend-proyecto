package controller;

import dto.ProyectoDTO;
import dto.ProyectoCreateDTO;
import services.ProyectoService;
import util.JWTUtil;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/proyectos")
@Produces(MediaType.APPLICATION_JSON)
public class ProyectoController {

    private ProyectoService proyectoService = new ProyectoService();

    @GET
    public Response getTodosLosProyectos() {
        List<ProyectoDTO> proyectos = proyectoService.getTodosLosProyectos();
        return Response.ok(proyectos).build();
    }

    @GET
    @Path("/{id}")
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
    public Response crearProyecto(
        ProyectoCreateDTO createDTO,
        @HeaderParam("Authorization") String authHeader
    ) {
        try {
            // Validar token
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity("Token no proporcionado")
                        .build();
            }

            // Extraer usuario_id del token
            String token = authHeader.substring(7);
            int usuarioId = JWTUtil.getUserIdFromToken(token);
            
            // IMPORTANTE: Sobrescribir con el ID del token
            createDTO.setUsuario_id(usuarioId);
            
            ProyectoDTO nuevoProyecto = proyectoService.crearProyecto(createDTO);
            
            if (nuevoProyecto == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Error al crear el proyecto")
                        .build();
            }
            
            return Response.status(Response.Status.CREATED).entity(nuevoProyecto).build();
            
        } catch (RuntimeException e) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(e.getMessage())
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error: " + e.getMessage())
                    .build();
        }
    }
    
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response actualizarProyecto(@PathParam("id") int id, ProyectoDTO proyectoDTO) {
        return Response.ok(proyectoDTO).build();
    }

    @DELETE
    @Path("/{id}")
    public Response eliminarProyecto(@PathParam("id") int id) {
        return Response.noContent().build();
    }
}