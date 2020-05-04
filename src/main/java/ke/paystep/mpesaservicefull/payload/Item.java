package ke.paystep.mpesaservicefull.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({
        "Name",
        "Value"
})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Item {

    @JsonProperty("Name")
    private String name;

    @JsonProperty("Value")
    private Object value;

}
