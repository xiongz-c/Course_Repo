package com.sustech.dbproj.controller;

import com.sustech.dbproj.domain.Schedule;
import com.sustech.dbproj.domain.Station;
import com.sustech.dbproj.domain.Train;
import com.sustech.dbproj.repository.ScheduleRepository;
import com.sustech.dbproj.repository.StationRepository;
import com.sustech.dbproj.repository.TrainRepository;
import javafx.util.converter.ShortStringConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
public class ScheduleController {
    @Autowired
    private ScheduleRepository repository;

    @Autowired
    private TrainRepository rep_train;

    @Autowired
    private StationRepository rep_station;

    /**
     * 直达查询
     * find Train by two station and date
     */
    @PostMapping(value = "/Schedule/query/bystation")
    public List<Object[]> queryScheduleByStation( String start_station , String dest_station , String date ) {
        return repository.scheduleStation( start_station, dest_station, date );
    }

    /**
     * 非直达查询：行程推荐
     * find Schedule by two station and date
     */
    @PostMapping(value = "/Schedule/query/bystation/recommend")
    @Transactional
    public List<Object[]> queryRecommend( String start_station , String dest_station , String date ) {
        return repository.scheduleRecommend( start_station, dest_station, date );
    }



    /**
     * find Train by two city and date
     */
    @PostMapping(value = "/Schedule/query/bycity")
    public List<Object[]> queryScheduleByCity( String city1 , String city2 , String date ) {
        return repository.scheduleCity( city1,city2,date );
    }


    /**
     * find Schedule by train name
     */
    @PostMapping(value = "/Schedule/queryByName")
    public List<Schedule> findSchedule( String train_name ) {
        return repository.findByTrain( rep_train.findByName( train_name ) );
    }

    /**
     * 固定一天/一辆车的行程表
     */
    @PostMapping(value = "/Schedule/distinct")
    public List<Object[]> getSchedule( String train_name, String date ) {
        return repository.getSchedule( rep_train.findByName( train_name ).getId(), date );
    }

    /**
     * set a station en_able
     */
    @PostMapping(value = "/Schedule/setEnable")
    @Transactional
    public boolean setEnable( String train , String station , String date , String en_able ) {
        Train t = rep_train.findByName( train );
        if ( t == null ) return false;
        Station sta = rep_station.findByName( station );
        if ( sta == null ) return false;
        Schedule sc = repository.findByTrainAndStationAndDate( t , sta , date ).get( 0 );
        if ( sc == null ) return false;
        if ( en_able.equalsIgnoreCase( "true" ) ) repository.changeOne( true , sc.getId( ) );
        else if ( en_able.equalsIgnoreCase( "false" ) ) repository.changeOne( false , sc.getId( ) );
        else return false;
        return true;
    }

    /**
     * add a new schedule
     */
    @PostMapping(value = "/Schedule/create")
    @Transactional
    public boolean createSchedule( String text ) {
        String[] lines = text.split( "\n" );
        for (String line : lines) {
            String[] tmp = line.split( "," );
            if ( tmp.length < 7 ) return false;
            Schedule sc = new Schedule( );
            sc.setTrain( rep_train.findByName( tmp[0] ) );
            sc.setStation( rep_station.findByName( tmp[1] ) );
            sc.setArrive_time( tmp[2] );
            sc.setLeave_time( tmp[3] );
            sc.setStation_no( Integer.parseInt( tmp[4] ) );
            sc.setEn_able( tmp[5].equalsIgnoreCase( "True" ) );
            sc.setDate( tmp[6] );
            if ( !checkDate( tmp[6] ) ) return false;
            repository.save( sc );
        }
        return true;
    }

    /**
     * @param train_name
     * @param date
     * @return whether delete success
     */
    @PostMapping(value = "/Schedule/delete")
    @Transactional
    public boolean deleteSchedule( String train_name , String date ) {
        repository.deleteByTrainAndDate( rep_train.findByName( train_name ).getId( ) , date );
        return true;
    }




    /**
     * 仅供内部调试使用
     * @param station
     * @return
     */
    @PostMapping(value = "/Schedule/query/station")
    @Transactional
    public List<Schedule> queryScheduleByStation( String station ) {
        return repository.findByStation( rep_station.findByName( station ) );
    }

    static boolean checkDate( String str ) {
        SimpleDateFormat sd = new SimpleDateFormat( "yyyy-MM-dd" );
        try {
            sd.setLenient( false );//此处指定日期/时间解析是否不严格，在true是不严格，false时为严格
            sd.parse( str );//从给定字符串的开始解析文本，以生成一个日期
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 通过车号、站点、日期查找具体的开车时间
     */
    @PostMapping(value = "/Schedule/query/detail")
    public List<Object[]> findScheduleByTrainStationDate(String train, String station, String date){
        return repository.getDetail( train, station, date );
    }
}
