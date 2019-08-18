package ke.paystep.mpesaservicefull.payload;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class SignUpRequest
{
    @NotBlank
    @Size(min = 1, max = 20)
    private String firstName;

    @NotBlank
    @Size(min=2, max = 20)
    private String lastName;

    @NotBlank
    @Size(min =3 ,max = 15)
    @Column
    private String userName;

    @NotBlank
    @Size(max = 20)
    @Column
    private String accountType;

    @NotBlank
    @Email
    @Size(max = 40)
    private String emailAddress;

    @NotBlank
    @Size(max = 40)
    private String companyName;

    @NotBlank
    @Size(max = 20)
    private String country;

    @NotBlank
    @Size(max = 15)
    private String mobileNumber;

    @NotBlank
    @Size(min = 6, max = 20)
    private String password;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
