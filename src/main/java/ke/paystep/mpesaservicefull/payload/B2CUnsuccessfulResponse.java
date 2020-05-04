package ke.paystep.mpesaservicefull.payload;

import com.fasterxml.jackson.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({
        "Result"
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class B2CUnsuccessfulResponse {

    @JsonIgnore
    @JsonProperty("Result")
    public Result result;
}