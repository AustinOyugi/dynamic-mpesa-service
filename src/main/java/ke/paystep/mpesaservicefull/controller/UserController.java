package ke.paystep.mpesaservicefull.controller;


import ke.paystep.mpesaservicefull.exception.ResourceNotFoundException;
import ke.paystep.mpesaservicefull.model.Users;
import ke.paystep.mpesaservicefull.payload.UserIdentityAvailability;
import ke.paystep.mpesaservicefull.payload.UserProfile;
import ke.paystep.mpesaservicefull.payload.UserSummary;
import ke.paystep.mpesaservicefull.repository.B2CRepository;
import ke.paystep.mpesaservicefull.repository.C2BRespository;
import ke.paystep.mpesaservicefull.repository.StkPushRepository;
import ke.paystep.mpesaservicefull.repository.UserRepository;
import ke.paystep.mpesaservicefull.security.CurrentUser;
import ke.paystep.mpesaservicefull.security.UserPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Austin Oyugi on 17/8/2019.
 */

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserRepository userRepository;

    private final B2CRepository b2CRepository;

    private final C2BRespository c2BRespository;

    private final StkPushRepository stkPushRepository;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    public UserController(UserRepository userRepository, B2CRepository b2CRepository, C2BRespository c2BRespository, StkPushRepository stkPushRepository) {
        this.userRepository = userRepository;
        this.b2CRepository = b2CRepository;
        this.c2BRespository = c2BRespository;
        this.stkPushRepository = stkPushRepository;
    }

    @GetMapping("/user/me")
    @PreAuthorize("hasRole('USER')")
    public UserSummary getCurrentUser(@CurrentUser UserPrincipal currentUser) {
        return new UserSummary(currentUser.getId(), currentUser.getUsername(), currentUser.getUsername());
    }

    @GetMapping("/user/checkUsernameAvailability")
    public UserIdentityAvailability checkUsernameAvailability(@RequestParam(value = "username") String username) {
        Boolean isAvailable = !userRepository.existsByUserName(username);
        return new UserIdentityAvailability(isAvailable);
    }

    @GetMapping("/user/checkEmailAvailability")
    public UserIdentityAvailability checkEmailAvailability(@RequestParam(value = "email") String email) {
        Boolean isAvailable = !userRepository.existsByEmailAddress(email);
        return new UserIdentityAvailability(isAvailable);
    }

    @GetMapping("/users/{username}")
    public UserProfile getUserProfile(@PathVariable(value = "username") String username) {
        Users user = userRepository.findByUserName(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        long numOfTransactions = b2CRepository.countByUserId(user.getId()) + c2BRespository.countByUserId(user.getId())+
                stkPushRepository.countByUserId(user.getId());

        return new UserProfile(user.getId(), user.getUserName(), user.getEmailAddress(),
                user.getCreatedAt(), numOfTransactions);
    }
}
