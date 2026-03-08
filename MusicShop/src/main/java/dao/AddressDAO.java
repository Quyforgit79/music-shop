/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import db.JDBCUtil;
import enums.AddressStyle;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Address;
import model.User;

/**
 *
 * @author Nguyen Hoang Thai Vinh - CE190384
 */
public class AddressDAO extends JDBCUtil {

    public boolean insertAddress(Address address) {
        String sql = "INSERT INTO Address (user_id, street, ward, district, city, type, is_default, receiver_name, receiver_phone) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Object[] params = {
            address.getUser().getUserId(), address.getStreet(), address.getWard(), address.getDistrict(), address.getCity(), address.getType().getStyle(), address.isIsDefault(), address.getReceiverName(), address.getReceiverPhone()
        };
        try {
            return execQuery(sql, params) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public List<Address> getAddressesByUserId(int userId) {
        List<Address> list = new ArrayList<>();
        String sql = "SELECT a.*, \n"
                + "	u.user_id,\n"
                + "	u.full_name,\n"
                + "	u.phone\n"
                + "FROM Address a\n"
                + "LEFT JOIN Users u ON u.user_id = a.user_id\n"
                + "WHERE u.user_id = ? AND is_deleted = 0";
        try {
            Object[] params = {userId};

            ResultSet rs = execSelectQuery(sql, params);
            while (rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setFullName(rs.getString("full_name"));
                user.setPhone(rs.getString("phone"));

                Address address = new Address(
                        rs.getInt("address_id"),
                        user,
                        rs.getString("street"),
                        rs.getString("ward"),
                        rs.getString("district"),
                        rs.getString("city"),
                        AddressStyle.fromStyle(rs.getInt("type")),
                        rs.getBoolean("is_default"),
                        rs.getString("receiver_phone"),
                        rs.getString("receiver_name"),
                        rs.getBoolean("is_deleted")
                );
                list.add(address);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Address getAddressById(int addressId, int userId) {

        String sql = "SELECT *, \n"
                + "	u.user_id,\n"
                + "	u.full_name,\n"
                + "	u.phone\n"
                + "FROM Address a\n"
                + "LEFT JOIN Users u ON u.user_id = a.user_id\n"
                + "WHERE a.address_id = ? AND u.user_id = ?";
        try {
            Object[] params = {addressId, userId};

            ResultSet rs = execSelectQuery(sql, params);
            if (rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setFullName(rs.getString("full_name"));
                user.setPhone(rs.getString("phone"));

                return new Address(
                        rs.getInt("address_id"),
                        user,
                        rs.getString("street"),
                        rs.getString("ward"),
                        rs.getString("district"),
                        rs.getString("city"),
                        AddressStyle.fromStyle(rs.getInt("type")),
                        rs.getBoolean("is_default"),
                        rs.getString("receiver_phone"),
                        rs.getString("receiver_name"),
                        rs.getBoolean("is_deleted")
                );

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateAddress(Address address) {
        String sql = "UPDATE Address SET receiver_name = ?, receiver_phone = ?, street = ?, ward = ?, district = ?, city = ?, type = ?, is_default = ? WHERE address_id = ? AND user_id = ?";
        Object[] params = {
            address.getReceiverName(),
            address.getReceiverPhone(),
            address.getStreet(),
            address.getWard(),
            address.getDistrict(),
            address.getCity(),
            address.getType().getStyle(),
            address.isIsDefault(),
            address.getAddressId(),
            address.getUser().getUserId()
        };

        try {
            return execQuery(sql, params) > 0;
        } catch (SQLException e) {
            System.err.println("SQL Error in update: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteAddress(int addressId, int userId) {
        String sql = "UPDATE Address SET is_deleted = 1 WHERE address_id = ? AND user_id = ?";
        Object[] params = {addressId, userId};
        try {
            return execQuery(sql, params) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Address getDefaultAddress(int userId) {
        String sql = "SELECT *,  \n"
                + "   u.full_name,  \n"
                + "   u.phone   \n"
                + "   FROM Address a  \n"
                + "   LEFT JOIN Users u ON u.user_id = a.user_id\n"
                + "   WHERE a.user_id = ? AND a.is_default = 1";
        Object[] params = {userId};

        try {
            ResultSet rs = execSelectQuery(sql, params);

            if (rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt("user_id"));

                return new Address(
                        rs.getInt("address_id"),
                        user,
                        rs.getString("street"),
                        rs.getString("ward"),
                        rs.getString("district"),
                        rs.getString("city"),
                        AddressStyle.fromStyle(rs.getInt("type")),
                        rs.getBoolean("is_default"),
                        rs.getString("receiver_phone"),
                        rs.getString("receiver_name"),
                        rs.getBoolean("is_deleted")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean setDefaultAddress(int userId, int addressId) {
        String resetSql = "UPDATE Address SET is_default = 0 WHERE user_id = ?";
        String setSql = "UPDATE Address SET is_default = 1 WHERE address_id = ?";
        try {
            int resetCount = execQuery(resetSql, new Object[]{userId});
            int setCount = execQuery(setSql, new Object[]{addressId});
            return setCount > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Address insertAndReturnAddress(Address address) {
        String sql = "INSERT INTO Address (user_id, receiver_name, receiver_phone, street, ward, district, city, is_default) "
                + "OUTPUT INSERTED.address_id "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        Object[] params = {
            address.getUser().getUserId(),
            address.getReceiverName(),
            address.getReceiverPhone(),
            address.getStreet(),
            address.getWard(),
            address.getDistrict(),
            address.getCity(),
            address.isIsDefault() == true ? 1 : 0
        };
        try ( ResultSet rs = execSelectQuery(sql, params)) {
            if (rs.next()) {
                int id = rs.getInt(1);
                address.setAddressId(id); // Cập nhật ID cho address
                return address;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(new AddressDAO().getAddressById(2, 2));
    }

}
