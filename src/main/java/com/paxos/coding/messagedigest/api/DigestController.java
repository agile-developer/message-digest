package com.paxos.coding.messagedigest.api;

import com.paxos.coding.messagedigest.service.DigestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * Controller class that provides our API endpoints.
 *
 */
@RestController
@RequestMapping(value = "/messages")
public class DigestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DigestController.class);

    private final DigestService digestService;

    public DigestController(DigestService digestService) {

        this.digestService = digestService;
    }

    /**
     * Extract the message from the request-body and invoke {@link DigestService} to generate a digest.
     *
     * @param request request containing message to be converted to a SHA-256 digest.
     * @return response containing a hexadecimal string digest for {@code request}
     */
    @PostMapping
    public ResponseEntity<DigestResponse> calculateDigest(@RequestBody Message request) {

        String message = request.getMessage();
        LOGGER.debug("Received request to calculate digest for message: {}.", message);
        Optional<String> digestOpt = digestService.calculateDigest(message);
        DigestResponse digestResponse = digestOpt.map(DigestResponse::new).orElse(null);
        if (digestResponse == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return ResponseEntity.ok(digestResponse);
    }

    /**
     * Extract hexadecimal digest from the request-body and invoke {@link DigestService} to map digest to its original message.
     * If the digest is unknown to this service, return an 'HTTP 404 - Not Found' response.
     *
     * @param digest hexadecimal string digest for a previously submitted message.
     * @return original string message for the given {@code digest}.
     */
    @GetMapping(value = "/{digest}")
    public ResponseEntity<? extends Message> retrieveMessageForDigest(@PathVariable String digest) {

        LOGGER.debug("Received request to retrieve message for digest: {}.", digest);
        Optional<String> messageOpt = digestService.retrieveMessageForDigest(digest);
        Message message = messageOpt.map(Message::new).orElse(null);
        if (message == null) {
            return new ResponseEntity<>(new Message.ErrorResponse("Message not found."), HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(message);
    }
}
