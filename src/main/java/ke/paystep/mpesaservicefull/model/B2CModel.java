package ke.paystep.mpesaservicefull.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ke.paystep.mpesaservicefull.model.audit.DateAudit;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "B2C", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "ConversationID"
        }),
        @UniqueConstraint(columnNames = {
                "OriginatorConversationID"
        })
})
public class B2CModel extends DateAudit
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long requestId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id", nullable = false)
    @JsonIgnore
    private Users user;

    @NotNull
    private short transactionComplete;

    @NotNull
    private String commandID;

    @NotNull
    private String amount;

    @NotNull
    @Size(max = 13)
    private String phoneNumber;

    @NotNull
    private String remarks;

    @NotNull
    private String ConversationID;

    @NotNull
    private String OriginatorConversationID;

    public String getConversationID() {
        return ConversationID;
    }

    public void setConversationID(String conversationID) {
        ConversationID = conversationID;
    }

    public String getOriginatorConversationID() {
        return OriginatorConversationID;
    }

    public void setOriginatorConversationID(String originatorConversationID) {
        OriginatorConversationID = originatorConversationID;
    }

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public short getTransactionComplete() {
        return transactionComplete;
    }

    public void setTransactionComplete(short transactionComplete) {
        this.transactionComplete = transactionComplete;
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

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
