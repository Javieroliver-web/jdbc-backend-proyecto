package controller;

import dto.ProyectoDTO;
import dto.ProyectoCreateDTO;
import services.ProyectoService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

/**
 * Controlador API REST para gestionar Proyectos.
 * Define los endpoints para el CRUD de Proyectos.
 */
@Path("/proyectos")
@Produces(MediaType.APPLICATION_JSON) // Todos los métodos devolverán JSON
public class ProyectoController {

    private ProyectoService proyectoService = new ProyectoService();

    /**
     * GET /api/proyectos
     * Obtiene una lista de todos los proyectos.
     * @return Lista de ProyectoDTO.
     */
    @GET
    public Response getTodosLosProyectos() {
        List<ProyectoDTO> proyectos = proyectoService.getTodosLosProyectos();
        return Response.ok(proyectos).build(); // 200 OK
    }

    /**
     * GET /api/proyectos/{id}
     * Obtiene un proyecto específico por su ID.
     * @param id El ID del proyecto.
     * @return Un ProyectoDTO o un error 404 si no se encuentra.
     */
    @GET
    @Path("/{id}")
    public Response getProyectoPorId(@PathParam("id") int id) {
        ProyectoDTO dto = proyectoService.getProyectoPorId(id);
        if (dto != null) {
            return Response.ok(dto).build(); // 200 OK
        } else {
            return Response.status(Response.Status.NOT_FOUND).build(); // 404 Not Found
        }
    }

    /**
     * POST /api/proyectos
     * Crea un nuevo proyecto.
     * @param createDTO El DTO con la información para crear el proyecto.
     * @return El nuevo proyecto creado (ProyectoDTO) con un estado 201.
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON) // ¡Correcto! Acepta JSON.
    public Response crearProyecto(ProyectoCreateDTO createDTO) {
        ProyectoDTO nuevoProyecto = proyectoService.crearProyecto(createDTO);
        
        // ¡Tu excelente manejo de errores!
        if (nuevoProyecto == null) {
            // Esto sucede si el usuario_id no es válido
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity("El usuario creador no existe.")
                           .build(); // 400 Bad Request
        }
        
        return Response.status(Response.Status.CREATED).entity(nuevoProyecto).build(); // 201 Created
    }
    
    /**
     * PUT /api/proyectos/{id}
     * Actualiza un proyecto existente.
     * @param id El ID del proyecto a actualizar.
     * @param proyectoDTO El DTO con los datos actualizados.
     * @return El proyecto actualizado.
     */
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON) // ¡También necesario para PUT!
    public Response actualizarProyecto(@PathParam("id") int id, ProyectoDTO proyectoDTO) {
        // Aquí deberías llamar a tu servicio para actualizar
        // ProyectoDTO actualizado = proyectoService.actualizarProyecto(id, proyectoDTO);
        
        // Simplemente devolvemos el DTO como confirmación por ahora
        return Response.ok(proyectoDTO).build(); // 200 OK
    }

    /**
     * DELETE /api/proyectos/{id}
     * Elimina un proyecto por su ID.
     * @param id El ID del proyecto a eliminar.
     * @return Un estado 204 No Content.
     */
    @DELETE
    @Path("/{id}")
    public Response eliminarProyecto(@PathParam("id") int id) {
        // Aquí deberías llamar a tu servicio para eliminar
        // proyectoService.eliminarProyecto(id);
        
        return Response.noContent().build(); // 204 No Content
    }
}