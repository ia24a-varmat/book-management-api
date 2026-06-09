package org.example.bookapi.service;


import org.example.bookapi.dao.BookDao;
import org.example.bookapi.model.Book;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;


public class BookService {

    private final BookDao dao = new BookDao();

    public List<Book> findAll() {
        return dao.findAll();
    }

    public Book findById(int id) {
        return dao.findById(id);
    }

    public void save(Book book) {
        dao.save(book);
    }

    public void update(Book book) {
        dao.update(book);
    }

    public boolean delete(int id) {
        return dao.delete(id);
    }
    public long count() {
        return dao.count();
    }

    public List<Book> findByAvailable(boolean available) {
        return dao.findByAvailable(available);
    }

    public List<Book> findByPriceGreaterThan(BigDecimal price) {
        return dao.findByPriceGreaterThan(price);
    }

    public void saveAll(List<Book> books) {
        dao.saveAll(books);
    }

    public int deleteByPublishDate(LocalDate date) {
        return dao.deleteByPublishDate(date);
    }
}
