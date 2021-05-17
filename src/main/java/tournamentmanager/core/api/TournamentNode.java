package tournamentmanager.core.api;

import java.util.Optional;

/**
 * Represents a node in the tournament tree.
 * <p>
 * A node is part of a round, and can be connected to a GameNode located in the next round (called "following node").
 * <p>
 * A node contains one or several participants, one of whom will progress to the following node.
 */
public interface TournamentNode {

    /**
     * Retrieve the winner of the TournamentNode.
     *
     * Must be called when there is a winner (for instance if a game is finished).
     *
     * @return The winner of this node.
     * @throws TournamentException If there is no winner yet.
     */
    Participant getWinner() throws TournamentException;

    /**
     * Retrieve the loser of the TournamentNode, if any.
     * If there simply is no loser, returns an empty Optional.
     *
     * Must be called when there is a loser (for instance if a game is finished).
     *
     * @return The loser of this node.
     * @throws TournamentException If we do not know yet if there is a loser.
     */
    Optional<Participant> getLoser() throws TournamentException;

    /**
     * Retrieve the following GameNode, which is the GameNode where the winner of this node will go.
     * If this node is the final game, then returns an empty Optional.
     * @return
     */
    Optional<GameNode> getFollowingGame();

    /**
     * Sets the following node to a specific GameNode.
     * @param gameNode The gameNode to set as following node.
     * @throws IllegalArgumentException If the provided gameNode is null.
     */
    void setFollowingGame(GameNode gameNode) throws IllegalArgumentException;

}
