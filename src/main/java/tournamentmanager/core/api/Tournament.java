package tournamentmanager.core.api;

import java.util.List;
import java.util.Set;

/**
 * Represents a single-elimination tournament, and can be used to manage both participants and games.
 *
 * A Tournament is a sequence of Rounds, each round containing a set of Games, each Game eliminating one participant.
 *
 * A Tournament is managed in three phases (based on the values defined in the Status enumeration):
 * - First, participants must be added to the Tournament. A participant may have a seed (see next part), which
 * will influence how the tournament tree is organized.
 * - Second, the Tournament can be started, which creates the game tree, and make it possible to register results.
 * When a game is over, the participant is automatically sent to the next game.
 * - Third, the Tournament can be ended, and the final ranking can be computed.
 *
 * If the number of players is not a power of 2, then Participants with the highest seeds are automatically
 * given *byes* which allow them to automatically go to the second round.
 * In a round, this is represented by a Bye object, which automatically brings the player to the next Round.
 * See TournamentTreeBuilder for more information.
 *
 * A Tournament can rely on *seeds* assigned to participants to create a more fair competition.
 * If seeds are assigned to participants, a preliminary ranking is computed and used to make sure high-ranked
 * participants will not play against each other too early in the tournament.
 *
 */
public interface Tournament {


    /**
     * Add a new participant to the Tournament.
     * Can only be called before the Tournament has started.
     *
     * @param participant The Participant to add.
     * @throws TournamentException If the Tournament has already started.
     */
    void addParticipant(Participant participant) throws TournamentException;

    /**
     * Start the tournament.
     * This will create the complete tournament tree and assign participants to their first games.
     *
     * Can only be called if the Tournament has not started, and cannot be undone.
     *
     * @throws TournamentException If the tournament has already started.
     */
    void start() throws TournamentException;

    /**
     * End the tournament.
     * Once a tournament has ended, no new results can be registered.
     *
     * Can only be called if the Tournament has already started, and if all games have ended.
     *
     * @throws TournamentException If the tournament has not started, or if some games have not ended yet.
     */
    void end() throws TournamentException;

    /**
     * Retrieve all the nodes of the Tournament tree.
     * @return All the nodes of the Tournament tree.
     */
    List<TournamentNode> getAllNodes();

    List<GameNode> getAllGames();

    /**
     * Retrieve all rounds of the Tournament. A round is simply a list of tournament nodes that take place in parallel.
     * Only in the first round, a node can be either a ByeNode (which means a participant will automatically "win" this
     * round, or a GameNode (which is a normal confrontation between 2 participants).
     * In all other rounds, there are only GameNodes.
     * @return The list of rounds.
     */
    List<List<TournamentNode>> getRounds();

    /**
     * Retrieve all GameNodes of the tournament tree that are ready to start.
     * A GameNode is ready to start if it has two participants and if it has neither started or ended.
     * @return All GameNodes of the tournament tree that are ready to start.
     */
    List<GameNode> getGamesReadyToStart();

    /**
     * Retrieve all GameNodes of the tournament tree  that are finished.
     * @returnAll All GameNodes of the tournament tree that are finished.
     */
    List<GameNode> getFinishedGames();

    /**
     * Retrieve all games of the tournament tree that are in progress.
     * @return All games of the tournament tree that are in progress.
     */
    List<GameNode> getGamesInProgress();

    /**
     * Retrieve all games of the tournament tree that are not ready to start and that will be played in the future.
     * @return All future games of the tournament tree.
     */
    List<GameNode> getFutureGames();

    /**
     * Compute a preliminary ranking based on seeds, and return it.
     * Can be used to preview the current list of players ranked by seed,
     * or can be used to build the actual Tournament tree.
     *
     * The computed ranking is provided as an *ordered partition*, which is essentially
     * a list of sets. Each set contains one of several Participants that are ex-æquo.
     * The list of set gives the ranking among sets of Participants.
     *
     * Two participants are ex-æquo if their seeds are equal, or if neither has a seed.
     *
     * Can only be called before the Tournament has started.
     *
     * @return The preliminary ranking based on seeds.
     */
    List<Set<Participant>> computePreliminaryRanking();

    /**
     * Compute the final ranking based on the Tournament results, and return it.
     *
     * The computed ranking is provided as an *ordered partition*, which is essentially
     * a list of sets. Each set contains one of several Participants that are ex-æquo.
     * The list of set gives the ranking among sets of Participants.
     *
     * Two participants are ex-æquo if they were eliminated in the same Round.
     *
     * Can only be called after the Tournament has ended.
     *
     * @return The final ranking.
     */
    List<Set<Participant>> computeFinalRanking() throws TournamentException;

    /**
     * Retrieves the Status of the Tournament.
     * It can either be:
     * - "not started" when adding players and setting seeds,
     * - "in progress" when playing games and registering results,
     * - "finished" when no more games can be played and the final ranking can be computed.
     * @return The current Status of the Tournament.
     */
    Status getStatus();


}
