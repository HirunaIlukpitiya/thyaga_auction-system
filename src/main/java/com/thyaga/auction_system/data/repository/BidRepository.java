package com.thyaga.auction_system.data.repository;

import com.thyaga.auction_system.data.entity.Bid;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BidRepository extends JpaRepository<Bid, Long> {

}
