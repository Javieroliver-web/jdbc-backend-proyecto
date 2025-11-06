package services;

import dao.UsuarioDAO;
import dto.UsuarioDTO;
import dto.UsuarioCreateDTO;
import entity.Usuario;
import java.util.List;
import java.util.stream.Collectors;

public class UsuarioService {

    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    public List<UsuarioDTO> getTodosLosUsuarios() {
        return usuarioDAO.getTodosLosUsuarios().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public UsuarioDTO getUsuarioPorId(int id) {
        Usuario usuario = usuarioDAO.getUsuarioPorId(id);
        return (usuario != null) ? toDTO(usuario) : null;
    }

    public UsuarioDTO crearUsuario(UsuarioCreateDTO createDTO) {
        // Mapea de DTO a Entidad
        Usuario nuevoUsuario = new Usuario();
        
        // ¡Aquí es donde ocurrían tus errores!
        // Ahora 'nuevoUsuario' (del tipo Usuario) SÍ tiene estos métodos.
        nuevoUsuario.setNombre(createDTO.getNombre());
        nuevoUsuario.setApellido(createDTO.getApellido());
        nuevoUsuario.setEmail(createDTO.getEmail());
        nuevoUsuario.setPassword(createDTO.getPassword()); // En un proyecto real, hashea esto
        nuevoUsuario.setRol(createDTO.getRol());
        nuevoUsuario.setAvatar(createDTO.getAvatar());
        
        // Guarda la entidad
        usuarioDAO.crearUsuario(nuevoUsuario);
        
        // Mapea la entidad (con el nuevo ID) de vuelta a un DTO para la respuesta
        return toDTO(nuevoUsuario);
    }

    // --- Mapeadores ---

    private UsuarioDTO toDTO(Usuario usuario) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(usuario.getId());
        dto.setNombre(usuario.getNombre());
        dto.setApellido(usuario.getApellido());
        dto.setEmail(usuario.getEmail());
        dto.setRol(usuario.getRol());
        dto.setAvatar(usuario.getAvatar());
        dto.setFecha_registro(usuario.getFecha_registro());
        return dto;
    }
    
    // (No necesitas un 'toEntity' aquí porque ya lo haces
    // manualmente en 'crearUsuario', pero es un patrón común)
}