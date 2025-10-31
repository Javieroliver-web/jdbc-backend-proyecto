package dao;

import entity.Usuario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class UsuarioDAO {

    private final EntityManagerFactory emf;

    // Recibe el EMF desde el controlador
    public UsuarioDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    // Método para encontrar todos los usuarios
    public List<Usuario> findAll() {
        EntityManager em = emf.createEntityManager();
        try {
            // Usamos una consulta JPQL (similar a SQL pero con Clases)
            return em.createQuery("SELECT u FROM Usuario u", Usuario.class).getResultList();
        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }
    }
}