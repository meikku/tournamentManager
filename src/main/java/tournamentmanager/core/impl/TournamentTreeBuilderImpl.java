package tournamentmanager.core.impl;

import tournamentmanager.core.api.*;
import tournamentmanager.util.Util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class TournamentTreeBuilderImpl implements TournamentTreeBuilder {




    @Override
    public List<List<TournamentNode>> buildAllRounds(List<Set<Participant>> rankedParticipants) throws TournamentException {
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
    public List<TournamentNode> buildInitialRound(List<Set<Participant>> rankedParticipants) throws TournamentException {

        // Take the ordered partition of participants, and transform it into a regular list.
        // Each partition is randomized.
        List<Participant> flattenedList = new ArrayList<>();
        for (Set<Participant> partition : rankedParticipants) {
            List<Participant> randomizedPartition = new ArrayList<>(partition);
            Collections.shuffle(randomizedPartition);
            flattenedList.addAll(randomizedPartition);
        }

        // Use a RankedParticipantsList to
        CustomList<Participant> remainingRankedParticipants = new CustomList<Participant>(flattenedList);
        List<TournamentNode> initialGames = new ArrayList<>();
        try {
            int amountOfInitialNodes = Util.findNextPowerOfTwo(rankedParticipants.size());
            int byes = amountOfInitialNodes - rankedParticipants.size();
            for (int i = 0; i < amountOfInitialNodes; i++) {
                TournamentNode node;
                if (byes > 0) {
                    ByeNode bye = new ByeNodeImpl();
                    bye.setParticipant(remainingRankedParticipants.takeNext());
                    node = bye;
                    byes--;
                } else {
                    GameNode game = new GameNodeImpl();
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
    public List<GameNode> buildNextRound(List<? extends TournamentNode> round) throws TournamentException {
        List<GameNode> nextRound = new ArrayList<>();
        for (int i = 0; i < round.size() - 1; i = i + 2) {
            TournamentNode nodeA = round.get(i);
            TournamentNode nodeB = round.get(i + 1);
            GameNode newGame = new GameNodeImpl();
            newGame.setPreviousNode1(nodeA);
            newGame.setPreviousNode2(nodeB);
            nodeA.setFollowingGame(newGame);
            nodeB.setFollowingGame(newGame);
            if (nodeA instanceof ByeNode) {
                newGame.addParticipant(((ByeNode) nodeA).getParticipant());
            }
            if (nodeB instanceof ByeNode) {
                newGame.addParticipant(((ByeNode) nodeB).getParticipant());
            }
            nextRound.add(newGame);
        }
        return nextRound;
    }

    private class CustomList<T> extends ArrayList<T> implements List<T> {
        private boolean takeFirst = true;

        public CustomList(List<T> items) {
            super(items);
        }

        public T takeNext() {
            T item = this.takeFirst ? this.remove(0) : this.remove(this.size() - 1);
            this.takeFirst = !this.takeFirst;
            return item;
        }
    }


}
