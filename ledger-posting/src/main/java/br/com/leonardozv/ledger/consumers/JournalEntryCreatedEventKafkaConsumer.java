package br.com.leonardozv.ledger.consumers;

import br.com.leonardozv.ledger.models.JournalEntryCreatedEvent;
import br.com.leonardozv.ledger.services.LedgerPostingService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import java.time.Duration;
import java.util.*;
import java.util.stream.StreamSupport;

@Service
public class JournalEntryCreatedEventKafkaConsumer {

    private final LedgerPostingService ledgerPostingService;

    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    public JournalEntryCreatedEventKafkaConsumer(LedgerPostingService ledgerPostingService) {
        this.ledgerPostingService = ledgerPostingService;
        this.createConsumer();
    }

    private Consumer<Long, GenericRecord> createConsumer() {

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
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class.getName());

        props.put(KafkaAvroDeserializerConfig.SCHEMA_REGISTRY_URL_CONFIG, "https://psrc-4j1d2.westus2.azure.confluent.cloud");
        props.put(KafkaAvroDeserializerConfig.BASIC_AUTH_CREDENTIALS_SOURCE, "USER_INFO");
        props.put(KafkaAvroDeserializerConfig.USER_INFO_CONFIG, "2BEQE2KDNBJGDH2Y:8nixndjUyjXqTJoXnm3X3GwLZPz5F8umq74/g9ioG2mIi4lm0CWF1nUAf8deIFbP");

        final Consumer<Long, GenericRecord> consumer = new KafkaConsumer<>(props);

        consumer.subscribe(Collections.singletonList("accounting-journal-entry-created"));

        return consumer;

    }

    public void runConsumer() throws InterruptedException {

        final Consumer<Long, GenericRecord> consumer = createConsumer();

        long totalStartTime = System.nanoTime();

        while (true) {

            final ConsumerRecords<Long, GenericRecord> consumerRecords = consumer.poll(Duration.ofSeconds(10));

            if (consumerRecords.count() > 0) {

                System.out.println("Quantidade de eventos no microbatch: " + consumerRecords.count());

                List<JournalEntryCreatedEvent> lista = Collections.synchronizedList(new ArrayList <>());

                StopWatch swConversaoObjeto = new StopWatch();

                swConversaoObjeto.start();

                StreamSupport.stream(consumerRecords.spliterator(), true).forEach(record -> {

                    try {

                        JournalEntryCreatedEvent event = mapper.readValue(record.value().toString(), JournalEntryCreatedEvent.class);

                        event.setTopic(record.topic());
                        event.setPartition(record.partition());
                        event.setOffset(record.offset());

                        lista.add(event);

                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }

                });

                swConversaoObjeto.stop();

                System.out.println("Tempo de execucao da conversão para objeto em milisegundos: " + swConversaoObjeto.getTotalTimeMillis());

                StopWatch swSumarizacao = new StopWatch();

                swSumarizacao.start();

                ledgerPostingService.createJournalEntryCreatedStats(lista);

                swSumarizacao.stop();

                double totalEndTime = (System.nanoTime()-totalStartTime) / 1e6;

                System.out.println("Tempo de execucao da sumarização em milisegundos: " + swSumarizacao.getTotalTimeMillis());

                System.out.println("Tempo de execucao total em milisegundos: " + totalEndTime);

            }

//            consumer.commitAsync();

        }

    }

}
