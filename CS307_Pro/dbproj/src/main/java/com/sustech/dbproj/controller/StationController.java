package com.sustech.dbproj.controller;

import com.sustech.dbproj.domain.Schedule;
import com.sustech.dbproj.domain.Station;
import com.sustech.dbproj.repository.StationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class StationController {
    @Autowired
    private StationRepository repository;

    /**
     * creaete station
     */
    @PostMapping(value = "/Station/create")
    public boolean createStation( String station_name,String code,String city) {
        if(repository.findByName( station_name )!=null)return false;
        if(repository.findByCode( code )!=null)return false;
        Station sta = new Station();
        sta.setCity( city );
        sta.setCode( code );
        sta.setName( station_name );
        repository.save( sta );
        return true;
    }

    /**
     * query a station by code
     */
    @PostMapping(value = "/Station/query/code")
    public Station findByCode(String code){
        return repository.findByCode( code );
    }

    /**
     * query a station by name
     */
    @PostMapping(value = "/Station/query/name")
    public Station findByName(String name){
        return repository.findByName( name );
    }

    /**
     * query a station by city name
     */
    @PostMapping(value = "/Station/query/city")
    public List<Station> findByCity(String city){
        return repository.findByCity( city );
    }

}
