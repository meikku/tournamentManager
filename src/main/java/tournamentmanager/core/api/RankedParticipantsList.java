package tournamentmanager.core.api;

import java.util.List;

public interface RankedParticipantsList extends List<Participant> {
    Participant takeNext();
}
