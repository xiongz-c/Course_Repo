package com.sustech.dbproj.repository;

import com.sustech.dbproj.domain.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;


public interface PassengerRepository extends JpaRepository<Passenger,Integer> {

    Passenger findByPhone(String phone_num);
    Passenger findByIdc(String id_card);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update passenger set phone_num=?1,password=?2 where id_card=?3 ", nativeQuery = true)
    void changeOne(String phone, String pass,String idc);
}
