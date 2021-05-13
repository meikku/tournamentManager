package tournamentmanager.core.api;

public interface Game {

    Participant getParticipant1();
    Participant getParticipant2();
    Participant getCurrentWinner() throws TournamentException;
    Participant getCurrentLoser() throws TournamentException;
    Status getStatus();
    Game getFollowingGame();
    Game getPreviousGame1();
    Game getPreviousGame2();
    void addParticipant(Participant participant) throws TournamentException;
    void setFollowingGame(Game game);
    void setPreviousGame1(Game game);
    void setPreviousGame2(Game game);
    void changeStatus(Status status) throws TournamentException;
    void registerForfeit(Participant participant) throws TournamentException;
    void addPoints(Participant participant, int points) throws TournamentException;

}
