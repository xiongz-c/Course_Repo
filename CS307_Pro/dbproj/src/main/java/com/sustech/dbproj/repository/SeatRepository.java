package com.sustech.dbproj.repository;

import com.sustech.dbproj.domain.Seat;
import com.sustech.dbproj.domain.Train;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface SeatRepository extends JpaRepository<Seat,Integer> {

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update seat set ticket_num= ticket_num-1 where id=?1 ", nativeQuery = true)
    void decreaseSeat(Integer id);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update seat set ticket_num= ticket_num+1 where id=?1 ", nativeQuery = true)
    void increaseSeat(Integer id);

    List<Seat> findByTrain( Train train );

    @Query(value = "select * from seat where train=:par_train and from_station=:par_begin and to_station=:par_end and date=:now_date", nativeQuery = true)
    List<Seat> getLeftTicket( @Param("par_train")Integer train, @Param("par_begin")Integer station1,@Param("par_end")Integer station2, @Param("now_date")String date);

}
