package tournamentmanager.core.impl;

import tournamentmanager.core.api.Game;
import tournamentmanager.core.api.Participant;
import tournamentmanager.core.api.Status;
import tournamentmanager.core.api.TournamentException;

public class GameImpl implements Game {

    Participant p1;
    Participant p2;
    int p1Points = 0;
    int p2Points = 0;
    Status status = Status.NOTSTARTED;
    Game followingGame;
    Game previousGame1;
    Game previousGame2;

    @Override
    public void addParticipant(Participant participant) throws TournamentException {
        if (this.p1 == null) {
            this.p1 = participant;
        } else if (this.p2 == null) {
            this.p2 = participant;
        } else {
            throw new TournamentException("Cannot add participant to game, game already has two participants.");
        }
    }

    @Override
    public void addPoints(Participant participant, int points) throws TournamentException {
        if (participant == p1) {
            p1Points += points;
        } else if (participant == p2) {
            p2Points += points;
        } else {
            throw new TournamentException("Cannot add points to a participants that is not part of this game.");
        }
    }


    @Override
    public void changeStatus(Status status) throws TournamentException {
        if (status == Status.FINISHED) {
            if (p1Points == p2Points) {
                throw new TournamentException("Cannot set the game to 'finished', the scores are ex-aequo!");
            }
            this.getFollowingGame().addParticipant(this.getCurrentWinner());
        }
    }

    @Override
    public void registerForfeit(Participant participant) throws TournamentException {
        if (participant == p1) {
            p1Points = -1;
        } else if (participant == p2) {
            p2Points = -1;
        } else {
            throw new TournamentException("Cannot register forfeit for a participants that is not part of this game.");
        }
        this.status = Status.FINISHED;
    }

    @Override
    public Participant getParticipant1() {
        return p1;
    }

    @Override
    public Participant getParticipant2() {
        return p2;
    }

    @Override
    public Participant getCurrentWinner() throws TournamentException {

        if (p1Points > p2Points) {
            return p1;
        } else if (p2Points > p1Points) {
            return p2;
        } else {
            throw new TournamentException("There is no winner yet, participants are ex-aequo.");
        }

    }

    @Override
    public Participant getCurrentLoser() throws TournamentException {

        if (p1Points < p2Points) {
            return p1;
        } else if (p2Points < p1Points) {
            return p2;
        } else {
            throw new TournamentException("There is no loser yet, participants are ex-aequo.");
        }
    }

    @Override
    public Status getStatus() {
        return status;
    }

    @Override
    public Game getFollowingGame() {
        return this.followingGame;
    }

    @Override
    public Game getPreviousGame1() {
        return this.previousGame1;
    }

    @Override
    public Game getPreviousGame2() {
        return this.previousGame2;
    }

    @Override
    public void setFollowingGame(Game game) {
        this.followingGame = game;
    }

    @Override
    public void setPreviousGame1(Game game) {
        this.previousGame1 = game;
    }

    @Override
    public void setPreviousGame2(Game game) {
        this.previousGame2 = game;
    }


}
