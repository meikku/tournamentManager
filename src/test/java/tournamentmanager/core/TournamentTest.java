package tournamentmanager.core;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import tournamentmanager.core.api.*;
import tournamentmanager.core.impl.GameImpl;
import tournamentmanager.core.impl.ParticipantImpl;
import tournamentmanager.core.impl.TournamentImpl;
import tournamentmanager.core.impl.TournamentTreeBuilderImpl;

import java.util.ArrayList;
import java.util.List;

public class TournamentTest {
	
	Tournament t;
	Participant p1;
    Participant p2;
    Participant p3;
    Participant p4;
    Participant p5;
    
    @BeforeEach
    void beforeEach(){
        t = new TournamentImpl();
        p1 = new ParticipantImpl("player1");
        p2 = new ParticipantImpl("player2");
        p3 = new ParticipantImpl("player3");
        p4 = new ParticipantImpl("player4");
    }

    @Test
    public void testAddParticipantGameNotStarted() throws TournamentException{
    	assertDoesNotThrow(() -> {
            t.addParticipant(p1);
        });
    }
    
    @Test
    public void testAddParticipantGameHasStarted() throws TournamentException{
    	assertThrows(TournamentException.class,
                () -> {
                	t.start();
                	t.addParticipant(p1);
        });
    }
    
    @Test
    public void testAddParticipantGameHasEnded() throws TournamentException{
    	assertThrows(TournamentException.class,
                () -> {
                	t.start();
                	t.end();
                	t.addParticipant(p1);
        });
    }
    
    @Test
    public void testAddNullParticipant() throws TournamentException{
    	assertDoesNotThrow(() -> {
    		p5 = null; 
            t.addParticipant(p5);
        });
    }
    
    @Test
    public void testStartWithLessThanTwoParticipants() throws TournamentException{
    	assertThrows(TournamentException.class, () -> {
            t.addParticipant(p1);
            t.start();
        });
    }
    
    @Test
    public void testStartNumberOfParticipantsNotPowerOfTwo() throws TournamentException{
    	assertThrows(TournamentException.class, () -> {
            t.addParticipant(p1);
            t.addParticipant(p2);
            t.addParticipant(p3);
            t.start();
        });
    }
    
    @Test
    public void testStartWithTwoOrMoreParticipantsInPowerOfTwo() throws TournamentException{
    	assertDoesNotThrow(() -> {
            t.addParticipant(p1);
            t.addParticipant(p2);
            t.addParticipant(p3);
            t.addParticipant(p4);
            t.start();
        });
    }
    
    @Test
    public void testAddSameParticipantMultipleTimes() throws TournamentException{
    	assertThrows(TournamentException.class, () -> {
            t.addParticipant(p1);
            t.addParticipant(p2);
            t.addParticipant(p2);
            t.addParticipant(p3);
            t.start();
        });
    }
    
    @Test
    public void testStartWhenTournamentInProgress() throws TournamentException{
    	assertThrows(TournamentException.class, () -> {
    		
    		t.addParticipant(p1);
    		t.addParticipant(p2);
    		t.start();
            t.addParticipant(p3);
            t.addParticipant(p4);
            t.start();
        });
    }
    
    @Test
    public void testStartWhenTournamentFinished() throws TournamentException{
    	assertThrows(TournamentException.class, () -> {
    		
    		t.addParticipant(p1);
    		t.addParticipant(p2);
    		t.start();
    		t.end();
            t.addParticipant(p3);
            t.addParticipant(p4);
            t.start();
        });
    }

    @Test
    void testGetGamesInProgressRetrivesGamesInProgress(){
        TournamentTreeBuilder fakeTtb = mock(TournamentTreeBuilder.class);

        List<List<Game>> returnList = new ArrayList<>();
        List<Participant> participantsList = new ArrayList<>();
        participantsList.add(p1);
        participantsList.add(p2);

        Game fakeGame = mock(Game.class);
        when(fakeGame.getStatus()).thenReturn(Status.INPROGRESS);

        List<Game> fakeRound = new ArrayList<>();
        fakeRound.add(fakeGame);
        returnList.add(fakeRound);

        when(fakeTtb.buildAllRounds(participantsList)).thenReturn(returnList);

        assertDoesNotThrow(() -> t.addParticipant(p1));
        assertDoesNotThrow(() -> t.addParticipant(p2));

        assertDoesNotThrow(() -> t.startWithTtb(fakeTtb));

        assertTrue(t.getGamesInProgress().containsAll(fakeRound));
        assertTrue(fakeRound.containsAll(t.getGamesInProgress()));
    }


}
