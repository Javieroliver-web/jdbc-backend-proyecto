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
    public void crearArchivo(Archivo archivo) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(archivo);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    // READ por ID
    public Archivo getArchivoPorId(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Archivo.class, id);
        } finally {
            em.close();
        }
    }

    // READ todos los archivos de un proyecto
    public List<Archivo> getArchivosPorProyecto(int proyectoId) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Archivo> query = em.createQuery(
                "SELECT a FROM Archivo a WHERE a.proyecto.id = :proyectoId ORDER BY a.fecha_subida DESC",
                Archivo.class
            );
            query.setParameter("proyectoId", proyectoId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    // READ archivos subidos por un usuario
    public List<Archivo> getArchivosSubidosPorUsuario(int usuarioId) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Archivo> query = em.createQuery(
                "SELECT a FROM Archivo a WHERE a.usuario.id = :usuarioId ORDER BY a.fecha_subida DESC",
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

    // DELETE
    public void eliminarArchivo(int id) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            Archivo archivo = em.find(Archivo.class, id);
            if (archivo != null) {
                em.remove(archivo);
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