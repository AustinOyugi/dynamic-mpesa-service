package ke.paystep.mpesaservicefull.payload;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({
        "ResultParameter"
})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResultParameters {

    @JsonProperty("ResultParameter")
    @JsonIgnore
    private List<ResultParameter> resultParameter;
}