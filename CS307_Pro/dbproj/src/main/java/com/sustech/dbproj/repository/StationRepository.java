package com.sustech.dbproj.repository;

import com.sustech.dbproj.domain.Station;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StationRepository extends JpaRepository<Station, Integer> {

    Station findByName( String name );

    Station findByCode(String code);

    List<Station> findByCity( String city );
}
