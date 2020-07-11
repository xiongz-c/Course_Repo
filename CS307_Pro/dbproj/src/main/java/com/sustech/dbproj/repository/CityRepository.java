package com.sustech.dbproj.repository;

import com.sustech.dbproj.domain.City;
import org.springframework.data.jpa.repository.JpaRepository;

//The first para is the name of class, the second one is the type of primary key.
public interface CityRepository extends JpaRepository<City,Integer> {

}
