package services;

import dao.NotificacionDAO;
import dao.UsuarioDAO;
import dto.NotificacionCreateDTO;
import dto.NotificacionDTO;
import entity.Notificacion;
import entity.Usuario;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class NotificacionService {

    private NotificacionDAO notificacionDAO;
    private UsuarioDAO usuarioDAO;

    public NotificacionService() {
        this.notificacionDAO = new NotificacionDAO();
        this.usuarioDAO = new UsuarioDAO(); 
    }

    public NotificacionDTO crearNotificacion(NotificacionCreateDTO createDTO) {
        Usuario usuario = usuarioDAO.getUsuarioPorId(createDTO.getUsuario_id());
        if (usuario == null) {
            System.err.println("❌ Error: No se puede crear notificación para un usuario_id que no existe: " + createDTO.getUsuario_id());
            return null; 
        }
        Notificacion notificacion = new Notificacion();
        notificacion.setMensaje(createDTO.getMensaje());
        notificacion.setTipo(createDTO.getTipo());
        notificacion.setUsuario(usuario); 
        notificacion.setLeida(false);
        notificacion.setFecha(new Date()); 
        try {
            notificacionDAO.crearNotificacion(notificacion);
            return convertirA_DTO(notificacion);
        } catch (Exception e) {
            System.err.println("❌ Error en NotificacionService al crear: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public List<NotificacionDTO> getNotificacionesPorUsuario(int usuarioId) {
        List<Notificacion> notificaciones = notificacionDAO.getNotificacionesPorUsuario(usuarioId);
        return notificaciones.stream()
                .map(this::convertirA_DTO)
                .collect(Collectors.toList());
    }

    public List<NotificacionDTO> getNotificacionesNoLeidasPorUsuario(int usuarioId) {
        List<Notificacion> notificaciones = notificacionDAO.getNotificacionesNoLeidasPorUsuario(usuarioId);
        return notificaciones.stream()
                .map(this::convertirA_DTO)
                .collect(Collectors.toList());
    }

    public boolean marcarComoLeida(int notificacionId) {
        return notificacionDAO.marcarComoLeida(notificacionId);
    }

    public int marcarTodasComoLeidas(int usuarioId) {
        return notificacionDAO.marcarTodasComoLeidas(usuarioId);
    }

    // --- ¡AQUÍ ESTÁ EL NUEVO MÉTODO AÑADIDO! ---
    /**
     * Llama al DAO para eliminar una notificación por su ID.
     * @param notificacionId El ID de la notificación.
     * @return true si se eliminó, false si no.
     */
    public boolean eliminarNotificacion(int notificacionId) {
        return notificacionDAO.eliminarNotificacion(notificacionId);
    }


    /**
     * Método helper privado para convertir una Entidad a un DTO.
     */
    private NotificacionDTO convertirA_DTO(Notificacion notificacion) {
        NotificacionDTO dto = new NotificacionDTO();
        dto.setId(notificacion.getId());
        dto.setMensaje(notificacion.getMensaje());
        dto.setTipo(notificacion.getTipo());
        dto.setLeida(notificacion.isLeida());
        dto.setFecha(notificacion.getFecha());
        dto.setUsuario_id(notificacion.getUsuario().getId()); 
        return dto;
    }
}