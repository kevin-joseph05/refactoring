package theater;

/**
 * Represents a performance of a play with audience information.
 */
public class Performance {

    private String playID;
    private int audience;

    /**
     * Creates a new Performance object.
     * @param playID the play ID
     * @param audience the audience size
     */
    public Performance(String playID, int audience) {
        this.playID = playID;
        this.audience = audience;
    }

    public String getPlayID() {
        return playID;
    }

    public int getAudience() {
        return audience;
    }
}
