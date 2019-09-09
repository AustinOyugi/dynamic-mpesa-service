package ke.paystep.mpesaservicefull.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ke.paystep.mpesaservicefull.model.audit.DateAudit;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "C2B", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "ConversationID"
        }),
        @UniqueConstraint(columnNames = {
                "OriginatorCoversationID"
        })
})
public class C2BModel extends DateAudit
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long requestId;

    @NotNull
    private String lipaNaMpesaShortcode;

    private String  commandID;

    @NotNull
    private String amount;

    @NotNull
    @Size(max = 15)
    private String phoneNumber;

    @NotNull
    private String ConversationID;

    @NotNull
    private String OriginatorCoversationID;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id", nullable = false)
    @JsonIgnore
    private Users user;

    private short transactionComplete;

    public C2BModel() {
    }

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public String getLipaNaMpesaShortcode() {
        return lipaNaMpesaShortcode;
    }

    public void setLipaNaMpesaShortcode(String lipaNaMpesaShortcode) {
        this.lipaNaMpesaShortcode = lipaNaMpesaShortcode;
    }

    public String getCommandID() {
        return commandID;
    }

    public void setCommandID(String commandID) {
        this.commandID = commandID;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getConversationID() {
        return ConversationID;
    }

    public void setConversationID(String conversationID) {
        ConversationID = conversationID;
    }

    public String getOriginatorCoversationID() {
        return OriginatorCoversationID;
    }

    public void setOriginatorCoversationID(String originatorCoversationID) {
        OriginatorCoversationID = originatorCoversationID;
    }

    public short getTransactionComplete() {
        return transactionComplete;
    }

    public void setTransactionComplete(short transactionComplete) {
        this.transactionComplete = transactionComplete;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }
}
