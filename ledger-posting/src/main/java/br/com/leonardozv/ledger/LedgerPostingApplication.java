package br.com.leonardozv.ledger;

import br.com.leonardozv.ledger.consumers.JournalEntryCreatedEventKafkaConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LedgerPostingApplication implements CommandLineRunner {

	private final JournalEntryCreatedEventKafkaConsumer kafkaConsumer;

	@Autowired
	public LedgerPostingApplication(JournalEntryCreatedEventKafkaConsumer kafkaConsumer) {
		this.kafkaConsumer = kafkaConsumer;
	}

	public static void main(String[] args) {
		SpringApplication.run(LedgerPostingApplication.class, args);
	}

	@Override
	public void run(String... args) throws InterruptedException {

		kafkaConsumer.runConsumer();

//		List<AccountingJournalEntryCreatedEvent> eventos = new ArrayList<>();
//
//		for (int i = 0; i < 1000000; i++) {
//			eventos.add(new AccountingJournalEntryCreatedEvent((long) i, "100", 1.0, 0.0, 1.0));
//		}
//
//		long startTime;
//		double endTime;
//
//		startTime = System.nanoTime();
//		Map<String, Double> totalByAccountIdSerial = eventos.stream().collect(Collectors.groupingBy(AccountingJournalEntryCreatedEvent::getAccountId, Collectors.summingDouble(AccountingJournalEntryCreatedEvent::getMovement)));
//		endTime = (System.nanoTime()-startTime) / 1e6;
//		System.out.println("Apenas soma, Sem multithread: " + endTime);
//
//		startTime = System.nanoTime();
//		Map<String, Double> totalByAccountIdParallel = eventos.parallelStream().collect(Collectors.groupingBy(AccountingJournalEntryCreatedEvent::getAccountId, Collectors.summingDouble(AccountingJournalEntryCreatedEvent::getMovement)));
//		endTime = (System.nanoTime()-startTime) / 1e6;
//		System.out.println("Apenas soma, Com multithread: " + endTime);
//
//		startTime = System.nanoTime();
//		eventos
//				.parallelStream()
//				.collect(groupingBy(AccountingJournalEntryCreatedEvent::getAccountId, collectingAndThen(toList(), list -> {
//					LongSummaryStatistics offsetSummary = list.stream().collect(summarizingLong(AccountingJournalEntryCreatedEvent::getOffset));
//					Double totalMovement = list.stream().collect(summingDouble(AccountingJournalEntryCreatedEvent::getMovement));
//					return new AccountingJournalEntryCreatedStats(offsetSummary, totalMovement);
//				})));
//		endTime = (System.nanoTime()-startTime) / 1e6;
//		System.out.println("Soma e Max, Com multithread 1: " + endTime);
//
//		startTime = System.nanoTime();
//		eventos
//				.parallelStream()
//				.collect(groupingBy(AccountingJournalEntryCreatedEvent::getAccountId, collectingAndThen(toList(), list -> {
//					LongSummaryStatistics offsetSummary = list.parallelStream().collect(summarizingLong(AccountingJournalEntryCreatedEvent::getOffset));
//					Double totalMovement = list.parallelStream().collect(summingDouble(AccountingJournalEntryCreatedEvent::getMovement));
//					return new AccountingJournalEntryCreatedStats(offsetSummary, totalMovement);
//				})));
//		endTime = (System.nanoTime()-startTime) / 1e6;
//		System.out.println("Soma e Max, Com multithread 1: " + endTime);

	}

}
