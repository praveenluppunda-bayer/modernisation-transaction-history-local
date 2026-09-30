package com.bankofanthos.transactionhistory;
import static org.junit.jupiter.api.Assertions.assertEquals; import static org.mockito.ArgumentMatchers.anyString; import static org.mockito.Mockito.when;
import com.auth0.jwt.*; import com.auth0.jwt.exceptions.JWTVerificationException; import com.auth0.jwt.interfaces.*;
import com.bankofanthos.transactionhistory.config.AppProperties; import com.bankofanthos.transactionhistory.domain.Transaction;
import com.bankofanthos.transactionhistory.ledger.HistoryService; import com.bankofanthos.transactionhistory.web.TransactionHistoryController;
import java.time.Instant; import java.util.List;
import org.junit.jupiter.api.*; import org.junit.jupiter.api.extension.ExtendWith; import org.mockito.*; import org.mockito.junit.jupiter.MockitoExtension; import org.springframework.http.HttpStatus;
@ExtendWith(MockitoExtension.class)
class TransactionHistoryControllerTest {
  @Mock JWTVerifier verifier; @Mock HistoryService historyService; @Mock DecodedJWT jwt; @Mock Claim claim; TransactionHistoryController controller;
  @BeforeEach void setUp(){ controller=new TransactionHistoryController(verifier, historyService, new AppProperties("2.0.0-test","987654321","standins/keys/jwtRS256.key.pub",100,1_000_000,new AppProperties.Standin(true,"in-process"))); }
  @Test void version(){ assertEquals("2.0.0-test", controller.version().getBody()); }
  @Test void rejectsMismatchedAcct(){ when(verifier.verify("tok")).thenReturn(jwt); when(jwt.getClaim("acct")).thenReturn(claim); when(claim.asString()).thenReturn("999"); assertEquals(HttpStatus.UNAUTHORIZED, controller.getTransactions("Bearer tok","1011226111").getStatusCode()); }
  @Test void returnsHistoryWhenAuthorized(){ when(verifier.verify("tok")).thenReturn(jwt); when(jwt.getClaim("acct")).thenReturn(claim); when(claim.asString()).thenReturn("1011226111"); when(historyService.historyFor("1011226111")).thenReturn(List.of(new Transaction("1011226111","987654321","1011226112","987654321",100,Instant.now()))); assertEquals(HttpStatus.OK, controller.getTransactions("Bearer tok","1011226111").getStatusCode()); }
  @Test void rejectsBadJwt(){ when(verifier.verify(anyString())).thenThrow(new JWTVerificationException("bad")); assertEquals(HttpStatus.UNAUTHORIZED, controller.getTransactions("Bearer bad","1011226111").getStatusCode()); }
}
