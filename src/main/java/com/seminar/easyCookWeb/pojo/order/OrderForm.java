package com.seminar.easyCookWeb.pojo.order;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.seminar.easyCookWeb.pojo.appUser.Member;
import lombok.*;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.LinkedList;
import java.util.List;

@Entity
@ToString
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class OrderForm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="order_id")
    private long id;

    @Column(columnDefinition = "nvarchar(30)")
    private String orderNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @JsonBackReference
    private Member member;

    @OneToMany(mappedBy = "orderForm", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    @Builder.Default
    private List<OrderItem> orderItems = new LinkedList<>();

    @Column(columnDefinition = "TIMESTAMP")
    private OffsetDateTime orderTime;

    @Column(columnDefinition = "nvarchar(30)")
    private String payWay;

    @Column(columnDefinition = "nvarchar(30)")
    private String serviceWay;

    @Column(columnDefinition = "TIMESTAMP")
    private OffsetDateTime hopeDeliverTime;

    @Column(columnDefinition = "TIMESTAMP")
    private OffsetDateTime realDeliverTime;

    @Column(columnDefinition = "TIMESTAMP")
    private OffsetDateTime shippingTime;

    @Column(columnDefinition = "nvarchar(30)")
    private String status;

    @Column(columnDefinition = "nvarchar(50)")
    private String address;

    @Column(columnDefinition = "decimal(28,4)")
    @Builder.Default
    private Double transportFee = 0D;

    @Column(columnDefinition = "decimal(28,4)")
    @Builder.Default
    private Double discount = 0D;

    @Column(columnDefinition = "decimal(28,4)")
    @Builder.Default
    private Double sum = 0D;

}
