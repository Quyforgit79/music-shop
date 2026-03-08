/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import model.Address;

/**
 *
 * @author Nguyen Hoang Thai Vinh - CE190384
 */
public class AddressUtil {
    public static String buildFullAddress(Address address) {
        StringBuilder sb = new StringBuilder();
        if (address.getStreet() != null) sb.append(address.getStreet());
        if (address.getWard() != null) sb.append(", ").append(address.getWard());
        if (address.getDistrict() != null) sb.append(", ").append(address.getDistrict());
        if (address.getCity() != null) sb.append(", ").append(address.getCity());
        return sb.toString();
    }
}
