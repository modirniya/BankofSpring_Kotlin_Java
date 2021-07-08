package mobi.cyrus.bank_of_spring.controller

import mobi.cyrus.bank_of_spring.entity.SingleTableItem
import mobi.cyrus.bank_of_spring.service.BankingService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*


private const val ACCOUNT_PORTAL_PATH = "accounts-portal"
private const val ACCOUNT_PATH = "$ACCOUNT_PORTAL_PATH/{targetAccountId}"
private const val TRANSACTION_PORTAL_PATH = "$ACCOUNT_PATH/transaction"
private const val TRANSACTION_PATH = "$TRANSACTION_PORTAL_PATH/{targetTransactionId}"
private const val TAG_ACCOUNT = "account"
private const val TAG_TRANSACTION = "transaction"

@RestController
class Controller {

    @Autowired
    lateinit var bankingService: BankingService

    /*--**----**----**----**----**----**----**   Account   **----**----**----**----**----**----**--*/

    private fun tagAccountId(id: String) = "$TAG_ACCOUNT#$id"

    @GetMapping(ACCOUNT_PATH)
    fun retrieveAccount(@PathVariable targetAccountId: String): SingleTableItem? =
        bankingService.retrieveItem(tagAccountId(targetAccountId))

    @GetMapping("$ACCOUNT_PORTAL_PATH/ssn/{number}")
    fun searchAccountBySSN(@PathVariable number: String): List<SingleTableItem?> =
        bankingService.searchSSN(number)

    @PostMapping(ACCOUNT_PORTAL_PATH)
    fun addAccount(@RequestBody entryItem: SingleTableItem): SingleTableItem =
        bankingService.addAccount(entryItem)

    @PutMapping(ACCOUNT_PATH)
    fun updateAccount(
        @RequestBody entryItem: SingleTableItem,
        @PathVariable targetAccountId: String
    ): SingleTableItem = bankingService.addAccount(
        entryItem,
        tagAccountId(targetAccountId)
    )

    @DeleteMapping(ACCOUNT_PATH)
    fun deleteAccount(@PathVariable targetAccountId: String): SingleTableItem? =
        bankingService.deleteItem(tagAccountId(targetAccountId))

    /*--**----**----**----**----**----**----** Transaction **----**----**----**----**----**----**--*/

    @GetMapping(TRANSACTION_PATH)
    fun retrieveTransaction(
        @PathVariable targetAccountId: String,
        @PathVariable targetTransactionId: String
    ): SingleTableItem? =
        bankingService.retrieveItem(
            tagAccountId(targetAccountId),
            tagTransactionId(targetTransactionId)
        )

    @PostMapping(TRANSACTION_PORTAL_PATH)
    fun addTransaction(
        @RequestBody entryItem: SingleTableItem,
        @PathVariable targetAccountId: String
    ): SingleTableItem =
        bankingService.addTransaction(entryItem, tagAccountId(targetAccountId))

    private fun tagTransactionId(id: String) = "$TAG_TRANSACTION#$id"

}