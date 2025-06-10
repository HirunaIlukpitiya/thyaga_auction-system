package com.thyaga.auction_system.data.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import com.thyaga.auction_system.util.AuctionStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@ToString
@Entity
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    private BigDecimal startingPrice;

    private BigDecimal currentBid;

    @Enumerated(EnumType.STRING)
    private AuctionStatus auctionStatus;

    @Column(nullable = false)
    private LocalDateTime auctionEndTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", nullable = false)
    private User seller;

    @OneToMany(mappedBy = "item",cascade = CascadeType.ALL)
    private List<Bid> bids = new ArrayList<>();

    @Version
    private Long version;
}
