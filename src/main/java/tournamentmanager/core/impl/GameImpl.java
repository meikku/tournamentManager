package tournamentmanager.core.impl;

import tournamentmanager.core.api.Game;
import tournamentmanager.core.api.Participant;
import tournamentmanager.core.api.Status;
import tournamentmanager.core.api.TournamentException;

import java.util.Optional;

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
        if (participant == null) {
            throw new IllegalArgumentException("A participant cannot be null.");
        }
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
        if (participant == null) {
            throw new IllegalArgumentException("A participant cannot be null.");
        }
        if (this.status != Status.INPROGRESS) {
            throw new TournamentException("Cannot change status of a game that is not in progress.");
        }
        if (participant == p1) {
            p1Points += points;
        } else if (participant == p2) {
            p2Points += points;
        } else {
            throw new TournamentException("Cannot add points to a participants that is not part of this game.");
        }
    }


    @Override
    public void changeStatus(Status newStatus) throws TournamentException {
        if (newStatus == null) {
            throw new IllegalArgumentException("A status cannot be null.");
        }
        if (newStatus == Status.FINISHED) {
            if (p1Points == p2Points) {
                throw new TournamentException("Cannot set the game to 'finished', the scores are ex-aequo. A winner is required.");
            }
            if (this.followingGame != null) {
                this.followingGame.addParticipant(this.getCurrentWinner().get());
            }
            this.getCurrentLoser().get().setEliminated(true);
        }
    }

    @Override
    public void registerForfeit(Participant participant) throws TournamentException {
        if (participant == null) {
            throw new IllegalArgumentException("A participant cannot be null.");
        }
        if (participant == p1) {
            p1Points = -1;
        } else if (participant == p2) {
            p2Points = -1;
        } else {
            throw new TournamentException("Cannot register forfeit for a participant that is not part of this game.");
        }
        this.status = Status.FINISHED;
    }

    @Override
    public Optional<Participant> getParticipant1() {
        return Optional.ofNullable(p1);
    }

    @Override
    public Optional<Participant> getParticipant2() {
        return Optional.ofNullable(p2);
    }

    @Override
    public Optional<Participant> getCurrentWinner() {
        if (p1Points > p2Points) {
            return Optional.of(p1);
        } else if (p2Points > p1Points) {
            return Optional.of(p2);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Participant> getCurrentLoser() {
        if (p1Points < p2Points) {
            return Optional.of(p1);
        } else if (p2Points < p1Points) {
            return Optional.of(p2);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Status getStatus() {
        return status;
    }

    @Override
    public Optional<Game> getFollowingGame() {
        return Optional.ofNullable(this.followingGame);
    }

    @Override
    public Optional<Game> getPreviousGame1() {
        return Optional.ofNullable(this.previousGame1);
    }

    @Override
    public Optional<Game> getPreviousGame2() {
        return Optional.ofNullable(this.previousGame2);
    }

    @Override
    public void setFollowingGame(Game game) {
        if (game == null) {
            throw new IllegalArgumentException("A game cannot be null.");
        }
        this.followingGame = game;
    }

    @Override
    public void setPreviousGame1(Game game) {
        if (game == null) {
            throw new IllegalArgumentException("A game cannot be null.");
        }
        this.previousGame1 = game;
    }

    @Override
    public void setPreviousGame2(Game game) {
        if (game == null) {
            throw new IllegalArgumentException("A game cannot be null.");
        }
        this.previousGame2 = game;
    }


}
