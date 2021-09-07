package br.com.leonardozv.ledger.models;

import java.util.LongSummaryStatistics;

public class JournalEntryCreatedStats {

    private LongSummaryStatistics offsetSummary;
    private Double totalMovement;

    public JournalEntryCreatedStats(LongSummaryStatistics offsetSummary, Double totalMovement) {
        this.offsetSummary = offsetSummary;
        this.totalMovement = totalMovement;
    }

    public LongSummaryStatistics getOffsetSummary() {
        return offsetSummary;
    }

    public void setOffsetSummary(LongSummaryStatistics offsetSummary) {
        this.offsetSummary = offsetSummary;
    }

    public Double getTotalMovement() {
        return totalMovement;
    }

    public void setTotalMovement(Double totalMovement) {
        this.totalMovement = totalMovement;
    }

}
