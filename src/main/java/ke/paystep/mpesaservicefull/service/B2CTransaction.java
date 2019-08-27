package ke.paystep.mpesaservicefull.service;

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
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Austin Oyugi on 26/8/2019.
 */

@Service
public class B2CTransaction
{
    private final MpesaAuthentication mpesaAuthentication;

    @Value("${app.mode}")
    private String appMode;

    @Value("${mpesa.b2cInitiatorName}")
    private String b2cInitiatorName;

    @Value("${mpesa.b2c_shortcode}")
    private String b2c_shortcode;

    @Value("${mpesa.pass_key}")
    private String pass_key;

    @Value("${api-settings.url}")
    private String api_url;

    private String queueTimeOutURL = api_url + "/v1/payment/mpesa/b2c/timeout";
    private String resultURL = api_url +"/v1/payment/mpesa/b2c/result";

    @Autowired
    public B2CTransaction(MpesaAuthentication mpesaAuthentication)
    {
        this.mpesaAuthentication = mpesaAuthentication;
    }

    public JSONObject request(String commandID, String amount, String phoneNumber, String comments) throws IOException
    {
        String url;

        if (appMode.equals("development"))
        {
            url = "https://sandbox.safaricom.co.ke/mpesa/b2c/v1/paymentrequest";
        }else{
            url = "https://live.safaricom.co.ke/mpesa/b2c/v1/paymentrequest";
        }

        String timestamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());

        //Some hardcoded variables
        String password = b2c_shortcode + pass_key + timestamp;
        String encodedPassword = Base64.getEncoder().encodeToString(password.getBytes(StandardCharsets.UTF_8));

        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("InitiatorName", b2cInitiatorName);
        jsonObject.put("SecurityCredential", encodedPassword);
        jsonObject.put("CommandID", commandID); /*SalaryPayment or BusinessPayment or PromotionPayment*/
        jsonObject.put("Amount", amount);
        jsonObject.put("PartyA", b2c_shortcode);
        jsonObject.put("PartyB", phoneNumber);
        jsonObject.put("Remarks", comments);
        jsonObject.put("QueueTimeOutURL", queueTimeOutURL);
        jsonObject.put("ResultURL", resultURL);
        jsonObject.put("Occassion", "");
        jsonArray.put(jsonObject);

        String requestJson = jsonArray.toString().replaceAll("[\\[\\]]", "");

        return mpesaAuthentication.connect(requestJson, url);
    }

    public Map<Integer, String> requestSuccess(String jsonResult)
    {
        Map<Integer, String> result = new HashMap<>();

        JSONObject jsonObject = new JSONObject(jsonResult);
        JSONObject jsonChildObject = (JSONObject) jsonObject.get("Result");

        int resultCode = Integer.parseInt(jsonChildObject.getString("ResultCode"));
        String resultDesc = jsonChildObject.getString("ResultDesc");

        if (resultCode != 0){
            result.put(500, resultDesc);
        }else
        {
            result.put(200,resultDesc);
        }

        return result;
    }
}
