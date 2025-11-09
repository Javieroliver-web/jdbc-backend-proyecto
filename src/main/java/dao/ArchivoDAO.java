package dao;

import entity.Archivo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class ArchivoDAO {

    @PersistenceContext
    private EntityManager em;

    /**
     * Busca un archivo por su ID.
     */
    public Archivo findById(int id) {
        return em.find(Archivo.class, id);
    }

    /**
     * Guarda un archivo nuevo (persist) o actualiza uno existente (merge).
     */
    public Archivo save(Archivo archivo) {
        // Merge es seguro tanto para crear como para actualizar
        return em.merge(archivo);
    }

    /**
     * Elimina un archivo de la base de datos usando su ID.
     */
    public void delete(int id) {
        Archivo archivo = findById(id);
        if (archivo != null) {
            em.remove(archivo);
        }
    }

    /**
     * Devuelve todos los archivos de la base de datos.
     */
    public List<Archivo> findAll() {
        TypedQuery<Archivo> query = em.createQuery("SELECT a FROM Archivo a", Archivo.class);
        return query.getResultList();
    }

    /**
     * Busca todos los archivos asociados a un proyecto específico.
     * Esto es útil para tu API (ej. GET /proyectos/{id}/archivos)
     */
    public List<Archivo> findByProyectoId(int proyectoId) {
        TypedQuery<Archivo> query = em.createQuery(
            "SELECT a FROM Archivo a WHERE a.proyecto.id = :proyectoId", 
            Archivo.class
        );
        query.setParameter("proyectoId", proyectoId);
        return query.getResultList();
    }
}