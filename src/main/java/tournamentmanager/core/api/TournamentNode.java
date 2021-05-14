package tournamentmanager.core.api;

import java.util.List;
import java.util.Optional;

public interface TournamentNode {

    Participant getWinner() throws TournamentException;

    Optional<Participant> getLoser() throws TournamentException;

    Optional<Game> getFollowingGame();

    void setFollowingGame(Game game);

}
