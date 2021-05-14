package tournamentmanager.core.impl;

import tournamentmanager.core.api.Bye;
import tournamentmanager.core.api.Game;
import tournamentmanager.core.api.Participant;
import tournamentmanager.core.api.TournamentException;

import java.util.Optional;

public class ByeImpl implements Bye {

    Participant participant;
    Game followingGame;

    @Override
    public void setParticipant(Participant participant) {
        this.participant = participant;
    }

    @Override
    public Participant getParticipant() {
        return this.participant;
    }

    @Override
    public Participant getWinner() throws TournamentException {
        return this.participant;
    }

    @Override
    public Optional<Participant> getLoser() throws TournamentException {
        return Optional.empty();
    }

    @Override
    public Optional<Game> getFollowingGame() {
        return Optional.ofNullable(followingGame);
    }

    @Override
    public void setFollowingGame(Game game) {
        this.followingGame = game;
    }




}
