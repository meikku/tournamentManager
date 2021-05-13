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
    List<Game> getGamesReadyToStart() throws TournamentException;
    List<Game> getFinishedGames() throws TournamentException;
    List<Game> getGamesInProgress() throws TournamentException;
    List<Game> getFutureGames() throws TournamentException;
    List<Set<Participant>>getFinalRanking() throws TournamentException;
    Status getStatus();


}
