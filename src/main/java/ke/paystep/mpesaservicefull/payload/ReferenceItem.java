package ke.paystep.mpesaservicefull.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonPropertyOrder({
        "Key",
        "Value"
})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReferenceItem {

    @JsonProperty("Key")
    private String key;
    @JsonProperty("Value")
    private String value;
}
