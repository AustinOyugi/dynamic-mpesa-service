package ke.paystep.mpesaservicefull.model;

import ke.paystep.mpesaservicefull.model.audit.DateAudit;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Austin Oyugi on 17/8/2019.
 */
@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "userName"
        }),
        @UniqueConstraint(columnNames = {
                "emailAddress"
        })
})
public class User extends DateAudit
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @NotBlank
    @Size(max = 20)
    @Column
    private String firstName;

    @NotBlank
    @Size(max = 20)
    @Column
    private String lastName;

    @NotBlank
    @Size(max = 15)
    @Column
    private String userName;

    @NotBlank
    @Size(max = 20)
    @Column
    private String accountType;

    @NaturalId
    @NotBlank
    @Email
    @Size(max = 40)
    @Column
    private String emailAddress;

    @NotBlank
    @Size(max = 40)
    @Column
    private String companyName;

    @NotBlank
    @Size(max = 20)
    @Column
    private String country;

    @NotBlank
    @Size(max = 15)
    @Column
    private String mobileNumber;

    @NotBlank
    @Size(max = 100)
    private String password;

    @Column
    private short accountActivated;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
    joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    public User(){

    }

    public User(@NotBlank @Size(max = 20) String firstName, @NotBlank @Size(max = 20) String lastName, @NotBlank @Size(max = 15) String userName, @NotBlank @Size(max = 20) String accountType, @NotBlank @Email @Size(max = 40) String emailAddress, @NotBlank @Size(max = 40) String companyName, @NotBlank @Size(max = 20) String country, @NotBlank @Size(max = 15) String mobileNumber, @NotBlank @Size(max = 100) String password, short accountActivated) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.accountType = accountType;
        this.emailAddress = emailAddress;
        this.companyName = companyName;
        this.country = country;
        this.mobileNumber = mobileNumber;
        this.password = password;
        this.accountActivated = accountActivated;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public short getAccountActivated() {
        return accountActivated;
    }

    public void setAccountActivated(short accountActivated) {
        this.accountActivated = accountActivated;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
