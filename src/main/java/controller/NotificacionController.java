package controller;

import dto.NotificacionCreateDTO;
import dto.NotificacionDTO;
import services.NotificacionService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.Map;

@Path("/notificaciones")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class NotificacionController {

    private NotificacionService notificacionService = new NotificacionService();

    @POST 
    public Response crearNotificacion(NotificacionCreateDTO createDTO) {
        try {
            NotificacionDTO nuevaNotificacion = notificacionService.crearNotificacion(createDTO);
            if (nuevaNotificacion == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(Map.of("error", "Error al crear la notificación, revise los datos (ej. usuario_id no existe)"))
                        .build();
            }
            return Response.status(Response.Status.CREATED).entity(nuevaNotificacion).build();
            
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("error", "Error interno: " + e.getMessage()))
                    .build();
        }
    }

    @GET
    @Path("/usuario/{usuarioId}")
    public Response getNotificacionesPorUsuario(@PathParam("usuarioId") int usuarioId) {
        List<NotificacionDTO> notificaciones = notificacionService.getNotificacionesPorUsuario(usuarioId);
        return Response.ok(notificaciones).build();
    }

    @GET
    @Path("/usuario/{usuarioId}/no-leidas")
    public Response getNotificacionesNoLeidasPorUsuario(@PathParam("usuarioId") int usuarioId) {
        List<NotificacionDTO> notificaciones = notificacionService.getNotificacionesNoLeidasPorUsuario(usuarioId);
        return Response.ok(notificaciones).build();
    }

    @PUT
    @Path("/{id}/leer")
    public Response marcarComoLeida(@PathParam("id") int notificacionId) {
        boolean exito = notificacionService.marcarComoLeida(notificacionId);
        if (exito) {
            return Response.ok(Map.of("message", "Notificación marcada como leída")).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "Notificación no encontrada"))
                    .build();
        }
    }

    @PUT
    @Path("/usuario/{usuarioId}/leer-todas")
    public Response marcarTodasComoLeidas(@PathParam("usuarioId") int usuarioId) {
        try {
            int updateCount = notificacionService.marcarTodasComoLeidas(usuarioId);
            return Response.ok(Map.of(
                "message", "Todas las notificaciones han sido marcadas como leídas",
                "actualizadas", updateCount
            )).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("error", "Error interno: " + e.getMessage()))
                    .build();
        }
    }

    // --- ¡AQUÍ ESTÁ EL NUEVO ENDPOINT AÑADIDO! ---
    /**
     * Endpoint para eliminar una notificación por su ID.
     * DELETE /api/notificaciones/{id}
     */
    @DELETE
    @Path("/{id}")
    public Response eliminarNotificacion(@PathParam("id") int id) {
        try {
            boolean exito = notificacionService.eliminarNotificacion(id);
            
            if (exito) {
                // 204 No Content es la respuesta estándar para un DELETE exitoso
                return Response.noContent().build(); 
            } else {
                // 404 Not Found si el DAO no encontró la notificación a eliminar
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(Map.of("error", "Notificación no encontrada"))
                        .build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("error", "Error interno: " + e.getMessage()))
                    .build();
        }
    }
}