package db.tests;

import jakarta.persistence.EntityManager;
import org.example.bookapi.util.HibernateUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class HibernateEntityTest {

    @Test
    public void testEntityManager() {
        EntityManager em = HibernateUtil
                .getEntityManagerFactory()
                .createEntityManager();

        assertNotNull(em);

        em.close();
    }
}