package tournamentmanager.core.api;

/**
 * Represents a Participant to the Tournament.
 * <p>
 * A Participant has a mandatory, name and an optionnal seed.
 * A seed is a value used to rank the participants before the Tournament,
 * in order to create a more fair tournament tree. The higher the seed,
 * the "better" is considered the player.
 * <p>
 * The default seed for a Participant is zero.
 * <p>
 * A Participant can is eliminated, which means she or he has lost a game
 * and cannot play in the Tournament anymore.
 */
public interface Participant {

    /**
     * Retrieve the name of the Player.
     *
     * @return The name of the Player.
     */
    String getName();

    /**
     * Retrieve the seed of the Player.
     *
     * @return The seed of the Player, or zero if no seed was given.
     */
    int getSeed();

    /**
     * Retrieve whether the Player was eliminated from the Tournament (ie. has lost a game).
     * If true, then the player cannot play anymore.
     *
     * @return True if the the Player was eliminated from the Tournament, false otherwise.
     */
    boolean isEliminated();

    /**
     * Sets the player as eliminated.
     * Must be called when a player loses a game.
     */
    void eliminate();

    /**
     * Sets the seed of the Participant.
     *
     * @param value The seed value.
     */
    void setSeed(int value);

    /**
     * Sets the name of the Participant.
     *
     * @param name The name.
     */
    void setName(String name);
}
