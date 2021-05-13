package tournamentmanager.core.api;

import java.util.List;

public interface Tournament {

    void addParticipant(Participant participant);
    List<Participant> previewPreliminaryRanking();
    void start();
    void end();
    List<Game> getAllGames();
    List<Game> getInitialGames();
    List<Game> getGamesWaitingToStart();
    List<Game> getFinishedGames();
    List<Game> getGamesInProgress();
    List<Game> getNotStartedGames();
    List<Participant> getFinalRanking();


}
