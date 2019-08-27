package ke.paystep.mpesaservicefull.payload;


import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by Austin Oyugi on 24/8/2019.
 */

public class C2BRequest
{

    enum CommandId{
        CustomerPayBillOnline, CustomerBuyGoodsOnline
    }

    @NotNull
    private String lipaNaMpesaShortcode;

    @Enumerated(EnumType.STRING)
    private CommandId commandID;

    @NotNull
    private String amount;

    @NotNull
    @Size(max = 15)
    private String phoneNumber;

    public String getLipaNaMpesaShortcode() {
        return lipaNaMpesaShortcode;
    }

    public void setLipaNaMpesaShortcode(String lipaNaMpesaShortcode) {
        this.lipaNaMpesaShortcode = lipaNaMpesaShortcode;
    }

    public CommandId getCommandID() {
        return commandID;
    }

    public void setCommandID(CommandId commandID) {
        this.commandID = commandID;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}