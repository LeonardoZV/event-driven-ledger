package br.com.leonardozv.ledger.consumers;

import br.com.leonardozv.ledger.models.JournalEntryCreatedEvent;
import br.com.leonardozv.ledger.serializers.CustomKafkaAvroDeserializer;
import br.com.leonardozv.ledger.services.LedgerPostingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.confluent.kafka.serializers.GenericContainerWithVersion;
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import java.time.Duration;
import java.util.*;

@Service
public class JournalEntryCreatedEventKafkaConsumer {

    private final LedgerPostingService ledgerPostingService;

    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    public JournalEntryCreatedEventKafkaConsumer(LedgerPostingService ledgerPostingService) {
        this.ledgerPostingService = ledgerPostingService;
        this.createConsumer();
    }

    private Consumer<Long, GenericContainerWithVersion> createConsumer() {

        final Properties props = new Properties();

        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "pkc-epwny.eastus.azure.confluent.cloud:9092");
        props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SASL_SSL");
        props.put(SaslConfigs.SASL_JAAS_CONFIG, "org.apache.kafka.common.security.plain.PlainLoginModule required username='BIMCMFF6WU3YBB34' password='Xnr9geulvxPYeyNeL2r56iyjNG5dwkB2CTnQz+syVZwOUfJIQFxmSJT0+MskxOnQ';");
        props.put(SaslConfigs.SASL_MECHANISM, "PLAIN");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "daily-accounting-balance-projection-service");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 4000000);
        props.put(ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG, 40000000);
        props.put(ConsumerConfig.FETCH_MIN_BYTES_CONFIG, 40000000);
//        props.put(ConsumerConfig.FETCH_MAX_BYTES_CONFIG, 30000000);
//        props.put(ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG, 30000);
        props.put(ConsumerConfig.RECEIVE_BUFFER_CONFIG, -1);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, CustomKafkaAvroDeserializer.class.getName());

        props.put(KafkaAvroDeserializerConfig.SCHEMA_REGISTRY_URL_CONFIG, "https://psrc-4j1d2.westus2.azure.confluent.cloud");
        props.put(KafkaAvroDeserializerConfig.BASIC_AUTH_CREDENTIALS_SOURCE, "USER_INFO");
        props.put(KafkaAvroDeserializerConfig.USER_INFO_CONFIG, "2BEQE2KDNBJGDH2Y:8nixndjUyjXqTJoXnm3X3GwLZPz5F8umq74/g9ioG2mIi4lm0CWF1nUAf8deIFbP");

        final Consumer<Long, GenericContainerWithVersion> consumer = new KafkaConsumer<>(props);

        consumer.subscribe(Collections.singletonList("accounting-journal-entry-created"));

        return consumer;

    }

    public void runConsumer() throws InterruptedException {

        final Consumer<Long, GenericContainerWithVersion> consumer = createConsumer();

//        ThreadFactory threadFactory = DaemonThreadFactory.INSTANCE;
//
//        WaitStrategy waitStrategy = new BusySpinWaitStrategy();
//
//        Disruptor<AccountingJournalEntryCreatedEvent> disruptor = new Disruptor<>(AccountingJournalEntryCreatedEvent.EVENT_FACTORY, 16777216, threadFactory, ProducerType.SINGLE, waitStrategy);
//
//        disruptor.handleEventsWith(ProcessorConsumer.getEventHandler());
//
//        RingBuffer<AccountingJournalEntryCreatedEvent> ringBuffer = disruptor.start();

        long totalStartTime = System.nanoTime();

        while (true) {

            final ConsumerRecords<Long, GenericContainerWithVersion> consumerRecords = consumer.poll(Duration.ofSeconds(100));

            if (consumerRecords.count() > 0) {

                System.out.println("Quantidade de eventos no microbatch: " + consumerRecords.count());

                List<JournalEntryCreatedEvent> lista = new ArrayList<>();

                consumerRecords.forEach(record -> {
                    JournalEntryCreatedEvent event = new JournalEntryCreatedEvent(1L, "100", 1.0, 0.0, 1.0);
                    lista.add(event);
                });

                StopWatch swProcessamento = new StopWatch();

                swProcessamento.start();

                ledgerPostingService.createJournalEntryCreatedStats(lista);

                swProcessamento.stop();

                double totalEndTime = (System.nanoTime()-totalStartTime) / 1e6;

                System.out.println("Tempo de execucao do processamento do microbatch em milisegundos: " + swProcessamento.getTotalTimeMillis());

                System.out.println("Tempo de execucao total em milisegundos: " + totalEndTime);

            }

//            consumerRecords.forEach(record -> {
//
//                long sequenceId = ringBuffer.next();
//
//                AccountingJournalEntryCreatedEvent ringBufferEvent = ringBuffer.get(sequenceId);
//
//                ringBufferEvent.setAccountId("100");
//                ringBufferEvent.setCredit(1.0);
//                ringBufferEvent.setDebit(0.0);
//                ringBufferEvent.setMovement(1.0);
//
//                ringBuffer.publish(sequenceId);
//
////                System.out.printf("Consumer Record:(%d, %s, %d, %d)\n", record.key(), record.value().container().toString(), record.partition(), record.offset());
//
//            });

//            consumer.commitAsync();

        }

    }

}
