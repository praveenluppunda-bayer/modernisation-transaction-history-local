package com.bankofanthos.transactionhistory.web;
import com.auth0.jwt.JWTVerifier; import com.auth0.jwt.exceptions.JWTVerificationException; import com.auth0.jwt.interfaces.DecodedJWT;
import com.bankofanthos.transactionhistory.config.AppProperties; import com.bankofanthos.transactionhistory.ledger.HistoryService;
import org.slf4j.*; import org.springframework.http.*; import org.springframework.web.bind.annotation.*;
@RestController
public class TransactionHistoryController {
  private static final Logger log=LoggerFactory.getLogger(TransactionHistoryController.class);
  private final JWTVerifier verifier; private final HistoryService historyService; private final AppProperties props;
  public TransactionHistoryController(JWTVerifier v, HistoryService h, AppProperties p){verifier=v;historyService=h;props=p;}
  @GetMapping("/version") public ResponseEntity<String> version(){return ResponseEntity.ok(props.version());}
  @GetMapping("/ready") public ResponseEntity<String> readiness(){return ResponseEntity.ok("ok");}
  @GetMapping("/healthy") public ResponseEntity<String> liveness(){ if(!historyService.projectionHealthy()) return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("history projection not healthy"); return ResponseEntity.ok("ok"); }
  @GetMapping("/transactions/{accountId}") public ResponseEntity<?> getTransactions(@RequestHeader(value="Authorization",required=false) String authorization, @PathVariable String accountId){
    String token=authorization; if(token!=null&&token.startsWith("Bearer ")) token=token.substring(7);
    if(token==null||token.isBlank()) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("not authorized");
    try { DecodedJWT jwt=verifier.verify(token); if(!accountId.equals(jwt.getClaim("acct").asString())) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("not authorized"); return ResponseEntity.ok(historyService.historyFor(accountId)); }
    catch(JWTVerificationException e){ return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("not authorized"); }
    catch(Exception e){ log.error("Failed to load history for {}", accountId, e); return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("history error"); }
  }
}
