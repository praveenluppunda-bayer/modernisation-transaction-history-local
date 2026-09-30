package com.bankofanthos.transactionhistory.config;
import com.bankofanthos.transactionhistory.domain.Transaction;
import com.bankofanthos.transactionhistory.events.LedgerTransactionEvent;
import com.bankofanthos.transactionhistory.ledger.TransactionRepository;
import java.time.Instant;
import org.slf4j.*; import org.springframework.boot.*; import org.springframework.context.ApplicationEventPublisher; import org.springframework.stereotype.Component;
@Component
public class StandInLedgerBootstrap implements ApplicationRunner {
  private static final Logger log=LoggerFactory.getLogger(StandInLedgerBootstrap.class);
  private final AppProperties props; private final TransactionRepository repository; private final ApplicationEventPublisher publisher;
  public StandInLedgerBootstrap(AppProperties p, TransactionRepository r, ApplicationEventPublisher pub){props=p;repository=r;publisher=pub;}
  @Override public void run(ApplicationArguments args){ if(!props.standin().seedLedger()||repository.count()>0) return; String route=props.localRoutingNum(); Instant now=Instant.now();
    for(Transaction t: new Transaction[]{ new Transaction("1011226111",route,"1011226112",route,12500,now.minusSeconds(300)), new Transaction("1011226112",route,"1011226111",route,5000,now.minusSeconds(180)), new Transaction("1011226111",route,"1011226113",route,9900,now.minusSeconds(60)) }) publisher.publishEvent(new LedgerTransactionEvent(repository.save(t)));
    log.info("Seeded stand-in ledger"); }
}
