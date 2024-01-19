package za.co.digitalcowboy.profile.master.service.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.co.digitalcowboy.profile.master.service.domain.MessageType;
import za.co.digitalcowboy.profile.master.service.domain.ProfileRequest;
import za.co.digitalcowboy.profile.master.service.entity.DynamoEventEntity;
import za.co.digitalcowboy.profile.master.service.repository.DynamoProfileEventsRepository;
import za.co.digitalcowboy.profile.master.service.service.ProfileService;

import java.time.LocalDateTime;
import java.util.UUID;


@Service
public class ProfileServiceImpl implements ProfileService {

    String SOURCE = "co.za.digital.cowboy.profile.events.master";
    String VERSION = "1.0";

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    DynamoProfileEventsRepository dynamoProfileEventsRepository;

    @Override
    public void saveProfileEvent(ProfileRequest profileRequest) {
        try {

            MessageType messageType;
            String profileId;

                if (profileRequest.getProfileId().isBlank()) {
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

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
}
