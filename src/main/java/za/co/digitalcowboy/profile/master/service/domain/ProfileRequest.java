package za.co.digitalcowboy.profile.master.service.domain;


import lombok.*;
import lombok.extern.jackson.Jacksonized;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Jacksonized
public class ProfileRequest {

    private String id;
    private String profileId;
    private String emailAddress;
    private String name;
    private String surname;
    private String status;
    private String created_date;


}
