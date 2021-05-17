package tournamentmanager.core.api;

import java.util.List;
import java.util.Set;


/**
 * Set of services to build the Tournament tree from a ranked list of participants.
 * <p>
 * A Tournament tree is composed of a list of rounds, each round composed of a list of nodes.
 * <p>
 * Except for the final GameNode, each node is connected to a node of the next round. When a participant wins in a node, he or she goes to the following node in the next round.
 * <p>
 * There are two types of nodes:
 * - The ByeNode is a special node that only exists in the first round. It contains only one
 * participant, and this participant automatically "wins" and goes to the next round.
 * A ByeNode has no previous node.
 * - The GameNode is a normal node that contains two players. Only the winner goes to the
 * following node in the next round. A GameNode may have previous nodes and following nodes.
 * <p>
 * If the amount of players in the tournament is not a power of two (2, 4, 8, 16, 32, etc.), then ByeNodes are be created to ensure that that the number of nodes in the first round
 * is equal to a power of two (1, 2, 4, 8, 16, etc.).
 */
public interface TournamentTreeBuilder {

    /**
     * Build a complete Tournament tree for all rounds of the Tournament, using a ranked list of participants.
     * <p>
     * The input ranked list must be an ordered partition, ie. a list where each item is a set of Participants that have a "similar level".
     * This ordered partition is typically based on the seeds given to the players, and must reflect the differences between players.
     * <p>
     * A Tournament tree is simply a list of rounds. Since a round is a list of
     * TournamentNodes, a Tournament tree is a list of lists of TournamentNodes.
     * <p>
     * See buildInitialRound() regarding how the first round is built, and
     * buildNextRound() regarding how all other round is built.
     *
     * @param participants
     * @return
     * @throws TournamentException
     */
    List<List<GameNode>> buildAllRounds(List<Participant> participants) throws TournamentException;

    /**
     * Build the first round of a tournament tree. This first round is special because it is initially filled with all participants.
     * <p>
     * The input ranked list must be an ordered partition, ie. a list where each item is a set of Participants that have a "similar level".
     * This ordered partition is typically based on the seeds given to the players, and must reflect the differences between players.
     * Using this ranking, buildInitialRound() makes sure that the best players (according to the seeds) will be "spread" as much as possible in the initial round, and will thus only compete in the last rounds.
     * <p>
     * Because a single-elimination tournament relies on a binary tree, and because the
     * amount of leaf nodes in a binary tree is always a power of two, the first round of
     * a tournament tree *must* contain a number of nodes equal to a number of two. However,
     * it is possible that the amount of participants is not a power of two. In that case,
     * the size of the tournament is the lowest power of two higher than the amount of
     * participants (eg. if there are 9 participants, the size of the tournament must be 16).
     * <p>
     * To manage the different tournament sizes, there are two cases:
     * - If the number of participants is already a power of two (2, 4, 8, 16, 32), then
     * there is nothing to do, and the first round will only contains GameNodes
     * in a number equal to participants divided by two.
     * - If the number of participants is not a power of two, then the first round must
     * also include a number of ByeNodes to reach the correct tournament size.
     * A ByeNode means that the participant benefits from a bye, and can thus automatically
     * go to the next round without competing.
     * The number of ByeNodes to create is equal to: tournament size − number of participants
     * (eg. if there are 9 participants, the size of the tournament, must be 16, and 7
     * ByeNodes must be created in the initial round).
     *
     * @param participants The ordered partition with all participants.
     * @return The initial round of the tournament tree.
     * @throws TournamentException If the amount of players is so high that the resulting
     * tournament size is higher than Integer.MAX_VALUE.
     */
    List<GameNode> buildInitialRound(List<Participant> participants) throws TournamentException;

    /**
     * Given an already created round, build the next round of a tournament tree.
     *
     * Each node of this next round is connected to two nodes from the previous round. If
     * one of these previous nodes is a ByeNode, then the participant of this ByeNode is
     * automatically added as a participant in the newly created node.
     *
     * @param previousRound The round that precedes this newly created round.
     * @return The round that follows the given previous round.
     */
    List<GameNode> buildNextRound(List<? extends GameNode> previousRound);


}
