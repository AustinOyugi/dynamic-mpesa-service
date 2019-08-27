package ke.paystep.mpesaservicefull.payload;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by Austin Oyugi on 24/8/2019.
 */

public class B2CRequest
{
    enum CommandId{
        SalaryPayment, BusinessPayment , PromotionPayment
    }

    @NotNull
    @Enumerated(EnumType.STRING)
    private CommandId commandID;

    @NotNull
    private String amount;

    @NotNull
    @Size(max = 13)
    private String phoneNumber;

    @NotNull
    private String remarks;

    @NotNull
    private String transactionId;

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

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public CommandId getCommandID() {
        return commandID;
    }

    public void setCommandID(CommandId commandID) {
        this.commandID = commandID;
    }
}
