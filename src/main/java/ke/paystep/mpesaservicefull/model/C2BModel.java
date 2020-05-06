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
@NoArgsConstructor
@AllArgsConstructor
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

    @JsonProperty("TransID")
    private String transID;

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
}
