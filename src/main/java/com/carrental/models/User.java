package com.carrental.models;

import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "user_type", discriminatorType = DiscriminatorType.STRING)
@Table(name = "users")
public abstract class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String name;
    private String telephone;
    private String email;
    private String address;
    
    private Boolean is_admin;

    public User() {
        super();
    }

    public User(String name, String telephone, String email, String address, Boolean is_admin) {
        super();
        this.name = name;
        this.telephone = telephone;
        this.email = email;
        this.address = address;
        this.is_admin = is_admin;
    }

    public User(Long userId, String name, String telephone, String email, String address, Boolean is_admin) {
        super();
        this.userId = userId;
        this.name = name;
        this.telephone = telephone;
        this.email = email;
        this.address = address;
        this.is_admin = is_admin;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Boolean getIs_admin() {
        return is_admin;
    }

    public void setIs_admin(Boolean is_admin) {
        this.is_admin = is_admin;
    }
}
