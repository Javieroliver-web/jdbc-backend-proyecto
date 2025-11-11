package dao;

import entity.Archivo;
import jakarta.persistence.*;
import java.util.List;

public class ArchivoDAO {

    private EntityManagerFactory emf;

    public ArchivoDAO() {
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
    public Archivo crearArchivo(Archivo archivo) { // Lo cambiamos para que devuelva la entidad creada
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(archivo);
            em.getTransaction().commit();
            System.out.println("✅ Archivo creado con ID: " + archivo.getId());
            return archivo; // Devolvemos el objeto con el ID
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            e.printStackTrace();
            return null; // Devolvemos null si falla
        } finally {
            em.close();
        }
    }

    // READ por ID
    public Archivo getArchivoPorId(int id) {
        EntityManager em = getEntityManager();
        try {
            // Usamos JOIN FETCH aquí también por si se usa
            TypedQuery<Archivo> query = em.createQuery(
                "SELECT a FROM Archivo a JOIN FETCH a.usuario JOIN FETCH a.proyecto WHERE a.id = :id",
                Archivo.class
            );
            query.setParameter("id", id);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null; // No encontrado
        } finally {
            em.close();
        }
    }

    // READ todos los archivos de un proyecto - CORREGIDO
    public List<Archivo> getArchivosPorProyecto(int proyectoId) {
        EntityManager em = getEntityManager();
        try {
            // --- ¡CORRECCIÓN AQUÍ! ---
            // Le decimos a JPA que cargue el usuario y el proyecto EN LA MISMA CONSULTA
            TypedQuery<Archivo> query = em.createQuery(
                "SELECT a FROM Archivo a " +
                "JOIN FETCH a.usuario u " +
                "JOIN FETCH a.proyecto p " +
                "WHERE p.id = :proyectoId ORDER BY a.fecha_subida DESC",
                Archivo.class
            );
            query.setParameter("proyectoId", proyectoId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    // READ archivos subidos por un usuario - CORREGIDO
    public List<Archivo> getArchivosSubidosPorUsuario(int usuarioId) {
        EntityManager em = getEntityManager();
        try {
            // --- ¡CORRECCIÓN AQUÍ! ---
            // También cargamos las relaciones para esta consulta
            TypedQuery<Archivo> query = em.createQuery(
                "SELECT a FROM Archivo a " +
                "JOIN FETCH a.usuario u " +
                "JOIN FETCH a.proyecto p " +
                "WHERE u.id = :usuarioId ORDER BY a.fecha_subida DESC",
                Archivo.class
            );
            query.setParameter("usuarioId", usuarioId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    // UPDATE
    public Archivo actualizarArchivo(Archivo archivo) {
        // (Este método no lo estás usando, pero lo dejamos como estaba)
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            Archivo actualizado = em.merge(archivo);
            em.getTransaction().commit();
            return actualizado;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            e.printStackTrace();
            return null;
        } finally {
            em.close();
        }
    }

    // DELETE - CORREGIDO
    public boolean eliminarArchivo(int id) { // Lo cambiamos para que devuelva boolean
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            Archivo archivo = em.find(Archivo.class, id);
            if (archivo != null) {
                em.remove(archivo);
                em.getTransaction().commit();
                System.out.println("✅ Archivo " + id + " eliminado.");
                return true; // Éxito
            } else {
                System.out.println("⚠️ No se encontró archivo para eliminar: " + id);
                em.getTransaction().rollback();
                return false; // No encontrado
            }
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            e.printStackTrace();
            return false; // Error
        } finally {
            em.close();
        }
    }
}