package com.sustech.dbproj.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class City {
    @Id
    @GeneratedValue
    private Integer id;

    private String city_name;
    public City(){

    }

    public Integer getId() {
        return id;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setId( Integer id ) {
        this.id = id;
    }

    public void setCity_name( String city_name ) {
        this.city_name = city_name;
    }
}
