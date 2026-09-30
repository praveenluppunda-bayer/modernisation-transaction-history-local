package com.bankofanthos.transactionhistory.domain;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import java.time.Instant;
@Entity @Table(name="TRANSACTIONS")
public class Transaction {
  private static final double CENTS_PER_DOLLAR=100.0;
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY) @Column(name="TRANSACTION_ID",nullable=false,updatable=false) private Long transactionId;
  @Column(name="FROM_ACCT",nullable=false,updatable=false) @JsonProperty("fromAccountNum") private String fromAccountNum;
  @Column(name="FROM_ROUTE",nullable=false,updatable=false) @JsonProperty("fromRoutingNum") private String fromRoutingNum;
  @Column(name="TO_ACCT",nullable=false,updatable=false) @JsonProperty("toAccountNum") private String toAccountNum;
  @Column(name="TO_ROUTE",nullable=false,updatable=false) @JsonProperty("toRoutingNum") private String toRoutingNum;
  @Column(name="AMOUNT",nullable=false,updatable=false) @JsonProperty("amount") private Integer amount;
  @Column(name="TIMESTAMP",nullable=false,updatable=false) @JsonProperty("timestamp") private Instant timestamp;
  protected Transaction(){}
  public Transaction(String a,String b,String c,String d,Integer e,Instant f){fromAccountNum=a;fromRoutingNum=b;toAccountNum=c;toRoutingNum=d;amount=e;timestamp=f;}
  public Long getTransactionId(){return transactionId;} public String getFromAccountNum(){return fromAccountNum;} public String getFromRoutingNum(){return fromRoutingNum;}
  public String getToAccountNum(){return toAccountNum;} public String getToRoutingNum(){return toRoutingNum;} public Integer getAmount(){return amount;} public Instant getTimestamp(){return timestamp;}
  @Override public String toString(){return String.format("%s->$%.2f->%s",fromAccountNum,amount/CENTS_PER_DOLLAR,toAccountNum);}
}
