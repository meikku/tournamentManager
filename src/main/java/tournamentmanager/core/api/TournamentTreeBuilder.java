package tournamentmanager.core.api;

import java.util.List;

public interface TournamentTreeBuilder {

    List<List<TournamentNode>> buildAllRounds(List<Participant> rankedParticipants) throws TournamentException;

    List<TournamentNode> buildInitialRound(List<Participant> rankedParticipants) throws TournamentException;

    List<Game> buildNextRound(List<? extends TournamentNode> games) throws TournamentException;


}
