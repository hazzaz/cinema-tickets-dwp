package uk.gov.dwp.uc.pairtest.domain;

/**
 * Immutable Object
 */

public final class TicketTypeRequest implements Cloneable{

    private final int noOfTickets;
    private final Type type;

    public TicketTypeRequest(Type type, int noOfTickets) {
        this.type = type;
        this.noOfTickets = noOfTickets;
    }

    public int getNoOfTickets() {
        return noOfTickets;
    }

    public Type getTicketType() {
        return type;
    }

    public enum Type {
        ADULT, CHILD , INFANT
    }

    @Override
    public String toString() {
        return noOfTickets + " " + type;
    }

}
