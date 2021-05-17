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
        List<Participant> flattenedRankedList = new ArrayList<>();
        for (Set<Participant> partition : rankedParticipants) {
            List<Participant> randomizedPartition = new ArrayList<>(partition);
            Collections.shuffle(randomizedPartition);
            flattenedRankedList.addAll(randomizedPartition);
        }

        // Use a CustomList to alternatively pick high ranked participants / low ranked participants
        // CustomList provides a takeNext() operation that alternates between taking an element
        // from the start and from the end of the list.
        CustomList<Participant> remainingRankedParticipants = new CustomList<>(flattenedRankedList);
        List<TournamentNode> initialRound = new ArrayList<>();
        try {
            int tournamentSize = Util.findNextPowerOfTwo(flattenedRankedList.size());
            int amountOfInitialNodes = tournamentSize / 2;
            int remainingByes = tournamentSize - flattenedRankedList.size();
            for (int i = 0; i < amountOfInitialNodes; i++) {
                TournamentNode node;
                if (remainingByes > 0) {
                    ByeNode bye = new ByeNodeImpl();
                    bye.setParticipant(remainingRankedParticipants.takeNext());
                    node = bye;
                    remainingByes--;
                } else {
                    GameNode game = new GameNodeImpl();
                    game.addParticipant(remainingRankedParticipants.takeNext());
                    game.addParticipant(remainingRankedParticipants.takeNext());
                    node = game;
                }

                // Add the new node in the middle of the round, to make sure the best players are
                // put as apart as possible from each other.
                initialRound.add((initialRound.size() / 2), node);
            }
            if (remainingRankedParticipants.size() > 0) {
                throw new RuntimeException("INTERNAL ERROR: there are participants remaining! This should never happen.");
            }
            return initialRound;
        } catch (Exception e) {
            throw new TournamentException("Too many players, cannot start the tournament.");
        }
    }

    @Override
    public List<GameNode> buildNextRound(List<? extends TournamentNode> previousRound) {
        List<GameNode> nextRound = new ArrayList<>();
        for (int i = 0; i < previousRound.size() - 1; i = i + 2) {
            TournamentNode nodeA = previousRound.get(i);
            TournamentNode nodeB = previousRound.get(i + 1);
            GameNode newGame = new GameNodeImpl();
            try {
                newGame.addPreviousNode(nodeA);
                newGame.addPreviousNode(nodeB);
            } catch (TournamentException e) {
                throw new RuntimeException("INTERNAL ERROR: cannot add previous nodes to a new GameNode, should never happen!", e);
            }
            nodeA.setFollowingGame(newGame);
            nodeB.setFollowingGame(newGame);
            try {
                if (nodeA instanceof ByeNode) {
                    newGame.addParticipant(((ByeNode) nodeA).getParticipant());
                }
                if (nodeB instanceof ByeNode) {
                    newGame.addParticipant(((ByeNode) nodeB).getParticipant());
                }
            } catch (TournamentException e) {
                throw new RuntimeException("INTERNAL ERROR: cannot add participants to a new GameNode, should never happen!", e);
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
