package br.com.leonardozv.ledger.transaction.controllers;

import br.com.leonardozv.ledger.transaction.dtos.AccountingTransactionDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
public class AccountingTransactionController {

    @PostMapping
    public CompletableFuture<String> create(@RequestBody AccountingTransactionDTO dto) {
        var command = new AddBankAccountCommand(UUID.randomUUID().toString(), dto.getName());
//        log.info("Executing command: {}", command);
        return commandGateway.send(command);
    }

}
