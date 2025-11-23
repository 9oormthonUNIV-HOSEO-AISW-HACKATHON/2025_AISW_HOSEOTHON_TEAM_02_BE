package com.backend.dosol.repository;

import com.backend.dosol.entity.Exchange;
import com.backend.dosol.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExchangeRepository extends JpaRepository<Exchange, Long> {

	List<Exchange> findAllByReceiver(User receiver);
}
