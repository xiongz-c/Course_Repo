package com.sustech.dbproj.repository;

import com.sustech.dbproj.domain.Schedule;
import com.sustech.dbproj.domain.Station;
import com.sustech.dbproj.domain.Train;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule,Integer> {

    @Query(value = "with a as (select station , station_no , date , train from schedule where station =:start_s and date = '2020-05-01'), " +
    "b as (select station , station_no , date , train from schedule where station =:dest_s and date =:now_date) " +
    "select distinct a.train from a inner join b on a.train = b.train;",nativeQuery=true)
    List<Integer> findTrainId( @Param("start_s")Integer station1, @Param("dest_s")Integer station2, @Param("now_date")String date);

    @Query(value = "select distinct * from schedule where train=:par_train and station=:par_station and date=:now_date",nativeQuery=true)
    List<Schedule> findByTidSidDate( @Param("par_train")Integer train, @Param("par_station")Integer station, @Param("now_date")String date);

    List<Schedule> findByTrainAndDate( Train train , String date );

    List<Schedule> findByTrainAndStationAndDate( Train train , Station station , String date );

    List<Schedule> findByTrain( Train train );

    List<Schedule> findByStationAndDate( Station station , String date );

    List<Schedule> findByStation( Station station );

    @Query(value = "select distinct s.name, arrive_time, leave_time,date,station_no from schedule join station s on schedule.station = s.id where train=:par_train and date=:now_date order by arrive_time;",nativeQuery=true)
    List<Object[]> getSchedule( @Param("par_train")Integer train, @Param("now_date")String date);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update schedule set en_able=?1 where id=?2 ", nativeQuery = true)
    void changeOne(boolean en_able, Integer id);

    @Modifying(clearAutomatically = true)
    @Query(value = "delete from schedule where train=?1 and date=?2 ", nativeQuery = true)
    void deleteByTrainAndDate( Integer train , String date );

    @Query(value = "with fi as (select ts1.train   tid, ts1.station sid1, ts2.station sid2, (select cast((substring(ts1.arrive_time, 1, 2) || substring(ts1.arrive_time, 4, 2)) as integer)) s1r, (select cast((substring(ts1.leave_time, 1, 2) || substring(ts1.leave_time, 4, 2)) as integer))   s1l, (select cast((substring(ts2.arrive_time, 1, 2) || substring(ts2.arrive_time, 4, 2)) as integer)) s2r, (select cast((substring(ts2.leave_time, 1, 2) || substring(ts2.leave_time, 4, 2)) as integer))   s2l     from schedule ts1   join schedule ts2 on ts1.train = ts2.train and ts1.station_no < ts2.station_no and ts1.date=ts2.date   join station s  on ts1.station = s.id     where s.name =?1 and ts1.date=?3 and ts2.arrive_time > ts1.leave_time) select distinct s1,  s2,  s3,  t1,  t2,  (case when dept >= 2400 then dept - 2400 else dept end) dept,  (case when dept >= 2400 then arrt - 2400 else arrt end) arrt,  (case when dept >= 2400 then leat - 2400 else leat end) leat,  (case when dept >= 2400 then endt - 2400 else endt end) endt from (select s1,      s2,      s3,      t1,      t2,      (case when dept >= 2400 then dept - 2400 else dept end) dept,      (case when dept >= 2400 then arrt - 2400 else arrt end) arrt,      (case when dept >= 2400 then leat - 2400 else leat end) leat,      (case when dept >= 2400 then endt - 2400 else endt end) endt from (  select s1,    s2,    s3,    t1,    t2,    (case when dept >= 2400 then dept - 2400 else dept end) dept,    (case when dept >= 2400 then arrt - 2400 else arrt end) arrt,    (case when dept >= 2400 then leat - 2400 else leat end) leat,    (case when dept >= 2400 then endt - 2400 else endt end) endt  from (select s1,  s2,  s3,  t1,  t2,  (case when dept >= 2400 then dept - 2400 else dept end) dept,  (case when dept >= 2400 then arrt - 2400 else arrt end) arrt,  (case when dept >= 2400 then leat - 2400 else leat end) leat,  (case when dept >= 2400 then endt - 2400 else endt end) endt   from (select s1,  s2,  s3,  t1,  t2,  dept,  arrt,  (arrt6 + transtime) / 60 * 100 + (arrt6 + transtime) % 60 leat,  (arrt6 + transtime + endt6 - leat6) / 60 * 100 +  (arrt6 + transtime + endt6 - leat6) % 60  endt   from (select s1.name s1,   s2.name s2,   s3.name s3,   t1.name t1,   t2.name t2,   fi.s1l  dept,   fi.s2r  arrt,   se.s1l  leat,   (fi.s2r / 100) * 60 + fi.s2r % 100 arrt6,   (se.s1l / 100) * 60 + se.s1l % 100 leat6,   se.s2r  endt,   se.s2r / 100 * 60 + se.s2r % 100   endt6,   ((se.s1l / 100 - fi.s2r / 100) * 60 + (se.s1l % 100 - fi.s2r % 100) +    5 * 24 * 60) % 1440  transtime,   (fi.s1l % 2400 + fi.s2r - fi.s1l +    ((se.s1l / 100 - fi.s2r / 100) * 60 + (se.s1l % 100 - fi.s2r % 100) +     5 * 24 * 60) % 1440 + se.s2r - se.s1l) wei from fi     join (   select ts1.train   tid, ts1.station sid1, ts2.station sid2, (select cast((substring(ts1.arrive_time, 1, 2) || substring(ts1.arrive_time, 4, 2)) as integer)) s1r, (select cast((substring(ts1.leave_time, 1, 2) || substring(ts1.leave_time, 4, 2)) as integer))   s1l, (select cast((substring(ts2.arrive_time, 1, 2) || substring(ts2.arrive_time, 4, 2)) as integer)) s2r, (select cast((substring(ts2.leave_time, 1, 2) || substring(ts2.leave_time, 4, 2)) as integer))   s2l   from schedule ts1   join schedule ts2      on ts1.train = ts2.train and ts1.station_no < ts2.station_no and ts1.date=ts2.date   join station s on ts2.station = s.id   where s.name =?2 and ts1.date=?3  and ts2.arrive_time > ts1.leave_time) se  on fi.sid2 = se.sid1 and     ((se.s1l / 100 - fi.s2r / 100) * 60 + (se.s1l % 100 - fi.s2r % 100) +      5 * 24 * 60) % 1440 >= 15 and     ((se.s1l / 100 - fi.s2r / 100) * 60 + (se.s1l % 100 - fi.s2r % 100) +      5 * 24 * 60) % 1440 <= 480 and fi.tid != se.tid     join station s1  on s1.id = fi.sid1     join station s2 on s2.id = se.sid1     join station s3 on s3.id = se.sid2     join train t1 on t1.id = fi.tid     join train t2 on t2.id = se.tid order by wei      ) t) g) k) o) u", nativeQuery = true)
    List<Object[]> scheduleRecommend( String station1 , String station2, String date );
    
    @Query(value = "select distinct t.name train, s.name start_station, s2.name dest_station, ts1.arrive_time arrtime1, ts1.leave_time leavetime1, ts2.arrive_time arrtime2, ts2.leave_time leavetime2, ts1.date date from schedule ts1  join schedule ts2 on ts1.train = ts2.train and ts1.station_no < ts2.station_no and ts1.date = ts2.date  join station s on ts1.station = s.id  join station s2 on ts2.station = s2.id  join train t on ts1.train = t.id where ts1.station in (select id from station where station.city = ?1)   and ts2.station in (select id from station where station.city = ?2)   and ts1.date = ?3 ", nativeQuery = true)
    List<Object[]> scheduleCity(String city1, String city2, String date);

    @Query(value = "select distinct t.name train, s.name start_station, s2.name dest_station, ts1.arrive_time arr1, ts1.leave_time leave1, ts2.arrive_time arr2, ts2.leave_time leave2, ts1.date date from schedule ts1 join schedule ts2 on ts1.train = ts2.train and ts1.station_no < ts2.station_no and ts1.date = ts2.date join station s on ts1.station = s.id join station s2 on ts2.station = s2.id join train t on ts1.train = t.id where s.name= ?1 and s2.name= ?2 and ts1.date =  ?3  ", nativeQuery = true)
    List<Object[]> scheduleStation(String station1, String station2, String date);

    @Query(value="select t.name train, s.name station, arrive_time arrtime, leave_time ltime, date date from schedule join station s on schedule.station = s.id join train t on schedule.train = t.id where t.name=?1 and s.name=?2 and date=?3 ", nativeQuery=true)
    List<Object[]> getDetail(String train, String station, String date);
}
