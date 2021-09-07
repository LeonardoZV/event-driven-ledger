package br.com.leonardozv.ledger.services;

import br.com.leonardozv.ledger.repository.DailyLedgerMovementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DailyLedgerMovementProjectionService {

    private final DailyLedgerMovementRepository dailyLedgerMovementRepository;

    @Autowired
    public DailyLedgerMovementProjectionService(DailyLedgerMovementRepository dailyLedgerMovementRepository) {
        this.dailyLedgerMovementRepository = dailyLedgerMovementRepository;
    }

}
