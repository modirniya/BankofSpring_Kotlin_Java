package mobi.cyrus.bank_of_spring.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamoDBTable(tableName = "BankOfSpringAccounts")
public class SingleTableItem {

    /*--**----**----**----**----**----**----** Account Attributes **----**----**----**----**----**----**--*/

    @DynamoDBAttribute
    private String firstName;

    @DynamoDBAttribute
    private String lastName;

    @DynamoDBAttribute
    private String balance;

    @DynamoDBAttribute
    private ContactInfo contactInfo;

    @DynamoDBAttribute
    private String customerSince;

    /*--**----**----**----**----**----**----** Transaction Attributes **----**----**----**----**----**----**--*/

    @DynamoDBAttribute
    private String transactionDate;

    @DynamoDBAttribute
    private String amount;

    @DynamoDBAttribute
    private String merchantName;

    @DynamoDBAttribute
    private String type;

    /*--**----**----**----**----**----**----** Common Attributes **----**----**----**----**----**----**--*/

    @DynamoDBAttribute
    private String entityType;

    private String PK;
    private String SK;
    private String ssn;

    @DynamoDBIndexHashKey(globalSecondaryIndexName = "gsi1")
    public String getSsn() {
        return ssn;
    }

    @DynamoDBHashKey(attributeName = "PK")
    public String getPK() {
        if (this.PK == null)
            this.PK = UUID.randomUUID().toString();
        return this.PK;
    }

    @DynamoDBRangeKey(attributeName = "SK")
    public String getSK() {
        if (this.SK == null)
            this.SK = UUID.randomUUID().toString();
        return this.SK;
    }

    /*--**----**----**----**----**----**----** Getters And Setters **----**----**----**----**----**----**--*/

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public void setPK(String PK) {
        this.PK = PK;
    }

    public void setSK(String SK) {
        this.SK = SK;
    }

    public String getCustomerSince() {
        return customerSince;
    }

    public void setCustomerSince(String customerSince) {
        this.customerSince = customerSince;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }
}
