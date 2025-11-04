package entity;

import com.fasterxml.jackson.annotation.JsonIgnore; // <-- IMPORTA ESTO
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "tarea")
public class Tarea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 255)
    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(length = 50)
    private String estado;

    @Column(name = "fecha_limite")
    private LocalDateTime fechaLimite;

    // --- Relaciones (con @JsonIgnore) ---

    @JsonIgnore // <-- AÑADE ESTO
    @ManyToOne
    @JoinColumn(name = "proyecto_id", nullable = false)
    private Proyecto proyecto;

    @JsonIgnore // <-- AÑADE ESTO
    @ManyToMany
    @JoinTable(
        name = "tarea_asignada", 
        joinColumns = @JoinColumn(name = "tarea_id"),
        inverseJoinColumns = @JoinColumn(name = "usuario_id")
    )
    private Set<Usuario> usuariosAsignados;

    @JsonIgnore // <-- AÑADE ESTO
    @ManyToMany
    @JoinTable(
        name = "tarea_favorita", 
        joinColumns = @JoinColumn(name = "tarea_id"),
        inverseJoinColumns = @JoinColumn(name = "usuario_id")
    )
    private Set<Usuario> usuariosQueDieronFavorito;

    // --- Constructores, Getters y Setters ---
    public Tarea() {
    }

    // ... (Todos tus Getters y Setters) ...
}