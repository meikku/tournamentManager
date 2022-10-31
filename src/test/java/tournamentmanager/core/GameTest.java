package tournamentmanager.core;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tournamentmanager.core.api.Game;
import tournamentmanager.core.api.Participant;
import tournamentmanager.core.api.TournamentException;
import tournamentmanager.core.impl.GameImpl;
import tournamentmanager.core.impl.ParticipantImpl;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GameTest {

    Game g;
    Participant p1;
    Participant p2;

    @BeforeEach
    void beforeEach(){
        g = new GameImpl();
        p1 = new ParticipantImpl("player1");
        p2 = new ParticipantImpl("player2");
    }

    //addPoints
    @Test
    void addPointsProperlyAddsPositiveAmountOfPoints(){

        assertDoesNotThrow(() -> g.addParticipant(p1));
        assertDoesNotThrow(() -> g.addParticipant(p2));

        assertDoesNotThrow(() -> g.start());

        assertDoesNotThrow(() -> g.addPoints(p1, 2));

        assertEquals(g.getPoints(p1),
                2);
    }

    @Test
    void addPointsThrowsErrorWhenPlayerIsNull(){

        assertDoesNotThrow(() -> g.addParticipant(p1));
        assertDoesNotThrow(() -> g.addParticipant(p2));

        assertDoesNotThrow(() -> g.start());

        assertThrows(IllegalArgumentException.class,
                () -> g.addPoints(null, 5) );
    }

    @Test
    void addPointsProperlyAddsNegativeAmountOfPoints(){

        assertDoesNotThrow(() -> g.addParticipant(p1));
        assertDoesNotThrow(() -> g.addParticipant(p2));

        assertDoesNotThrow(() -> g.start());

        assertDoesNotThrow(() -> g.addPoints(p1, -2));

        assertEquals(g.getPoints(p1),
                -2);
    }

    @Test
    void addPointsThrowsErrorWhenGameIsNotInProgress(){
        assertDoesNotThrow(() -> g.addParticipant(p1));

        assertDoesNotThrow(() -> g.addParticipant(p2));

        assertThrows(TournamentException.class,
                () -> g.addPoints(p1, 5));
    }

    //addParticipant
    @Test
    void addParticipantThrowsIllegalArgumentExceptionWhenParticipantIsNull(){
        assertThrows(IllegalArgumentException.class,
                () -> g.addParticipant(null));
    }

    @Test
    void addParticipantProperlyAddsParticipantWhenParticipantsListContainsTwoOrLessParticipantsAndParticipantIsNotNull(){
        assertDoesNotThrow(() -> g.addParticipant(p1));

        List<Participant> expected = new ArrayList<>();
        expected.add(p1);

        assertIterableEquals(expected, g.getParticipants());
    }

    @Test
    void addParticipantThrowsTournamentExceptionWhenParticipantsListAlreadyContainsTwoOrMoreParticipants(){
        assertDoesNotThrow(() -> g.addParticipant(p1));
        assertDoesNotThrow(() -> g.addParticipant(p2));

        Participant p3 = new ParticipantImpl("player3");

        assertThrows(TournamentException.class,
                () -> g.addParticipant(p3));
    }

    //addPreviousGame

}
