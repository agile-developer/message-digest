package com.paxos.coding.messagedigest.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.function.Function;
import java.util.function.Predicate;

public class DigestUtils {

    private DigestUtils() {
        // Private constructor.
    }

    public static final Predicate<String> IS_BLANK = value -> (value == null) || value.trim().isEmpty();

    public static final Function<String, DigestResult> SHA_256 = message -> {
        String digest = null;
        Exception exception = null;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] digestBytes = messageDigest.digest(message.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte digestByte : digestBytes) {
                String hex = Integer.toHexString(0xff & digestByte);
                if(hex.length() == 1) sb.append('0');
                sb.append(hex);
            }
            digest = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            exception = e;
        }

        return new DigestResult(digest, exception);
    };

    public static class DigestResult {

        private String digest;
        private Exception exception;

        DigestResult(String digest, Exception exception) {

            this.digest = digest;
            this.exception = exception;
        }

        public String getDigest() {
            return digest;
        }

        public Exception getException() {
            return exception;
        }
    }
}
