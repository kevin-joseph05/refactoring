package theater;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;

/**
 * This class generates a statement for a given invoice of performances.
 */
public class StatementPrinter {
    private Invoice invoice;
    private Map<String, Play> plays;

    public StatementPrinter(Invoice invoice, Map<String, Play> plays) {
        this.invoice = invoice;
        this.plays = plays;
    }

    /**
     * Returns a formatted statement of the invoice associated with this printer.
     * @return the formatted statement
     * @throws RuntimeException if one of the play types is not known
     */
    public String statement() {
        final StringBuilder result = new StringBuilder("Statement for " + invoice.getCustomer() + System.lineSeparator());
        for (Performance performance : invoice.getPerformances()) {
            final Play play = getPlay(performance);
            final int thisAmount = getAmount(performance);
            result.append(String.format("  %s: %s (%s seats)%n", play.getName(),
                    usd(thisAmount), performance.getAudience()));
        }
        result.append(String.format("Amount owed is %s%n", usd(getTotalAmount())));
        result.append(String.format("You earned %s credits%n", getTotalVolumeCredits()));
        return result.toString();
    }

    private int getTotalVolumeCredits() {
        int result = 0;
        for (Performance performance : invoice.getPerformances()) {
            result += getVolumeCredits(performance);
        }
        return result;
    }

    private int getTotalAmount() {
        int result = 0;
        for (Performance performance : invoice.getPerformances()) {
            result += getAmount(performance);
        }
        return result;
    }

    private static String usd(int totalAmount) {
        return NumberFormat.getCurrencyInstance(Locale.US).format(totalAmount / Constants.PERCENT_FACTOR);
    }

    /**
     * Calculates the volume credits for a given performance.
     * @param performance the performance to calculate volume credits for
     * @return the calculated volume credits
     */
    public int getVolumeCredits(Performance performance) {
        final Play play = getPlay(performance);

        int result = Math.max(performance.getAudience() - Constants.BASE_VOLUME_CREDIT_THRESHOLD, 0);
        if ("comedy".equals(play.getType())) {
            result += performance.getAudience() / Constants.COMEDY_EXTRA_VOLUME_FACTOR;
        }
        return result;
    }

    /**
     * Gets the play associated with a performance.
     * @param performance the performance to get the play for
     * @return the associated play
     */
    private Play getPlay(Performance performance) {
        return plays.get(performance.getPlayID());
    }

    /**
     * Calculates the amount owed for a given performance.
     * @param performance the performance to calculate amount for
     * @return the calculated amount in cents
     * @throws RuntimeException if the play type is unknown
     */
    public int getAmount(Performance performance) {
        final Play play = getPlay(performance);

        int thisAmount = 0;
        switch (play.getType()) {
            case "tragedy":
                thisAmount = Constants.PASTORAL_BASE_AMOUNT;
                if (performance.getAudience() > Constants.TRAGEDY_AUDIENCE_THRESHOLD) {
                    thisAmount += Constants.TRAGEDY_OVER_BASE_CAPACITY_PER_PERSON
                            * (performance.getAudience() - Constants.TRAGEDY_AUDIENCE_THRESHOLD);
                }
                break;
            case "comedy":
                thisAmount = Constants.COMEDY_BASE_AMOUNT;
                if (performance.getAudience() > Constants.COMEDY_AUDIENCE_THRESHOLD) {
                    thisAmount += Constants.COMEDY_OVER_BASE_CAPACITY_AMOUNT
                            + (Constants.COMEDY_OVER_BASE_CAPACITY_PER_PERSON
                            * (performance.getAudience() - Constants.COMEDY_AUDIENCE_THRESHOLD));
                }
                thisAmount += Constants.COMEDY_AMOUNT_PER_AUDIENCE * performance.getAudience();
                break;
            default:
                throw new RuntimeException(String.format("unknown type: %s", play.getType()));
        }
        return thisAmount;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public Map<String, Play> getPlays() {
        return plays;
    }
}
