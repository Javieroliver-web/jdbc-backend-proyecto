package services;

import dao.TareaDAO;
import dao.ProyectoDAO;
import dto.TareaDTO;
import dto.TareaCreateDTO;
import entity.Tarea;
import entity.Proyecto;
import java.util.List;
import java.util.stream.Collectors;

public class TareaService {

    private TareaDAO tareaDAO = new TareaDAO();
    private ProyectoDAO proyectoDAO = new ProyectoDAO();

    public TareaDTO getTareaPorId(int id) {
        Tarea tarea = tareaDAO.getTareaPorId(id);
        return (tarea != null) ? toDTO(tarea) : null;
    }

    public List<TareaDTO> getTareasPorProyecto(int proyectoId) {
        return tareaDAO.getTareasPorProyecto(proyectoId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<TareaDTO> getTareasAsignadasAUsuario(int usuarioId) {
        return tareaDAO.getTareasAsignadasAUsuario(usuarioId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<TareaDTO> getTareasFavoritasDeUsuario(int usuarioId) {
        return tareaDAO.getTareasFavoritasDeUsuario(usuarioId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public TareaDTO crearTarea(TareaCreateDTO createDTO) {
        Tarea tarea = new Tarea();
        tarea.setTitulo(createDTO.getTitulo());
        tarea.setDescripcion(createDTO.getDescripcion());
        tarea.setEstado(createDTO.getEstado() != null ? createDTO.getEstado() : "pendiente");
        tarea.setFecha_limite(createDTO.getFecha_limite());

        // Asignar proyecto
        Proyecto proyecto = proyectoDAO.getProyectoPorId(createDTO.getProyecto_id());
        if (proyecto == null) {
            return null; // Error: proyecto no existe
        }
        tarea.setProyecto(proyecto);

        tareaDAO.crearTarea(tarea);
        return toDTO(tarea);
    }

    public TareaDTO actualizarTarea(int id, TareaDTO tareaDTO) {
        Tarea tarea = tareaDAO.getTareaPorId(id);
        if (tarea == null) {
            return null;
        }

        tarea.setTitulo(tareaDTO.getTitulo());
        tarea.setDescripcion(tareaDTO.getDescripcion());
        tarea.setEstado(tareaDTO.getEstado());
        tarea.setFecha_limite(tareaDTO.getFecha_limite());

        Tarea actualizada = tareaDAO.actualizarTarea(tarea);
        return toDTO(actualizada);
    }

    public void eliminarTarea(int id) {
        tareaDAO.eliminarTarea(id);
    }

    public void asignarUsuario(int tareaId, int usuarioId) {
        tareaDAO.asignarUsuario(tareaId, usuarioId);
    }

    public void toggleFavorito(int tareaId, int usuarioId) {
        tareaDAO.toggleFavorito(tareaId, usuarioId);
    }

    // Mapeador
    private TareaDTO toDTO(Tarea tarea) {
        TareaDTO dto = new TareaDTO();
        dto.setId(tarea.getId());
        dto.setTitulo(tarea.getTitulo());
        dto.setDescripcion(tarea.getDescripcion());
        dto.setEstado(tarea.getEstado());
        dto.setFecha_limite(tarea.getFecha_limite());
        
        if (tarea.getProyecto() != null) {
            dto.setProyecto_id(tarea.getProyecto().getId());
        }
        
        return dto;
    }
}