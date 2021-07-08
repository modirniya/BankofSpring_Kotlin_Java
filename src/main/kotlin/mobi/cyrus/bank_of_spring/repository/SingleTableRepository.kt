package mobi.cyrus.bank_of_spring.repository

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList
import mobi.cyrus.bank_of_spring.entity.SingleTableItem
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
class SingleTableRepository(
    @Autowired override val mapper: DynamoDBMapper
) :
    DynamoDBRepository<SingleTableItem> {

    override fun retrieveItem(
        partitionKey: String,
        sortKey: String?
    ): SingleTableItem? =
        mapper.load(SingleTableItem::class.java, partitionKey, sortKey ?: partitionKey)


    override fun storeItem(
        item: SingleTableItem
    ): SingleTableItem = item.also { mapper.save(it) }

    override fun search(partitionKey: String): List<SingleTableItem?> {
        /*val eav  = mapOf <String, AttributeValue>(
            ":ssn" to AttributeValue().withS(partitionKey)
        )*/
        val keyObject = SingleTableItem()
        keyObject.ssn = partitionKey
        val queryExpression = DynamoDBQueryExpression<SingleTableItem>()
        queryExpression.hashKeyValues = keyObject
        queryExpression.indexName = "gsi1"
        queryExpression.withConsistentRead(false)
        val listResult = mutableListOf<SingleTableItem?>()
        mapper.query(SingleTableItem::class.java, queryExpression)
            .forEach { listResult.add(this.retrieveItem(it.pk, it.sk)) }
        return listResult.toList()
    }

    override fun deleteItem(
        partitionKey: String,
        sortKey: String?
    ): SingleTableItem? =
        this.retrieveItem(partitionKey, sortKey ?: partitionKey)
            .also { mapper.delete(it) }
}