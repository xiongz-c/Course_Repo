package com.sustech.dbproj.domain;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Admin {
    @Id
    private Integer id;

    @Column(nullable = false)
    private String password;

    public Admin( ) {

    }

    public Integer getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public void setId( Integer id ) {
        this.id = id;
    }

    public void setPassword( String password ) {
        this.password = password;
    }

}
