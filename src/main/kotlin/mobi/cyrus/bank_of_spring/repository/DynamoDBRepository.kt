package mobi.cyrus.bank_of_spring.repository

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper
import mobi.cyrus.bank_of_spring.entity.SingleTableItem


interface DynamoDBRepository<T> {

    val mapper: DynamoDBMapper

    fun store(item: T): T

    fun retrieve(partitionKey: String, sortKey: String): T?

    fun search(
        partitionKey: String? = null,
        date: String? = null,
        ssn: String? = null
    ): List<SingleTableItem?>

    fun delete(partitionKey: String): T?

}