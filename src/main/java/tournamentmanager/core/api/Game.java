package tournamentmanager.core.api;

public interface Game {

    void addPoints(Participant participant, int points);
    void setStatus(GameStatus status);
    void setForfeit(Participant participant);
    Participant getParticipant1();
    Participant getParticipant2();
    Participant getWinner();
    Participant getLoser();
    GameStatus getStatus();
    Game getFollowingGame();
    Game getPreviousGame1();
    Game getPreviousGame2();

}
