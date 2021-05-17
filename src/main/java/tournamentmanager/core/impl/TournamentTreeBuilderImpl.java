package tournamentmanager.core.impl;

import tournamentmanager.core.api.GameNode;
import tournamentmanager.core.api.Participant;
import tournamentmanager.core.api.TournamentException;
import tournamentmanager.core.api.TournamentTreeBuilder;

import java.util.ArrayList;
import java.util.List;

public class TournamentTreeBuilderImpl implements TournamentTreeBuilder {

    @Override
    public List<List<GameNode>> buildAllRounds(List<Participant> rankedParticipants) throws TournamentException {
        List<List<GameNode>> rounds = new ArrayList<>();
        List<GameNode> nextRound = buildInitialRound(rankedParticipants);
        while (!nextRound.isEmpty()) {
            rounds.add(nextRound);
            nextRound.clear();
            nextRound.addAll(buildNextRound(nextRound));
        }
        return rounds;
    }

    @Override
    public List<GameNode> buildInitialRound(List<Participant> participants) throws TournamentException {

        List<Participant> remainingRankedParticipants = new ArrayList<>(participants);
        List<GameNode> initialRound = new ArrayList<>();
        try {
            int amountOfInitialNodes = participants.size() / 2;
            for (int i = 0; i < amountOfInitialNodes; i++) {
                GameNode game = new GameNodeImpl();
                game.addParticipant(remainingRankedParticipants.remove(0));
                game.addParticipant(remainingRankedParticipants.remove(0));

                // Add the new node in the middle of the round, to make sure the best players are
                // put as apart as possible from each other.
                initialRound.add((initialRound.size() / 2), game);
            }
            if (remainingRankedParticipants.size() > 0) {
                throw new RuntimeException("INTERNAL ERROR: there are participants remaining! This should never happen.");
            }
            return initialRound;
        } catch (Exception e) {
            throw new TournamentException("Too many players, cannot create the tournament.");
        }
    }

    @Override
    public List<GameNode> buildNextRound(List<? extends GameNode> previousRound) {
        List<GameNode> nextRound = new ArrayList<>();
        for (int i = 0; i < previousRound.size() - 1; i = i + 2) {
            GameNode nodeA = previousRound.get(i);
            GameNode nodeB = previousRound.get(i + 1);
            GameNode newGame = new GameNodeImpl();
            try {
                newGame.addPreviousNode(nodeA);
                newGame.addPreviousNode(nodeB);
            } catch (TournamentException e) {
                throw new RuntimeException("INTERNAL ERROR: cannot add previous nodes to a new GameNode, should never happen!", e);
            }
            nodeA.setFollowingGame(newGame);
            nodeB.setFollowingGame(newGame);
            nextRound.add(newGame);
        }
        return nextRound;
    }


}
