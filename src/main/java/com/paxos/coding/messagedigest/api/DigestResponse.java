package com.paxos.coding.messagedigest.api;

/**
 * DTO representing a response containing a hexadecimal digest.
 *
 */
public class DigestResponse {

    private String digest;

    public DigestResponse(String digest) {
        this.digest = digest;
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }
}
