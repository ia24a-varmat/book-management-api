package db.tests;

import org.example.bookapi.util.HibernateUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TestConnection {

    @Test
    public void testConnection() {
        assertNotNull(HibernateUtil.getEntityManagerFactory());
    }
}