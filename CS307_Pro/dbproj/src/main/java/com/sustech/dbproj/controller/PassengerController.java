package com.sustech.dbproj.controller;


import com.sustech.dbproj.domain.Passenger;
import com.sustech.dbproj.repository.PassengerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PassengerController {
    @Autowired
    private PassengerRepository repository;

    /**
     * new a passenger
     */
    @PostMapping(value = "/Passenger/register")
    public Passenger create( Integer age, String id_card, String name, String phone,String password) {
        Passenger pp = new Passenger();
        pp.setAge( age );
        pp.setIdc( id_card );
        pp.setName( name );
        pp.setPhone( phone );
        pp.setPassword( password );
        return repository.save(pp);
    }

    /**
     * check id_card
     */
    @PostMapping(value = "/Passenger/check/idc")
    public boolean checkIdc( String IDC ) {
        Passenger p =repository.findByIdc( IDC );//If not found, return null
        return p == null;//If no this id_card, return true, else return false;
    }
    /**
     * check phone number
     */
    @PostMapping(value = "/Passenger/check/phone")
    public boolean checkPhone( String phone ) {
        Passenger p =repository.findByPhone( phone );//If not found, return null
        return p == null;//If no this phone, return true, else return false;
    }

    /**
     * check phone number and password
     */
    @PostMapping(value = "/Passenger/login")
    public boolean check( Passenger ps ) {
        Passenger p =repository.findByPhone( ps.getPhone() );//If not found, return null
        if ( p == null )return false;
        return ps.getPassword( ).equals( p.getPassword( ) );
    }

    /**
     * query passenger by phone
     */
    @PostMapping(value = "/Passenger/query/phone")
    public Passenger getPassengerByPhone( String phone ) {
        return repository.findByPhone( phone );//If not found, return null
    }

    /**
     * query passenger by id_card
     */
    @PostMapping(value = "/Passenger/query/IDC")
    public Passenger getPassengerByIdc( String IDC) {
        return repository.findByIdc( IDC );//If not found, return null
    }

    /**
     * update phone or password
     */
    @PostMapping(value="/Passenger/update")
    @Transactional
    public boolean updateUser(String phone,String password, String idc){
//        System.out.println( phone + " " + password + " " + idc );
        if(getPassengerByPhone( phone )!=null && !getPassengerByPhone( phone ).getIdc().equals( idc ))return false;// this phone have been used.
        repository.changeOne( phone,password,idc );
        return true;
    }

    /**
     * find all passenger
     */
    @GetMapping(value = "/Passenger/all")
    public List<Passenger> getAll() {
        return repository.findAll();//If not found, return null
    }
}
