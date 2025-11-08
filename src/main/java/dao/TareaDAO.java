package dao;

import entity.Tarea;
import entity.Usuario;
import jakarta.persistence.*;
import java.util.List;

public class TareaDAO {

    private EntityManagerFactory emf;

    public TareaDAO() {
        try {
            this.emf = Persistence.createEntityManagerFactory("proyecto-pu");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    // CREATE
    public void crearTarea(Tarea tarea) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(tarea);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    // READ por ID
    public Tarea getTareaPorId(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Tarea.class, id);
        } finally {
            em.close();
        }
    }

    // READ todas las tareas de un proyecto
    public List<Tarea> getTareasPorProyecto(int proyectoId) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Tarea> query = em.createQuery(
                "SELECT t FROM Tarea t WHERE t.proyecto.id = :proyectoId", 
                Tarea.class
            );
            query.setParameter("proyectoId", proyectoId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    // READ tareas asignadas a un usuario
    public List<Tarea> getTareasAsignadasAUsuario(int usuarioId) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Tarea> query = em.createQuery(
                "SELECT t FROM Tarea t JOIN t.usuariosAsignados u WHERE u.id = :usuarioId",
                Tarea.class
            );
            query.setParameter("usuarioId", usuarioId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    // READ tareas favoritas de un usuario
    public List<Tarea> getTareasFavoritasDeUsuario(int usuarioId) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Tarea> query = em.createQuery(
                "SELECT t FROM Tarea t JOIN t.usuariosFavoritos u WHERE u.id = :usuarioId",
                Tarea.class
            );
            query.setParameter("usuarioId", usuarioId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    // UPDATE
    public Tarea actualizarTarea(Tarea tarea) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            Tarea actualizada = em.merge(tarea);
            em.getTransaction().commit();
            return actualizada;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            e.printStackTrace();
            return null;
        } finally {
            em.close();
        }
    }

    // DELETE
    public void eliminarTarea(int id) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            Tarea tarea = em.find(Tarea.class, id);
            if (tarea != null) {
                em.remove(tarea);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    // Asignar usuario a tarea
    public void asignarUsuario(int tareaId, int usuarioId) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            Tarea tarea = em.find(Tarea.class, tareaId);
            Usuario usuario = em.find(Usuario.class, usuarioId);
            if (tarea != null && usuario != null) {
                tarea.getUsuariosAsignados().add(usuario);
                em.merge(tarea);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    // Marcar/desmarcar favorito
    public void toggleFavorito(int tareaId, int usuarioId) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            Tarea tarea = em.find(Tarea.class, tareaId);
            Usuario usuario = em.find(Usuario.class, usuarioId);
            if (tarea != null && usuario != null) {
                if (tarea.getUsuariosFavoritos().contains(usuario)) {
                    tarea.getUsuariosFavoritos().remove(usuario);
                } else {
                    tarea.getUsuariosFavoritos().add(usuario);
                }
                em.merge(tarea);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
}