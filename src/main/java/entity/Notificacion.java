package entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notificacion")
public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "TEXT")
    private String mensaje;

    @Column(length = 50)
    private String tipo;

    private boolean leida;

    private LocalDateTime fecha;

    // --- Relaciones ---

    // Relación: Muchas notificaciones pertenecen a UN usuario.
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    // --- Constructores, Getters y Setters ---

    public Notificacion() {
    }

    // ... Genera todos los Getters y Setters para todos los campos ...
}