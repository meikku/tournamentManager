package tournamentmanager.core.impl;

import tournamentmanager.core.api.*;

import java.util.*;

public class TournamentImpl implements Tournament {

    private List<Participant> participants = new ArrayList();
    private Status status = Status.NOTSTARTED;
    private List<List<Game>> rounds = new ArrayList();


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
    public List<Participant> computePossiblePreliminaryRanking() {
        List<Participant> randomizedParticipants = new ArrayList<>(this.participants);
        Collections.shuffle(randomizedParticipants);
        randomizedParticipants.sort(Comparator.comparingInt(Participant::getSeed));
        return Collections.unmodifiableList(randomizedParticipants);
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
        List<Participant> initialRankingParticipants = this.computePossiblePreliminaryRanking();
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
        for (Game game : this.getAllGames()) {
            if (game.getStatus() != Status.FINISHED) {
                throw new TournamentException("Cannot end a tournament that had unfinished games.");
            }
        }

        // Set status
        this.status = Status.FINISHED;

    }

    @Override
    public List<Game> getAllGames() throws TournamentException {
        List<Game> allGames = new ArrayList<>();
        for (List<Game> round : rounds) {
            allGames.addAll(round);
        }
        return allGames;
    }

    @Override
    public List<List<Game>> getRounds() throws TournamentException {
        return Collections.unmodifiableList(this.rounds);
    }

    @Override
    public List<Game> getGamesReadyToStart() throws TournamentException {
        List<Game> result = new ArrayList<>();
        for (Game game : this.getAllGames()) {
            if (game.getParticipant1() != null && game.getParticipant2() != null
                    && game.getStatus() == Status.NOTSTARTED) {
                result.add(game);
            }
        }
        return result;
    }

    @Override
    public List<Game> getFinishedGames() throws TournamentException {
        List<Game> result = new ArrayList<>();
        for (Game game : this.getAllGames()) {
            if (game.getStatus() == Status.FINISHED) {
                result.add(game);
            }
        }
        return result;
    }

    @Override
    public List<Game> getGamesInProgress() throws TournamentException {
        List<Game> result = new ArrayList<>();
        for (Game game : this.getAllGames()) {
            if (game.getStatus() == Status.INPROGRESS) {
                result.add(game);
            }
        }
        return result;
    }

    @Override
    public List<Game> getFutureGames() throws TournamentException {
        List<Game> result = new ArrayList<>();
        for (Game game : this.getAllGames()) {
            if (game.getStatus() == Status.NOTSTARTED) {
                result.add(game);
            }
        }
        return result;
    }

    @Override
    public List<Set<Participant>> getFinalRanking() throws TournamentException {
        List<Set<Participant>> finalRanking = new ArrayList<>();
        for (List<Game> round : this.rounds) {
            Set<Participant> exaequo = new HashSet<>();
            for (Game game : round) {
                exaequo.add(game.getCurrentLoser());
            }
            finalRanking.add(exaequo);
        }
        Set<Participant> winner = new HashSet<>();
        winner.add(this.rounds.get(this.rounds.size() - 1).get(0).getCurrentWinner());
        finalRanking.add(winner);
        return finalRanking;
    }

    @Override
    public Status getStatus() {
        return this.status;
    }


}
