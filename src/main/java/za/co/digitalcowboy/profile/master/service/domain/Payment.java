package za.co.digitalcowboy.profile.master.service.domain;


import lombok.*;
import lombok.extern.jackson.Jacksonized;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Jacksonized
public class Payment {

    private String status;

}
