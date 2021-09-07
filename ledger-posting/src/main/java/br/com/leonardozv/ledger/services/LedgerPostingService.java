package br.com.leonardozv.ledger.services;

import br.com.leonardozv.ledger.models.JournalEntryCreatedEvent;
import br.com.leonardozv.ledger.models.JournalEntryCreatedStats;
import br.com.leonardozv.ledger.repository.DailyLedgerMovementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;

import static java.util.stream.Collectors.*;

@Service
public class LedgerPostingService {

    private final DailyLedgerMovementRepository dailyLedgerMovementRepository;

    @Autowired
    public LedgerPostingService(DailyLedgerMovementRepository dailyLedgerMovementRepository) {
        this.dailyLedgerMovementRepository = dailyLedgerMovementRepository;
    }

    public Map<Object, Double> summarizeJournalEntries(List<JournalEntryCreatedEvent> lista) {

        Function<JournalEntryCreatedEvent, List<Object>> compositeKey = journalEntryCreatedEvent ->
                Arrays.asList(
                        journalEntryCreatedEvent.getData().getAccountingDate(),
                        journalEntryCreatedEvent.getData().getCompanyId(),
                        journalEntryCreatedEvent.getData().getGaapId(),
                        journalEntryCreatedEvent.getData().getBalanceVersionId(),
                        journalEntryCreatedEvent.getData().getAccountId());

        Map<Object, Double> statsPerAccountId = lista
                .parallelStream()
                .collect(groupingBy(compositeKey, summingDouble(e -> e.getData().getMovementAmount())));

        statsPerAccountId.forEach((key, value) -> System.out.println("Key: " + key + ", TotalMovement: " + value));

        return statsPerAccountId;

    }

    public Map<Object, JournalEntryCreatedStats> createJournalEntryCreatedStats(List<JournalEntryCreatedEvent> lista) {

        Function<JournalEntryCreatedEvent, List<Object>> compositeKey = journalEntryCreatedEvent ->
                Arrays.asList(
                        journalEntryCreatedEvent.getData().getAccountingDate(),
                        journalEntryCreatedEvent.getData().getCompanyId(),
                        journalEntryCreatedEvent.getData().getGaapId(),
                        journalEntryCreatedEvent.getData().getBalanceVersionId(),
                        journalEntryCreatedEvent.getData().getAccountId());

        Map<Object, JournalEntryCreatedStats> statsPerAccountId = lista
                .parallelStream()
                .collect(groupingBy(compositeKey, collectingAndThen(toList(), list -> {
                    LongSummaryStatistics offsetSummary = list.parallelStream().collect(summarizingLong(JournalEntryCreatedEvent::getOffset));
                    Double totalDebits = list.parallelStream().mapToDouble(e -> e.getData().getDebitAmount()).sum();
                    Double totalCredits = list.parallelStream().mapToDouble(e -> e.getData().getCreditAmount()).sum();
                    Double totalMovement = list.parallelStream().mapToDouble(e -> e.getData().getMovementAmount()).sum();
                    return new JournalEntryCreatedStats(offsetSummary, totalDebits, totalCredits, totalMovement);
                })));

        statsPerAccountId.forEach((key, value) -> System.out.println("Key: " + key + ", MaxOffset: " + value.getOffsetSummary().getMax() + ", TotalMovement: " + value.getTotalMovement()));

        dailyLedgerMovementRepository.upsert(statsPerAccountId);

        return statsPerAccountId;

    }

}
