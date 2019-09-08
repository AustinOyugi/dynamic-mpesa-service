package ke.paystep.mpesaservicefull.service;

import com.fasterxml.jackson.annotation.JsonAlias;
import org.apache.commons.lang3.RandomStringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

/**
 * Created by Austin Oyugi on 24/8/2019.
 */

@Service
public class C2BTransaction {

    @Value("${app.mode}")
    private String appMode;

    @Value("${mpesa.business_short_code}")
    private String business_short_code;

    @Value("${mpesa.pass_key}")
    private String pass_key;

    @Value("${api-settings.url}")
    private String api_url;


    private final MpesaAuthentication mpesaAuthentication;

    @Autowired
    public C2BTransaction(MpesaAuthentication mpesaAuthentication) {
        this.mpesaAuthentication = mpesaAuthentication;
    }

    public JSONObject simulation(String lipaNaMpesaShortcode, String commandID, String amount, String phoneNumber) throws IOException {

        String url;

        if (appMode.equals("development"))
        {
            url = "https://sandbox.safaricom.co.ke/mpesa/c2b/v1/simulate";
        }else{
            url = "https://live.safaricom.co.ke/mpesa/c2b/v1/simulate";
        }

        String billRefNumber = RandomStringUtils.randomAlphabetic(16);

        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ShortCode", lipaNaMpesaShortcode);
        jsonObject.put("CommandID", commandID); /*CustomerPayBillOnline or CustomerBuyGoodsOnline*/
        jsonObject.put("Amount", amount);
        jsonObject.put("Msisdn", phoneNumber);
        jsonObject.put("BillRefNumber", billRefNumber);

        jsonArray.put(jsonObject);

        String requestJson = jsonArray.toString().replaceAll("[\\[\\]]", "");
        System.out.println(requestJson);

        return mpesaAuthentication.connect(requestJson, url);
    }

    public JSONObject url_registration(String lipaNaMpesaShortcode, String responseType,
                                      String confirmationURL, String validationURL)
            throws IOException {

        String url;

        if (appMode.equals("development"))
        {
            url = "https://sandbox.safaricom.co.ke/mpesa/c2b/v1/registerurl";
        }else{
            url = "https://live.safaricom.co.ke/mpesa/c2b/v1/registerurl";;
        }

        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ShortCode", lipaNaMpesaShortcode);
        jsonObject.put("ResponseType", "Completed");
        jsonObject.put("ConfirmationURL", confirmationURL);
        jsonObject.put("ValidationURL", validationURL);
        jsonArray.put(jsonObject);


        String requestJson = jsonArray.toString().replaceAll("[\\[\\]]", "");
        System.out.println(requestJson);

        return mpesaAuthentication.connect(requestJson, url);
    }

    public JSONObject queryPaymentStatus(String checkoutRequestID)
            throws IOException {

        String url;

        if (appMode.equals("development"))
        {
            url = "https://sandbox.safaricom.co.ke/mpesa/stkpushquery/v1/query";
        }else{
            url = "https://live.safaricom.co.ke/mpesa/stkpushquery/v1/query";
        }

        String timeStamp = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
        String password = business_short_code + pass_key + timeStamp;
        String encodedPassword = Base64.getEncoder().encodeToString(password.getBytes(StandardCharsets.UTF_8));

        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("BusinessShortCode", business_short_code);
        jsonObject.put("Password", encodedPassword);
        jsonObject.put("Timestamp", timeStamp);
        jsonObject.put("CheckoutRequestID", checkoutRequestID);
        jsonArray.put(jsonObject);

        String requestJson = jsonArray.toString().replaceAll("[\\[\\]]", "");

        return mpesaAuthentication.connect(requestJson, url);

    }

    public JSONObject payment(String amount, String phoneNumber, String accountName, String transactionDesc) throws IOException {

        String url;

        if (appMode.equals("development"))
        {
            url = "https://sandbox.safaricom.co.ke/mpesa/stkpush/v1/processrequest";
        }else{
            url = "https://live.safaricom.co.ke/mpesa/stkpush/v1/processrequest";
        }

        String callBackUrl = api_url + "/v1/payment/mpesa/c2b/stkpush/result";
        String queueTimeOutURL = api_url + "/v1/payment/mpesa/c2b/stkpush/timeout";

        String timeStamp = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
        String password = business_short_code + pass_key + timeStamp;
        String encodedPassword = Base64.getEncoder().encodeToString(password.getBytes("UTF-8"));

        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("BusinessShortCode", business_short_code);
        jsonObject.put("Password", encodedPassword);
        jsonObject.put("Timestamp", timeStamp);
        jsonObject.put("TransactionType", "CustomerPayBillOnQueueTimeOutURLlineQueueTimeOutURL");
        jsonObject.put("Amount", amount);
        jsonObject.put("PhoneNumber", phoneNumber);
        jsonObject.put("PartyA", phoneNumber);
        jsonObject.put("PartyB", business_short_code);
        jsonObject.put("CallBackURL", callBackUrl);
        jsonObject.put("AccountReference", accountName); //Name of the company/owner of the paybill that will be
        // displayed to the client
        jsonObject.put("QueueTimeOutURL", queueTimeOutURL);
        jsonObject.put("TransactionDesc", transactionDesc);
        jsonArray.put(jsonObject);

        String requestJson = jsonArray.toString().replaceAll("[\\[\\]]", "");

        return mpesaAuthentication.connect(requestJson, url);
    }

    public JSONObject validateTransaction(String json) throws IOException {
        Map<String, Object> jsonToMap = new ObjectMapper().readValue(json, Map.class);
        String amount = (String) jsonToMap.get("TransAmount");

        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();

        boolean accepted = false;
        //Do Validation Here

        if (accepted)
        {
            jsonObject.put("ResultCode", 0);
            jsonObject.put("ResultDesc","Accepted");
        }else
        {
            jsonObject.put("ResultCode", 1);
            jsonObject.put("ResultDesc","Rejected");
        }

        jsonArray.put(jsonObject);
        String requestJson = jsonArray.toString().replaceAll("[\\[\\]]", "");

        //Get back to this
        return jsonObject;
    }
}
