package io.mkrzywanski.battleships.domain;

class IllegalShipPartPositionException extends RuntimeException {
    IllegalShipPartPositionException(final String message) {
        super(message);
    }
}
