package za.co.digitalcowboy.profile.master.service.service;
import za.co.digitalcowboy.profile.master.service.domain.ProfileRequest;

import java.util.List;

public interface ProfileService {

    void saveProfileEvent(ProfileRequest profileRequest);

    ProfileRequest getProfile(String id);

    List<ProfileRequest> getAllProfiles();

}
