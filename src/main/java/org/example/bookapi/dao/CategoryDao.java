package org.example.bookapi.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.example.bookapi.model.Category;
import org.example.bookapi.util.HibernateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

public class CategoryDao {

    private static final Logger logger = LoggerFactory.getLogger(CategoryDao.class);
    public Category findById(int id) {
        logger.info("Searching category with id {}", id);
        EntityManager em =
                HibernateUtil.getEntityManagerFactory()
                        .createEntityManager();

        Category category = em.find(Category.class, id);

        em.close();
        if (category == null) {
            logger.warn("Category with id {} not found", id);
        }

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
        logger.info("Saving category with id {}", category.getCategoryId());

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
        logger.info("Updating category with id {}", category.getCategoryId());

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
        logger.info("Deleting category with id {}", id);
        EntityManager em =
                HibernateUtil.getEntityManagerFactory()
                        .createEntityManager();

        EntityTransaction tx = em.getTransaction();

        Category category = em.find(Category.class, id);

        if (category == null) {
            logger.warn("Category with id {} not found, delete cancelled", id);
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