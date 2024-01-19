package za.co.digitalcowboy.profile.master.service.config;



import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.regions.Region;
import za.co.digitalcowboy.profile.master.service.entity.DynamoEventEntity;

import java.net.URI;


@Data
@Configuration
@EnableAsync
public class DynamoDbConfig {

        private String profileTableName = "profile-master-events-table";

    public DynamoDbConfig() {

    }


    private DynamoDbClient dynamoDbClient() {
        return DynamoDbClient
                .builder()
            //    .endpointOverride(URI.create("http://localhost:4566"))
                .region(Region.EU_WEST_2)
                .build();
    }


    @Bean
    public DynamoDbEnhancedClient dynamoDbEnhancedClient() {
        return DynamoDbEnhancedClient
                .builder()
                .dynamoDbClient(dynamoDbClient())
                .build();
    }

    @Bean
    public DynamoDbTable<DynamoEventEntity> transactionTable() {
        return dynamoDbEnhancedClient().table(profileTableName, TableSchema.fromBean(DynamoEventEntity.class));
    }
}
