package dao;

import entity.Notificacion;
import jakarta.persistence.*;
import java.util.List;

public class NotificacionDAO {

    private EntityManagerFactory emf;

    public NotificacionDAO() {
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
    public void crearNotificacion(Notificacion notificacion) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(notificacion);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    // READ por ID
    public Notificacion getNotificacionPorId(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Notificacion.class, id);
        } finally {
            em.close();
        }
    }

    // READ todas las notificaciones de un usuario
    public List<Notificacion> getNotificacionesPorUsuario(int usuarioId) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Notificacion> query = em.createQuery(
                "SELECT n FROM Notificacion n WHERE n.usuario.id = :usuarioId ORDER BY n.fecha DESC",
                Notificacion.class
            );
            query.setParameter("usuarioId", usuarioId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    // READ notificaciones no leídas de un usuario
    public List<Notificacion> getNotificacionesNoLeidas(int usuarioId) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Notificacion> query = em.createQuery(
                "SELECT n FROM Notificacion n WHERE n.usuario.id = :usuarioId AND n.leida = false ORDER BY n.fecha DESC",
                Notificacion.class
            );
            query.setParameter("usuarioId", usuarioId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    // UPDATE - Marcar como leída
    public void marcarComoLeida(int id) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            Notificacion notificacion = em.find(Notificacion.class, id);
            if (notificacion != null) {
                notificacion.setLeida(true);
                em.merge(notificacion);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    // UPDATE - Marcar todas como leídas
    public void marcarTodasComoLeidas(int usuarioId) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            Query query = em.createQuery(
                "UPDATE Notificacion n SET n.leida = true WHERE n.usuario.id = :usuarioId"
            );
            query.setParameter("usuarioId", usuarioId);
            query.executeUpdate();
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    // DELETE
    public void eliminarNotificacion(int id) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            Notificacion notificacion = em.find(Notificacion.class, id);
            if (notificacion != null) {
                em.remove(notificacion);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    // DELETE todas las notificaciones de un usuario
    public void eliminarTodasPorUsuario(int usuarioId) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            Query query = em.createQuery(
                "DELETE FROM Notificacion n WHERE n.usuario.id = :usuarioId"
            );
            query.setParameter("usuarioId", usuarioId);
            query.executeUpdate();
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
}