package com.cwmd.finance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.stefan.snrpc.domain.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {

}
