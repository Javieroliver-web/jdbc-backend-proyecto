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
            System.out.println("✅ EntityManagerFactory para TareaDAO inicializado correctamente");
        } catch (Exception e) {
            System.err.println("❌ Error al inicializar EntityManagerFactory en TareaDAO");
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
            System.out.println("✅ Tarea creada con ID: " + tarea.getId());
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("❌ Error al crear tarea: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    // READ por ID
    public Tarea getTareaPorId(int id) {
        EntityManager em = getEntityManager();
        try {
            Tarea tarea = em.find(Tarea.class, id);
            if (tarea != null) {
                // Inicializar colecciones lazy para evitar LazyInitializationException
                tarea.getUsuariosAsignados().size();
                tarea.getUsuariosFavoritos().size();
                System.out.println("✅ Tarea encontrada: ID=" + id + ", Título=" + tarea.getTitulo());
            } else {
                System.out.println("⚠️ No se encontró tarea con ID: " + id);
            }
            return tarea;
        } catch (Exception e) {
            System.err.println("❌ Error al buscar tarea por ID: " + e.getMessage());
            e.printStackTrace();
            return null;
        } finally {
            em.close();
        }
    }

    // READ todas las tareas de un proyecto
    public List<Tarea> getTareasPorProyecto(int proyectoId) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Tarea> query = em.createQuery(
                "SELECT t FROM Tarea t WHERE t.proyecto.id = :proyectoId ORDER BY t.id DESC", 
                Tarea.class
            );
            query.setParameter("proyectoId", proyectoId);
            List<Tarea> tareas = query.getResultList();
            
            System.out.println("📋 Tareas encontradas para proyecto " + proyectoId + ": " + tareas.size());
            return tareas;
        } catch (Exception e) {
            System.err.println("❌ Error al buscar tareas por proyecto: " + e.getMessage());
            e.printStackTrace();
            return List.of(); // Retornar lista vacía en caso de error
        } finally {
            em.close();
        }
    }

    // READ tareas asignadas a un usuario
    public List<Tarea> getTareasAsignadasAUsuario(int usuarioId) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Tarea> query = em.createQuery(
                "SELECT DISTINCT t FROM Tarea t JOIN t.usuariosAsignados u WHERE u.id = :usuarioId ORDER BY t.id DESC",
                Tarea.class
            );
            query.setParameter("usuarioId", usuarioId);
            List<Tarea> tareas = query.getResultList();
            
            System.out.println("📋 Tareas asignadas al usuario " + usuarioId + ": " + tareas.size());
            
            // Log detallado para debugging
            if (tareas.isEmpty()) {
                System.out.println("⚠️ No hay tareas asignadas al usuario " + usuarioId);
            } else {
                tareas.forEach(t -> System.out.println("  - Tarea ID: " + t.getId() + ", Título: " + t.getTitulo()));
            }
            
            return tareas;
        } catch (Exception e) {
            System.err.println("❌ Error al buscar tareas asignadas: " + e.getMessage());
            e.printStackTrace();
            return List.of();
        } finally {
            em.close();
        }
    }

    // READ tareas favoritas de un usuario
    public List<Tarea> getTareasFavoritasDeUsuario(int usuarioId) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Tarea> query = em.createQuery(
                "SELECT DISTINCT t FROM Tarea t JOIN t.usuariosFavoritos u WHERE u.id = :usuarioId ORDER BY t.id DESC",
                Tarea.class
            );
            query.setParameter("usuarioId", usuarioId);
            List<Tarea> tareas = query.getResultList();
            
            System.out.println("⭐ Tareas favoritas del usuario " + usuarioId + ": " + tareas.size());
            return tareas;
        } catch (Exception e) {
            System.err.println("❌ Error al buscar tareas favoritas: " + e.getMessage());
            e.printStackTrace();
            return List.of();
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
            System.out.println("✅ Tarea actualizada: ID=" + actualizada.getId());
            return actualizada;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("❌ Error al actualizar tarea: " + e.getMessage());
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
                System.out.println("✅ Tarea eliminada: ID=" + id);
            } else {
                System.out.println("⚠️ No se pudo eliminar: Tarea con ID " + id + " no encontrada");
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("❌ Error al eliminar tarea: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    // Asignar usuario a tarea - CORREGIDO
    public void asignarUsuario(int tareaId, int usuarioId) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            
            System.out.println("🔄 Intentando asignar usuario " + usuarioId + " a tarea " + tareaId);
            
            Tarea tarea = em.find(Tarea.class, tareaId);
            if (tarea == null) {
                System.err.println("❌ ERROR: No se encontró la tarea con ID: " + tareaId);
                em.getTransaction().rollback();
                return;
            }
            
            Usuario usuario = em.find(Usuario.class, usuarioId);
            if (usuario == null) {
                System.err.println("❌ ERROR: No se encontró el usuario con ID: " + usuarioId);
                em.getTransaction().rollback();
                return;
            }
            
            tarea.getUsuariosAsignados().size(); // Inicializar lazy
            
            if (tarea.getUsuariosAsignados().contains(usuario)) {
                System.out.println("⚠️ El usuario " + usuarioId + " ya está asignado a la tarea " + tareaId);
                em.getTransaction().rollback();
                return;
            }
            
            // ---------------------------------------------------
            // --- ¡¡AQUÍ ESTÁ LA CORRECCIÓN!! ---
            // ---------------------------------------------------
            // Usamos el helper de Tarea.java para sincronizar AMBOS lados
            
            tarea.addUsuarioAsignado(usuario); 
            
            // ---------------------------------------------------
            
            em.merge(tarea);
            
            em.getTransaction().commit();
            System.out.println("✅ Usuario " + usuarioId + " asignado exitosamente a tarea " + tareaId);
            System.out.println("   Total de usuarios asignados (en memoria): " + tarea.getUsuariosAsignados().size());
            
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("❌ ERROR al asignar usuario: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    // Desasignar usuario de tarea - CORREGIDO
    public void desasignarUsuario(int tareaId, int usuarioId) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            
            System.out.println("🔄 Intentando desasignar usuario " + usuarioId + " de tarea " + tareaId);
            
            Tarea tarea = em.find(Tarea.class, tareaId);
            Usuario usuario = em.find(Usuario.class, usuarioId);
            
            if (tarea != null && usuario != null) {
                tarea.getUsuariosAsignados().size(); // Inicializar lazy
                
                // ---------------------------------------------------
                // --- ¡¡AQUÍ ESTÁ LA CORRECCIÓN!! ---
                // ---------------------------------------------------
                // Verificamos si existe, y si sí, usamos el helper
                // que sincroniza AMBOS lados
                
                if (tarea.getUsuariosAsignados().contains(usuario)) {
                    tarea.removeUsuarioAsignado(usuario); // <-- CORREGIDO
                    em.merge(tarea);
                    System.out.println("✅ Usuario " + usuarioId + " desasignado de tarea " + tareaId);
                } else {
                    System.out.println("⚠️ El usuario " + usuarioId + " no estaba asignado a la tarea " + tareaId);
                }
            } else {
                System.err.println("❌ Tarea o Usuario no encontrado para desasignar");
            }
            
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("❌ ERROR al desasignar usuario: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    // Marcar/desmarcar favorito - CORREGIDO
    public void toggleFavorito(int tareaId, int usuarioId) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            
            System.out.println("🔄 Toggle favorito - Tarea: " + tareaId + ", Usuario: " + usuarioId);
            
            Tarea tarea = em.find(Tarea.class, tareaId);
            Usuario usuario = em.find(Usuario.class, usuarioId);
            
            if (tarea == null) {
                System.err.println("❌ ERROR: No se encontró la tarea con ID: " + tareaId);
                em.getTransaction().rollback();
                return;
            }
            
            if (usuario == null) {
                System.err.println("❌ ERROR: No se encontró el usuario con ID: " + usuarioId);
                em.getTransaction().rollback();
                return;
            }
            
            tarea.getUsuariosFavoritos().size(); // Inicializar lazy
            
            // ---------------------------------------------------
            // --- ¡¡AQUÍ ESTÁ LA CORRECCIÓN!! ---
            // ---------------------------------------------------
            // Usamos los helpers de Tarea.java para sincronizar AMBOS lados
            
            if (tarea.getUsuariosFavoritos().contains(usuario)) {
                tarea.removeUsuarioFavorito(usuario); // <-- CORREGIDO
                System.out.println("⭐ Tarea " + tareaId + " removida de favoritos del usuario " + usuarioId);
            } else {
                tarea.addUsuarioFavorito(usuario); // <-- CORREGIDO
                System.out.println("⭐ Tarea " + tareaId + " agregada a favoritos del usuario " + usuarioId);
            }
            
            // ---------------------------------------------------
            
            em.merge(tarea);
            em.getTransaction().commit();
            
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("❌ ERROR al toggle favorito: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    // Verificar si un usuario está asignado a una tarea - NUEVO
    public boolean estaUsuarioAsignado(int tareaId, int usuarioId) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery(
                "SELECT COUNT(t) FROM Tarea t JOIN t.usuariosAsignados u WHERE t.id = :tareaId AND u.id = :usuarioId",
                Long.class
            );
            query.setParameter("tareaId", tareaId);
            query.setParameter("usuarioId", usuarioId);
            
            Long count = query.getSingleResult();
            return count > 0;
        } catch (Exception e) {
            System.err.println("❌ Error al verificar asignación: " + e.getMessage());
            return false;
        } finally {
            em.close();
        }
    }

    // Verificar si una tarea es favorita de un usuario - NUEVO
    public boolean esTareaFavorita(int tareaId, int usuarioId) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery(
                "SELECT COUNT(t) FROM Tarea t JOIN t.usuariosFavoritos u WHERE t.id = :tareaId AND u.id = :usuarioId",
                Long.class
            );
            query.setParameter("tareaId", tareaId);
            query.setParameter("usuarioId", usuarioId);
            
            Long count = query.getSingleResult();
            return count > 0;
        } catch (Exception e) {
            System.err.println("❌ Error al verificar favorito: " + e.getMessage());
            return false;
        } finally {
            em.close();
        }
    }

    // Cerrar EntityManagerFactory - IMPORTANTE para cleanup
    public void close() {
        if (emf != null && emf.isOpen()) {
            emf.close();
            System.out.println("✅ EntityManagerFactory cerrado correctamente");
        }
    }
}