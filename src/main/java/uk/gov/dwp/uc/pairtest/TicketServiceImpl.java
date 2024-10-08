package uk.gov.dwp.uc.pairtest;

import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.paymentgateway.TicketPaymentServiceImpl;
import thirdparty.seatbooking.SeatReservationService;
import thirdparty.seatbooking.SeatReservationServiceImpl;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class TicketServiceImpl implements TicketService {

    private static final Log logger = LogFactory.getLog(TicketServiceImpl.class);

    private static final int ADULT_PRICE = 25;
    private static final int CHILD_PRICE = 15;
    private static final int INFANT_PRICE = 0;
    private static final int MAXIMUM_TICKETS = 25;

    private final TicketPaymentService tps;
    private final SeatReservationService srs;

    private final Map<String, Integer> results = new HashMap<String, Integer>();


    public TicketServiceImpl(TicketPaymentService tps, SeatReservationService srs) {
        this.tps = tps;
        this.srs = srs;
    }


    @Override
    public Map<String, Integer> purchaseTickets(Long accountId, TicketTypeRequest... ticketTypeRequests) throws InvalidPurchaseException {

        validateAccountId(accountId);
        
        int totalPrice = 0;
        int totalTickets = 0;
        int totalSeats = 0;
        int adultTickets = 0;

        for (TicketTypeRequest tts : ticketTypeRequests){
            logger.info(tts);
            int ticketCount = tts.getNoOfTickets();
            switch (tts.getTicketType()){
                case ADULT:
                    totalPrice += ADULT_PRICE * ticketCount;
                    totalTickets += ticketCount;
                    totalSeats += ticketCount;
                    adultTickets += ticketCount;
                    break;
                case CHILD:
                    totalPrice += CHILD_PRICE * ticketCount;
                    totalTickets += ticketCount;
                    totalSeats += ticketCount;
                    break;
                case INFANT:
                    totalTickets += ticketCount;
                    break;
                default:
                    logger.error("Invalid ticket type");
            }
        }
        logger.info("Total Price : " + totalPrice);
        logger.info("Total Tickets : " + totalTickets);
        logger.info("Total Seats : " + totalSeats);

        validateticketCounts(totalTickets, adultTickets);

        tps.makePayment(accountId, totalPrice);

        srs.reserveSeat(accountId, totalSeats);

        results.put("totalPrice", totalPrice);
        results.put("totalTickets", totalTickets);
        results.put("totalSeats", totalSeats);

        return results;
    }

    private void validateAccountId(Long accountId) {
        if (accountId == null || accountId <= 0) {
            throw new InvalidPurchaseException("Invalid account ID, zero or negative", 1);
        }
    }

    private void validateticketCounts(int totalTickets, int adultTickets) {
        if (totalTickets > MAXIMUM_TICKETS) {
            throw new InvalidPurchaseException("Exceeded maximum number of tickets to be purchased at once", 2);
        }
        if (adultTickets == 0){
            throw new InvalidPurchaseException("At least one Adult ticket is required for Child or Infant tickets", 3);
        }
    }
}
