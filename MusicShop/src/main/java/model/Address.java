/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import enums.AddressStyle;

/**
 *
 * @author Nguyen Hoang Thai Vinh - CE190384
 */
public class Address {

    private int addressId;
    private User user;
    private String street;
    private String ward;
    private String district;
    private String city;
    private AddressStyle type;
    private boolean isDefault;
    private String receiverPhone;
    private String receiverName;
    private boolean isDelete;

    public Address() {
    }

    public Address(int addressId, User user, String street, String ward, String district, String city, AddressStyle type, boolean isDefault, String receiverName, String receiverPhone, boolean isDelete) {
        this.addressId = addressId;
        this.user = user;
        this.street = street;
        this.ward = ward;
        this.district = district;
        this.city = city;
        this.type = type;
        this.isDefault = isDefault;
        this.receiverPhone = receiverName;
        this.receiverName = receiverPhone;
        this.isDelete = isDelete;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getWard() {
        return ward;
    }

    public void setWard(String ward) {
        this.ward = ward;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public AddressStyle getType() {
        return type;
    }

    public void setType(AddressStyle type) {
        this.type = type;
    }

    public boolean isIsDefault() {
        return isDefault;
    }

    public void setIsDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public boolean isIsDelete() {
        return isDelete;
    }

    public void setIsDelete(boolean isDelete) {
        this.isDelete = isDelete;
    }

    public String getFullAddress() {
        StringBuilder sb = new StringBuilder();
        if (street != null) {
            sb.append(street);
        }
        if (ward != null && !ward.isEmpty()) {
            sb.append(", ").append(ward);
        }
        if (district != null && !district.isEmpty()) {
            sb.append(", ").append(district);
        }
        if (city != null) {
            sb.append(", ").append(city);
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return "Address{" + "addressId=" + addressId + ", user=" + user + ", street=" + street + ", ward=" + ward + ", district=" + district + ", city=" + city + ", type=" + type + ", isDefault=" + isDefault + ", receiverPhone=" + receiverPhone + ", receiverName=" + receiverName + ", isDelete=" + isDelete + '}';
    }

}
