package tournamentmanager.core.impl;

import tournamentmanager.core.api.*;
import tournamentmanager.util.Util;

import java.util.ArrayList;
import java.util.List;

public class TournamentTreeBuilderImpl implements TournamentTreeBuilder {


    @Override
    public List<List<TournamentNode>> buildAllRounds(List<Participant> rankedParticipants) throws TournamentException {
        List<List<TournamentNode>> rounds = new ArrayList<>();
        List<TournamentNode> nextRound = buildInitialRound(rankedParticipants);
        while (!nextRound.isEmpty()) {
            rounds.add(nextRound);
            nextRound.clear();
            nextRound.addAll(buildNextRound(nextRound));
        }
        return rounds;
    }

    @Override
    public List<TournamentNode> buildInitialRound(List<Participant> rankedParticipants) throws TournamentException {
        RankedParticipantsList remainingRankedParticipants = new RankedParticipantsListImpl(rankedParticipants);
        List<TournamentNode> initialGames = new ArrayList<>();
        try {
            int amountOfInitialNodes = Util.findNextPowerOfTwo(rankedParticipants.size());
            int byes = amountOfInitialNodes - rankedParticipants.size();
            for (int i = 0; i < amountOfInitialNodes; i++) {
                TournamentNode node;
                if (byes > 0) {
                    Bye bye = new ByeImpl();
                    bye.setParticipant(remainingRankedParticipants.takeNext());
                    node = bye;
                    byes--;
                } else {
                    Game game = new GameImpl();
                    game.addParticipant(remainingRankedParticipants.takeNext());
                    game.addParticipant(remainingRankedParticipants.takeNext());
                    node = game;
                }
                initialGames.add(node);
            }
            return initialGames;
        } catch (Exception e) {
            throw new TournamentException("Too many players, cannot start the tournament.");
        }
    }

    @Override
    public List<Game> buildNextRound(List<? extends TournamentNode> round) throws TournamentException {
        List<Game> nextRound = new ArrayList<>();
        for (int i = 0; i < round.size() - 1; i = i + 2) {
            TournamentNode nodeA = round.get(i);
            TournamentNode nodeB = round.get(i + 1);
            Game newGame = new GameImpl();
            newGame.setPreviousNode1(nodeA);
            newGame.setPreviousNode2(nodeB);
            nodeA.setFollowingGame(newGame);
            nodeB.setFollowingGame(newGame);
            if (nodeA instanceof Bye) {
                newGame.addParticipant(((Bye) nodeA).getParticipant());
            }
            if (nodeB instanceof Bye) {
                newGame.addParticipant(((Bye) nodeB).getParticipant());
            }
            nextRound.add(newGame);
        }
        return nextRound;
    }
}
