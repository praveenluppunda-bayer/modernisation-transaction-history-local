package com.bankofanthos.transactionhistory.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.bankofanthos.transactionhistory.config.AppProperties;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {
  @Bean
  public JWTVerifier jwtVerifier(AppProperties props) throws Exception {
    Path path = Path.of(props.pubKeyPath());
    if (!Files.exists(path)) {
      throw new IllegalStateException("PUB_KEY_PATH not found: " + path.toAbsolutePath());
    }
    String keyStr =
        Files.readString(path)
            .replace("-----BEGIN PUBLIC KEY-----", "")
            .replace("-----END PUBLIC KEY-----", "")
            .replaceAll("\\s", "");
    RSAPublicKey publicKey =
        (RSAPublicKey)
            KeyFactory.getInstance("RSA")
                .generatePublic(new X509EncodedKeySpec(Base64.getDecoder().decode(keyStr)));
    return JWT.require(Algorithm.RSA256(publicKey, null)).build();
  }
}
