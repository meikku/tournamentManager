package tournamentmanager.core.impl;

import tournamentmanager.core.api.*;
import tournamentmanager.util.Util;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Basic Tournament implementation.
 */
public class TournamentImpl implements Tournament {

    private final List<Participant> participants = new ArrayList<>();
    private Status status = Status.NOTSTARTED;
    private List<List<GameNode>> rounds = new ArrayList<>();


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
    public void start() throws TournamentException {
        if (this.getStatus() != Status.NOTSTARTED) {
            throw new TournamentException("Cannot start a tournament that has already started.");
        } else if (this.participants.size() < 2) {
            throw new TournamentException("A tournament requires at least two participants.");
        } else if (!Util.isPowerOfTwo(this.participants.size())) {
            throw new TournamentException("A tournament requires a number of participants equal to a power of two.");
        }

        // Build tournament tree
        TournamentTreeBuilder builder = new TournamentTreeBuilderImpl();
        this.rounds = builder.buildAllRounds(Collections.unmodifiableList(this.participants));

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
    public List<GameNode> getAllNodes() {
        List<GameNode> allGames = new ArrayList<>();
        for (List<GameNode> round : rounds) {
            allGames.addAll(round);
        }
        return allGames;
    }

    @Override
    public List<GameNode> getAllGames() {
        List<GameNode> allGames = new ArrayList<>();
        for (List<GameNode> round : rounds) {
            for (GameNode node : round) {
                if (node instanceof GameNode) {
                    allGames.add((GameNode) node);
                }
            }
        }
        return allGames;
    }

    @Override
    public List<List<GameNode>> getRounds() {
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
        for (List<GameNode> round : this.rounds) {
            Set<Participant> exaequo = new HashSet<>();
            for (GameNode node : round) {
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
