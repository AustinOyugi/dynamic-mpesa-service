package ke.paystep.mpesaservicefull.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;
import ke.paystep.mpesaservicefull.exception.ResourceNotFoundException;
import ke.paystep.mpesaservicefull.model.B2CModel;
import ke.paystep.mpesaservicefull.model.Users;
import ke.paystep.mpesaservicefull.payload.*;
import ke.paystep.mpesaservicefull.repository.B2CRepository;
import ke.paystep.mpesaservicefull.repository.UserRepository;
import ke.paystep.mpesaservicefull.security.CurrentUser;
import ke.paystep.mpesaservicefull.security.UserPrincipal;
import ke.paystep.mpesaservicefull.service.B2CTransaction;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

/**
 * Created by Austin Oyugi on 26/8/2019.
 */

@RestController
@RequestMapping("/v1/payment/mpesa/b2c")
public class B2CController
{
    private static final Logger LOGGER = LoggerFactory.getLogger(B2CController.class);

    private final UserRepository userRepository;

    private final B2CTransaction b2CTransaction;

    private final B2CRepository b2CRepository;

    @Autowired
    public B2CController(UserRepository userRepository, B2CTransaction b2CTransaction, B2CRepository b2CRepository) {
        this.userRepository = userRepository;
        this.b2CTransaction = b2CTransaction;
        this.b2CRepository = b2CRepository;
    }

    @ApiOperation(value = "Business to Consumer (B2C) API enables a Business or Organization to pay their customers")
    @ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(code = 200, message = "Successfully initiated the Transaction"),
            @io.swagger.annotations.ApiResponse(code = 401, message = "You are not authorized to access this resource"),
            @io.swagger.annotations.ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @io.swagger.annotations.ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error")
    })
    @PostMapping("/initiate")
    public ResponseEntity<?> initiate(@CurrentUser UserPrincipal currentUser, @Valid @RequestBody B2CRequest b2cRequest)
    {
        Users user = userRepository.findById(Long.parseLong(b2cRequest.getTransactionId())).orElse(null);
        if (user == null)
        {
            return ResponseEntity.status(404).body(new ResourceNotFoundException("User", "Id", currentUser.getId()));
        }

        JSONObject jsonObject;

        try {
            jsonObject = b2CTransaction.request(String.valueOf(b2cRequest.getCommandID()), b2cRequest.getAmount(),
                    b2cRequest.getPhoneNumber(),b2cRequest.getRemarks());

            LOGGER.info("xxx"+jsonObject.toString());
        } catch (IOException e) {
            LOGGER.error("Transaction Failed {}", e.getLocalizedMessage());
            return ResponseEntity.status(500).body(new ApiResponse(false,"Internal Server Error"));
        }

        try{
            int responseCode = Integer.parseInt(jsonObject.getString("ResponseCode"));
            LOGGER.info("Response Code -> " + responseCode);

            if (responseCode != 0)
            {
                return ResponseEntity.status(500).body(new ApiResponse(false,"An Error Occurred Try Again"));
            }

        }catch (Exception e){
            B2CUnsuccessfulResponse b2CUnsuccessfulResponse = null;
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.disable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES);
                b2CUnsuccessfulResponse = (B2CUnsuccessfulResponse) objectMapper.readValue(jsonObject.toString(), B2CUnsuccessfulResponse.class);
            } catch (IOException exception) {
                exception.printStackTrace();
            }
            assert b2CUnsuccessfulResponse != null;
            return ResponseEntity.ok(b2CUnsuccessfulResponse);
        }

        B2CModel b2CModel = new B2CModel();
        b2CModel.setAmount(b2cRequest.getAmount());
        b2CModel.setCommandID(String.valueOf(b2cRequest.getCommandID()));
        b2CModel.setPhoneNumber(b2cRequest.getPhoneNumber());
        b2CModel.setRemarks(b2cRequest.getRemarks());
        b2CModel.setTransactionComplete((short) 0);
        b2CModel.setConversationID(jsonObject.getString("ConversationID"));
        b2CModel.setOriginatorConversationID(jsonObject.getString("OriginatorConversationID"));
        b2CModel.setUser(user);
        b2CModel.setTransactionResponse("Initiated");
        b2CModel.setCreatedAt(new Date().toInstant());

        b2CRepository.save(b2CModel);
        return ResponseEntity.status(200).body(jsonObject.toMap());
    }

    @PostMapping("/result")
    public String result(@RequestBody B2CSuccessResponse b2CSuccessResponse)
    {
        try {
            LOGGER.info(" In result"+new ObjectMapper().writeValueAsString(b2CSuccessResponse));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        String conversationID = b2CSuccessResponse.getResult().getConversationID();
        B2CModel b2CModel = b2CRepository.findByConversationID(conversationID).orElse(null);
        assert b2CModel != null;

        if (!b2CSuccessResponse.getResult().getResultCode().equals("0")){
            try {
                assert b2CModel != null;
                b2CModel.setTransactionResponse(new ObjectMapper().writeValueAsString(b2CSuccessResponse));
                b2CRepository.save(b2CModel);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }else {
            assert b2CModel != null;
            b2CModel.setTransactionComplete((short) 1);
            b2CModel.setTransactionResponse("Transaction Complete");
            b2CRepository.save(b2CModel);
        }

        return "body";
    }

    @PostMapping("/timeout")
    public void timeout()
    {

    }
}