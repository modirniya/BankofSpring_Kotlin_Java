package mobi.cyrus.bank_of_spring.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@DynamoDBDocument
public class ContactInfo {

    @DynamoDBAttribute
    private String email;

    @DynamoDBAttribute
    private String phone;

    @DynamoDBAttribute
    private String mobile;

    @DynamoDBAttribute
    private String address;

}
