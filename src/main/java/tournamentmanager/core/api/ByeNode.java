package tournamentmanager.core.api;

/**
 * Represents a bye node in the tournament tree.
 * <p>
 * A ByeNode means that a bye is used by a participant in the first round, which means the participant
 * is automatically able to go to the next round without playing.
 * <p>
 * A ByeNode can only be found in the first round of a Tournament.
 * Therefore it cannot have previous nodes, and only have following nodes, which
 * can only be GameNodes.
 * <p>
 * Byes are required for all tournaments that use a number of players that is not a power of two.
 */
public interface ByeNode extends TournamentNode {

    /**
     * Set the participant that uses this bye.
     *
     * @param participant The participant that uses this bye.
     */
    void setParticipant(Participant participant);

    /**
     * Retrieve the participant that uses this bye.
     *
     * @return The participant that uses this bye.
     */
    Participant getParticipant();
}
