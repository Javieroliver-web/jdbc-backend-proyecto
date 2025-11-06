package dao;

import entity.Usuario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery; // Para consultas tipadas
import java.util.List;

public class UsuarioDAO {

    // Es mejor tener un lugar central para manejar esto, 
    // pero por ahora funciona para probar.
    private EntityManagerFactory emf;

    public UsuarioDAO() {
        // "proyecto-pu" debe coincidir con tu <persistence-unit name="...">
        // en el archivo persistence.xml
        try {
            this.emf = Persistence.createEntityManagerFactory("proyecto-pu");
        } catch (Exception e) {
            System.err.println("Error al crear EntityManagerFactory: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private EntityManager getEntityManager() {
        if (emf == null) {
            throw new IllegalStateException("EntityManagerFactory no está inicializado.");
        }
        return emf.createEntityManager();
    }

    // CREATE
    public void crearUsuario(Usuario usuario) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(usuario); // Guarda el nuevo usuario
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    // READ (por ID)
    public Usuario getUsuarioPorId(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Usuario.class, id);
        } finally {
            em.close();
        }
    }

    // READ (todos)
    public List<Usuario> getTodosLosUsuarios() {
        EntityManager em = getEntityManager();
        try {
            // JPQL (Java Persistence Query Language)
            TypedQuery<Usuario> query = em.createQuery("SELECT u FROM Usuario u", Usuario.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    // UPDATE
    public Usuario actualizarUsuario(Usuario usuario) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            Usuario usuarioActualizado = em.merge(usuario); // Actualiza
            em.getTransaction().commit();
            return usuarioActualizado;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
            return null;
        } finally {
            em.close();
        }
    }

    // DELETE
    public void eliminarUsuario(int id) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            Usuario usuario = em.find(Usuario.class, id);
            if (usuario != null) {
                em.remove(usuario); // Elimina
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
}