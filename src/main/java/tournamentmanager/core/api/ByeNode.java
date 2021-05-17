package tournamentmanager.core.api;

public interface ByeNode extends TournamentNode {
    void setParticipant(Participant participant);
    Participant getParticipant();
}
