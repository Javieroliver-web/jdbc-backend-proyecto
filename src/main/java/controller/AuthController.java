package controller;

import dto.LoginDTO;
import dto.UsuarioCreateDTO;
import dto.UsuarioDTO;
import dto.AuthResponseDTO;
import services.AuthService;
import util.JWTUtil;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * Controlador para autenticación (login/register)
 */
@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthController {

    private AuthService authService = new AuthService();

    /**
     * POST /api/auth/register
     * Registra un nuevo usuario
     */
    @POST
    @Path("/register")
    public Response register(UsuarioCreateDTO createDTO) {
        try {
            AuthResponseDTO response = authService.register(createDTO);
            
            if (response.isSuccess()) {
                return Response.status(Response.Status.CREATED).entity(response).build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST).entity(response).build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            AuthResponseDTO errorResponse = new AuthResponseDTO(false, "Error al registrar usuario: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorResponse).build();
        }
    }

    /**
     * POST /api/auth/login
     * Autentica un usuario
     */
    @POST
    @Path("/login")
    public Response login(LoginDTO loginDTO) {
        try {
            AuthResponseDTO response = authService.login(loginDTO);
            
            if (response.isSuccess()) {
                return Response.ok(response).build();
            } else {
                return Response.status(Response.Status.UNAUTHORIZED).entity(response).build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            AuthResponseDTO errorResponse = new AuthResponseDTO(false, "Error al iniciar sesión: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorResponse).build();
        }
    }

    /**
     * GET /api/auth/me
     * Obtiene el perfil del usuario autenticado
     * Requiere token JWT en el header: Authorization: Bearer <token>
     */
    @GET
    @Path("/me")
    public Response getProfile(@HeaderParam("Authorization") String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity(new AuthResponseDTO(false, "Token no proporcionado"))
                        .build();
            }

            String token = authHeader.substring(7); // Remover "Bearer "
            
            // Validar token y obtener usuario ID
            int usuarioId = JWTUtil.getUserIdFromToken(token);
            
            // Obtener perfil del usuario
            UsuarioDTO usuario = authService.getProfile(usuarioId);
            
            if (usuario != null) {
                return Response.ok(usuario).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(new AuthResponseDTO(false, "Usuario no encontrado"))
                        .build();
            }
        } catch (RuntimeException e) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(new AuthResponseDTO(false, e.getMessage()))
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new AuthResponseDTO(false, "Error al obtener perfil"))
                    .build();
        }
    }

    /**
     * POST /api/auth/logout
     * Cierra sesión (en realidad solo devuelve un mensaje, el cliente debe borrar el token)
     */
    @POST
    @Path("/logout")
    public Response logout() {
        AuthResponseDTO response = new AuthResponseDTO(true, "Sesión cerrada exitosamente");
        return Response.ok(response).build();
    }
}