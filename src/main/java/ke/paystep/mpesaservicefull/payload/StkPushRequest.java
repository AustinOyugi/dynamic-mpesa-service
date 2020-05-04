package ke.paystep.mpesaservicefull.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StkPushRequest
{
    @NotNull
    @JsonProperty("Amount")
    private String amount;

    @NotNull
    @JsonProperty("PhoneNumber")
    private String phoneNumber;

    @NotNull
    @JsonProperty("AccountName")
    private String accountName;

    @NotNull
    @JsonProperty("ConfirmationUrl")
    private String confirmationUrl;

    @NotNull
    @JsonProperty("TransactionDesc")
    private String transactionDesc;

}
