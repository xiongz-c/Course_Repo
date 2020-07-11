package com.sustech.dbproj.domain;

import javax.persistence.*;

@Entity
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "seat", nullable = false)
    private Seat seat;

    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "passenger", nullable = false)
    private Passenger passenger;

    private String ticket_entrance;

    @Column(name = "seat_number", nullable = false)
    private String seatNumber;

    public Ticket() {
        this.ticket_entrance = "B13";//random it
    }

    public Integer getId() {
        return id;
    }

    public void setId( Integer id ) {
        this.id = id;
    }

    public Seat getSeat() {
        return seat;
    }

    public void setSeat( Seat seat ) {
        this.seat = seat;
    }


    public Passenger getPassenger() {
        return passenger;
    }

    public void setPassenger( Passenger passenger ) {
        this.passenger = passenger;
    }

    public String getTicket_entrance() {
        return ticket_entrance;
    }

    public void setTicket_entrance( String ticket_entrance ) {
        this.ticket_entrance = ticket_entrance;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber( String seatNumber ) {
        this.seatNumber = seatNumber;
    }
}
