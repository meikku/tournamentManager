package tournamentmanager.core.api;

import java.util.List;
import java.util.Set;

public interface TournamentTreeBuilder {

    List<List<TournamentNode>> buildAllRounds(List<Set<Participant>> rankedParticipants) throws TournamentException;

    List<TournamentNode> buildInitialRound(List<Set<Participant>> rankedParticipants) throws TournamentException;

    List<GameNode> buildNextRound(List<? extends TournamentNode> games) throws TournamentException;


}
