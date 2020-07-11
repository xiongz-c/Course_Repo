package com.sustech.dbproj.repository;

import com.sustech.dbproj.domain.Order;
import com.sustech.dbproj.domain.Passenger;
import com.sustech.dbproj.domain.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Integer> {
    List<Order> findByPassenger( Passenger passenger );

    @Modifying(clearAutomatically = true)
    @Query(value = "insert into order_info(create_time, status, ticket, passenger) values (?1,?2,?3,?4)",nativeQuery=true)
    void createOrder(String createTime,Integer status, Integer ticket, Integer passenger);

    @Query(value = "select id from order_info where passenger=?1 and ticket=?2",nativeQuery=true)
    Integer getOrderId(Integer passenger, Integer ticket);

    @Query(value = "select * from order_info where passenger=?1 and ticket=?2",nativeQuery=true)
    List<Order> getOrder(Integer passenger, Integer ticket);
}
