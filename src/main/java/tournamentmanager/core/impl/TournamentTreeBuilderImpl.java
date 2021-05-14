package tournamentmanager.core.impl;

import tournamentmanager.core.api.Game;
import tournamentmanager.core.api.Participant;
import tournamentmanager.core.api.TournamentException;
import tournamentmanager.core.api.TournamentTreeBuilder;
import tournamentmanager.util.Util;

import java.util.ArrayList;
import java.util.List;

public class TournamentTreeBuilderImpl implements TournamentTreeBuilder {


    @Override
    public List<List<Game>> buildAllRounds(List<Participant> rankedParticipants) throws TournamentException {
        List<List<Game>> rounds = new ArrayList<>();
        List<Game> nextRound = buildInitialRound(rankedParticipants);
        while (!nextRound.isEmpty()) {
            rounds.add(nextRound);
            nextRound = buildNextRound(nextRound);
        }
        return rounds;
    }

    @Override
    public List<Game> buildInitialRound(List<Participant> participants) throws TournamentException {
        List<Participant> remainingParticipants = new ArrayList<>();
        remainingParticipants.addAll(participants);
        List<Game> initialGames = new ArrayList<>();
        try {
            int amountOfInitialGames = Util.findNextPowerOfTwo(participants.size());
            for (int i = 0; i < amountOfInitialGames; i++) {
                Game game = new GameImpl();
                if (!remainingParticipants.isEmpty()) {
                    Participant p1 = remainingParticipants.remove(0);
                    game.addParticipant(p1);
                }
                if (!remainingParticipants.isEmpty()) {
                    Participant p2 = remainingParticipants.remove(0);
                    game.addParticipant(p2);
                }
                initialGames.add(game);
            }
            return initialGames;
        } catch (Exception e) {
            throw new TournamentException("Too many players, cannot start the tournament.");
        }
    }

    @Override
    public List<Game> buildNextRound(List<Game> games) {
        //TODO make sure that automatic wins are registered and propagated
        List<Game> nextGames = new ArrayList<>();
        for (int i = 0; i < games.size() - 1; i = i + 2) {
            Game gameA = games.get(i);
            Game gameB = games.get(i + 1);
            Game newGame = new GameImpl();
            newGame.setPreviousGame1(gameA);
            newGame.setPreviousGame2(gameB);
            gameA.setFollowingGame(newGame);
            gameB.setFollowingGame(newGame);
            nextGames.add(newGame);
        }
        return nextGames;
    }
}
