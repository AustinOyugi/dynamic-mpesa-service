package ke.paystep.mpesaservicefull;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class MpesaServiceFullApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(MpesaServiceFullApplication.class, args);
    }

}
