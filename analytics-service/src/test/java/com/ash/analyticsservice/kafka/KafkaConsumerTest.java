package com.ash.analyticsservice.kafka;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import patient.events.PatientEvent;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
class KafkaConsumerTest {

    //region Generated with Explyt. Tests for KafkaConsumer

    @Autowired
    private KafkaConsumer kafkaConsumer;

    /**
     * Given the analytics service is listening to the "patient" Kafka topic with group ID "analytics-group"<br>
     * When a valid Protobuf-encoded patient event message is received containing patient identifier, full name, email address, and event type<br>
     * Then the system should successfully parse the Protobuf message<br>
     * And the system should log the message reception confirmation<br>
     * And the system should log the patient identifier from the event<br>
     * And the system should log the patient name from the event<br>
     * And the system should log the patient email address from the event<br>
     * And the system should log the event type from the event<br>
     * And no error should be logged during the processing
     */
    @Test
    void testValidPatientEventProcessing() {
        PatientEvent validEvent = PatientEvent.newBuilder()
                .setPatientId("P12345")
                .setName("John Doe")
                .setEmail("john.doe@example.com")
                .setEventType("CREATED")
                .build();
        byte[] eventBytes = validEvent.toByteArray();

        assertDoesNotThrow(() -> kafkaConsumer.consumeEvent(eventBytes));
    }

    /**
     * Given the analytics service is listening to the "patient" Kafka topic with group ID "analytics-group"<br>
     * When a corrupted or malformed byte array that cannot be parsed as a PatientEvent is received from the Kafka topic<br>
     * Then the system should attempt to parse the message as a Protobuf PatientEvent<br>
     * And the parsing should fail with an InvalidProtocolBufferException<br>
     * And the system should log an error message indicating failure to parse the Protobuf message<br>
     * And the system should include the exception details in the error log<br>
     * And the consumer should continue running without terminating
     */
    @Test
    void testMalformedProtobufMessageHandling() {
        byte[] malformedBytes = new byte[]{0x00, 0x01, 0x02, (byte) 0xFF, (byte) 0xFE};

        assertDoesNotThrow(() -> kafkaConsumer.consumeEvent(malformedBytes));
    }

    /**
     * Given the analytics service is listening to the "patient" Kafka topic with group ID "analytics-group"<br>
     * When a valid Protobuf-encoded patient event message is received with empty strings for patient identifier, name, email, and event type fields<br>
     * Then the system should successfully parse the Protobuf message without throwing exceptions<br>
     * And the system should log the message reception confirmation<br>
     * And the system should log empty values for patient identifier, name, email, and event type<br>
     * And no InvalidProtocolBufferException should be thrown<br>
     * And the consumer should continue processing subsequent messages
     */
    @Test
    void testPatientEventWithEmptyFields() {
        PatientEvent emptyFieldsEvent = PatientEvent.newBuilder()
                .setPatientId("")
                .setName("")
                .setEmail("")
                .setEventType("")
                .build();
        byte[] eventBytes = emptyFieldsEvent.toByteArray();

        assertDoesNotThrow(() -> kafkaConsumer.consumeEvent(eventBytes));
    }

    /**
     * Given the analytics service is listening to the "patient" Kafka topic with group ID "analytics-group"<br>
     * When multiple valid Protobuf-encoded patient event messages are received in sequence from the Kafka topic<br>
     * And each message contains distinct patient information with different identifiers, names, emails, and event types<br>
     * Then the system should successfully parse each Protobuf message in the order received<br>
     * And the system should log the reception confirmation for each message<br>
     * And the system should log all patient details for each event separately<br>
     * And the system should maintain the correct sequence of message processing<br>
     * And no messages should be skipped or processed out of order
     */
    @Test
    void testMultipleConsecutivePatientEvents() {
        PatientEvent event1 = PatientEvent.newBuilder()
                .setPatientId("P001")
                .setName("Alice Smith")
                .setEmail("alice.smith@example.com")
                .setEventType("CREATED")
                .build();
        PatientEvent event2 = PatientEvent.newBuilder()
                .setPatientId("P002")
                .setName("Bob Johnson")
                .setEmail("bob.johnson@example.com")
                .setEventType("UPDATED")
                .build();
        PatientEvent event3 = PatientEvent.newBuilder()
                .setPatientId("P003")
                .setName("Charlie Brown")
                .setEmail("charlie.brown@example.com")
                .setEventType("DELETED")
                .build();

        assertDoesNotThrow(() -> {
            kafkaConsumer.consumeEvent(event1.toByteArray());
            kafkaConsumer.consumeEvent(event2.toByteArray());
            kafkaConsumer.consumeEvent(event3.toByteArray());
        });
    }

    /**
     * Given the analytics service is listening to the "patient" Kafka topic with group ID "analytics-group"<br>
     * When a valid Protobuf-encoded patient event message is received containing special characters, Unicode characters, and international text in the name and email fields<br>
     * Then the system should successfully parse the Protobuf message using UTF-8 encoding<br>
     * And the system should log the message reception confirmation<br>
     * And the system should correctly log the patient identifier without encoding issues<br>
     * And the system should correctly log the patient name preserving all special and international characters<br>
     * And the system should correctly log the email address with special characters intact<br>
     * And the system should correctly log the event type<br>
     * And no character encoding errors should occur during processing
     */
    @Test
    void testPatientEventWithSpecialCharacters() {
        PatientEvent specialCharsEvent = PatientEvent.newBuilder()
                .setPatientId("P-特殊-123")
                .setName("José García-Müller")
                .setEmail("josé.garcía@例え.com")
                .setEventType("CRÉÉ")
                .build();
        byte[] eventBytes = specialCharsEvent.toByteArray();

        assertDoesNotThrow(() -> kafkaConsumer.consumeEvent(eventBytes));
    }

    //endregion

}
