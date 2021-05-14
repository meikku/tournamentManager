package tournamentmanager.core.impl;

import tournamentmanager.core.api.Participant;
import tournamentmanager.core.api.RankedParticipantsList;

import java.util.ArrayList;
import java.util.List;

public class RankedParticipantsListImpl extends ArrayList<Participant> implements RankedParticipantsList  {

    private boolean takeFirst = true;

    public RankedParticipantsListImpl(List<Participant> participants) {
        super(participants);
    }

    @Override
    public Participant takeNext() {
        Participant result = this.takeFirst ? this.remove(0) : this.remove(this.size() - 1);
        this.takeFirst = !this.takeFirst;
        return result;
    }
}
