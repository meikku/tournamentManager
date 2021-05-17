package tournamentmanager.core.api;

import java.util.Optional;

public interface TournamentNode {

    Participant getWinner() throws TournamentException;

    Optional<Participant> getLoser() throws TournamentException;

    Optional<GameNode> getFollowingGame();

    void setFollowingGame(GameNode game);

}
