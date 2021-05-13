package tournamentmanager.core.api;

public interface Participant {

    String getName();

    int getSeed();

    boolean isEliminated();

    void setEliminated(boolean value);

    void setSeed(int value);

    void setName(String name);
}
