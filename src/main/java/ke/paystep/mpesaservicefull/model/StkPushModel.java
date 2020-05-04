package ke.paystep.mpesaservicefull.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ke.paystep.mpesaservicefull.model.audit.DateAudit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "STK_Push", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "CheckoutRequestID"
        })
})
public class StkPushModel extends DateAudit
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String merchantRequestID;

    @NotNull
    private String checkoutRequestID;

    @NotNull
    private String amount;

    @NotNull
    private String phoneNumber;

    @NotNull
    private String confirmationUrl;

    @NotNull
    private short transactionComplete;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private Users user;
}
