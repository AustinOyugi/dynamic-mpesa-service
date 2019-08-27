package ke.paystep.mpesaservicefull.payload;

import javax.validation.constraints.NotNull;

public class StkPushQueryRequest
{
    @NotNull
    private String checkoutRequestID;

    public String getCheckoutRequestID() {
        return checkoutRequestID;
    }

    public void setCheckoutRequestID(String checkoutRequestID) {
        this.checkoutRequestID = checkoutRequestID;
    }
}