package services;

import dao.ArchivoDAO;
import dao.ProyectoDAO;
import dao.UsuarioDAO;
import dto.ArchivoDTO;
import dto.ArchivoCreateDTO;
import entity.Archivo;
import entity.Proyecto;
import entity.Usuario;
import java.util.List;
import java.util.stream.Collectors;

public class ArchivoService {

    private ArchivoDAO archivoDAO = new ArchivoDAO();
    private ProyectoDAO proyectoDAO = new ProyectoDAO();
    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    public ArchivoDTO getArchivoPorId(int id) {
        Archivo archivo = archivoDAO.getArchivoPorId(id);
        return (archivo != null) ? toDTO(archivo) : null;
    }

    public List<ArchivoDTO> getArchivosPorProyecto(int proyectoId) {
        return archivoDAO.getArchivosPorProyecto(proyectoId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<ArchivoDTO> getArchivosSubidosPorUsuario(int usuarioId) {
        return archivoDAO.getArchivosSubidosPorUsuario(usuarioId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public ArchivoDTO crearArchivo(ArchivoCreateDTO createDTO) {
        Archivo archivo = new Archivo();
        archivo.setNombre(createDTO.getNombre());
        archivo.setTipo(createDTO.getTipo());
        archivo.setUrl(createDTO.getUrl());
        archivo.setTamano(createDTO.getTamano());

        // Asignar proyecto
        Proyecto proyecto = proyectoDAO.getProyectoPorId(createDTO.getProyecto_id());
        if (proyecto == null) {
            return null; // Error: proyecto no existe
        }
        archivo.setProyecto(proyecto);

        // Asignar usuario
        Usuario usuario = usuarioDAO.getUsuarioPorId(createDTO.getUsuario_id());
        if (usuario == null) {
            return null; // Error: usuario no existe
        }
        archivo.setUsuario(usuario);

        archivoDAO.crearArchivo(archivo);
        return toDTO(archivo);
    }

    public void eliminarArchivo(int id) {
        archivoDAO.eliminarArchivo(id);
    }

    // Mapeador
    private ArchivoDTO toDTO(Archivo archivo) {
        ArchivoDTO dto = new ArchivoDTO();
        dto.setId(archivo.getId());
        dto.setNombre(archivo.getNombre());
        dto.setTipo(archivo.getTipo());
        dto.setUrl(archivo.getUrl());
        dto.setTamano(archivo.getTamano());
        dto.setFecha_subida(archivo.getFecha_subida());
        
        if (archivo.getProyecto() != null) {
            dto.setProyecto_id(archivo.getProyecto().getId());
        }
        
        if (archivo.getUsuario() != null) {
            dto.setUsuario_id(archivo.getUsuario().getId());
            dto.setNombreUsuario(archivo.getUsuario().getNombre() + " " + archivo.getUsuario().getApellido());
        }
        
        return dto;
    }
}