package entity;

import com.fasterxml.jackson.annotation.JsonIgnore; // <-- IMPORTA ESTO
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

    // --- Relaciones (con @JsonIgnore) ---

    @JsonIgnore // <-- AÑADE ESTO
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    // --- Constructores, Getters y Setters ---
    public Notificacion() {
    }

    // ... (Todos tus Getters y Setters) ...
}