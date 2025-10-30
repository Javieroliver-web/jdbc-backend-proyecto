package entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "proyecto")
public class Proyecto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 255)
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "fecha_inicio")
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin")
    private LocalDate fechaFin;

    @Column(length = 50)
    private String estado;

    // --- Relaciones ---

    // Relación: Muchos proyectos pueden ser creados por UN usuario.
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false) // Esta es la FK
    private Usuario creador;

    // Relación: Un proyecto tiene MUCHAS tareas.
    // "mappedBy" le dice a Hibernate que la clave foránea está en la clase Tarea (en el campo "proyecto")
    @OneToMany(mappedBy = "proyecto")
    private Set<Tarea> tareas;

    // Relación: Muchos usuarios son miembros de Muchos proyectos.
    @ManyToMany
    @JoinTable(
        name = "usuario_proyecto", // Nombre de la tabla intermedia
        joinColumns = @JoinColumn(name = "proyecto_id"), // Columna de esta entidad
        inverseJoinColumns = @JoinColumn(name = "usuario_id") // Columna de la otra entidad
    )
    private Set<Usuario> miembros;

    // --- Constructores, Getters y Setters ---

    public Proyecto() {
    }

    // (Omito los Getters y Setters por brevedad, pero debes generarlos
    // igual que hiciste en la clase Usuario)
    
    // ... Genera todos los Getters y Setters para todos los campos...
}