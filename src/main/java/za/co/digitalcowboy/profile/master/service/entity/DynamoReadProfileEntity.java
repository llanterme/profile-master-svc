package za.co.digitalcowboy.profile.master.service.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

import java.io.Serializable;

@DynamoDbBean
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DynamoReadProfileEntity implements Serializable {

    public static final long serialVersionUID = 1L;

    @Getter(onMethod_ = @DynamoDbPartitionKey)
    private String id;

    @Getter(onMethod_ = @DynamoDbSortKey)
    private String profile_id;

    private String email;

    private String name;

    private String status;

    private String surname;



}
