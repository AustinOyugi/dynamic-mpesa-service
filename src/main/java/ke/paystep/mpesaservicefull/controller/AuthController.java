package ke.paystep.mpesaservicefull.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponses;
import ke.paystep.mpesaservicefull.exception.AppException;
import ke.paystep.mpesaservicefull.model.Role;
import ke.paystep.mpesaservicefull.model.RoleName;
import ke.paystep.mpesaservicefull.model.Users;
import ke.paystep.mpesaservicefull.payload.*;
import ke.paystep.mpesaservicefull.repository.RoleRepository;
import ke.paystep.mpesaservicefull.repository.UserRepository;
import ke.paystep.mpesaservicefull.security.JwtTokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Collections;

/**
 * Created by Austin Oyugi on 19/8/2019.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController
{
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @ApiOperation(value = "Used to generate jwt token used to enable access  to other resources ")
    @ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(code = 200, message = "Successfully Generated the request"),
            @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error"),
            @io.swagger.annotations.ApiResponse(code = 400, message = "Check your details, the server failed to process your request"),
            @io.swagger.annotations.ApiResponse(code = 0, message = "Timed Out, try again")
    })
    @PostMapping("/getToken")
    public ResponseEntity<?> getAccessToken(@ApiParam(value = "Authentication Object") @Valid @RequestBody AuthenticationRequest authRequest)
    {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getUsernameOrEmail(),
                        authRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtTokenProvider.generateToken(authentication);
        return ResponseEntity.status(200).body(new JwtAuthenticationResponse(jwt));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest)
    {
        if (userRepository.existsByUserName(signUpRequest.getUserName()))
        {
            return new ResponseEntity<>(new ApiResponse(false, "Username is already taken!"),
                    HttpStatus.BAD_REQUEST);
        }

        if (userRepository.existsByEmailAddress(signUpRequest.getEmailAddress()))
        {
            return new ResponseEntity<>(new ApiResponse(false,"Email Address already in use!"),
                    HttpStatus.BAD_REQUEST);
        }

        Users user = new Users(signUpRequest.getFirstName(), signUpRequest.getLastName(),
                signUpRequest.getUserName(),signUpRequest.getAccountType(), signUpRequest.getEmailAddress(),
                signUpRequest.getCompanyName(),signUpRequest.getCountry(),signUpRequest.getMobileNumber(),
                signUpRequest.getPassword(), (short) 1);

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Role userRole = roleRepository.findByName(RoleName.ROLE_USER).orElse(null);
        
        if(userRole == null)
        {
            Role role = new Role();
            role.setName(RoleName.ROLE_USER);
        
            roleRepository.save(role);
        }
        
//        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
//                .orElseThrow(() -> new AppException("User Role not set"));

        user.setRoles(Collections.singleton(userRole));

        Users result = userRepository.save(user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/users/{username}")
                .buildAndExpand(result.getUserName()).toUri();

        return ResponseEntity.created(location).body(new ApiResponse(true,"User registered successfully"));
    }
}
