package com.paxos.coding.messagedigest.service;

import com.paxos.coding.messagedigest.repository.DigestRepository;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DigestServiceTest {

    private DigestService digestService;

    @Before
    public void setup() {

        this.digestService = new DigestService(new DigestRepository());
    }

    @Test
    public void check_known_digest_for_message_matches_generated_digest() {

        String message = "foo";
        String expected = "2c26b46b68ffc68ff99b453c1d30413413422d706483bfa0f98a5e886266e7ae";

        String result = digestService.calculateDigest(message).orElse("");

        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void check_generated_digest_for_message_retrieves_original_message() {

        String message = "foo";
        String digest = digestService.calculateDigest(message).orElse("");

        assertThat(digest).isNotBlank();

        String retrievedMessage = digestService.retrieveMessageForDigest(digest).orElse("");

        assertThat(retrievedMessage).isEqualTo(message);
    }

    @Test
    public void check_unknown_digest_retrieves_no_result() {

        String unknownDigest = "2c26b46b68ffc68ff99b453c1d30413413422d706483bfa0f98a5e886266e7ae";
        String retrievedMessage = digestService.retrieveMessageForDigest(unknownDigest).orElse(null);

        assertThat(retrievedMessage).isNull();
    }
}
