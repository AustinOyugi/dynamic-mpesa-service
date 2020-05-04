package ke.paystep.mpesaservicefull.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.text.DecimalFormat;

@Getter
@Setter
@NoArgsConstructor
@JsonPropertyOrder({
        "Key",
        "Value"
})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResultParameter {

    @JsonProperty("Key")
    private String key;

    @JsonProperty("Value1")
    private Double doubleValue;
    DecimalFormat decimalFormat = new DecimalFormat("#.##");

    @JsonProperty("Value2")
    private String stringValue;

    public ResultParameter(String key, Double doubleValue){
        this.key = key;
        this.doubleValue = Double.parseDouble(decimalFormat.format(doubleValue));
    }

    public ResultParameter(String key, String stringValue){
        this.key = key;
        this.stringValue = stringValue;
    }


}
