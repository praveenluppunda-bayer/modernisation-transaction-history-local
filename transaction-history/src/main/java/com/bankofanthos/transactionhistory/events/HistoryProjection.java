package com.bankofanthos.transactionhistory.events;
import com.bankofanthos.transactionhistory.config.AppProperties;
import com.bankofanthos.transactionhistory.domain.Transaction;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
@Component
public class HistoryProjection {
  private final AppProperties props; private final Map<String, Deque<Transaction>> byAccount = new ConcurrentHashMap<>();
  public HistoryProjection(AppProperties props){this.props=props;}
  public boolean isTracked(String id){return byAccount.containsKey(id);}
  public void put(String id, Deque<Transaction> h){ if(byAccount.size()>=props.cacheSize()&&!byAccount.containsKey(id)) byAccount.remove(byAccount.keySet().iterator().next()); byAccount.put(id,h);}
  public Collection<Transaction> snapshot(String id){ Deque<Transaction> l=byAccount.get(id); if(l==null) return null; synchronized(l){return new ArrayDeque<>(l);} }
  @EventListener public void onLedgerEvent(LedgerTransactionEvent e){ Transaction t=e.transaction(); String local=props.localRoutingNum();
    if(local.equals(t.getFromRoutingNum())&&isTracked(t.getFromAccountNum())) prepend(t.getFromAccountNum(),t);
    if(local.equals(t.getToRoutingNum())&&isTracked(t.getToAccountNum())) prepend(t.getToAccountNum(),t); }
  private void prepend(String id, Transaction t){ Deque<Transaction> l=byAccount.get(id); if(l==null) return; synchronized(l){ l.addFirst(t); while(l.size()>props.historyLimit()) l.removeLast(); } }
}
