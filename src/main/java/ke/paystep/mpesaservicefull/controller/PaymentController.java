package ke.paystep.mpesaservicefull.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Austin Oyugi on 24/8/2019.
 */

@RestController
@RequestMapping("/v1/pay")
public class PaymentController
{
    @PostMapping("/initiate")
    public void initiate()
    {

    }
}
