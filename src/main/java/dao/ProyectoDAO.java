package dao;

import entity.Proyecto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class ProyectoDAO {

    private EntityManagerFactory emf;

    public ProyectoDAO() {
        // "proyecto-pu" debe coincidir con tu persistence.xml
        try {
            this.emf = Persistence.createEntityManagerFactory("proyecto-pu");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public Proyecto getProyectoPorId(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Proyecto.class, id);
        } finally {
            em.close();
        }
    }

    public List<Proyecto> getTodosLosProyectos() {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Proyecto> query = em.createQuery("SELECT p FROM Proyecto p", Proyecto.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public void crearProyecto(Proyecto proyecto) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(proyecto);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    // ... aquí irían los métodos de actualizar y eliminar ...
}