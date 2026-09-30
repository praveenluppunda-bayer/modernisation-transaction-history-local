# Transaction History (modernized)

Spring Boot 3.4 / Java 21. Event-driven updates. Local H2 + JWT stand-ins.

```bash
cd transaction-history
mvn spring-boot:run
TOKEN=$(node scripts/mint-jwt.mjs 1011226111)
curl -H "Authorization: Bearer $TOKEN" localhost:8080/transactions/1011226111
```
