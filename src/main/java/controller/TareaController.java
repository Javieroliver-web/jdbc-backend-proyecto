package controller;

import dto.TareaDTO;
import dto.TareaCreateDTO;
import services.TareaService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.Map;

@Path("/tareas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TareaController {

    private TareaService tareaService = new TareaService();

    @GET
    @Path("/{id}")
    public Response getTareaPorId(@PathParam("id") int id) {
        TareaDTO tarea = tareaService.getTareaPorId(id);
        if (tarea != null) {
            return Response.ok(tarea).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @GET
    @Path("/proyecto/{proyectoId}")
    public Response getTareasPorProyecto(@PathParam("proyectoId") int proyectoId) {
        List<TareaDTO> tareas = tareaService.getTareasPorProyecto(proyectoId);
        return Response.ok(tareas).build();
    }

    @GET
    @Path("/usuario/{usuarioId}/asignadas")
    public Response getTareasAsignadas(@PathParam("usuarioId") int usuarioId) {
        List<TareaDTO> tareas = tareaService.getTareasAsignadasAUsuario(usuarioId);
        return Response.ok(tareas).build();
    }

    @GET
    @Path("/usuario/{usuarioId}/favoritas")
    public Response getTareasFavoritas(@PathParam("usuarioId") int usuarioId) {
        List<TareaDTO> tareas = tareaService.getTareasFavoritasDeUsuario(usuarioId);
        return Response.ok(tareas).build();
    }

    @POST
    public Response crearTarea(TareaCreateDTO createDTO) {
        TareaDTO nuevaTarea = tareaService.crearTarea(createDTO);
        
        if (nuevaTarea == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("El proyecto no existe")
                    .build();
        }
        
        return Response.status(Response.Status.CREATED).entity(nuevaTarea).build();
    }

    @PUT
    @Path("/{id}")
    public Response actualizarTarea(@PathParam("id") int id, TareaDTO tareaDTO) {
        TareaDTO actualizada = tareaService.actualizarTarea(id, tareaDTO);
        
        if (actualizada == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        
        return Response.ok(actualizada).build();
    }

    @DELETE
    @Path("/{id}")
    public Response eliminarTarea(@PathParam("id") int id) {
        tareaService.eliminarTarea(id);
        return Response.noContent().build();
    }

    @POST
    @Path("/{id}/asignar")
    public Response asignarUsuario(@PathParam("id") int tareaId, Map<String, Integer> body) {
        Integer usuarioId = body.get("usuario_id");
        if (usuarioId == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("usuario_id es requerido")
                    .build();
        }
        
        tareaService.asignarUsuario(tareaId, usuarioId);
        return Response.ok().entity(Map.of("message", "Usuario asignado exitosamente")).build();
    }

    @POST
    @Path("/{id}/favorito")
    public Response toggleFavorito(@PathParam("id") int tareaId, Map<String, Integer> body) {
        Integer usuarioId = body.get("usuario_id");
        if (usuarioId == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("usuario_id es requerido")
                    .build();
        }
        
        tareaService.toggleFavorito(tareaId, usuarioId);
        return Response.ok().entity(Map.of("message", "Favorito actualizado")).build();
    }
}