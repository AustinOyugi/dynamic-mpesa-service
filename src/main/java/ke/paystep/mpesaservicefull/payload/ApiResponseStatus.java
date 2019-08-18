package ke.paystep.mpesaservicefull.payload;

/**
 * Created by Austin Oyugi on 19/8/2019.
 */
public enum ApiResponseStatus {

    SUCCESS("00"),
    ERROR("01");

    private final String responseStatus;

    private ApiResponseStatus(String responseStatus) {
        this.responseStatus = responseStatus;
    }

    public String getResponseStatus() {
        return responseStatus;
    }

}
