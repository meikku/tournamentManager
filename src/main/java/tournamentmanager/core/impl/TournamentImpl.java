package tournamentmanager.core.impl;

import tournamentmanager.core.api.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Basic Tournament implementation.
 */
public class TournamentImpl implements Tournament {

    private final List<Participant> participants = new ArrayList<>();
    private Status status = Status.NOTSTARTED;
    private List<List<TournamentNode>> rounds = new ArrayList<>();


    @Override
    public void addParticipant(Participant participant) throws TournamentException {
        if (this.getStatus() != Status.NOTSTARTED) {
            throw new TournamentException("Cannot add a participant to a started tournament.");
        }
        if (!this.participants.contains(participant)) {
            this.participants.add(participant);
        }
    }

    @Override
    public List<Set<Participant>> computePreliminaryRanking() {

        // Put all participants in a hashmap to group them by seed
        Map<Integer, Set<Participant>> groupBy = new HashMap<Integer, Set<Participant>>();
        for (Participant participant : this.participants) {
            if (!groupBy.containsKey(participant.getSeed())) {
                groupBy.put(participant.getSeed(), new HashSet<>());
            }
            groupBy.get(participant.getSeed()).add(participant);
        }

        // Transform the hashmap into a list, and sort the list by seed (higher seeds at the beginning of the list)
        List<Map.Entry<Integer, Set<Participant>>> entries = new ArrayList(groupBy.entrySet());
        entries.sort(Comparator.comparingInt((Map.Entry e) -> (Integer) e.getKey()));
        Collections.reverse(entries);

        // Transform the list of entries into a regular list (ie. remove the seeds)
        List<Set<Participant>> flattenedEntries = entries.stream().map(e -> e.getValue()).collect(Collectors.toList());
        return flattenedEntries;
    }

    @Override
    public void start() throws TournamentException {
        if (this.getStatus() != Status.NOTSTARTED) {
            throw new TournamentException("Cannot start a tournament that has already started.");
        }

        if (this.participants.size() < 2) {
            throw new TournamentException("A tournament requires at least two participants.");
        }

        // Build tournament tree
        TournamentTreeBuilder builder = new TournamentTreeBuilderImpl();
        List<Set<Participant>> initialRankingParticipants = this.computePreliminaryRanking();
        this.rounds = builder.buildAllRounds(initialRankingParticipants);


        // Set status
        this.status = Status.INPROGRESS;
    }

    @Override
    public void end() throws TournamentException {
        if (this.getStatus() != Status.INPROGRESS) {
            throw new TournamentException("Cannot finish a tournament that is not in progress.");
        }

        // Check that all games have ended
        for (GameNode game : this.getAllGames()) {
            if (game.getStatus() != Status.FINISHED) {
                throw new TournamentException("Cannot end a tournament that had unfinished games.");
            }
        }

        // Set status
        this.status = Status.FINISHED;

    }

    @Override
    public List<TournamentNode> getAllNodes() {
        List<TournamentNode> allGames = new ArrayList<>();
        for (List<TournamentNode> round : rounds) {
            allGames.addAll(round);
        }
        return allGames;
    }

    @Override
    public List<GameNode> getAllGames() {
        List<GameNode> allGames = new ArrayList<>();
        for (List<TournamentNode> round : rounds) {
            for (TournamentNode node : round) {
                if (node instanceof GameNode) {
                    allGames.add((GameNode) node);
                }
            }
        }
        return allGames;
    }

    @Override
    public List<List<TournamentNode>> getRounds() {
        return Collections.unmodifiableList(this.rounds);
    }

    @Override
    public List<GameNode> getGamesReadyToStart() {
        List<GameNode> result = new ArrayList<>();
        for (GameNode game : this.getAllGames()) {
            if (game.getParticipants().size() == 2 && game.getStatus() == Status.NOTSTARTED) {
                result.add(game);
            }
        }
        return result;
    }

    @Override
    public List<GameNode> getFinishedGames() {
        List<GameNode> result = new ArrayList<>();
        for (GameNode game : this.getAllGames()) {
            if (game.getStatus() == Status.FINISHED) {
                result.add(game);
            }
        }
        return result;
    }

    @Override
    public List<GameNode> getGamesInProgress() {
        List<GameNode> result = new ArrayList<>();
        for (GameNode game : this.getAllGames()) {
            if (game.getStatus() == Status.INPROGRESS) {
                result.add(game);
            }
        }
        return result;
    }

    @Override
    public List<GameNode> getFutureGames() {
        List<GameNode> result = new ArrayList<>();
        for (GameNode game : this.getAllGames()) {
            if (game.getStatus() == Status.NOTSTARTED) {
                result.add(game);
            }
        }
        return result;
    }

    @Override
    public List<Set<Participant>> computeFinalRanking() throws TournamentException {
        if (this.status != Status.FINISHED) {
            throw new TournamentException("Cannot compute ranking of unfinished tournament.");
        }
        List<Set<Participant>> finalRanking = new ArrayList<>();
        for (List<TournamentNode> round : this.rounds) {
            Set<Participant> exaequo = new HashSet<>();
            for (TournamentNode node : round) {
                if (node.getLoser().isPresent()) {
                    exaequo.add(node.getLoser().get());
                }
            }
            finalRanking.add(exaequo);
        }
        Set<Participant> winner = Set.of(this.rounds.get(this.rounds.size() - 1).get(0).getWinner());
        finalRanking.add(winner);
        Collections.reverse(finalRanking);
        return finalRanking;
    }

    @Override
    public Status getStatus() {
        return this.status;
    }


}
