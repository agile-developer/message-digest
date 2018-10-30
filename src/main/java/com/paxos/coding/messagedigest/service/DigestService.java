package com.paxos.coding.messagedigest.service;

import com.paxos.coding.messagedigest.repository.DigestRepository;
import com.paxos.coding.messagedigest.util.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotBlank;
import java.util.Optional;

@Service
public class DigestService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DigestService.class);

    private final DigestRepository digestRepository;

    public DigestService(DigestRepository digestRepository) {
        this.digestRepository = digestRepository;
    }

    public Optional<String> calculateDigest(@NotBlank String message) {

        if (DigestUtils.IS_BLANK.test(message)) {
            LOGGER.warn("Could not calculate digest as 'message' is blank.");

            return Optional.empty();
        }

        Optional<String> storedDigestOpt = digestRepository.fetchDigestForMessage(message);
        if (storedDigestOpt.isPresent()) {
            LOGGER.debug("Mapping for message: {} to digest: {} found in repository.", message, storedDigestOpt);

            return storedDigestOpt;
        }

        LOGGER.debug("Calculating digest for message: {}.", message);
        DigestUtils.DigestResult digestResult = DigestUtils.SHA_256.apply(message);
        Exception e = digestResult.getException();
        if (e != null) {
            LOGGER.error("Couuld not calculate digest due to exception: {}.", e.getMessage());
        }

        Optional<String> digestOpt = Optional.ofNullable(digestResult.getDigest());
        digestOpt.ifPresent(hexDigest -> {
            LOGGER.debug("Storing mapping for message: {} to digest: {}.", message, hexDigest);
            digestRepository.saveMappings(message, hexDigest);
        });

        return digestOpt;
    }

    public Optional<String> retrieveMessageForDigest(@NotBlank String digest) {

        if (DigestUtils.IS_BLANK.test(digest)) {
            LOGGER.warn("Could not find message as value of digest is blank.");

            return Optional.empty();
        }

        Optional<String> messageOpt = digestRepository.fetchMessageForDigest(digest);
        messageOpt.ifPresent(s -> LOGGER.debug("Mapping for digest: {} to message: {} found in repository.", digest, s));

        return messageOpt;
    }
}
