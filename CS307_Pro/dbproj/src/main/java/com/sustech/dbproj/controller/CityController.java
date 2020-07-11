package com.sustech.dbproj.controller;


import com.sustech.dbproj.domain.City;
import com.sustech.dbproj.repository.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CityController {

    @Autowired
    private CityRepository repository;
    /**
     * 获取city列表
     */
    @GetMapping("/CityList")
    public List<City> list(){
        return repository.findAll();
    }
}
