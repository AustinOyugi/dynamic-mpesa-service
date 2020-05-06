package ke.paystep.mpesaservicefull.payload;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by Austin Oyugi on 24/8/2019.
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class C2BRequest
{

    enum CommandId{
        CustomerPayBillOnline, CustomerBuyGoodsOnline
    }

    @NotNull
    @JsonProperty("LipaNaMpesaShortcode")
    private String lipaNaMpesaShortcode;

    @JsonProperty("CommandID")
    @Enumerated(EnumType.STRING)
    private CommandId commandID;

    @JsonProperty("Amount")
    @NotNull
    private String amount;

    @NotNull
    @Size(max = 15)
    @JsonProperty("PhoneNumber")
    private String phoneNumber;

}