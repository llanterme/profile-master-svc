package za.co.digitalcowboy.profile.master.service.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.co.digitalcowboy.profile.master.service.domain.MessageType;
import za.co.digitalcowboy.profile.master.service.domain.ProfileRequest;
import za.co.digitalcowboy.profile.master.service.entity.DynamoEventEntity;
import za.co.digitalcowboy.profile.master.service.entity.DynamoReadProfileEntity;
import za.co.digitalcowboy.profile.master.service.exception.DomainException;
import za.co.digitalcowboy.profile.master.service.exception.ErrorCode;
import za.co.digitalcowboy.profile.master.service.repository.DynamoProfileEventsRepository;
import za.co.digitalcowboy.profile.master.service.service.ProfileService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
public class ProfileServiceImpl implements ProfileService {

    String SOURCE = "co.za.digital.cowboy.profile.events.master";
    String VERSION = "1.0";

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    DynamoProfileEventsRepository dynamoProfileEventsRepository;

    @Override
    public ProfileRequest saveProfileEvent(ProfileRequest profileRequest) {
        try {

            MessageType messageType;
            String profileId;

                if (profileRequest.getProfileId() == null) {
                messageType = MessageType.PROFILE_CREATED;
                profileId = UUID.randomUUID().toString();

            } else {
                messageType = MessageType.PROFILE_MODIFIED;
                profileId = profileRequest.getProfileId();
            }

            String eventId = UUID.randomUUID().toString();
            String Id = UUID.randomUUID().toString();
            profileRequest.setProfileId(profileId);
            profileRequest.setId(UUID.randomUUID().toString());
            profileRequest.setCreated_date(LocalDateTime.now().toString());
           var newProfileEvent =  DynamoEventEntity.builder()
                    .event_id(eventId)
                    .id(Id)
                    .version(VERSION)
                    .created_date(LocalDateTime.now().toString())
                    .type(messageType.toString())
                    .source(SOURCE)
                    .payload(objectMapper.writeValueAsString(profileRequest))
                    .build();

            dynamoProfileEventsRepository.save(newProfileEvent);

            return profileRequest;

        } catch (Exception e) {
            throw new DomainException(ErrorCode.UNABLE_SO_SAVE_PROFILE);
        }

    }

    @Override
    public ProfileRequest getProfile(String id) {
        var fetchedProfile = dynamoProfileEventsRepository.getProfile(id);
        var profile  = ProfileRequest.builder()
                .status(fetchedProfile.getStatus())
                .emailAddress(fetchedProfile.getEmail())
                .profileId(id)
                .id(id)
                .name(fetchedProfile.getName())
                .surname(fetchedProfile.getSurname())
                .build();

        return profile;

    }

    @Override
    public List<ProfileRequest> getAllProfiles() {
        return dynamoProfileEventsRepository.fetchAllProfiles().stream()
                .map(profileEntity -> ProfileRequest.builder()
                        .id(profileEntity.getId())
                        .profileId(profileEntity.getProfile_id())
                        .name(profileEntity.getName())
                        .surname(profileEntity.getSurname())
                        .emailAddress(profileEntity.getEmail())
                        .status(profileEntity.getStatus())
                        .build())
                .collect(Collectors.toList());
    }
}
