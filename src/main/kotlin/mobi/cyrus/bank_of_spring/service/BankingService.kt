package mobi.cyrus.bank_of_spring.service

import mobi.cyrus.bank_of_spring.entity.SingleTableItem
import mobi.cyrus.bank_of_spring.repository.DynamoDBRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Service
class BankingService {

    @Autowired
    private lateinit var singleTableItemsRepository: DynamoDBRepository<SingleTableItem>


    fun retrieveItem(accountId: String, transactionId: String? = null): SingleTableItem? =
        singleTableItemsRepository.retrieve(accountId, transactionId ?: accountId)

    fun addAccount(
        item: SingleTableItem,
        partitionKey: String? = null
    ): SingleTableItem =
        item.apply {
            pk = partitionKey ?: pk
            sk = pk
            customerSince = getDate()
            singleTableItemsRepository.store(this)
            entityType.lowercase()
        }

    fun addTransaction(
        item: SingleTableItem,
        accountId: String
    ): SingleTableItem =
        item.apply {
            pk = accountId
            entityType.lowercase()
            transactionDate = getDate()
            val account = retrieveItem(accountId)
            val balance = (account!!.balance.toDouble()) - amount.toDouble()
            isApproved = balance >= 0
            if (isApproved) {
                // isApproved
                account.balance = balance.toString()
                addAccount(account, account.pk)
            }
            singleTableItemsRepository.store(this)
        }

    fun deleteAccount(
        accountId: String
    ): SingleTableItem? =
        singleTableItemsRepository.delete(accountId)


    fun findAccountBySSN(ssnNumber: String): List<SingleTableItem?> =
        singleTableItemsRepository.search(ssn = ssnNumber)

    fun findTransactionByDate(accountId: String, date: String): List<SingleTableItem?> =
        singleTableItemsRepository.search(accountId, date)

    fun getDate(): String = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE)

}