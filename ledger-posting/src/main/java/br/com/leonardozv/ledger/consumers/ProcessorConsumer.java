package br.com.leonardozv.ledger.consumers;

import br.com.leonardozv.ledger.models.JournalEntryCreatedEvent;
import com.lmax.disruptor.EventHandler;

public class ProcessorConsumer {

    public static EventHandler<JournalEntryCreatedEvent>[] getEventHandler() {
        EventHandler<JournalEntryCreatedEvent> eventHandler = (event, sequence, endOfBatch) -> print(event);
        return new EventHandler[] { eventHandler };
    }

    private static void print(JournalEntryCreatedEvent evento) {
        System.out.println(evento.getMovement());
    }

}
