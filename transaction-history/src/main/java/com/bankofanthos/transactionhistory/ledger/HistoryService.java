package com.bankofanthos.transactionhistory.ledger;
import com.bankofanthos.transactionhistory.config.AppProperties;
import com.bankofanthos.transactionhistory.domain.Transaction;
import com.bankofanthos.transactionhistory.events.HistoryProjection;
import java.util.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
public class HistoryService {
  private final TransactionRepository repository; private final HistoryProjection projection; private final AppProperties props;
  public HistoryService(TransactionRepository r, HistoryProjection p, AppProperties props){this.repository=r;this.projection=p;this.props=props;}
  @Transactional(readOnly=true) public Collection<Transaction> historyFor(String accountId){
    Collection<Transaction> cached=projection.snapshot(accountId); if(cached!=null) return cached;
    List<Transaction> rows=repository.findForAccount(accountId, props.localRoutingNum(), PageRequest.of(0, props.historyLimit()));
    projection.put(accountId, new ArrayDeque<>(rows)); return projection.snapshot(accountId);
  }
  public boolean projectionHealthy(){ return true; }
}
