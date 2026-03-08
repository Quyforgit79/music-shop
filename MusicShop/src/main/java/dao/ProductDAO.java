/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import db.JDBCUtil;
import enums.DiscountType;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Brand;
import model.Category;
import model.Discount;
import model.Product;
import model.SubCategory;

/**
 *
 * @author Nguyen Hoang Thai Vinh - CE190384
 */
public class ProductDAO extends JDBCUtil {

    public boolean insert(Product product) {
        String sql = "INSERT INTO Products(name, description, price, stock_quantity, category_id, image_url) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            Object[] params = {
                product.getName(), product.getDescription(), product.getPrice(), product.getStockQuantity(), product.getCategory().getCategoryId(), product.getImageUrl()
            };

            return execQuery(sql, params) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(Product product) {
        String sql = "UPDATE Products SET name=?, description=?, price=?, stock_quantity=?, category_id=?, image_url=?, discount_type=? WHERE product_id=?";

        try {
            Object[] params = {
                product.getDescription(), product.getPrice(), product.getStockQuantity(), product.getCategory().getCategoryId(), product.getImageUrl(), product.getName()
            };
            return execQuery(sql, params) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean delete(int productId) {
        String sql = "DELETE FROM Products WHERE product_id=?";
        Object[] params = {productId};

        try {
            return execQuery(sql, params) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Product getProductById(int productId) {
        String sql = "SELECT \n"
                + "d.discount_id, d.discount_type, d.discount_value, d.code, d.start_date, d.end_date, d.usage_limit, d.used_count,\n"
                + "b.*, \n"
                + "p.*, \n"
                + "c.name as 'Cata Name'\n"
                + " FROM Products p   \n"
                + " LEFT JOIN Categories c ON c.category_id = p.category_id  \n"
                + " LEFT JOIN Brands b ON b.brand_id = p.brand_id \n"
                + " LEFT JOIN Discounts d ON d.discount_id = p.discount_id \n"
                + " WHERE p.product_id = ?";
        Object[] params = {productId};

        try {
            ResultSet rs = execSelectQuery(sql, params);

            if (rs.next()) {

                Category category = new Category(rs.getInt("category_id"), rs.getString("Cata Name"), rs.getString("description"));
                SubCategory subCategory = new SubCategory(rs.getInt("subcategory_id"), rs.getString("name"), category);
                Brand brand = new Brand(rs.getInt("brand_id"), rs.getString("brand_name"));

                Date startDate = rs.getDate("start_date");
                Date endDate = rs.getDate("end_date");
                Discount discount = new Discount(
                        rs.getInt("discount_id"), rs.getString("code"),
                        rs.getString("description"), null,
                        rs.getBigDecimal("discount_value"),
                        startDate != null ? startDate.toLocalDate() : null,
                        endDate != null ? endDate.toLocalDate() : null,
                        rs.getInt("usage_limit"),
                        rs.getInt("used_count"),
                        rs.getBoolean("is_active"));

                return new Product(
                        rs.getInt("product_id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getBigDecimal("price"),
                        rs.getInt("stock_quantity"),
                        category,
                        rs.getString("image_url"),
                        rs.getTimestamp("created_at").toLocalDateTime(),
                        brand,
                        discount,
                        rs.getInt("sold_quantity"),
                        rs.getString("material"),
                        rs.getInt("manufacturing_year"),
                        rs.getString("made_in"),
                        subCategory
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public double getAverageRating(int productId) {
        double avgRating = 0.0;
        String sql = "SELECT AVG(CAST(rating AS FLOAT)) AS avg_rating FROM Reviews WHERE product_id = ?";
        Object[] params = {productId};
        try (ResultSet rs = execSelectQuery(sql, params)) {
            if (rs.next()) {
                avgRating = rs.getDouble("avg_rating");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return avgRating;
    }

    public List<Product> getAll() {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT * \n"
                + "  FROM Products p  \n"
                + "  LEFT JOIN Categories c ON c.category_id = p.category_id \n"
                + "  LEFT JOIN Brands b ON b.brand_id = p.brand_id\n"
                + "  LEFT JOIN Discounts d ON d.discount_id = p.discount_id";

        try {
            ResultSet rs = execSelectQuery(sql);

            while (rs.next()) {
                Category category = new Category(rs.getInt("category_id"), rs.getString("description"), rs.getString("name"));
                SubCategory subCategory = new SubCategory(rs.getInt("subcategory_id"), rs.getString("name"), category);
                Brand brand = new Brand(rs.getInt("brand_id"), rs.getString("brand_name"));
                Discount discount = new Discount(
                        rs.getInt("discount_id"), rs.getString("code"),
                        rs.getString("description"), null,
                        rs.getBigDecimal("discount_value"), rs.getDate("start_date").toLocalDate(),
                        rs.getDate("end_date").toLocalDate(),
                        rs.getInt("usage_limit"),
                        rs.getInt("used_count"),
                        rs.getBoolean("is_active"));

                Product product = new Product(
                        rs.getInt("product_id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getBigDecimal("price"),
                        rs.getInt("stock_quantity"),
                        category,
                        rs.getString("image_url"),
                        rs.getTimestamp("created_at").toLocalDateTime(),
                        brand,
                        discount,
                        rs.getInt("sold_quantity"),
                        rs.getString("material"),
                        rs.getInt("manufacturing_year"),
                        rs.getString("made_in"),
                        subCategory
                );
                list.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Product> getProductsByCategory(int categoryId) {
        List<Product> products = new ArrayList<>();
        Object[] params = {categoryId};

        String sql = "SELECT * \n"
                + "  FROM Products p  \n"
                + "  LEFT JOIN Categories c ON c.category_id = p.category_id \n"
                + "  LEFT JOIN Brands b ON b.brand_id = p.brand_id\n"
                + "  LEFT JOIN Discounts d ON d.discount_id = p.discount_id\n"
                + "  WHERE p.category_id = ?";

        try {
            ResultSet rs = execSelectQuery(sql, params);
            while (rs.next()) {
                Category category = new Category(
                        rs.getInt("category_id"),
                        rs.getString("description"),
                        rs.getString("name")
                );
                SubCategory subCategory = new SubCategory(rs.getInt("subcategory_id"), rs.getString("name"), category);
                Brand brand = new Brand(
                        rs.getInt("brand_id"),
                        rs.getString("brand_name")
                );

                Discount discount = null;
                int discountId = rs.getInt("discount_id");
                if (!rs.wasNull() && discountId != 0) {
                    int discountType = rs.getInt("discount_type");
                    DiscountType type = DiscountType.fromType(discountType);

                    discount = new Discount(
                            rs.getInt("discount_id"),
                            rs.getString("code"),
                            rs.getString("description"),
                            type,
                            rs.getBigDecimal("discount_value"),
                            rs.getDate("start_date") != null ? rs.getDate("start_date").toLocalDate() : null,
                            rs.getDate("end_date") != null ? rs.getDate("end_date").toLocalDate() : null,
                            rs.getInt("usage_limit"),
                            rs.getInt("used_count"),
                            rs.getBoolean("is_active")
                    );
                }

                Product product = new Product(
                        rs.getInt("product_id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getBigDecimal("price"),
                        rs.getInt("stock_quantity"),
                        category,
                        rs.getString("image_url"),
                        rs.getTimestamp("created_at").toLocalDateTime(),
                        brand,
                        discount,
                        rs.getInt("sold_quantity"),
                        rs.getString("material"),
                        rs.getInt("manufacturing_year"),
                        rs.getString("made_in"),
                        subCategory
                );
                products.add(product);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return products;
    }

    public List<Product> getOrderHistoryByUserId(int userID) {
        List<Product> productList = new ArrayList<>();
        Object[] params = {userID};

        String sql = "SELECT DISTINCT p.product_id, p.name, p.description, p.price, p.image_url "
                + "FROM Orders o "
                + "JOIN OrderDetails od ON o.order_id = od.order_id "
                + "JOIN Products p ON od.product_id = p.product_id "
                + "WHERE o.user_id = ?";

        try {
            ResultSet rs = execSelectQuery(sql, params);

            while (rs.next()) {
                Product product = new Product();
                product.setProductId(rs.getInt("product_id"));
                product.setName(rs.getString("name"));
                product.setDescription(rs.getString("description"));
                product.setPrice(rs.getBigDecimal("price"));
                product.setImageUrl(rs.getString("image_url"));
                productList.add(product);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productList;
    }

    public List<Product> getRelatedProducts(int productId, int limit) {
        List<Product> list = new ArrayList<>();

        String sql = "SELECT TOP " + limit + " * "
                + "FROM Products "
                + "WHERE product_id <> ? AND is_active = 1 "
                + "AND (category_id = (SELECT category_id FROM Products WHERE product_id = ?) "
                + "OR brand_id = (SELECT brand_id FROM Products WHERE product_id = ?)) "
                + "ORDER BY NEWID()";

        Object[] params = {limit, productId, productId};

        try ( ResultSet rs = execSelectQuery(sql, params)) {
            while (rs.next()) {
                Product product = new Product();
                product.setName(rs.getString("name"));
                product.setProductId(rs.getInt("product_id"));
                product.setImageUrl(rs.getString("image_url"));
                product.setPrice(rs.getBigDecimal("price"));

                list.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public int countProductsByCategory(int categoryId) {
        String sql = "SELECT COUNT(*) FROM Products WHERE category_id = ?";
        Object[] params = {categoryId};

        try ( ResultSet rs = execSelectQuery(sql, params)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<Product> getProductsByCategoryAndPage(int categoryId, int offset, int limit) {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT * FROM Products p\n"
                + "LEFT JOIN Categories c ON c.category_id = p.category_id\n"
                + "LEFT JOIN Brands b ON b.brand_id = p.brand_id\n"
                + "LEFT JOIN Discounts d ON d.discount_id = p.discount_id\n"
                + "WHERE p.category_id = ?\n"
                + "ORDER BY p.product_id\n"
                + "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        Object[] params = {categoryId, offset, limit};

        try ( ResultSet rs = execSelectQuery(sql, params)) {
            while (rs.next()) {
                Category category = new Category(rs.getInt("category_id"), rs.getString("description"), rs.getString("name"));
                SubCategory subCategory = new SubCategory(rs.getInt("subcategory_id"), rs.getString("name"), category);
                Brand brand = new Brand(rs.getInt("brand_id"), rs.getString("brand_name"));
                Discount discount = null;

                int discountId = rs.getInt("discount_id");
                if (!rs.wasNull() && discountId != 0) {
                    int discountType = rs.getInt("discount_type");
                    DiscountType type = DiscountType.fromType(discountType);
                    discount = new Discount(
                            discountId,
                            rs.getString("code"),
                            rs.getString("description"),
                            type,
                            rs.getBigDecimal("discount_value"),
                            rs.getDate("start_date") != null ? rs.getDate("start_date").toLocalDate() : null,
                            rs.getDate("end_date") != null ? rs.getDate("end_date").toLocalDate() : null,
                            rs.getInt("usage_limit"),
                            rs.getInt("used_count"),
                            rs.getBoolean("is_active")
                    );
                }

                Product product = new Product(
                        rs.getInt("product_id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getBigDecimal("price"),
                        rs.getInt("stock_quantity"),
                        category,
                        rs.getString("image_url"),
                        rs.getTimestamp("created_at").toLocalDateTime(),
                        brand,
                        discount,
                        rs.getInt("sold_quantity"),
                        rs.getString("material"),
                        rs.getInt("manufacturing_year"),
                        rs.getString("made_in"),
                        subCategory
                );
                list.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<Product> filterProductsByPage(int categoryId, String[] subcategoryId, String priceRange, boolean sale, boolean bestSeller, int offset, int limit) {
        List<Product> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM Products p\n"
                + "LEFT JOIN Categories c ON c.category_id = p.category_id\n"
                + "LEFT JOIN Brands b ON b.brand_id = p.brand_id\n"
                + "LEFT JOIN Discounts d ON d.discount_id = p.discount_id\n"
                + "WHERE p.category_id = ?");
        List<Object> params = new ArrayList<>();
        params.add(categoryId);

        // Lọc theo subcategory
        if (subcategoryId != null && subcategoryId.length > 0) {
            sql.append(" AND p.subcategory_id IN (");
            for (int i = 0; i < subcategoryId.length; i++) {
                sql.append("?");
                params.add(Integer.parseInt(subcategoryId[i]));
                if (i < subcategoryId.length - 1) {
                    sql.append(", ");
                }
            }
            sql.append(")");
        }

        // Lọc theo giá
        if (priceRange != null && !priceRange.equals("1")) {
            switch (priceRange) {
                case "2":
                    sql.append(" AND p.price < 2000000");
                    break;
                case "3":
                    sql.append(" AND p.price BETWEEN 2000000 AND 5000000");
                    break;
                case "4":
                    sql.append(" AND p.price BETWEEN 5000000 AND 10000000");
                    break;
                case "5":
                    sql.append(" AND p.price BETWEEN 10000000 AND 20000000");
                    break;
                case "6":
                    sql.append(" AND p.price BETWEEN 20000000 AND 50000000");
                    break;
                case "7":
                    sql.append(" AND p.price > 50000000");
                    break;
            }
        }

        if (sale) {
            sql.append(" AND p.discount_id IS NOT NULL");
        }
        if (bestSeller) {
            sql.append(" AND p.sold_quantity >= 10");
        }

        sql.append(" ORDER BY p.product_id OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");
        params.add(offset);
        params.add(limit);

        try ( ResultSet rs = execSelectQuery(sql.toString(), params.toArray())) {
            while (rs.next()) {
                Category category = new Category(
                        rs.getInt("category_id"),
                        rs.getString("description"),
                        rs.getString("name")
                );
                SubCategory subCategory = new SubCategory(rs.getInt("subcategory_id"), rs.getString("name"), category);
                Brand brand = new Brand(
                        rs.getInt("brand_id"),
                        rs.getString("brand_name")
                );

                Discount discount = null;
                int discountId = rs.getInt("discount_id");
                if (!rs.wasNull() && discountId != 0) {
                    int discountType = rs.getInt("discount_type");
                    DiscountType type = DiscountType.fromType(discountType);
                    discount = new Discount(
                            discountId,
                            rs.getString("code"),
                            rs.getString("description"),
                            type,
                            rs.getBigDecimal("discount_value"),
                            rs.getDate("start_date") != null ? rs.getDate("start_date").toLocalDate() : null,
                            rs.getDate("end_date") != null ? rs.getDate("end_date").toLocalDate() : null,
                            rs.getInt("usage_limit"),
                            rs.getInt("used_count"),
                            rs.getBoolean("is_active")
                    );
                }

                Product product = new Product(
                        rs.getInt("product_id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getBigDecimal("price"),
                        rs.getInt("stock_quantity"),
                        category,
                        rs.getString("image_url"),
                        rs.getTimestamp("created_at").toLocalDateTime(),
                        brand,
                        discount,
                        rs.getInt("sold_quantity"),
                        rs.getString("material"),
                        rs.getInt("manufacturing_year"),
                        rs.getString("made_in"),
                        subCategory
                );
                list.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<Product> searchProducts(String searchQuery, int categoryId, int offset, int limit) {
        List<Product> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM Products p\n"
                + "LEFT JOIN Categories c ON c.category_id = p.category_id\n"
                + "LEFT JOIN Brands b ON b.brand_id = p.brand_id\n"
                + "LEFT JOIN Discounts d ON d.discount_id = p.discount_id\n"
                + "WHERE p.category_id = ? AND (p.name LIKE ? OR p.description LIKE ?)\n"
                + "ORDER BY p.product_id\n"
                + "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");
        List<Object> params = new ArrayList<>();
        params.add(categoryId);
        params.add("%" + searchQuery.trim() + "%");
        params.add("%" + searchQuery.trim() + "%");
        params.add(offset);
        params.add(limit);

        try ( ResultSet rs = execSelectQuery(sql.toString(), params.toArray())) {
            while (rs.next()) {
                Category category = new Category(
                        rs.getInt("category_id"),
                        rs.getString("description"),
                        rs.getString("name")
                );
                SubCategory subCategory = new SubCategory(rs.getInt("subcategory_id"), rs.getString("name"), category);
                Brand brand = new Brand(
                        rs.getInt("brand_id"),
                        rs.getString("brand_name")
                );

                Discount discount = null;
                int discountId = rs.getInt("discount_id");
                if (!rs.wasNull() && discountId != 0) {
                    int discountType = rs.getInt("discount_type");
                    DiscountType type = DiscountType.fromType(discountType);
                    discount = new Discount(
                            discountId,
                            rs.getString("code"),
                            rs.getString("description"),
                            type,
                            rs.getBigDecimal("discount_value"),
                            rs.getDate("start_date") != null ? rs.getDate("start_date").toLocalDate() : null,
                            rs.getDate("end_date") != null ? rs.getDate("end_date").toLocalDate() : null,
                            rs.getInt("usage_limit"),
                            rs.getInt("used_count"),
                            rs.getBoolean("is_active")
                    );
                }

                Product product = new Product(
                        rs.getInt("product_id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getBigDecimal("price"),
                        rs.getInt("stock_quantity"),
                        category,
                        rs.getString("image_url"),
                        rs.getTimestamp("created_at").toLocalDateTime(),
                        brand,
                        discount,
                        rs.getInt("sold_quantity"),
                        rs.getString("material"),
                        rs.getInt("manufacturing_year"),
                        rs.getString("made_in"),
                        subCategory
                );
                list.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public int countFilteredProducts(int categoryId, String[] subcategoryId, String priceRange, boolean sale, boolean bestSeller) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) AS total FROM Products p WHERE p.category_id = ?");
        List<Object> params = new ArrayList<>();
        params.add(categoryId);

        // Lọc theo subcategory
        if (subcategoryId != null && subcategoryId.length > 0) {
            sql.append(" AND p.subcategory_id IN (");
            for (int i = 0; i < subcategoryId.length; i++) {
                sql.append("?");
                params.add(Integer.parseInt(subcategoryId[i]));
                if (i < subcategoryId.length - 1) {
                    sql.append(", ");
                }
            }
            sql.append(")");
        }

        // Lọc theo giá
        if (priceRange != null && !priceRange.equals("1")) {
            switch (priceRange) {
                case "2":
                    sql.append(" AND p.price < 2000000");
                    break;
                case "3":
                    sql.append(" AND p.price BETWEEN 2000000 AND 5000000");
                    break;
                case "4":
                    sql.append(" AND p.price BETWEEN 5000000 AND 10000000");
                    break;
                case "5":
                    sql.append(" AND p.price BETWEEN 10000000 AND 20000000");
                    break;
                case "6":
                    sql.append(" AND p.price BETWEEN 20000000 AND 50000000");
                    break;
                case "7":
                    sql.append(" AND p.price > 50000000");
                    break;
            }
        }

        if (sale) {
            sql.append(" AND p.discount_id IS NOT NULL");
        }
        if (bestSeller) {
            sql.append(" AND p.sold_quantity >= 10");
        }

        try ( ResultSet rs = execSelectQuery(sql.toString(), params.toArray())) {
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public int countSearchProducts(String searchQuery, int categoryId) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) AS total FROM Products p WHERE p.category_id = ? AND (p.name LIKE ? OR p.description LIKE ?)");
        List<Object> params = new ArrayList<>();
        params.add(categoryId);
        params.add("%" + searchQuery.trim() + "%");
        params.add("%" + searchQuery.trim() + "%");

        try ( ResultSet rs = execSelectQuery(sql.toString(), params.toArray())) {
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public List<Product> getRandomProducts(int category) {
        List<Product> products = new ArrayList<>();

        String sql = "SELECT TOP 12* \n"
                + "  FROM Products p  \n"
                + "  LEFT JOIN Discounts d ON d.discount_id = p.discount_id\n"
                + "  WHERE p.category_id = ?\n"
                + "  ORDER BY NEWID()";
        Object[] params = {category};

        try ( ResultSet rs = execSelectQuery(sql, params)) {
            Discount discount = null;
            while (rs.next()) {
                int discountId = rs.getInt("discount_id");
                if (!rs.wasNull() && discountId != 0) {
                    int discountType = rs.getInt("discount_type");
                    DiscountType type = DiscountType.fromType(discountType);
                    discount = new Discount(
                            discountId,
                            rs.getString("code"),
                            rs.getString("description"),
                            type,
                            rs.getBigDecimal("discount_value"),
                            rs.getDate("start_date") != null ? rs.getDate("start_date").toLocalDate() : null,
                            rs.getDate("end_date") != null ? rs.getDate("end_date").toLocalDate() : null,
                            rs.getInt("usage_limit"),
                            rs.getInt("used_count"),
                            rs.getBoolean("is_active")
                    );
                }

                Product product = new Product(
                        rs.getInt("product_id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getBigDecimal("price"),
                        rs.getInt("stock_quantity"),
                        null,
                        rs.getString("image_url"),
                        rs.getTimestamp("created_at").toLocalDateTime(),
                        null,
                        discount,
                        rs.getInt("sold_quantity"),
                        rs.getString("material"),
                        rs.getInt("manufacturing_year"),
                        rs.getString("made_in"),
                        null
                );
                products.add(product);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return products;
    }

    public static void main(String[] args) {
        Product p = new ProductDAO().getProductById(33);

        System.out.println(p.toString());
    }
}
