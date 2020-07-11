package com.sustech.dbproj.controller;

import com.sustech.dbproj.domain.Seat;
import com.sustech.dbproj.domain.Train;
import com.sustech.dbproj.repository.SeatRepository;
import com.sustech.dbproj.repository.StationRepository;
import com.sustech.dbproj.repository.TrainRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class SeatController {
    @Autowired
    private SeatRepository repository;

    @Autowired
    private TrainRepository rep_train;

    @Autowired
    private StationRepository rep_station;

    /**
     * query the seat types of a train
     */
    @PostMapping(value = "/Seat/query/types")
    List<String> querySeatType( String train_name ) {
        Train train = rep_train.findByName( train_name );
        List<String> types = new ArrayList<>( );
        types.add( "WZ" );
        types.add( "M" );
        types.add( "0" );
        types.add( "9" );
        if ( train_name.startsWith( "D" ) || train_name.startsWith( "G" ) ) {
            types.add( "F" );
        } else {
            types.add( "3" );
            types.add( "4" );
        }
        return types;
    }

    /**
     * query the seat types of a train
     */
    @PostMapping(value = "/Seat/query/leftTicket")
    List<String[]> queryLeftTicket( String start_station, String dest_station, String train_name, String date ) {
        Integer start = rep_station.findByName( start_station ).getId();
        Integer end = rep_station.findByName( dest_station ).getId();
        Integer train = rep_train.findByName( train_name ).getId();
        System.out.println( start + " " + end + " " +train  );
        List<String[]> ans = new ArrayList<>(  );
        List<Seat> seat_list = repository.getLeftTicket( train, start, end, date );
        for (Seat s : seat_list){
            ans.add( new String[]{s.getId().toString(),s.getTicketType(),s.getTicket().toString(),s.getPrice().toString()} );
        }
        return ans;
    }
}
