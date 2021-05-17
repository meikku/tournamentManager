package tournamentmanager.core.impl;

import tournamentmanager.core.api.ByeNode;
import tournamentmanager.core.api.GameNode;
import tournamentmanager.core.api.Participant;
import tournamentmanager.core.api.TournamentException;

import java.util.Optional;

public class ByeNodeImpl implements ByeNode {

    Participant participant;
    GameNode followingGame;

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
    public Optional<GameNode> getFollowingGame() {
        return Optional.ofNullable(followingGame);
    }

    @Override
    public void setFollowingGame(GameNode game) {
        this.followingGame = game;
    }




}
