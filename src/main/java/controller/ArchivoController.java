package controller;

import dto.ArchivoCreateDTO;
import dto.ArchivoDTO;
import services.ArchivoService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.Map;

@Path("/archivos") 
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ArchivoController {

    private ArchivoService archivoService = new ArchivoService();

    @POST
    public Response crearArchivo(ArchivoCreateDTO createDTO) {
        try {
            ArchivoDTO nuevoArchivo = archivoService.crearArchivo(createDTO);
            if (nuevoArchivo == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(Map.of("error", "Error al crear archivo. Verifica que proyecto_id y usuario_id existan."))
                        .build();
            }
            return Response.status(Response.Status.CREATED).entity(nuevoArchivo).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("error", "Error interno: " + e.getMessage()))
                    .build();
        }
    }

    @GET
    @Path("/proyecto/{proyectoId}")
    public Response getArchivosPorProyecto(@PathParam("proyectoId") int proyectoId) {
        // Esto ahora funcionará sin el error 500
        List<ArchivoDTO> archivos = archivoService.getArchivosPorProyecto(proyectoId);
        return Response.ok(archivos).build();
    }

    @GET
    @Path("/usuario/{usuarioId}")
    public Response getArchivosSubidosPorUsuario(@PathParam("usuarioId") int usuarioId) {
        // Esto ahora funcionará sin el error 500
        List<ArchivoDTO> archivos = archivoService.getArchivosSubidosPorUsuario(usuarioId);
        return Response.ok(archivos).build();
    }

    @DELETE
    @Path("/{id}")
    public Response eliminarArchivo(@PathParam("id") int id) {
        try {
            // --- ¡CORRECCIÓN AQUÍ! ---
            // El servicio ahora devuelve true/false
            boolean exito = archivoService.eliminarArchivo(id);
            
            if (exito) {
                // 204 No Content es la respuesta estándar para un DELETE exitoso
                return Response.noContent().build();
            } else {
                // Si el DAO devuelve false (no encontrado), devolvemos 404
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(Map.of("error", "Archivo no encontrado"))
                        .build();
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("error", "Error al eliminar: " + e.getMessage()))
                    .build();
        }
    }
}