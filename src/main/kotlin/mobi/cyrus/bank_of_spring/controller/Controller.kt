package mobi.cyrus.bank_of_spring.controller

import mobi.cyrus.bank_of_spring.entity.SingleTableItem
import mobi.cyrus.bank_of_spring.service.BankingService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*


private const val ACCOUNT_PORTAL_PATH = "accounts-portal"
private const val ACCOUNT_ID_PATH = "$ACCOUNT_PORTAL_PATH/{targetAccountId}"
private const val TRANSACTION_PORTAL_PATH = "$ACCOUNT_ID_PATH/transaction"
private const val TRANSACTION_ID_PATH = "$TRANSACTION_PORTAL_PATH/{targetTransactionId}"
private const val TRANSACTION_DATE_PATH = "$TRANSACTION_PORTAL_PATH/date/{date}"
private const val TAG_ACCOUNT = "account"
private const val TAG_TRANSACTION = "transaction"

@RestController
class Controller {

    @Autowired
    lateinit var bankingService: BankingService

    /*--**----**----**----**----**----**----**   Account   **----**----**----**----**----**----**--*/

    private fun tagAccountId(id: String) = "$TAG_ACCOUNT#$id"

    @GetMapping(ACCOUNT_ID_PATH)
    fun retrieveAccount(@PathVariable targetAccountId: String): SingleTableItem? =
        bankingService.retrieveItem(tagAccountId(targetAccountId))

    @GetMapping("$ACCOUNT_PORTAL_PATH/ssn/{number}")
    fun searchAccountBySSN(@PathVariable number: String): List<SingleTableItem?> =
        bankingService.findAccountBySSN(number)

    @PostMapping(ACCOUNT_PORTAL_PATH)
    fun addAccount(@RequestBody entryItem: SingleTableItem): SingleTableItem = entryItem.apply {
        pk = tagAccountId(pk)
        bankingService.addAccount(this)
    }

    @PutMapping(ACCOUNT_ID_PATH)
    fun updateAccount(
        @RequestBody entryItem: SingleTableItem,
        @PathVariable targetAccountId: String
    ): SingleTableItem = bankingService.addAccount(
        entryItem,
        tagAccountId(targetAccountId)
    )

    @DeleteMapping(ACCOUNT_ID_PATH)
    fun deleteAccount(@PathVariable targetAccountId: String): SingleTableItem? =
        bankingService.deleteAccount(tagAccountId(targetAccountId))

    /*--**----**----**----**----**----**----** Transaction **----**----**----**----**----**----**--*/

    @GetMapping(TRANSACTION_ID_PATH)
    fun retrieveTransaction(
        @PathVariable targetAccountId: String,
        @PathVariable targetTransactionId: String
    ): SingleTableItem? =
        bankingService.retrieveItem(
            tagAccountId(targetAccountId),
            tagTransactionId(targetTransactionId)
        )

    @GetMapping(TRANSACTION_DATE_PATH)
    fun findTransactionByDate(
        @PathVariable targetAccountId: String,
        @PathVariable date: String
    ): List<SingleTableItem?> = bankingService.findTransactionByDate(targetAccountId, date)

    @PostMapping(TRANSACTION_PORTAL_PATH)
    fun addTransaction(
        @RequestBody entryItem: SingleTableItem,
        @PathVariable targetAccountId: String
    ): SingleTableItem =
        bankingService.addTransaction(
            entryItem.apply { sk = tagTransactionId(sk) },
            tagAccountId(targetAccountId)
        )

    private fun tagTransactionId(id: String) = "$TAG_TRANSACTION#$id"

}