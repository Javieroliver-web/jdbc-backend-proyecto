package services;

import dao.ProyectoDAO;
import dao.UsuarioDAO; // Necesitaremos esto
import dto.ProyectoDTO;
import dto.ProyectoCreateDTO;
import entity.Proyecto;
import entity.Usuario;

import java.util.List;
import java.util.stream.Collectors;

public class ProyectoService {

    private ProyectoDAO proyectoDAO = new ProyectoDAO();
    private UsuarioDAO usuarioDAO = new UsuarioDAO(); // Para asignar el creador

    public ProyectoDTO getProyectoPorId(int id) {
        Proyecto proyecto = proyectoDAO.getProyectoPorId(id);
        return (proyecto != null) ? toDTO(proyecto) : null;
    }

    public List<ProyectoDTO> getTodosLosProyectos() {
        return proyectoDAO.getTodosLosProyectos().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public ProyectoDTO crearProyecto(ProyectoCreateDTO createDTO) {
        Proyecto proyecto = new Proyecto();
        proyecto.setNombre(createDTO.getNombre());
        proyecto.setDescripcion(createDTO.getDescripcion());
        proyecto.setFecha_inicio(createDTO.getFecha_inicio());
        proyecto.setFecha_fin(createDTO.getFecha_fin());
        proyecto.setEstado(createDTO.getEstado());

        // --- Asignar el creador ---
        // El DTO nos da el ID del creador
        Usuario creador = usuarioDAO.getUsuarioPorId(createDTO.getUsuario_id());
        if (creador == null) {
            // Manejar error: no se puede crear un proyecto sin un creador válido
            return null; 
        }
        proyecto.setCreador(creador);

        proyectoDAO.crearProyecto(proyecto);
        return toDTO(proyecto);
    }

    // --- Mapeadores ---
    private ProyectoDTO toDTO(Proyecto proyecto) {
        ProyectoDTO dto = new ProyectoDTO();
        dto.setId(proyecto.getId());
        dto.setNombre(proyecto.getNombre());
        dto.setDescripcion(proyecto.getDescripcion());
        dto.setFecha_inicio(proyecto.getFecha_inicio());
        dto.setFecha_fin(proyecto.getFecha_fin());
        dto.setEstado(proyecto.getEstado());
        
        // Incluimos el ID del creador en el DTO
        if (proyecto.getCreador() != null) {
            dto.setUsuario_id(proyecto.getCreador().getId());
        }
        
        // (Opcional) Contar participantes, tareas, etc.
        // dto.setNumeroDeTareas(proyecto.getTareas().size());

        return dto;
    }

    public ProyectoDAO getProyectoDAO() {
        return proyectoDAO;
    }

    public void setProyectoDAO(ProyectoDAO proyectoDAO) {
        this.proyectoDAO = proyectoDAO;
    }

    public UsuarioDAO getUsuarioDAO() {
        return usuarioDAO;
    }

    public void setUsuarioDAO(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }
}