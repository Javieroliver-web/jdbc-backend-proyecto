package entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Set; // ¡Importante añadir Set!

// @Entity le dice a Hibernate: "Esta clase representa una tabla en la BD"
@Entity
// @Table le dice a Hibernate el nombre exacto de la tabla
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

    // --- NUEVAS RELACIONES (Añadidas) ---

    // Un usuario puede crear MUCHOS proyectos
    @OneToMany(mappedBy = "creador")
    private Set<Proyecto> proyectosCreados;

    // Un usuario puede ser miembro de MUCHOS proyectos
    @ManyToMany(mappedBy = "miembros")
    private Set<Proyecto> proyectosDondeEsMiembro;

    // Un usuario puede tener MUCHAS tareas asignadas
    @ManyToMany(mappedBy = "usuariosAsignados")
    private Set<Tarea> tareasAsignadas;

    // Un usuario puede tener MUCHAS tareas favoritas
    @ManyToMany(mappedBy = "usuariosQueDieronFavorito")
    private Set<Tarea> tareasFavoritas;

    // Un usuario puede tener MUCHAS notificaciones
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