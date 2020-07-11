package com.sustech.dbproj.domain;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "train", nullable = false)
    private Train train;

    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "from_station", nullable = false)
    private Station from_station;

    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "to_station", nullable = false)
    private Station to_station;

    @Column(name = "ticket_num", nullable = false)
    private Integer ticket;

    @Column(nullable = false)
    private boolean windows;

    /**
     * ticketType:票种
     * WZ无座,F动卧,M一等座,O二等座,3硬卧,4软卧,9商务座
     */
    @Column(name = "level", nullable = false)
    private String ticketType;

    @Column(name = "date")
    private String date;

    @Column(name = "price")
    private BigDecimal price;

    public Seat() { }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice( BigDecimal price ) {
        this.price = price;
    }

    public String getDate() {
        return date;
    }
    public void setDate( String date ) {
        this.date = date;
    }


    public Integer getId() {
        return id;
    }

    public void setId( Integer id ) {
        this.id = id;
    }

    public Train getTrain() {
        return train;
    }

    public void setTrain( Train train ) {
        this.train = train;
    }

    public Station getFrom_station() {
        return from_station;
    }

    public void setFrom_station( Station from_station ) {
        this.from_station = from_station;
    }

    public Station getTo_station() {
        return to_station;
    }

    public void setTo_station( Station to_station ) {
        this.to_station = to_station;
    }

    public Integer getTicket() {
        return ticket;
    }

    public void setTicket( Integer ticket ) {
        this.ticket = ticket;
    }

    public boolean isWindows() {
        return windows;
    }

    public void setWindows( boolean windows ) {
        this.windows = windows;
    }
    public String getTicketType() {
        return ticketType;
    }

    public void setTicketType( String ticketType ) {
        this.ticketType = ticketType;
    }

}
