package ch.loydl.camunda.jwtprovider;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.assertj.core.api.Assertions;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.identity.User;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;


/**
 * Created by hloydl on 15.06.2017.
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {TestConfig.class, LoginController.class, LoginUtils.class})
@SpringBootTest()
public class JWTProviderTest {

	@Autowired
	IdentityService identityService;

	@Autowired
	LoginController loginController;

	@Autowired
	@Rule
	public ProcessEngineRule processEngineRule;

	private User createUser(String id, String firstName, String lastName, String email, String password) {
		User user = identityService.newUser(id);
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setEmail(email);
		user.setPassword(password);
		identityService.saveUser(user);
		return user;
	}

	@Test
	public void jwtAuthenticationTest() throws Exception{
		String userId = "kermit";
		String pwd = "12345";
		createUser(userId, "Kermit_", "The_frog", "kermit_@muppetshow.com", pwd);
		LoginRequestBody loginRequestBody = new LoginRequestBody();
		loginRequestBody.setUsername(userId);
		loginRequestBody.setPassword(pwd);
		ResponseEntity<String> responseEntity = loginController.credentials(loginRequestBody);
		Assertions.assertThat(responseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
		Assertions.assertThat(responseEntity.getBody()).isNotNull();
		DecodedJWT decoded = JWT.decode(responseEntity.getBody());
		Assertions.assertThat(decoded).isNotNull();
	}

}
