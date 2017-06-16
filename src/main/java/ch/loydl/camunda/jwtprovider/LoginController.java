package ch.loydl.camunda.jwtprovider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;

/**
 * Created by danielvogel on 13.06.17.
 */

@RestController
public class LoginController {

    @Autowired
    private LoginUtils loginUtils;

    @PostMapping("/jwt")
    @ResponseBody
    public ResponseEntity<String> credentials(@RequestBody LoginRequestBody loginRequestBody) throws ServletException {
        return loginUtils.getResponse(loginRequestBody);
    }
}
