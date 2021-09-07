package br.com.leonardozv.ledger.models;

import java.util.LongSummaryStatistics;

public class JournalEntryCreatedStats {

    private LongSummaryStatistics offsetSummary;
    private Double totalDebits;
    private Double totalCredits;
    private Double totalMovement;

    public JournalEntryCreatedStats(LongSummaryStatistics offsetSummary, Double totalDebits, Double totalCredits, Double totalMovement) {
        this.offsetSummary = offsetSummary;
        this.totalDebits = totalDebits;
        this.totalCredits = totalCredits;
        this.totalMovement = totalMovement;
    }

    public LongSummaryStatistics getOffsetSummary() {
        return offsetSummary;
    }

    public void setOffsetSummary(LongSummaryStatistics offsetSummary) {
        this.offsetSummary = offsetSummary;
    }

    public Double getTotalDebits() {
        return totalDebits;
    }

    public void setTotalDebits(Double totalDebits) {
        this.totalDebits = totalDebits;
    }

    public Double getTotalCredits() {
        return totalCredits;
    }

    public void setTotalCredits(Double totalCredits) {
        this.totalCredits = totalCredits;
    }

    public Double getTotalMovement() {
        return totalMovement;
    }

    public void setTotalMovement(Double totalMovement) {
        this.totalMovement = totalMovement;
    }

}
