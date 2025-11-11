package dao;

import entity.Notificacion;
import jakarta.persistence.*;
import java.util.List;

public class NotificacionDAO {

    private EntityManagerFactory emf;

    public NotificacionDAO() {
        this.emf = Persistence.createEntityManagerFactory("proyecto-pu");
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void crearNotificacion(Notificacion notificacion) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(notificacion);
            em.getTransaction().commit();
            System.out.println("✅ Notificación creada con ID: " + notificacion.getId());
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

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

    public boolean marcarComoLeida(int notificacionId) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            Notificacion notificacion = em.find(Notificacion.class, notificacionId);
            
            if (notificacion != null) {
                notificacion.setLeida(true);
                em.merge(notificacion);
                em.getTransaction().commit();
                System.out.println("✅ Notificación " + notificacionId + " marcada como leída.");
                return true; 
            } else {
                System.out.println("⚠️ No se encontró notificación con ID: " + notificacionId);
                em.getTransaction().rollback();
                return false; 
            }
        } catch (Exception e) {
            System.err.println("❌ Error al marcar notificación como leída: " + e.getMessage());
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            return false; 
        } finally {
            em.close();
        }
    }

    public List<Notificacion> getNotificacionesNoLeidasPorUsuario(int usuarioId) {
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

    public int marcarTodasComoLeidas(int usuarioId) {
        EntityManager em = getEntityManager();
        int updateCount = 0;
        try {
            em.getTransaction().begin();
            
            Query query = em.createQuery(
                "UPDATE Notificacion n SET n.leida = true " +
                "WHERE n.usuario.id = :usuarioId AND n.leida = false"
            );
            query.setParameter("usuarioId", usuarioId);
            updateCount = query.executeUpdate();
            em.getTransaction().commit();
            System.out.println("✅ Marcadas " + updateCount + " notificaciones como leídas para el usuario " + usuarioId);
            
        } catch (Exception e) {
            System.err.println("❌ Error en la actualización masiva de notificaciones: " + e.getMessage());
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        } finally {
            em.close();
        }
        return updateCount;
    }

    // --- ¡AQUÍ ESTÁ EL NUEVO MÉTODO AÑADIDO! ---
    /**
     * Elimina una notificación por su ID.
     * @param notificacionId El ID de la notificación a eliminar.
     * @return true si se eliminó, false si no se encontró.
     */
    public boolean eliminarNotificacion(int notificacionId) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            
            // 1. Buscar la notificación
            Notificacion notificacion = em.find(Notificacion.class, notificacionId);
            
            if (notificacion != null) {
                // 2. Si se encuentra, eliminarla
                em.remove(notificacion);
                em.getTransaction().commit();
                System.out.println("✅ Notificación " + notificacionId + " eliminada.");
                return true; // Éxito
            } else {
                // 3. Si no se encuentra, no hacer nada
                System.out.println("⚠️ No se encontró notificación para eliminar: " + notificacionId);
                em.getTransaction().rollback();
                return false; // No se encontró
            }
            
        } catch (Exception e) {
            System.err.println("❌ Error al eliminar notificación: " + e.getMessage());
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            return false; // Error
        } finally {
            em.close();
        }
    }
}