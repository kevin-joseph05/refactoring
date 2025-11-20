package theater;

/**
 * Represents a performance of a play with audience information.
 */
public class Play {

    private String name;
    private String type;

    /**
     * Creates a new Performance object.
     * @param name the play ID
     * @param type the audience size
     */
    public Play(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return this.name;
    }

    public String getType() {
        return type;
    }
}
