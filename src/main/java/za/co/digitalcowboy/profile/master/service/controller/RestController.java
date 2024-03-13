package za.co.digitalcowboy.profile.master.service.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.co.digitalcowboy.profile.master.service.domain.Payment;
import za.co.digitalcowboy.profile.master.service.domain.ProfileRequest;
import za.co.digitalcowboy.profile.master.service.domain.Version;
import za.co.digitalcowboy.profile.master.service.service.ProfileService;

import java.util.List;

@org.springframework.web.bind.annotation.RestController
@Slf4j
public class RestController {

    @Autowired
    ProfileService profileService;

    @RequestMapping(path = "/payment", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Payment> processPayment() {

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

    @RequestMapping(path = "/health", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Version> getHealth() {

        Version version = new Version();
        version.setService("Profile Master Service");
        version.setMessage("Healthy");
        version.setServiceVersion("1.0.0");

        return ResponseEntity.status(HttpStatus.OK).body(version);
    }

    @RequestMapping(path = "/profile/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProfileRequest> getProfile(@PathVariable String id) {
        return ResponseEntity.status(HttpStatus.OK).body(profileService.getProfile(id));
    }

    @RequestMapping(path = "/profiles", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ProfileRequest>> getAllProfiles() {
        return ResponseEntity.status(HttpStatus.OK).body(profileService.getAllProfiles());
    }
}
