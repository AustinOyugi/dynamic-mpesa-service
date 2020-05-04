package ke.paystep.mpesaservicefull.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({
        "ReferenceItem"
})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReferenceData {

    @JsonProperty("ReferenceItem")
    private ReferenceItem referenceItem;

}