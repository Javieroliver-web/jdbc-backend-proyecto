package entity;

import com.fasterxml.jackson.annotation.JsonIgnore; // <-- IMPORTA ESTO
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Set; 

@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nombre", length = 100)
    private String nombre;

    @Column(name = "apellido", length = 100)
    private String apellido;

    @Column(name = "email", unique = true, length = 255)
    private String email;

    @Column(name = "password", length = 255)
    private String password;

    @Column(name = "rol", length = 50)
    private String rol;

    @Column(name = "avatar", length = 255)
    private String avatar;

    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro;

    // --- RELACIONES CON @JsonIgnore ---

    @JsonIgnore // <-- AÑADE ESTO
    @OneToMany(mappedBy = "creador")
    private Set<Proyecto> proyectosCreados;

    @JsonIgnore // <-- AÑADE ESTO
    @ManyToMany(mappedBy = "miembros")
    private Set<Proyecto> proyectosDondeEsMiembro;

    @JsonIgnore // <-- AÑADE ESTO
    @ManyToMany(mappedBy = "usuariosAsignados")
    private Set<Tarea> tareasAsignadas;

    @JsonIgnore // <-- AÑADE ESTO
    @ManyToMany(mappedBy = "usuariosQueDieronFavorito")
    private Set<Tarea> tareasFavoritas;

    @JsonIgnore // <-- AÑADE ESTO
    @OneToMany(mappedBy = "usuario")
    private Set<Notificacion> notificaciones;

    // --- Constructores ---
    public Usuario() {
    }

    // --- Getters y Setters ---
    // (Asegúrate de tenerlos para TODOS los campos, 
    // incluidos los nuevos Set<...>)

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    // ... (El resto de Getters y Setters que ya tenías) ...
    
    // ... Y también genera Getters/Setters para las nuevas relaciones ...
    
    public Set<Proyecto> getProyectosCreados() {
        return proyectosCreados;
    }

    public void setProyectosCreados(Set<Proyecto> proyectosCreados) {
        this.proyectosCreados = proyectosCreados;
    }

    public Set<Proyecto> getProyectosDondeEsMiembro() {
        return proyectosDondeEsMiembro;
    }

    public void setProyectosDondeEsMiembro(Set<Proyecto> proyectosDondeEsMiembro) {
        this.proyectosDondeEsMiembro = proyectosDondeEsMiembro;
    }

    // ... (etc. para tareasAsignadas, tareasFavoritas, notificaciones) ...
}