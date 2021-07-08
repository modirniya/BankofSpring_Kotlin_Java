package mobi.cyrus.bank_of_spring.repository

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList
import mobi.cyrus.bank_of_spring.entity.SingleTableItem


interface DynamoDBRepository<T> {

    val mapper: DynamoDBMapper

    fun storeItem(item: T): T

    fun retrieveItem(partitionKey: String, sortKey: String? = null): T?

    fun search(partitionKey: String): List<SingleTableItem?>

    fun deleteItem(partitionKey: String, sortKey: String? = null): T?

}