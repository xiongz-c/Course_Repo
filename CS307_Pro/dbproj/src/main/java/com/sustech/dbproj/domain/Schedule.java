package com.sustech.dbproj.domain;

import javax.persistence.*;

@Entity
@Table(name = "schedule")
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "train", nullable = false)
    private Train train;

    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "station", nullable = false)
    private Station station;

    private Integer station_no;

    //    @Temporal( value = TemporalType.TIME)
    private String arrive_time;

    //    @Temporal( value = TemporalType.TIME)
    private String leave_time;

    //    @Temporal(value = TemporalType.DATE)
    @Column(nullable = false)
    private String date;

    @Column(name = "en_able")
    private boolean En_able;


    public Schedule() {
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

    public Station getStation() {
        return station;
    }

    public void setStation( Station station ) {
        this.station = station;
    }

    public Integer getStation_no() {
        return station_no;
    }

    public void setStation_no( Integer station_no ) {
        this.station_no = station_no;
    }

    public String getArrive_time() {
        return arrive_time;
    }

    public void setArrive_time( String arrive_time ) {
        this.arrive_time = arrive_time;
    }

    public String getLeave_time() {
        return leave_time;
    }

    public void setLeave_time( String leave_time ) {
        this.leave_time = leave_time;
    }

    public String getDate() {
        return date;
    }

    public void setDate( String date ) {
        this.date = date;
    }

    public boolean isEn_able() {
        return En_able;
    }

    public void setEn_able( boolean en_able ) {
        En_able = en_able;
    }
}
