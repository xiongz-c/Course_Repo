package com.sustech.dbproj.controller;

import com.sustech.dbproj.domain.Seat;
import com.sustech.dbproj.domain.Ticket;
import com.sustech.dbproj.repository.SeatRepository;
import com.sustech.dbproj.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class TicketController {
    @Autowired
    TicketRepository repository;

    @Autowired
    SeatRepository repo_seat;

    /**
     * create new ticket
     */
    @PostMapping(value = "/Ticket/create")
    @Transactional
    public boolean createTicket( Integer passenger , Integer seat ) {
        Ticket ticket = new Ticket( );
        List<Ticket> tickets = repository.getTicketBySeat( seat );
        List<String> friend_seat = new ArrayList<>( );
        for (Ticket t : tickets) {
            friend_seat.add( t.getSeatNumber( ) );
        }
        String mySeat;
        Seat nowSeat = repo_seat.findById( seat ).orElse( null );
        if ( nowSeat == null ) return false;
        if (nowSeat.getTicket() < 1)return false;
        String seatType = nowSeat.getTicketType( );
        int type = 0;
        switch (seatType) {// ticketType:票种 WZ无座,F动卧,M一等座,O二等座,3硬卧,4软卧,9商务座
            case "F":
            case "3":
            case "4":
                type = 1;
                break;//所有卧铺类型
            case "M":
            case "0":
            case "9":
                type = 2;
                break;//所有座位类型
            default:
        }
        if ( type == 0 ) {
            int luckyNum = ( (int) ( Math.random( ) * 20 ) + 1 );
            do {
                mySeat = luckyNum + "无座";
            } while (friend_seat.contains( mySeat ));
        } else if ( type == 1 ) {
            do {
                int luckyNum = ( (int) ( Math.random( ) * 20 ) + 1 );

                mySeat = luckyNum + "上";
                if ( friend_seat.contains( mySeat ) ) break;
                mySeat = luckyNum + "中";
                if ( friend_seat.contains( mySeat ) ) break;
                mySeat = luckyNum + "下";
                if ( friend_seat.contains( mySeat ) ) break;
            } while (friend_seat.contains( mySeat ));
        } else {
            do {
                int luckyNum = ( (int) ( Math.random( ) * 20 ) + 1 );
                String ss = "E";
                switch (luckyNum % 5) {
                    case 0:
                        ss = "A";
                        break;
                    case 1:
                        ss = "B";
                        break;
                    case 2:
                        ss = "C";
                        break;
                    case 3:
                        ss = "D";
                        break;
                    case 4:
                        ss = "F";
                        break;
                }
                mySeat = luckyNum + ss;

            } while (friend_seat.contains( mySeat ));
        }
        repository.createTicket( ticket.getTicket_entrance( ) , passenger , seat , mySeat );
        return true;
    }

    /**
     * query ticket id by some info
     */
    @PostMapping(value = "/Ticket/queryId")
    @Transactional
    public Integer queryTicket( Integer passenger , Integer seat ) {
        return repository.getTicketId( passenger , seat );
    }
}
