package org.example.bookapi.service;

import org.example.bookapi.dao.CategoryDao;
import org.example.bookapi.model.Category;

import java.util.List;

public class CategoryService {

    private final CategoryDao dao = new CategoryDao();

    public List<Category> findAll() {
        return dao.findAll();
    }

    public Category findById(int id) {
        return dao.findById(id);
    }

    public void save(Category category) {
        dao.save(category);
    }

    public void update(Category category) {
        dao.update(category);
    }

    public boolean delete(int id) {
        return dao.delete(id);
    }
}