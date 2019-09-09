package ke.paystep.mpesaservicefull.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;
import ke.paystep.mpesaservicefull.exception.ResourceNotFoundException;
import ke.paystep.mpesaservicefull.helpers.UrlValidator;
import ke.paystep.mpesaservicefull.model.C2BModel;
import ke.paystep.mpesaservicefull.model.StkPushModel;
import ke.paystep.mpesaservicefull.model.Users;
import ke.paystep.mpesaservicefull.payload.ApiResponse;
import ke.paystep.mpesaservicefull.payload.C2BRequest;
import ke.paystep.mpesaservicefull.payload.StkPushQueryRequest;
import ke.paystep.mpesaservicefull.payload.StkPushRequest;
import ke.paystep.mpesaservicefull.repository.C2BRespository;
import ke.paystep.mpesaservicefull.repository.StkPushRepository;
import ke.paystep.mpesaservicefull.repository.UserRepository;
import ke.paystep.mpesaservicefull.security.CurrentUser;
import ke.paystep.mpesaservicefull.security.UserPrincipal;
import ke.paystep.mpesaservicefull.service.C2BTransaction;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Map;

/**
 * Created by Austin Oyugi on 24/8/2019.
 */

@RestController
@RequestMapping("/v1/payment/mpesa/c2b")
public class C2BController
{
    private static final Logger LOGGER = LoggerFactory.getLogger(C2BController.class);

    @Value("${api-settings.url}")
    private  String api_url;

    @Value("${mpesa.business_short_code}")
    private String business_short_code;

    private final StkPushRepository stkPushRepository;
    private final C2BRespository c2BRespository;
    private final C2BTransaction c2BTransaction;
    private final UserRepository userRepository;
    private final RestTemplate restTemplate;

    @Autowired
    public C2BController(C2BTransaction c2BTransaction, C2BRespository c2BRespository, UserRepository userRepository, StkPushRepository stkPushRepository, RestTemplate restTemplate) {
        this.c2BTransaction = c2BTransaction;
        this.c2BRespository = c2BRespository;
        this.userRepository = userRepository;
        this.stkPushRepository = stkPushRepository;
        this.restTemplate = restTemplate;
    }

    @GetMapping("/register")
    @ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(code = 401, message = "You are not authorized to access this resource"),
            @io.swagger.annotations.ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error")
    })
    public ResponseEntity<?> registerUrl()
    {
        JSONObject jsonObject;
        String responseType = "Completed";
        String confirmationUrl = api_url+"/v1/payment/m/c2b/url/confirmation";
        String validationUrl = api_url+"/v1/payment/m/c2b/url/validation";

        try {
            jsonObject = c2BTransaction.url_registration(business_short_code, responseType, confirmationUrl, validationUrl);
        } catch (IOException e) {
            LOGGER.error("URL registration Failed {}", e.getLocalizedMessage());
            return ResponseEntity.status(500).body(new ApiResponse(false,"Internal Server Error"));
        }

        return new ResponseEntity<>( jsonObject.toMap(), HttpStatus.OK);
    }

    @ApiOperation(value = "Use this API to initiate online payment on behalf of a customer.")
    @ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(code = 200, message = "Successfully initiated the transaction"),
            @io.swagger.annotations.ApiResponse(code = 401, message = "You are not authorized to access this resource"),
            @io.swagger.annotations.ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @io.swagger.annotations.ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error"),
            @io.swagger.annotations.ApiResponse(code = 400, message = "Bad Request")
    })
    @PostMapping("/stkpush/initiate")
    public ResponseEntity<?> stkPushInitiate(@CurrentUser UserPrincipal userPrincipal,
                                             @Valid @RequestBody StkPushRequest stkPushRequest)
    {
        if (!UrlValidator.urlValidator(stkPushRequest.getConfirmationUrl()))
        {
            return ResponseEntity.status(400).body(new ApiResponse(false,"Enter a Valid URL"));
        }

        JSONObject jsonObject;

        try {
            jsonObject = c2BTransaction.payment(stkPushRequest.getAmount(),stkPushRequest.getPhoneNumber(),
                    stkPushRequest.getAccountName(),stkPushRequest.getTransactionDesc());
            
        } catch (IOException e) {
            LOGGER.error("Transaction Failed {}", e.getLocalizedMessage());
            return ResponseEntity.status(500).body(new ApiResponse(false,"Internal Server Error"));
        }

        assert jsonObject != null;
        int responseCode = Integer.parseInt(jsonObject.getString("ResponseCode"));
        
        LOGGER.info(jsonObject.toString());
        
        if (responseCode != 0)
        {
            return ResponseEntity.status(500).body(new ApiResponse(false,"An Error Occurred Try Again"));
        }

        StkPushModel stkPushModel = new StkPushModel();
        assert userPrincipal != null;
        stkPushModel.setUser(userRepository.findById(userPrincipal.getId()).orElse(null));
        stkPushModel.setAmount(stkPushRequest.getAmount());
        stkPushModel.setCheckoutRequestID(jsonObject.getString("CheckoutRequestID"));
        stkPushModel.setMerchantRequestID(jsonObject.getString("MerchantRequestID"));
        stkPushModel.setPhoneNumber(stkPushRequest.getPhoneNumber());
        stkPushModel.setTransactionComplete((short) 0);
        stkPushModel.setConfirmationUrl(stkPushRequest.getConfirmationUrl());
        stkPushRepository.save(stkPushModel);

        return ResponseEntity.status(200).body(new ApiResponse(true, "Transaction Initiated Successfully -> "));
    }

    @PostMapping("/stkpush/result")
    @ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(code = 200, message = "Successfully Completed the transaction"),
            @io.swagger.annotations.ApiResponse(code = 401, message = "You are not authorized to access this resource"),
            @io.swagger.annotations.ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @io.swagger.annotations.ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error")
    })
    public void stkPushResult(@RequestBody String jsonString)
    {
        JSONObject jsonObject = new JSONObject(jsonString);
        JSONObject jsonChildObject = (JSONObject) jsonObject.get("Body");
        JSONObject jsonFirstChild = (JSONObject) jsonObject.get("stkCallback");

        int resultCode = Integer.parseInt(jsonFirstChild.getString("ResultCode"));
        String checkoutRequestID = jsonChildObject.getString("CheckoutRequestID");

        StkPushModel  stkPushModel = stkPushRepository.getByCheckoutRequestID(checkoutRequestID).orElse(null);
        assert stkPushModel != null;
        String url = stkPushModel.getConfirmationUrl();

        if (resultCode == 0)
        {
            stkPushModel.setTransactionComplete((short) 1);
            stkPushRepository.save(stkPushModel);
        }

        restTemplate.postForEntity(url,jsonString,String.class);
    }

    @ApiOperation("Use this API to check the status of a Lipa Na M-Pesa Online Payment.")
    @ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(code = 200, message = "Successfully initiated the query"),
            @io.swagger.annotations.ApiResponse(code = 401, message = "You are not authorized to access this resource"),
            @io.swagger.annotations.ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @io.swagger.annotations.ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error")
    })
    @PostMapping("/stkpush/query")
    public ResponseEntity<?> queryPaymentStatus(@Valid @RequestBody StkPushQueryRequest stkPushQueryRequest)
    {
        if (stkPushRepository.getByCheckoutRequestID(stkPushQueryRequest.getCheckoutRequestID()).isPresent())
        {
            return  ResponseEntity.status(404).body(new ResourceNotFoundException("CheckoutRequest", "Id", stkPushQueryRequest.getCheckoutRequestID()));
        }

        JSONObject jsonObject;
        try {
            jsonObject = c2BTransaction.queryPaymentStatus(stkPushQueryRequest.getCheckoutRequestID());
        } catch (IOException e) {
            LOGGER.error("Query Failed {}", e.getLocalizedMessage());
            return ResponseEntity.status(500).body(new ApiResponse(false,"Internal Server Error"));
        }

        assert jsonObject != null;
        int responseCode = Integer.parseInt(jsonObject.getString("ResponseCode"));
        if (responseCode != 0)
        {
            return ResponseEntity.status(500).body(new ApiResponse(false,"An Error Occurred Try Again"));
        }

        return ResponseEntity.status(200).body(jsonObject.toMap());
    }

    @ApiOperation(value = "This API is used to make payment requests from Client to Business (C2B)." +
            " You can use the sandbox provided test credentials down below to simulates a payment made from the client phone's STK/SIM Toolkit menu, " +
            "and enables you to receive the payment requests in real time. ")
    @ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(code = 200, message = "Successfully initiated the Transaction"),
            @io.swagger.annotations.ApiResponse(code = 401, message = "You are not authorized to access this resource"),
            @io.swagger.annotations.ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @io.swagger.annotations.ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error")
    })
    @PostMapping("/initiate")
    public ResponseEntity<?> initiate(@CurrentUser UserPrincipal currentUser, @Valid @RequestBody C2BRequest c2BRequest)
    {
        Users user = userRepository.findById(currentUser.getId()).orElse(null);
        if (user == null)
        {
            return ResponseEntity.status(404).body(new ResourceNotFoundException("User", "Id", currentUser.getId()));
        }

        JSONObject jsonObject;

        try {
            jsonObject = c2BTransaction.simulation(c2BRequest.getLipaNaMpesaShortcode(), String.valueOf(c2BRequest.getCommandID()),
                    c2BRequest.getAmount(), c2BRequest.getPhoneNumber());
        } catch (IOException e) {
            LOGGER.error("Transaction Failed {}", e.getLocalizedMessage());
            return ResponseEntity.status(500).body(new ApiResponse(false,"Internal Server Error"));
        }

        assert jsonObject != null;
        String responseDescription = jsonObject.getString("ResponseDescription");

        if (responseDescription.equals("The service request has failed"))
        {
            return ResponseEntity.status(500).body(new ApiResponse(false,"An Error Occurred Try Again"));
        }

        C2BModel c2BModel = new C2BModel();
        c2BModel.setAmount(c2BRequest.getAmount());
        c2BModel.setCommandID(String.valueOf(c2BRequest.getCommandID()));
        c2BModel.setConversationID(jsonObject.getString("ConversationId"));
        c2BModel.setLipaNaMpesaShortcode(c2BRequest.getLipaNaMpesaShortcode());
        c2BModel.setOriginatorCoversationID(jsonObject.getString("OriginatorConversationId"));
        c2BModel.setPhoneNumber(c2BRequest.getPhoneNumber());
        c2BModel.setTransactionComplete((short) 0);
        c2BModel.setUser(user);

        c2BRespository.save(c2BModel);
        return ResponseEntity.status(200).body(new ApiResponse(true,"Transaction Initiated Successfully"));
    }

    @PostMapping(value = "/url/validation")
    public ResponseEntity<?> validationRequest(@RequestBody String json) throws IOException {
        Map<String, Object> jsonToMap = new ObjectMapper().readValue(json, Map.class);
        String transAmount = (String) jsonToMap.get("TransAmount");

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ResultCode", 0);
        jsonObject.put("ResultDesc","Accepted");

        //Get back to this
        return ResponseEntity.status(200).body(jsonObject.toMap());
    }

    @PostMapping(value = "/url/confirmation")
    public void confirmation()
    {

    }
}