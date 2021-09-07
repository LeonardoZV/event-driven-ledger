package br.com.leonardozv.ledger.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lmax.disruptor.EventFactory;

public class JournalEntryCreatedEvent {

    private String topic;
    private int partition;
    private Long offset;

    @JsonProperty("data")
    private JournalEntryCreatedEventData data;

    public JournalEntryCreatedEvent() {

    }

    public JournalEntryCreatedEvent(JournalEntryCreatedEventData data) {
        this.data = data;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public int getPartition() {
        return partition;
    }

    public void setPartition(int partition) {
        this.partition = partition;
    }

    public Long getOffset() {
        return offset;
    }

    public void setOffset(Long offset) {
        this.offset = offset;
    }

    public JournalEntryCreatedEventData getData() {
        return data;
    }

    public void setData(JournalEntryCreatedEventData data) {
        this.data = data;
    }

    public final static EventFactory EVENT_FACTORY = () -> new JournalEntryCreatedEvent();

}
