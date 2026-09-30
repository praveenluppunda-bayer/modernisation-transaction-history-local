package com.bankofanthos.transactionhistory.web;
import com.bankofanthos.transactionhistory.config.AppProperties;
import com.bankofanthos.transactionhistory.domain.Transaction;
import com.bankofanthos.transactionhistory.events.LedgerTransactionEvent;
import com.bankofanthos.transactionhistory.ledger.TransactionRepository;
import java.time.Instant; import java.util.Map;
import org.springframework.context.ApplicationEventPublisher; import org.springframework.http.ResponseEntity; import org.springframework.web.bind.annotation.*;
@RestController
public class StandInEventController {
  private final AppProperties props; private final TransactionRepository repository; private final ApplicationEventPublisher publisher;
  public StandInEventController(AppProperties p, TransactionRepository r, ApplicationEventPublisher pub){props=p;repository=r;publisher=pub;}
  @PostMapping("/standin/ledger-events") public ResponseEntity<?> publish(@RequestBody Map<String,Object> body){
    String from=String.valueOf(body.getOrDefault("fromAccountNum","1011226111")); String to=String.valueOf(body.getOrDefault("toAccountNum","1011226112"));
    int amount=Integer.parseInt(String.valueOf(body.getOrDefault("amount","100")));
    Transaction saved=repository.save(new Transaction(from, props.localRoutingNum(), to, props.localRoutingNum(), amount, Instant.now()));
    publisher.publishEvent(new LedgerTransactionEvent(saved)); return ResponseEntity.accepted().body(saved);
  }
}
