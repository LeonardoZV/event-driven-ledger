package br.com.leonardozv.ledger.services;

import br.com.leonardozv.ledger.models.JournalEntryCreatedEvent;
import br.com.leonardozv.ledger.models.JournalEntryCreatedStats;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.LongSummaryStatistics;
import java.util.Map;

import static java.util.stream.Collectors.*;

@Service
public class LedgerPostingService {

    public Map<String, JournalEntryCreatedStats> createJournalEntryCreatedStats(List<JournalEntryCreatedEvent> lista) {

        Map<String, JournalEntryCreatedStats> statsPerAccountId = lista
                .parallelStream()
                .collect(groupingBy(JournalEntryCreatedEvent::getAccountId, collectingAndThen(toList(), list -> {
                    LongSummaryStatistics offsetSummary = list.parallelStream().collect(summarizingLong(JournalEntryCreatedEvent::getOffset));
                    Double totalMovement = list.parallelStream().collect(summingDouble(JournalEntryCreatedEvent::getMovement));
                    return new JournalEntryCreatedStats(offsetSummary, totalMovement);
                })));

        statsPerAccountId.entrySet().forEach(s -> System.out.println("AccountId: " + s.getKey() + ", MaxOffset: " + s.getValue().getOffsetSummary().getMax() + ", TotalMovement: " + s.getValue().getTotalMovement()));

        return statsPerAccountId;

    }

}
