package za.co.digitalcowboy.profile.master.service.repository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.core.pagination.sync.SdkIterable;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.model.*;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse;
import software.amazon.awssdk.services.dynamodb.paginators.QueryIterable;
import za.co.digitalcowboy.profile.master.service.config.DynamoDbConfig;
import za.co.digitalcowboy.profile.master.service.entity.DynamoEventEntity;
import za.co.digitalcowboy.profile.master.service.entity.DynamoReadProfileEntity;

import java.util.*;


@Repository
public class DynamoProfileEventsRepository {


    private DynamoDbEnhancedClient dynamoDbenhancedClient;

    private DynamoDbConfig dynamoDbConfig;


    @Autowired
    public DynamoProfileEventsRepository(DynamoDbEnhancedClient dynamoDbenhancedClient, DynamoDbConfig dynamoDbConfig) {

        this.dynamoDbenhancedClient = dynamoDbenhancedClient;
        this.dynamoDbConfig = dynamoDbConfig;

    }

    @Async
    public void save(DynamoEventEntity transactionEvent) {

        try {

            getTable().putItem(PutItemEnhancedRequest
                    .builder(DynamoEventEntity.class)
                    .item(transactionEvent)
                    .build());

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public DynamoReadProfileEntity getProfile(String id) {

        DynamoDbTable<DynamoReadProfileEntity> readTable = dynamoDbenhancedClient.table(dynamoDbConfig.getProfileReadTable(),
                TableSchema.fromBean(DynamoReadProfileEntity.class));

        var profile = readTable.getItem(
                Key.builder()
                        .partitionValue(id)
                        .sortValue(id)
                        .build());
        return profile;

    }

    public List<DynamoReadProfileEntity> fetchAllProfiles() {

        DynamoDbTable<DynamoReadProfileEntity> readTable = dynamoDbenhancedClient.table(dynamoDbConfig.getProfileReadTable(),
                TableSchema.fromBean(DynamoReadProfileEntity.class));

        List<DynamoReadProfileEntity> resultList = new ArrayList<>();
        Iterator<Page<DynamoReadProfileEntity>> iterator = readTable.scan().iterator();
        while (iterator.hasNext()) {
            Page<DynamoReadProfileEntity> page = iterator.next();
            resultList.addAll(page.items());
        }
        return resultList;


    }


    public List<DynamoReadProfileEntity> getProfileByEmail(String email) {

        DynamoDbIndex<DynamoReadProfileEntity> readTable = dynamoDbenhancedClient.table(dynamoDbConfig.getProfileReadTable(),
                TableSchema.fromBean(DynamoReadProfileEntity.class)).index("EmailIndex");

        QueryConditional queryConditional = QueryConditional
                .keyEqualTo(Key.builder().partitionValue(email)
                        .build());

        final SdkIterable<Page<DynamoReadProfileEntity>> pagedResult = readTable.query(q -> q
                .queryConditional(queryConditional)
                .attributesToProject("name", "surname", "status", "profile_id", "id"));


        List<DynamoReadProfileEntity> collectedItems = new ArrayList<>();

        pagedResult.stream().forEach(page -> page.items().stream()
                .forEach(mt -> {
                    collectedItems.add(mt);
    }));
        return collectedItems;
    }




    private DynamoDbTable<DynamoEventEntity> getTable() {
        return dynamoDbenhancedClient.table(dynamoDbConfig.getProfileTableName(),
                        TableSchema.fromBean(DynamoEventEntity.class));

    }

    @Async
    public void saveBatch(List<DynamoEventEntity> transactionEvent) {

        List<WriteBatch> writeBatches = new ArrayList<>();

        for (DynamoEventEntity item : transactionEvent) {
            writeBatches.add(
                    WriteBatch.builder(DynamoEventEntity.class)
                            .mappedTableResource(getTable())
                            .addPutItem(builder -> builder.item(item))
                            .build()
            );
        }

        BatchWriteItemEnhancedRequest batchWriteItemEnhancedRequest = BatchWriteItemEnhancedRequest.builder()
                .writeBatches(writeBatches)
                .build();

        dynamoDbenhancedClient.batchWriteItem(batchWriteItemEnhancedRequest);

//        BatchWriteItemEnhancedRequest batchWriteItemEnhancedRequest = BatchWriteItemEnhancedRequest.builder()
//                .writeBatches(
//                        WriteBatch.builder(DynamoEventEntity.class)
//                                .mappedTableResource(getTable())
//                                .addPutItem(builder -> builder.item(transactionEvent))
//                                .build())
//                .build();
//
//        dynamoDbenhancedClient.batchWriteItem(batchWriteItemEnhancedRequest);

    }



}
