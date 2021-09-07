package br.com.leonardozv.ledger.repository;

import br.com.leonardozv.ledger.config.DynamoDBConfig;
import br.com.leonardozv.ledger.models.JournalEntryCreatedStats;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class DailyLedgerMovementRepository {

    private final AmazonDynamoDB ddb;

    @Autowired
    public DailyLedgerMovementRepository(DynamoDBConfig dynamoDBConfig) {

        this.ddb = AmazonDynamoDBClientBuilder.standard()
                .withRegion(Regions.US_EAST_2)
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(dynamoDBConfig.getAccessKey(), dynamoDBConfig.getSecretKey())))
                .build();

    }

    public void upsert(Map<Object, JournalEntryCreatedStats> lista) {

        lista.forEach((key, value) -> {

            UpdateItemRequest request = new UpdateItemRequest();

            HashMap<String, AttributeValue> itemKey = new HashMap<>();
            itemKey.put("id", new AttributeValue().withS(key.toString()));

            HashMap<String, AttributeValue> itemValues = new HashMap<>();
            itemValues.put(":d", new AttributeValue().withN(value.getTotalDebits().toString()));
            itemValues.put(":c", new AttributeValue().withN(value.getTotalCredits().toString()));
            itemValues.put(":m", new AttributeValue().withN(value.getTotalMovement().toString()));
            itemValues.put(":o", new AttributeValue().withN(String.valueOf(value.getOffsetSummary().getMax())));

            try {

                this.ddb.updateItem(request
                        .withTableName("DailyLedgerMovement")
                        .withKey(itemKey)
                        .withUpdateExpression("ADD debit_amount :d, credit_amount :c, movement_amount :m SET last_commited_offset = :o")
                        .withConditionExpression("last_commited_offset < :o or attribute_not_exists(last_commited_offset)")
                        .withExpressionAttributeValues(itemValues));

            } catch (ConditionalCheckFailedException ignored) { }

        });

    }

}
