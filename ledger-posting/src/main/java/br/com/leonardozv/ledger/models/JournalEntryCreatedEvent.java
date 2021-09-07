package br.com.leonardozv.ledger.models;

import com.lmax.disruptor.EventFactory;

public class JournalEntryCreatedEvent {

    private Long offset;
    private String accountId;
    private Double credit;
    private Double debit;
    private Double movement;

    public JournalEntryCreatedEvent() {

    }

    public JournalEntryCreatedEvent(Long offset, String accountId, Double credit, Double debit, Double movement) {
        this.offset = offset;
        this.accountId = accountId;
        this.credit = credit;
        this.debit = debit;
        this.movement = movement;
    }

    public final static EventFactory EVENT_FACTORY = () -> new JournalEntryCreatedEvent();

    public Long getOffset() { return offset;  }

    public void setOffset(Long offset) { this.offset = offset; }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public Double getCredit() {
        return credit;
    }

    public void setCredit(Double credit) {
        this.credit = credit;
    }

    public Double getDebit() {
        return debit;
    }

    public void setDebit(Double debit) {
        this.debit = debit;
    }

    public Double getMovement() {
        return movement;
    }

    public void setMovement(Double movement) {
        this.movement = movement;
    }
}
