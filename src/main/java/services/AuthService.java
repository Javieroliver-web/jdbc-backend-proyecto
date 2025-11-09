package services;

import dao.UsuarioDAO;
import dto.LoginDTO;
import dto.UsuarioCreateDTO;
import dto.UsuarioDTO;
import dto.AuthResponseDTO;
import entity.Usuario;
import util.JWTUtil;
import util.PasswordUtil;

public class AuthService {

    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    /**
     * Registra un nuevo usuario
     */
    public AuthResponseDTO register(UsuarioCreateDTO createDTO) {
        // Verificar si el email ya existe
        Usuario usuarioExistente = usuarioDAO.getTodosLosUsuarios().stream()
                .filter(u -> u.getEmail().equals(createDTO.getEmail()))
                .findFirst()
                .orElse(null);

        if (usuarioExistente != null) {
            return new AuthResponseDTO(false, "El email ya está registrado");
        }

        // Crear nuevo usuario
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombre(createDTO.getNombre());
        nuevoUsuario.setApellido(createDTO.getApellido());
        nuevoUsuario.setEmail(createDTO.getEmail());
        
        // Hashear la contraseña
        String hashedPassword = PasswordUtil.hashPassword(createDTO.getPassword());
        nuevoUsuario.setPassword(hashedPassword);
        
        nuevoUsuario.setRol(createDTO.getRol() != null ? createDTO.getRol() : "usuario");
        nuevoUsuario.setAvatar(createDTO.getAvatar());

        // Guardar en la base de datos
        usuarioDAO.crearUsuario(nuevoUsuario);

        // Generar token JWT
        String token = JWTUtil.generateToken(
            nuevoUsuario.getId(),
            nuevoUsuario.getEmail(),
            nuevoUsuario.getRol()
        );

        // Crear DTO para la respuesta
        UsuarioDTO usuarioDTO = toDTO(nuevoUsuario);

        return new AuthResponseDTO(true, "Usuario registrado exitosamente", token, usuarioDTO);
    }

    /**
     * Autentica un usuario (login)
     */
    public AuthResponseDTO login(LoginDTO loginDTO) {
        // Buscar usuario por email
        Usuario usuario = usuarioDAO.getTodosLosUsuarios().stream()
                .filter(u -> u.getEmail().equals(loginDTO.getEmail()))
                .findFirst()
                .orElse(null);

        if (usuario == null) {
            return new AuthResponseDTO(false, "Credenciales inválidas");
        }

        // Verificar contraseña
        boolean passwordValida = PasswordUtil.checkPassword(
            loginDTO.getPassword(),
            usuario.getPassword()
        );

        if (!passwordValida) {
            return new AuthResponseDTO(false, "Credenciales inválidas");
        }

        // Generar token JWT
        String token = JWTUtil.generateToken(
            usuario.getId(),
            usuario.getEmail(),
            usuario.getRol()
        );

        // Crear DTO para la respuesta
        UsuarioDTO usuarioDTO = toDTO(usuario);

        return new AuthResponseDTO(true, "Login exitoso", token, usuarioDTO);
    }

    /**
     * Obtiene el perfil del usuario autenticado
     */
    public UsuarioDTO getProfile(int usuarioId) {
        Usuario usuario = usuarioDAO.getUsuarioPorId(usuarioId);
        return (usuario != null) ? toDTO(usuario) : null;
    }

    // Mapeador
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
}