package br.com.leonardozv.ledger.transaction.models;

public class AccountingTransactionCreatedEvent extends Event {

    private AccountingTransaction transaction;

    public AccountingTransactionCreatedEvent(AccountingTransaction transaction) {
        this.transaction = transaction;
    }

    public AccountingTransaction getTransaction() {
        return transaction;
    }

    public void setTransaction(AccountingTransaction transaction) {
        this.transaction = transaction;
    }

}
