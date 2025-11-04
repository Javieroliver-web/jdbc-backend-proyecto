package entity;

import com.fasterxml.jackson.annotation.JsonIgnore; // <-- IMPORTA ESTO
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

    // --- Relaciones (con @JsonIgnore) ---

    @JsonIgnore // <-- AÑADE ESTO
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false) 
    private Usuario creador;

    @JsonIgnore // <-- AÑADE ESTO
    @OneToMany(mappedBy = "proyecto")
    private Set<Tarea> tareas;

    @JsonIgnore // <-- AÑADE ESTO
    @ManyToMany
    @JoinTable(
        name = "usuario_proyecto", 
        joinColumns = @JoinColumn(name = "proyecto_id"), 
        inverseJoinColumns = @JoinColumn(name = "usuario_id") 
    )
    private Set<Usuario> miembros;

    // --- Constructores, Getters y Setters ---
    // (Asegúrate de tenerlos todos)
    public Proyecto() {
    }
    
    // ... (Todos tus Getters y Setters) ...
}