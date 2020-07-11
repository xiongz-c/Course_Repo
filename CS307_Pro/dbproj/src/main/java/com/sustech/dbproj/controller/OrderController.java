package com.sustech.dbproj.controller;

import com.sustech.dbproj.domain.Order;
import com.sustech.dbproj.domain.Passenger;
import com.sustech.dbproj.domain.Ticket;
import com.sustech.dbproj.repository.OrderRepository;
import com.sustech.dbproj.repository.PassengerRepository;
import com.sustech.dbproj.repository.SeatRepository;
import com.sustech.dbproj.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@RestController
public class OrderController {
    @Autowired
    private OrderRepository repository;

    @Autowired
    private SeatRepository repo_seat;

    @Autowired
    private TicketRepository repo_ticket;

    @Autowired
    private PassengerRepository repo_passenger;

    /**
     * Cancel order by order ID
     * 可能调用此接口的情况：
     * 支付失败——如果是超时会自动取消无需调用此接口，指其他情况下支付失败
     * 用户主动取消订单（已付款退票，未付款直接取消）
     */
    @PostMapping(value = "/Order/cancel")
    @Transactional
    public boolean deleteOrder( Integer id ) {
        Order now = repository.findById( id ).orElse( null );
        if ( now == null ) return false;
        if(now.getStatus()==-1||now.getStatus()==2)return false;
        try {
            now.setStatus( -1 );
            repo_seat.increaseSeat( now.getTicket( ).getSeat( ).getId( ) );
            repo_ticket.cancelTicket( now.getTicket().getId() );
            return true;
        } catch (Exception e) {
            e.printStackTrace( );
        }
        return false;// return True when delete successfully, else return false
    }



    /**
     * create new order
     */
    @PostMapping(value = "/Order/create")
    @Transactional
    public boolean createOrder( Integer user , Integer ticket ) {
        LocalDateTime date = LocalDateTime.now( );
        Order ord = new Order( );
        ord.setStatus( 0 );
        ord.setCreate_time( date.toString( ) );
//        System.out.println(  ord.getCreate_time( ) +" "+ ord.getStatus( ) +" "+ ticket+" "+user  );
        Ticket myTicket = repo_ticket.findById( ticket ).orElse( null );
        if(myTicket == null)return false;
        if(myTicket.getSeat().getTicket() < 1)return false;
        repo_seat.decreaseSeat( myTicket.getSeat().getId() );
        repository.createOrder( ord.getCreate_time( ) , ord.getStatus( ) , ticket , user );
        return true;
    }

    /**
     * query a order of someone【指购票人而非乘车人】
     */
    @PostMapping(value = "/Order/query/byId")
    public List<Order> queryOrder( Integer user ) {
        Passenger User = repo_passenger.findById( user ).orElse( null );
        if ( User == null ) return null;
        return repository.findByPassenger( User );
    }

    /**
     * 查询具体的order
     */
    @PostMapping(value = "/Order/query")
    public List<Order> queryOrder( Integer user , Integer ticket ) {
        return repository.getOrder( user , ticket );
    }

    /**
     * [保留接口：只查询id]
     */
    @PostMapping(value = "/Order/queryId")
    public Integer queryOrderId( Integer user , Integer ticket ) {
        return repository.getOrderId( user , ticket );
    }


    /**
     * pay for an order
     */
    @PostMapping(value = "/Order/pay")
    @Transactional
    public String payOrder( Integer id ) {
        Order order = repository.findById( id ).orElse( null );
        if ( order == null ) return "This order is not found";

        LocalDateTime date = LocalDateTime.now( );
        LocalDateTime create_date = LocalDateTime.parse( order.getCreate_time( ) );
        Duration delta = Duration.between( date , create_date );
        if ( delta.toMinutes( ) >= 30 ) {
            order.setStatus( -1 );
            repo_ticket.cancelTicket( order.getTicket().getId() );
            repo_seat.decreaseSeat( order.getTicket( ).getSeat( ).getId( ) );
            return "Pay overtime (longer than 30 minutes). This order have been canceled!";
        }

        order.setStatus( 1 );
        return "Pay successful! Please check your order status again!";
    }

}
