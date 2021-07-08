package mobi.cyrus.bank_of_spring.repository

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression
import mobi.cyrus.bank_of_spring.entity.SingleTableItem
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
class SingleTableRepository(
    @Autowired override val mapper: DynamoDBMapper
) :
    DynamoDBRepository<SingleTableItem> {

    override fun retrieve(
        partitionKey: String,
        sortKey: String
    ): SingleTableItem? =
        mapper.load(SingleTableItem::class.java, partitionKey, sortKey)


    override fun store(
        item: SingleTableItem
    ): SingleTableItem = item.also { mapper.save(it) }

    override fun search(partitionKey: String?, date: String?, ssn: String?): List<SingleTableItem?> {
        /*val eav  = mapOf <String, AttributeValue>(
            ":ssn" to AttributeValue().withS(partitionKey)
        )*/
        if (partitionKey != null && date != null)
            return findTransactionByDate(partitionKey, date)
        if (ssn != null)
            return findAccountBySSN(ssn)
        return emptyList()
    }

    override fun delete(
        partitionKey: String
    ): SingleTableItem? =
        this.retrieve(partitionKey, partitionKey)
            .also { mapper.delete(it) }

    private fun getSSNQueryExpression(ssn: String): DynamoDBQueryExpression<SingleTableItem> =
        DynamoDBQueryExpression<SingleTableItem>()
            .apply {
                hashKeyValues = SingleTableItem().apply { this.ssn = ssn }
                indexName = "gsi1"
                withConsistentRead(false)
            }

    private fun getTransactionByDateQueryExpression(
        partitionKey: String,
        date: String
    ): DynamoDBQueryExpression<SingleTableItem> =
        DynamoDBQueryExpression<SingleTableItem>()
            .apply {
                hashKeyValues = SingleTableItem().also {
                    it.pk = partitionKey
                    it.transactionDate = date
                }
                indexName = "gsi2"
                keyConditionExpression
                withConsistentRead(false)
            }

    private fun findAccountBySSN(ssn: String): List<SingleTableItem?> {
        val tempResult = mutableListOf<SingleTableItem?>()
        mapper.query(
            SingleTableItem::class.java,
            getSSNQueryExpression(ssn)
        )
            .forEach { tempResult.add(this.retrieve(it.pk, it.sk)) }
        return tempResult
    }

    private fun findTransactionByDate(accountId: String, date: String): List<SingleTableItem?> {
        val tempResult = mutableListOf<SingleTableItem?>()
        mapper.query(
            SingleTableItem::class.java,
            getTransactionByDateQueryExpression(accountId, date)
        ).forEach { tempResult.add(this.retrieve(it.pk, it.sk)) }
        return tempResult
    }
}