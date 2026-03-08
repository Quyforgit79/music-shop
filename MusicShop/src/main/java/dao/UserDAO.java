/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import db.JDBCUtil;
import enums.Gender;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.Role;
import model.User;

/**
 *
 * @author Nguyen Hoang Thai Vinh - CE190384
 */
public class UserDAO extends JDBCUtil {

    public boolean insertUser(User user) {
        String sql = "INSERT INTO Users(full_name, email, password, phone, gender, birthdate, account) VALUES (?, ?, ?, ?, ?, ?, ?)";
        Object[] params = {
            user.getFullName(),
            user.getEmail(),
            user.getPassword(),
            user.getPhone(),
            user.getGender() != null ? user.getGender().name() : null,
            user.getBirthdate() != null ? java.sql.Date.valueOf(user.getBirthdate()) : null,
            user.getAccount()
        };

        try {
            ResultSet rs = execInsertWithGeneratedKeys(sql, params);
            if (rs.next()) {
                int generatedId = rs.getInt(1);
                user.setUserId(generatedId);
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean uploadAvatar(String avatarFilePath, int userId) {
        String sqlString = "UPDATE Users SET image_url = ? WHERE user_id = ?";
        Object[] params = {avatarFilePath, userId};

        try {
            return execQuery(sqlString, params) > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public String getAvatarUrl(int userId) {
        String sql = "SELECT image_url FROM Users WHERE user_id = ?";
        Object[] params = {userId};

        try ( ResultSet rs = execSelectQuery(sql, params)) {
            if (rs.next()) {
                return rs.getString("image_url");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean update(User user) {
        String sql = "UPDATE Users SET account=?, full_name=?, email=?, phone=?, gender=?, birthdate=? WHERE user_id=?";
        Object[] params = {
            user.getAccount(),
            user.getFullName(),
            user.getEmail(),
            user.getPhone(),
            user.getGender() != null ? user.getGender().getGender() : null,
            user.getBirthdate(),
            user.getUserId()
        };

        try {
            return execQuery(sql, params) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean delete(int userId) {
        String sql = "DELETE FROM Users WHERE user_id=?";
        Object[] params = {userId};

        try {
            return execQuery(sql, params) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public User getUserById(int userId) {
        String sql = "select *,\n"
                + "\tr.name,\n"
                + "\tr.description\n"
                + "FROM Users u \n"
                + "LEFT JOIN Roles r ON r.role_id = u.role_id\n"
                + "WHERE u.user_id = ?";
        Object[] params = {userId};

        try {
            ResultSet rs = execSelectQuery(sql, params);

            if (rs.next()) {
                Role role = new Role(rs.getInt("role_id"), rs.getString("name"), rs.getString("description"));

                User user = new User();
                user.setAccount(rs.getString("account"));
                user.setPassword(rs.getString("password"));
                user.setFullName(rs.getString("full_name"));
                user.setPhone(rs.getString("phone"));
                user.setEmail(rs.getString("email"));
                user.setCreateDateTime(rs.getTimestamp("created_at").toLocalDateTime());
                user.setImageUrl(rs.getString("image_url"));
                user.setGender(Gender.fromGender(rs.getInt("gender")));
                user.setRole(role);
                user.setUserId(rs.getInt("user_id"));
                user.setBirthdate(rs.getDate("birthdate") != null ? rs.getDate("birthdate").toLocalDate() : null);

                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<User> getAll() {
        List<User> list = new ArrayList<>();
        String sql = "select *,\n"
                + "	r.name,\n"
                + "	r.description\n"
                + "FROM Users u \n"
                + "LEFT JOIN Roles r ON r.role_id = u.role_id\n"
                + "WHERE u.user_id = ?";
        try {
            ResultSet rs = execSelectQuery(sql);

            while (rs.next()) {
                Role role = new Role(rs.getInt("role_id"), rs.getString("name"), rs.getString("description"));
                User user = new User();
                user.setAccount(rs.getString("account"));
                user.setPassword("password");
                user.setFullName(rs.getString("full_name"));
                user.setPhone(rs.getString("phone"));
                user.setEmail(rs.getString("email"));
                user.setCreateDateTime(rs.getTimestamp("created_at").toLocalDateTime());
                user.setImageUrl(rs.getString("image_url"));
                user.setGender(Gender.fromGender(rs.getInt("gender")));
                user.setRole(role);
                user.setUserId(rs.getInt("user_id"));
                user.setBirthdate(rs.getDate("birthdate") != null ? rs.getDate("birthdate").toLocalDate() : null);

                list.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public User getUserLogin(String login, String password) {
        String sql = "select *,\n"
                + "	r.name,\n"
                + "	r.description\n"
                + "FROM Users u \n"
                + "LEFT JOIN Roles r ON r.role_id = u.role_id\n"
                + "WHERE (email = ? OR account = ?) AND password = ?";
        Object[] params = {login, login, password};
        User user = null;

        try {
            ResultSet rs = execSelectQuery(sql, params);

            if (rs.next()) {
                Role role = new Role(rs.getInt("role_id"), rs.getString("name"), rs.getString("description"));
                user = new User();

                user.setAccount(rs.getString("account"));
                user.setPassword(rs.getString("password"));
                user.setFullName(rs.getString("full_name"));
                user.setPhone(rs.getString("phone"));
                user.setEmail(rs.getString("email"));
                user.setCreateDateTime(rs.getTimestamp("created_at").toLocalDateTime());
                user.setImageUrl(rs.getString("image_url"));
                user.setGender(Gender.fromGender(rs.getInt("gender")));
                user.setRole(role);
                user.setIsActive(rs.getBoolean("is_active"));
                user.setUserId(rs.getInt("user_id"));
                user.setBirthdate(rs.getDate("birthdate") != null ? rs.getDate("birthdate").toLocalDate() : null);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public User isUserTaken(String account, String phone, String email) {
        String sql = "SELECT * FROM Users WHERE account = ? OR phone = ? OR email = ?";
        Object[] params = {account, phone, email};
        User user = null;
        try {
            ResultSet rs = execSelectQuery(sql, params);

            if (rs.next()) {
                user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setAccount(rs.getString("account"));
                user.setEmail(rs.getString("email"));
                user.setPhone(rs.getString("phone"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }

    public boolean updateUserInfo(User user) {
        String sql = "UPDATE Users"
                + "SET full_name = ?, account = ?, gender = ?, phone = ?, birthdate = ?, email = ?"
                + "WHERE user_id = ?";
        Object[] params = {user.getFullName(), user.getAccount(), user.getGender(), user.getPhone(), user.getBirthdate(), user.getEmail(), user.getUserId()};

        try {
            return execQuery(sql, params) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public User checkDuplicateAccountInfo(String account, String phone, String email, int id) {
        String sql = "SELECT * FROM Users WHERE (account = ? OR phone = ? OR email = ?) AND user_id != ?";
        Object[] params = {account, phone, email, id};
        User user = null;
        try {
            ResultSet rs = execSelectQuery(sql, params);
            if (rs.next()) {
                user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setAccount(rs.getString("account"));
                user.setEmail(rs.getString("email"));
                user.setPhone(rs.getString("phone"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public boolean updatePasswordByUserId(String password, int id) {
        String sql = "UPDATE Users\n"
                + "SET password = ?\n"
                + "WHERE user_id = ?";

        Object[] params = {password, id};

        try {
            return execQuery(sql, params) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean isAccountTaken(String account, int currentUserId) {
        String sql = "SELECT 1 FROM Users WHERE account = ? AND user_id != ?";
        Object[] params = {account, currentUserId};
        try ( ResultSet rs = execSelectQuery(sql, params)) {
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isEmailTaken(String email, int currentUserId) {
        String sql = "SELECT 1 FROM Users WHERE email = ? AND user_id != ?";
        Object[] params = {email, currentUserId};
        try ( ResultSet rs = execSelectQuery(sql, params)) {
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isPhoneTaken(String phone, int currentUserId) {
        String sql = "SELECT 1 FROM Users WHERE phone = ? AND user_id != ?";
        Object[] params = {phone, currentUserId};
        try ( ResultSet rs = execSelectQuery(sql, params)) {
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Map<String, Boolean> checkDuplicateInfo(String account, String email, String phone, int userId) {
        Map<String, Boolean> result = new HashMap<>();
        result.put("account", false);
        result.put("email", false);
        result.put("phone", false);
        String sql = "SELECT account, email, phone FROM users WHERE (account = ? OR email = ? OR phone = ?) AND user_id != ?";
        Object[] params = {
            account, email, phone, userId
        };
        try ( ResultSet rs = execSelectQuery(sql, params)) {
            while (rs.next()) {
                if (rs.getString("account").equals(account)) {
                    result.put("account", true);
                }
                if (rs.getString("email").equals(email)) {
                    result.put("email", true);
                }
                if (rs.getString("phone").equals(phone)) {
                    result.put("phone", true);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void main(String[] args) {
        UserDAO u = new UserDAO();

        User user = u.getUserLogin("remcute", "93a6be90e81a902c15ab3103e2990ecb");

        System.out.println(user.toString());
    }

}
