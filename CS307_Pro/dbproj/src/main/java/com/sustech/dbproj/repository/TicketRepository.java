package com.sustech.dbproj.repository;

import com.sustech.dbproj.domain.Seat;
import com.sustech.dbproj.domain.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket,Integer> {
    @Modifying(clearAutomatically = true)
    @Query(value = "insert into ticket(ticket_entrance, passenger, seat, seat_number) values (?1,?2,?3,?4)",nativeQuery=true)
    void createTicket(String entrance, Integer passenger, Integer seat, String seatNumber);

    @Query(value = "select id from ticket where passenger=?1 and seat=?2 and seat_number<>'----'",nativeQuery=true)
    Integer getTicketId(Integer passenger, Integer seat);

    @Query(value = "select * from ticket where seat=?1",nativeQuery=true)
    List<Ticket> getTicketBySeat( Integer seat);

    @Modifying(clearAutomatically = true)
    @Query(value = "update ticket set seat_number='----' where id=?1",nativeQuery=true)
    void cancelTicket(Integer id);

}
