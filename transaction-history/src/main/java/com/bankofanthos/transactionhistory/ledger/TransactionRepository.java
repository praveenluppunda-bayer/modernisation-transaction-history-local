package com.bankofanthos.transactionhistory.ledger;
import com.bankofanthos.transactionhistory.domain.Transaction;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
  @Query("SELECT t FROM Transaction t WHERE (t.fromAccountNum=?1 AND t.fromRoutingNum=?2) OR (t.toAccountNum=?1 AND t.toRoutingNum=?2) ORDER BY t.timestamp DESC")
  List<Transaction> findForAccount(String accountNum, String routingNum, Pageable pager);
}
