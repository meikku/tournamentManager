package tournamentmanager.core.api;

public interface Bye extends TournamentNode {
    void setParticipant(Participant participant);
    Participant getParticipant();
}
