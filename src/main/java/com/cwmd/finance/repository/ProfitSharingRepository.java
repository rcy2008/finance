package com.cwmd.finance.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cwmd.finance.domain.ProfitSharing;
import com.cwmd.finance.domain.Transaction;

public interface ProfitSharingRepository extends JpaRepository<ProfitSharing, Long> {

	public ProfitSharing findByTransaction(Transaction transaction);

}
