package com.github.not.n0w.weblab3.repository;

import com.github.not.n0w.weblab3.model.Hit;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;

import javax.faces.bean.ManagedBean;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.Serializable;
import java.util.List;


@Slf4j
@Named
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

    public void clearAllBySessionId(String sessionId) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.createQuery("DELETE FROM Hit WHERE sessionId = :sessionId")
                    .setParameter("sessionId", sessionId)
                    .executeUpdate();
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public void sync(String oldSessionId, String newSessionId) {
        log.info(oldSessionId + " - " + newSessionId);
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
             em.createQuery(
                            "UPDATE Hit h SET h.sessionId = :newSessionId WHERE h.sessionId = :oldSessionId")
                    .setParameter("newSessionId", newSessionId)
                    .setParameter("oldSessionId", oldSessionId)
                    .executeUpdate();

            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public Hit save(Hit hit) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(hit);
            em.flush();
            em.getTransaction().commit();
            return hit;
        } finally {
            em.close();
        }
    }

    public List<Hit> getAllBySessionId(String sessionId) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT h FROM Hit h WHERE h.sessionId = :sessionId",
                            Hit.class
                    )
                    .setParameter("sessionId", sessionId)
                    .getResultList();        } finally {
            em.close();
        }
    }
}
