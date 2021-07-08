package mobi.cyrus.bank_of_spring.service

import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList
import mobi.cyrus.bank_of_spring.entity.SingleTableItem
import mobi.cyrus.bank_of_spring.repository.DynamoDBRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class BankingService {

    @Autowired
    private lateinit var singleTableItemsRepository: DynamoDBRepository<SingleTableItem>


    fun retrieveItem(accountId: String, transactionId: String? = null): SingleTableItem? =
        singleTableItemsRepository.retrieveItem(accountId, transactionId ?: accountId)

    fun addAccount(
        item: SingleTableItem,
        partitionKey: String? = null
    ): SingleTableItem =
        singleTableItemsRepository.storeItem(item.apply {
            entityType.lowercase()
            pk = partitionKey ?: pk
            sk = pk
            customerSince = Date().toString()
        })


    fun addTransaction(
        item: SingleTableItem,
        accountId: String
    ): SingleTableItem =
        singleTableItemsRepository.storeItem(item.apply {
            entityType.lowercase()
            transactionDate = Date().toString()
            pk = accountId
        })


    fun deleteItem(
        accountId: String,
        transactionId: String? = null,
    ): SingleTableItem? =
        singleTableItemsRepository.deleteItem(accountId, transactionId ?: accountId)


    fun searchSSN(target: String): List<SingleTableItem?> =
        singleTableItemsRepository.search(target)

}