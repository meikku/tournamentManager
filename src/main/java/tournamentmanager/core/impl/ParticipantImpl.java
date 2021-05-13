package tournamentmanager.core.impl;

import tournamentmanager.core.api.Participant;

public class ParticipantImpl implements Participant {

    private String name;
    private int seed;
    private boolean eliminated;

    public ParticipantImpl(String name, int seed, boolean eliminated) {
        this.name = name;
        this.seed = seed;
        this.eliminated = eliminated;
    }


    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public int getSeed() {
        return this.seed;
    }

    @Override
    public boolean isEliminated() {
        return this.eliminated;
    }

    @Override
    public void setEliminated(boolean value) {
        this.eliminated = value;
    }

    @Override
    public void setSeed(int value) {
        this.seed = value;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }


}
