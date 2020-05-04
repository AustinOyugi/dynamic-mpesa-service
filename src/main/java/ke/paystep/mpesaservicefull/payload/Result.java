package ke.paystep.mpesaservicefull.payload;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Result {

    @JsonProperty("MerchantRequestID")
    private String merchantRequestID;

    @JsonProperty("CheckoutRequestID")
    private String checkoutRequestID;

    @JsonProperty("ResultType")
    private Integer resultType;

    @JsonProperty("ResultCode")
    private String resultCode;

    @JsonProperty("ResultDesc")
    private String resultDesc;

    @JsonProperty("OriginatorConversationID")
    private String originatorConversationID;

    @JsonProperty("ConversationID")
    private String conversationID;

    @JsonProperty("TransactionID")
    private String transactionID;

    @JsonProperty("ResultParameters")
    @JsonIgnore
    private ResultParameters resultParameters;

    @JsonProperty("ReferenceData")
    @JsonIgnore
    private ReferenceData referenceData;

    @JsonProperty("CallbackMetadata")
    @JsonIgnore
    private CallbackMetadata callbackMetadata;
}
