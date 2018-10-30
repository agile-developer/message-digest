package com.paxos.coding.messagedigest.repository;

import com.paxos.coding.messagedigest.util.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class DigestRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(DigestRepository.class);

    private Map<String, String> messagesToDigests = new ConcurrentHashMap<>();
    private Map<String, String> digestsToMessages = new ConcurrentHashMap<>();

    public void saveMappings(String message, String digest) {

        LOGGER.debug("Persisting mappings between message: {} and its digest: {}.", message, digest);

        messagesToDigests.putIfAbsent(message, digest);
        digestsToMessages.putIfAbsent(digest, message);
    }

    public Optional<String> fetchMessageForDigest(String digest) {

        String message = digestsToMessages.get(digest);
        if (DigestUtils.IS_BLANK.test(message)) {
            LOGGER.info("No mapping exists for digest: {} to a message.", digest);
            return Optional.empty();
        }

        LOGGER.debug("Found mapping for digest: {} to message: {}.", digest, message);
        return Optional.of(message);
    }

    public Optional<String> fetchDigestForMessage(String message) {

        String digest = messagesToDigests.get(message);
        if (DigestUtils.IS_BLANK.test(digest)) {
            LOGGER.info("No mapping exists for message: {} to a digest.", message);
            return Optional.empty();
        }

        LOGGER.debug("Found mapping for message: {} to digest: {}.", message, digest);
        return Optional.of(digest);
    }
}
