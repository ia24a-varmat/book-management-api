package org.example.bookapi.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.example.bookapi.model.Category;
import org.example.bookapi.util.HibernateUtil;

import java.util.List;

public class CategoryDao {

    public Category findById(int id) {
        EntityManager em =
                HibernateUtil.getEntityManagerFactory()
                        .createEntityManager();

        Category category = em.find(Category.class, id);

        em.close();

        return category;
    }

    public List<Category> findAll() {
        EntityManager em =
                HibernateUtil.getEntityManagerFactory()
                        .createEntityManager();

        List<Category> categories =
                em.createQuery(
                        "FROM Category",
                        Category.class
                ).getResultList();

        em.close();

        return categories;
    }

    public void save(Category category) {
        EntityManager em =
                HibernateUtil.getEntityManagerFactory()
                        .createEntityManager();

        EntityTransaction tx = em.getTransaction();

        tx.begin();
        em.persist(category);
        tx.commit();

        em.close();
    }

    public void update(Category category) {
        EntityManager em =
                HibernateUtil.getEntityManagerFactory()
                        .createEntityManager();

        EntityTransaction tx = em.getTransaction();

        tx.begin();
        em.merge(category);
        tx.commit();

        em.close();
    }

    public boolean delete(int id) {
        EntityManager em =
                HibernateUtil.getEntityManagerFactory()
                        .createEntityManager();

        EntityTransaction tx = em.getTransaction();

        Category category = em.find(Category.class, id);

        if (category == null) {
            em.close();
            return false;
        }

        tx.begin();
        em.remove(category);
        tx.commit();

        em.close();

        return true;
    }
}