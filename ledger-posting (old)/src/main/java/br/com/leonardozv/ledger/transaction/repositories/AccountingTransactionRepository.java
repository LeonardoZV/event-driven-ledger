package br.com.leonardozv.ledger.transaction.repositories;

import br.com.leonardozv.ledger.transaction.models.AccountingTransaction;
import org.springframework.stereotype.Repository;

@Repository
public class AccountingTransactionRepository extends CrudRepository<AccountingTransaction, String> {

}
