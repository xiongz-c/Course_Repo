package com.sustech.dbproj.domain;

import javax.persistence.*;

@Entity
@Table(name = "order_info")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    //    @Temporal( value = TemporalType.TIMESTAMP)
    private String create_time;

    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "passenger", nullable = false)
    private Passenger passenger;

    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "ticket", nullable = false)
    private Ticket ticket;
    /**
     * 订单有以下几种状态：
     * -1：failed，所有操作取消的订单，座位释放
     * 0：创建订单成功，锁定座位，尚未付款
     * 1：付款成功，订单待执行（尚未发车）
     * 2：车辆发车，订单结束
     */
    @Column(nullable = false)
    private Integer status;

    public Order() {

    }

    public Integer getId() {
        return id;
    }

    public void setId( Integer id ) {
        this.id = id;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time( String create_time ) {
        this.create_time = create_time;
    }

    public Passenger getPassenger() {
        return passenger;
    }

    public void setPassenger( Passenger passenger ) {
        this.passenger = passenger;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket( Ticket ticket ) {
        this.ticket = ticket;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus( Integer status ) {
        this.status = status;
    }
}
