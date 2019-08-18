package ke.paystep.mpesaservicefull.payload;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class AuthenticationRequest
{
    @NotBlank
    @Size(max = 100)
    private String usernameOrEmail;

    @NotBlank
    @Size(max = 150)
    private String password;

    public String getUsernameOrEmail() {
        return usernameOrEmail;
    }

    public void setUsernameOrEmail(String usernameOrEmail) {
        this.usernameOrEmail = usernameOrEmail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
