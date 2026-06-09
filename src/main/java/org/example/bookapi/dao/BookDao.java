package org.example.bookapi.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.example.bookapi.model.Book;
import org.example.bookapi.util.HibernateUtil;
import java.math.BigDecimal;
import java.time.LocalDate;

import java.util.List;

public class BookDao {

    public Book findById(int id) {
        EntityManager em =
                HibernateUtil.getEntityManagerFactory()
                        .createEntityManager();

        Book book = em.find(Book.class, id);

        em.close();

        return book;
    }

    public List<Book> findAll() {
        EntityManager em =
                HibernateUtil.getEntityManagerFactory()
                        .createEntityManager();

        List<Book> books =
                em.createQuery(
                        "FROM Book",
                        Book.class
                ).getResultList();

        em.close();

        return books;
    }

    public void save(Book book) {
        EntityManager em =
                HibernateUtil.getEntityManagerFactory()
                        .createEntityManager();

        EntityTransaction tx = em.getTransaction();

        tx.begin();
        em.persist(book);
        tx.commit();

        em.close();
    }

    public void update(Book book) {
        EntityManager em =
                HibernateUtil.getEntityManagerFactory()
                        .createEntityManager();

        EntityTransaction tx = em.getTransaction();

        tx.begin();
        em.merge(book);
        tx.commit();

        em.close();
    }

    public boolean delete(int id) {
        EntityManager em =
                HibernateUtil.getEntityManagerFactory()
                        .createEntityManager();

        EntityTransaction tx = em.getTransaction();

        Book book = em.find(Book.class, id);

        if (book == null) {
            em.close();
            return false;
        }

        tx.begin();
        em.remove(book);
        tx.commit();

        em.close();

        return true;
    }
    public long count() {
        EntityManager em = HibernateUtil.getEntityManagerFactory().createEntityManager();

        long count = em.createQuery(
                "SELECT COUNT(b) FROM Book b",
                Long.class
        ).getSingleResult();

        em.close();
        return count;
    }

    public List<Book> findByAvailable(boolean available) {
        EntityManager em = HibernateUtil.getEntityManagerFactory().createEntityManager();

        List<Book> books = em.createQuery(
                "FROM Book b WHERE b.available = :available",
                Book.class
        ).setParameter("available", available).getResultList();

        em.close();
        return books;
    }

    public List<Book> findByPriceGreaterThan(BigDecimal price) {
        EntityManager em = HibernateUtil.getEntityManagerFactory().createEntityManager();

        List<Book> books = em.createQuery(
                "FROM Book b WHERE b.price >= :price",
                Book.class
        ).setParameter("price", price).getResultList();

        em.close();
        return books;
    }

    public void saveAll(List<Book> books) {
        EntityManager em = HibernateUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction tx = em.getTransaction();

        tx.begin();

        for (Book book : books) {
            em.persist(book);
        }

        tx.commit();
        em.close();
    }

    public int deleteByPublishDate(LocalDate date) {
        EntityManager em = HibernateUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction tx = em.getTransaction();

        tx.begin();

        int deleted = em.createQuery(
                "DELETE FROM Book b WHERE b.publishDate = :date"
        ).setParameter("date", date).executeUpdate();

        tx.commit();
        em.close();

        return deleted;
    }
}