package com.bankofanthos.transactionhistory.config;
import org.springframework.boot.context.properties.ConfigurationProperties;
@ConfigurationProperties(prefix = "app")
public record AppProperties(String version, String localRoutingNum, String pubKeyPath, int historyLimit, int cacheSize, Standin standin) {
  public record Standin(boolean seedLedger, String eventBus) {}
}
