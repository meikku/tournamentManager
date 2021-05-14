package tournamentmanager.core.api;

import java.util.Optional;

public interface Game {

    Optional<Participant> getParticipant1();
    Optional<Participant> getParticipant2();
    Optional<Participant> getCurrentWinner();
    Optional<Participant> getCurrentLoser();
    Status getStatus();
    Optional<Game> getFollowingGame();
    Optional<Game> getPreviousGame1();
    Optional<Game> getPreviousGame2();
    void addParticipant(Participant participant) throws TournamentException;
    void setFollowingGame(Game game);
    void setPreviousGame1(Game game);
    void setPreviousGame2(Game game);
    void changeStatus(Status status) throws TournamentException;
    void registerForfeit(Participant participant) throws TournamentException;
    void addPoints(Participant participant, int points) throws TournamentException;

}
