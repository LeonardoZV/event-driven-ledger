package br.com.leonardozv.ledger.transaction.models;

public class CreateAccountingTransactionCommand {

    private AccountingTransaction transaction;

    public CreateAccountingTransactionCommand(AccountingTransaction transaction) {
        this.transaction = transaction;
    }

    public AccountingTransaction getTransaction() {
        return transaction;
    }

    public void setTransaction(AccountingTransaction transaction) {
        this.transaction = transaction;
    }

}
