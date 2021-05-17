package tournamentmanager.core.api;

import java.util.List;
import java.util.Optional;

public interface GameNode extends TournamentNode {

    void registerForfeit(Participant participant) throws TournamentException;

    void addPoints(Participant participant, int points) throws TournamentException;

    void setPreviousNode1(TournamentNode game);

    void setPreviousNode2(TournamentNode game);

    void addParticipant(Participant participant) throws TournamentException;

    Optional<TournamentNode> getPreviousNode1();

    Optional<TournamentNode> getPreviousNode2();

    void start() throws TournamentException;

    void finish() throws TournamentException;

    Status getStatus();

    List<Participant> getParticipants();

}
