package com.sustech.dbproj.domain;

import javax.persistence.*;

@Entity
public class Passenger {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;
    private String name;
    private Integer age;

    @Column(name = "id_card",nullable = false)
    private String idc;

    @Column(name = "phone_num",nullable = false)
    private String phone;
    private String password;

    public Passenger() {

    }

    public Integer getId() {
        return Id;
    }

    public void setId( Integer id ) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName( String name ) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge( Integer age ) {
        this.age = age;
    }

    public String getIdc() {
        return idc;
    }

    public void setIdc( String ID_CARD ) {
        this.idc = ID_CARD;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone( String phone_num ) {
        this.phone = phone_num;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword( String password ) {
        this.password = password;
    }


}
