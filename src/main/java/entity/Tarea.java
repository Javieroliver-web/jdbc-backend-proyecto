package entity;

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

    // --- Relaciones ---

    // Relación: Muchas tareas pertenecen a UN proyecto.
    @ManyToOne
    @JoinColumn(name = "proyecto_id", nullable = false)
    private Proyecto proyecto;

    // Relación: Muchos usuarios están asignados a Muchas tareas.
    @ManyToMany
    @JoinTable(
        name = "tarea_asignada", // Tabla intermedia
        joinColumns = @JoinColumn(name = "tarea_id"),
        inverseJoinColumns = @JoinColumn(name = "usuario_id")
    )
    private Set<Usuario> usuariosAsignados;

    // Relación: Muchos usuarios marcan como favoritas Muchas tareas.
    @ManyToMany
    @JoinTable(
        name = "tarea_favorita", // Tabla intermedia
        joinColumns = @JoinColumn(name = "tarea_id"),
        inverseJoinColumns = @JoinColumn(name = "usuario_id")
    )
    private Set<Usuario> usuariosQueDieronFavorito;

    // --- Constructores, Getters y Setters ---

    public Tarea() {
    }

    // ... Genera todos los Getters y Setters para todos los campos ...
}