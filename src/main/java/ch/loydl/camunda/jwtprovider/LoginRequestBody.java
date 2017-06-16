package ch.loydl.camunda.jwtprovider;

/**
 * Created by danielvogel on 15.06.17.
 */
public class LoginRequestBody {

    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
