package tournamentmanager.core.api;

import java.util.List;

/**
 * Represents a game node in a Tournament tree. It can be found in any round of the Tournament.
 * <p>
 * A GameNode means that there is a confrontation between two participants.
 * <p>
 * At the beginning, a GameNode is NOTSTARTED.
 * When the confrontation between two players begins, a GameNode must be INPROGRESS.
 * Once in progress, points can be given to each player.
 * Once the confrontation is over, the GameNode must be FINISHED.
 */
public interface GameNode extends TournamentNode {

    /**
     * Give or remove points (with a negative value) to one Participant of the GameNode.
     * <p>
     * Can only be called if the game is INPROGRESS.
     *
     * @param participant The Participant to which points must be given.
     * @param points      The points to add (or  to remove, using a negative value).
     * @throws TournamentException      If the GameNode is not INPROGRESS.
     * @throws IllegalArgumentException If the given participant is either null or not part of the GameNode.
     */
    void addPoints(Participant participant, int points) throws TournamentException, IllegalArgumentException;

    /**
     * Add one previous TournamentNode to this node.
     * <p>
     * A previous node is a node from the previous round whose winner will become a participant
     * in this GameNode.
     * <p>
     * There can only be two previous nodes max for a GameNode.
     * <p>
     * Can only be used if the GameNode is NOTSTARTED.
     * Is used to initially construct the Tournament tree.
     *
     * @param tournamentNode The node to add as a previous node.
     * @throws TournamentException      If there are already two previous nodes, or if the node already part of the previous nodes, or if the game is not NOTSTARTED.
     * @throws IllegalArgumentException If the provided node is null.
     */
    void addPreviousNode(TournamentNode tournamentNode) throws IllegalArgumentException, TournamentException;

    /**
     * Add a Participant to the GameNode.
     * There can only be two participants max for a GameNode.
     *
     * @param participant The participant to add.
     * @throws TournamentException      If there are already two participants or if the participant is already part of this GameNode.
     * @throws IllegalArgumentException If the provided participant is null.
     */
    void addParticipant(Participant participant) throws TournamentException;

    /**
     * Sets the status to INPROGRESS.
     * <p>
     * Once a game has started, points can be given to players.
     *
     * @throws TournamentException If the game has already started, or if the game does not have two participants.
     */
    void start() throws TournamentException;

    /**
     * Retrieve the previous nodes of this GameNode.
     * If this GameNode is in the first round, returns an empty list.
     *
     * @return The previous nodes of this GameNode.
     */
    List<TournamentNode> getPreviousNodes();

    /**
     * Set the status to FINISHED.
     * <p>
     * Once finished, a GameNode cannot be edited anymore,
     * the winner is automatically added as a participant in the following GameNode.
     * and the loser is set to eliminated from the tournament.
     * <p>
     * Can only be called if the status is INPROGRESS, and if the scores are not ex-aequo.
     *
     * @throws TournamentException If the status is not INPROGRESS, or if the scores are ex-aequo.
     */
    void finish() throws TournamentException;

    /**
     * Retrieve the status of the GameNode.
     *
     * @return The status of the GameNode.
     */
    Status getStatus();

    /**
     * Retrieve the participants of the GameNode (max 2).
     *
     * @return The participants of the GameNode.
     */
    List<Participant> getParticipants();

}
