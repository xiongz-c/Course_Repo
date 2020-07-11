package com.sustech.dbproj.controller;

import com.sustech.dbproj.domain.Admin;
import com.sustech.dbproj.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AdminController {
    @Autowired
    private AdminRepository repository;
    /**
     * get all Admin
     *
     */
    @GetMapping("/AdminList")
    public List<Admin> list() {
        return repository.findAll();
    }

    /**
     * new an admin user
     */
    @PostMapping("/Admin/register")
    public Admin create(Admin admin) {
        return repository.save(admin);
    }

    /**
     * check id and password
     */
    @PostMapping(value = "/Admin/login")
    public boolean check( Admin ad ) {
        Admin admin = repository.findById(ad.getId()).orElse(null);
        if ( admin == null )return false;
        return admin.getPassword( ).equals( ad.getPassword( ) );
    }
}
