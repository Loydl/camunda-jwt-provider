package ch.loydl.camunda.jwtprovider;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.camunda.bpm.BpmPlatform;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngines;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import java.io.UnsupportedEncodingException;
import java.util.Date;

/**
 * Created by danielvogel on 13.06.17.
 */

@Component
public class LoginUtils {

    @Autowired
    private IdentityService identityService;

    @Value("${JWT.secret}")
    private String secret;

    @Value("${JWT.issuer}")
    private String issuer;

    @Value("${JWT.expiryDuration}")
    private Integer expiryDuration;

    public ResponseEntity<String> getResponse(LoginRequestBody loginRequestBody) {
        String username = loginRequestBody.getUsername();
        String password = loginRequestBody.getPassword();

        if (username != null && password != null) {
            if(isAuthenticated(username, password)) {

                try {
                    String token = createJWT(username, secret);

                    return ResponseEntity.status(HttpStatus.OK).body(token);
                } catch (ServletException exception) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
                }
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Wrong credentials");
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Credentials missing");
        }
    }

    private String createJWT(String username, String secret) throws ServletException {

        if(secret == null) {
            throw new ServletException("No secret defined");
        }
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            String token = JWT.create()
                    .withIssuer(issuer)
                    .withIssuedAt(new Date(System.currentTimeMillis()))
                    .withExpiresAt(new Date(System.currentTimeMillis() + expiryDuration))
                    .withClaim("username", username)
                    .sign(algorithm);
            return token;
        } catch (UnsupportedEncodingException exception) {
            throw new UnsupportedOperationException(exception);
        }
    }

    private boolean isAuthenticated(String userName, String password) {
        return identityService.checkPassword(userName, password);
    }

}
