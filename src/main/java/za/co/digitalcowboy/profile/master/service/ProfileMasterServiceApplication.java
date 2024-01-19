package za.co.digitalcowboy.profile.master.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class ProfileMasterServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProfileMasterServiceApplication.class, args);
    }

//    @Bean
//    public Consumer<Order> orderConsumer() {
//        return incomingOrder -> log.info("Incoming Number : {}", incomingOrder);
//    }


}
