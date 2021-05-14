package tournamentmanager.core.api;

import java.util.List;
import java.util.Set;

public interface Tournament {

    void addParticipant(Participant participant) throws TournamentException;

    List<Participant> computePossiblePreliminaryRanking();

    void start() throws TournamentException;

    void end() throws TournamentException;

    List<Game> getAllGames() throws TournamentException;

    List<List<Game>> getRounds() throws TournamentException;

    List<Game> getGamesReadyToStart();

    List<Game> getFinishedGames();

    List<Game> getGamesInProgress();

    List<Game> getFutureGames();

    List<Set<Participant>> computeFinalRanking() throws TournamentException;

    Status getStatus();


}
