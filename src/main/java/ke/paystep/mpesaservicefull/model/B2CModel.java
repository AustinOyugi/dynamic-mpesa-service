package ke.paystep.mpesaservicefull.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import ke.paystep.mpesaservicefull.model.audit.DateAudit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "B2CTransactions")
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
    private String conversationID;

    @NotNull
    private String originatorConversationID;

    private String transactionResponse;
}
