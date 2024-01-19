package za.co.digitalcowboy.profile.master.service.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import za.co.digitalcowboy.profile.master.service.domain.Payment;
import za.co.digitalcowboy.profile.master.service.domain.ProfileRequest;
import za.co.digitalcowboy.profile.master.service.service.ProfileService;

@org.springframework.web.bind.annotation.RestController
@Slf4j
public class RestController {

    @Autowired
    ProfileService profileService;

    @RequestMapping(path = "/payment", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Payment> getHealth() {

        Payment payment = Payment.builder()
                .status("Authorized")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(payment);
    }


    @RequestMapping(path = "/profile", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createOrUpdateProfile(@RequestBody ProfileRequest profileRequest) {

        profileService.saveProfileEvent(profileRequest);
        return ResponseEntity.status(HttpStatus.OK).body("OK");
    }
}
