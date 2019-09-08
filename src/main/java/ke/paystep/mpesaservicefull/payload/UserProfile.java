package ke.paystep.mpesaservicefull.payload;

import java.time.Instant;

public class UserProfile {
    private Long id;
    private String username;
    private String email;
    private Instant joinedAt;
    private Long numOfTransactions;

    public UserProfile(Long id, String username, String email, Instant joinedAt, Long numOfTransactions) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.joinedAt = joinedAt;
        this.numOfTransactions = numOfTransactions;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Instant getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(Instant joinedAt) {
        this.joinedAt = joinedAt;
    }

    public Long getNumOfTransactions() {
        return numOfTransactions;
    }

    public void setNumOfTransactions(Long numOfTransactions) {
        this.numOfTransactions = numOfTransactions;
    }
}
