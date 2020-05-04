package ke.paystep.mpesaservicefull.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class STKPushCallback {

    @JsonProperty("stkCallback")
    private Result result;
}
