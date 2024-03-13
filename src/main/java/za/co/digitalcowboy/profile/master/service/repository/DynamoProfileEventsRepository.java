package za.co.digitalcowboy.profile.master.service.repository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.BatchWriteItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.PutItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.WriteBatch;
import za.co.digitalcowboy.profile.master.service.config.DynamoDbConfig;
import za.co.digitalcowboy.profile.master.service.domain.ProfileRequest;
import za.co.digitalcowboy.profile.master.service.entity.DynamoEventEntity;
import za.co.digitalcowboy.profile.master.service.entity.DynamoReadProfileEntity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


@Repository
public class DynamoProfileEventsRepository {


    private DynamoDbEnhancedClient dynamoDbenhancedClient ;

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

           var profile =  readTable.getItem(
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
