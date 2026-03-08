/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import db.JDBCUtil;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Category;

/**
 *
 * @author Nguyen Hoang Thai Vinh - CE190384
 */
public class CategoryDAO extends JDBCUtil {

    public boolean insert(Category category) {
        String sql = "INSERT INTO Categories(name, description) VALUES (?, ?)";
        Object[] params = {category.getName(), category.getDescription()};

        try {
            return execQuery(sql, params) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(Category category) {
        String sql = "UPDATE Categories SET name=?, description=? WHERE category_id=?";
        Object[] params = {category.getName(), category.getDescription(), category.getCategoryId()};

        try {
            return execQuery(sql, params) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean delete(int categoryId) {
        String sql = "DELETE FROM Categories WHERE category_id=?";
        Object[] params = {categoryId};
        try {
            return execQuery(sql, params) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Category getCategoryById(int categoryId) {
        String sql = "SELECT * FROM Categories WHERE category_id=?";
        Object[] params = {categoryId};
        try {
            ResultSet rs = execSelectQuery(sql, params);

            if (rs.next()) {
                return new Category(
                        rs.getInt("category_id"),
                        rs.getString("name"),
                        rs.getString("description")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Category> getAll() {
        List<Category> list = new ArrayList<>();
        String sql = "SELECT * FROM Categories";

        try {
            ResultSet rs = execSelectQuery(sql);

            while (rs.next()) {
                Category category = new Category(
                        rs.getInt("category_id"),
                        rs.getString("name"),
                        rs.getString("description")
                );
                list.add(category);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
