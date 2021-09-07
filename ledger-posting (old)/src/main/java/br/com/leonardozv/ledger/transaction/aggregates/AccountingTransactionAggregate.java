package br.com.leonardozv.ledger.transaction.aggregates;

import br.com.leonardozv.ledger.transaction.models.AccountingTransactionCreatedEvent;
import br.com.leonardozv.ledger.transaction.models.CreateAccountingTransactionCommand;
import br.com.leonardozv.ledger.transaction.models.Event;
import br.com.leonardozv.ledger.transaction.repositories.AccountingTransactionRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class AccountingTransactionAggregate {

    private final AccountingTransactionRepository writeRepository;

    public AccountingTransactionAggregate(AccountingTransactionRepository writeRepository) {
        this.writeRepository = writeRepository;
    }

    public List<Event> handle(CreateAccountingTransactionCommand command) {
        AccountingTransactionCreatedEvent event = new AccountingTransactionCreatedEvent(command.getTransaction());
//        writeRepository.addUser(command.getTransaction());
        return Arrays.asList(event);
    }

}
