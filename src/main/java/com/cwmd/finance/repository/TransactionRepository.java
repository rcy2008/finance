package com.cwmd.finance.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cwmd.finance.domain.Transaction;
import com.cwmd.finance.domain.TransactionStatus;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

	public List<Transaction> findByStatusAndLastModifiedBetween(
			TransactionStatus completed, long l, long currentTimeMillis);

}
