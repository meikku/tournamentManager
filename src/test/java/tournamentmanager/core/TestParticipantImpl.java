package tournamentmanager.core;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tournamentmanager.core.impl.ParticipantImpl;
import static org.junit.jupiter.api.Assertions.*;

public class TestParticipantImpl {
    private ParticipantImpl participant;
    @BeforeEach
    public void initEach(){
        participant = new ParticipantImpl("Messi");

    }

    //Functional method test
    @Test
    public void testEliminated1(){
        participant.eliminate();
        assertTrue(participant.isEliminated());
    }

    //Functional method test
    @Test
    public void testEliminated2(){
        participant.setEliminated(true);
        participant.eliminate();
        assertTrue(participant.isEliminated());

    }

}
