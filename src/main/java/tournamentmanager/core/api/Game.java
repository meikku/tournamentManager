package tournamentmanager.core.api;

import java.util.List;
import java.util.Optional;

public interface Game {

    List<Participant> getParticipants();

    Optional<Participant> getWinner() throws TournamentException;

    Optional<Participant> getLoser() throws TournamentException;

    Status getStatus();

    Optional<Game> getFollowingGame();

    Optional<Game> getPreviousGame1();

    Optional<Game> getPreviousGame2();

    void addParticipant(Participant participant) throws TournamentException;

    void setFollowingGame(Game game);

    void setPreviousGame1(Game game);

    void setPreviousGame2(Game game);

    void start() throws TournamentException;

    void finish() throws TournamentException;

    void finish(boolean force) throws TournamentException;

    void registerForfeit(Participant participant) throws TournamentException;

    void addPoints(Participant participant, int points) throws TournamentException;
}
