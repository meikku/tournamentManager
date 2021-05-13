package tournamentmanager.core.api;

import java.util.List;

public interface TournamentTreeBuilder {

    List<List<Game>> buildAllRounds(List<Participant> participants) throws TournamentException;

    List<Game> buildInitialRound(List<Participant> participants) throws TournamentException;

    List<Game> buildNextRound(List<Game> games);


}
