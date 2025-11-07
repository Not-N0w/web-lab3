package com.github.not.n0w.weblab3;

import com.github.not.n0w.weblab3.model.Hit;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.Serializable;
import java.util.List;


@ManagedBean(name="hitRepository")
@ApplicationScoped
public class HitRepository implements Serializable {
    private transient EntityManagerFactory emf;

    public HitRepository() {
        try {
            emf = Persistence.createEntityManagerFactory("HitsPersistenceUnit");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Не удалось инициализировать EntityManagerFactory", e);
        }
    }

    public void clear() {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.createQuery("DELETE FROM Hit").executeUpdate();
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public void save(Hit hit) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(hit);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public List<Hit> getAll() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT h FROM Hit h", Hit.class).getResultList();
        } finally {
            em.close();
        }
    }
}
