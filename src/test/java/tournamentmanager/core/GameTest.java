package tournamentmanager.core;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tournamentmanager.core.api.Game;
import tournamentmanager.core.api.Participant;
import tournamentmanager.core.api.Status;
import tournamentmanager.core.api.TournamentException;
import tournamentmanager.core.impl.GameImpl;
import tournamentmanager.core.impl.ParticipantImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    @Test
    void addPreviousGameProperlyAddsAPreviousGameWhenItIsNotAlreadyAddedAndPreviousGamesContainsLessThanTwoGames(){
        Game gameToAdd = new GameImpl();

        assertDoesNotThrow(() -> g.addPreviousGame(gameToAdd));

        List<Game> expected = new ArrayList<>();
        expected.add(gameToAdd);

        assertIterableEquals(expected, g.getPreviousGames());
    }

    @Test
    void addPreviousGameThrowsTournamentExceptionWhenTheGameToAddHasAlreadyBeenAdded(){
        Game gameToAdd = new GameImpl();

        assertDoesNotThrow(() -> g.addPreviousGame(gameToAdd));

        assertThrows(TournamentException.class,
                () -> g.addPreviousGame(gameToAdd));
    }

    @Test
    void addPreviousGameThrowsTournamentExceptionWhenPreviousGamesAlreadyContainsTwoOrMoreGames(){
        Game fillerGame1 = new GameImpl();
        Game fillerGame2 = new GameImpl();
        Game gameToAdd = new GameImpl();

        assertDoesNotThrow(() -> g.addPreviousGame(fillerGame1));
        assertDoesNotThrow(() -> g.addPreviousGame(fillerGame2));

        assertThrows(TournamentException.class,
                () -> g.addPreviousGame(gameToAdd));
    }

    @Test
    void addPreviousGameThrowsTournamentExceptionWhenGameIsNotStarted(){
        Game gameToAdd = new GameImpl();

        assertDoesNotThrow(() -> g.addParticipant(p1));
        assertDoesNotThrow(() -> g.addParticipant(p2));

        assertDoesNotThrow(() -> g.start());

        assertThrows(TournamentException.class,
                () -> g.addPreviousGame(gameToAdd));
    }

    @Test
    void addPreviousGameThrowsIllegalArgumentExceptionWhenTheGameToAddIsNull(){
        Game gameToAdd = null;

        assertThrows(IllegalArgumentException.class,
                () -> g.addPreviousGame(gameToAdd));
    }
    
    //finish
    @Test
    void finishThrowsTournamentExceptionWhenScoresAreEven(){
        assertDoesNotThrow(() -> g.addParticipant(p1));
        assertDoesNotThrow(() -> g.addParticipant(p2));

        assertDoesNotThrow(() -> g.start());

        assertDoesNotThrow(() -> g.addPoints(p1, 1));
        assertDoesNotThrow(() -> g.addPoints(p2, 1));

        assertThrows(TournamentException.class,
                () -> g.finish());
    }

    @Test
    void finishThrowsTournamentExceptionWhenGameIsNotInProgress(){
        assertDoesNotThrow(() -> g.addParticipant(p1));
        assertDoesNotThrow(() -> g.addParticipant(p2));

        assertThrows(TournamentException.class,
                () -> g.finish());
    }

    @Test
    void finishProperlyFinishesTheGame(){
        Game followingGame = new GameImpl();
        g.setFollowingGame(followingGame);

        assertDoesNotThrow(() -> g.addParticipant(p1));
        assertDoesNotThrow(() -> g.addParticipant(p2));

        assertDoesNotThrow(() -> g.start());

        assertDoesNotThrow(() -> g.addPoints(p1, 1));
        assertDoesNotThrow(() -> g.addPoints(p2, 2));

        assertDoesNotThrow(() -> g.finish());

        assertEquals(Status.FINISHED, g.getStatus());
        assertTrue(g.getFollowingGame().get().getParticipants().contains(p2));
        assertTrue(p1.isEliminated());
    }

    //start
    @Test
    void startThrowsTournamentExceptionWhenTheGameIsInProgressAndThereAreTwoParticipants(){
        assertDoesNotThrow(() -> g.addParticipant(p1));
        assertDoesNotThrow(() -> g.addParticipant(p2));

        assertDoesNotThrow(() -> g.start());

        assertThrows(TournamentException.class,
                () -> g.start());

    }

    @Test
    void startThrowsTournamentExceptionWhenTheGameIsNotStartedAndThereAreLessThanTwoParticipants(){
        assertDoesNotThrow(() -> g.addParticipant(p1));

        assertThrows(TournamentException.class,
                () -> g.start());
    }

    @Test
    void startSetsTheGameStatusToInProgressWhenThereAreTwoParticipantsAndTheGameIsNotStarted(){
        assertDoesNotThrow(() -> g.addParticipant(p1));
        assertDoesNotThrow(() -> g.addParticipant(p2));

        assertDoesNotThrow(() -> g.start());

        assertEquals(Status.INPROGRESS, g.getStatus());
    }

    //setPreviousGame
    @Test
    void setFollowingGameThrowsTournamentExceptionWhenFollowingGameIsNull(){
        assertThrows(IllegalArgumentException.class,
                () -> g.setFollowingGame(null));
    }

    @Test
    void setFollowingGameProperlyAssociatesGameAndFollowingGame(){
        Game followingGameToAdd = new GameImpl();
        g.setFollowingGame(followingGameToAdd);

        Optional<Game> expected = Optional.of(followingGameToAdd);

        assertEquals(expected,
                g.getFollowingGame());
    }

    //getPreviousGames
    @Test
    void getPreviousGamesContainsExactlyAllPreviousGamesOfCurrentGame(){
        Game previousGameToAdd = new GameImpl();
        assertDoesNotThrow(() -> g.addPreviousGame(previousGameToAdd));

        List<Game> expected = new ArrayList<>();
        expected.add(previousGameToAdd);

        assertIterableEquals(expected, g.getPreviousGames());
    }
    @Test
    void getPreviousGamesReturnsAnEmptyListWhenThereAreNoPreviousGames(){
        assertIterableEquals(new ArrayList<>(), g.getPreviousGames());
    }

    //getWinner
    @Test
    void getWinnerReturnsTheWinnerOfAGameWhenThereIsAWinnerAndTheGameIsFinished(){
        assertDoesNotThrow(() -> g.addParticipant(p1));
        assertDoesNotThrow(() -> g.addParticipant(p2));

        assertDoesNotThrow(() -> g.start());

        assertDoesNotThrow(() -> g.addPoints(p1, 2));
        assertDoesNotThrow(() -> g.addPoints(p2, 1));

        assertDoesNotThrow(() -> g.finish());

        try {
            assertEquals(p1, g.getWinner());
        } catch (TournamentException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getWinnerThrowsTournamentExceptionWhenScoresAreNotEvenButTheGameIsNotFinished(){
        assertDoesNotThrow(() -> g.addParticipant(p1));
        assertDoesNotThrow(() -> g.addParticipant(p2));

        assertDoesNotThrow(() -> g.start());

        assertDoesNotThrow(() -> g.addPoints(p1, 2));
        assertDoesNotThrow(() -> g.addPoints(p2, 1));

        assertThrows(TournamentException.class,
                () -> g.getWinner());
    }

    @Test
    void getWinnerThrowsTournamentExceptionWhenScoresAreEvenAndTheGameIsNotFinished(){
        assertDoesNotThrow(() -> g.addParticipant(p1));
        assertDoesNotThrow(() -> g.addParticipant(p2));

        assertDoesNotThrow(() -> g.start());

        assertDoesNotThrow(() -> g.addPoints(p1, 2));
        assertDoesNotThrow(() -> g.addPoints(p2, 2));

        assertThrows(TournamentException.class,
                () -> g.getWinner());
    }

    //getLoser
    @Test
    void getLoserReturnsTheWinnerOfAGameWhenThereIsAWinnerAndTheGameIsFinished(){
        assertDoesNotThrow(() -> g.addParticipant(p1));
        assertDoesNotThrow(() -> g.addParticipant(p2));

        assertDoesNotThrow(() -> g.start());

        assertDoesNotThrow(() -> g.addPoints(p1, 2));
        assertDoesNotThrow(() -> g.addPoints(p2, 1));

        assertDoesNotThrow(() -> g.finish());

        try {
            assertEquals(p2, g.getLoser());
        } catch (TournamentException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getLoserThrowsTournamentExceptionWhenScoresAreNotEvenButTheGameIsNotFinished(){
        assertDoesNotThrow(() -> g.addParticipant(p1));
        assertDoesNotThrow(() -> g.addParticipant(p2));

        assertDoesNotThrow(() -> g.start());

        assertDoesNotThrow(() -> g.addPoints(p1, 2));
        assertDoesNotThrow(() -> g.addPoints(p2, 1));

        assertThrows(TournamentException.class,
                () -> g.getLoser());
    }

    @Test
    void getLoserThrowsTournamentExceptionWhenScoresAreEvenAndTheGameIsNotFinished(){
        assertDoesNotThrow(() -> g.addParticipant(p1));
        assertDoesNotThrow(() -> g.addParticipant(p2));

        assertDoesNotThrow(() -> g.start());

        assertDoesNotThrow(() -> g.addPoints(p1, 2));
        assertDoesNotThrow(() -> g.addPoints(p2, 2));

        assertThrows(TournamentException.class,
                () -> g.getLoser());
    }

    //getStatus
    @Test
    void getStatusReturnsTheStatusOfTheGame(){
        assertEquals(Status.NOTSTARTED, g.getStatus());

        assertDoesNotThrow(() -> g.addParticipant(p1));
        assertDoesNotThrow(() -> g.addParticipant(p2));

        assertDoesNotThrow(() -> g.start());

        assertEquals(Status.INPROGRESS, g.getStatus());

        assertDoesNotThrow(() -> g.addPoints(p1, 2));
        assertDoesNotThrow(() -> g.addPoints(p2, 1));

        assertDoesNotThrow(() -> g.finish());

        assertEquals(Status.FINISHED, g.getStatus());
    }

    //getStatus
    @Test
    void getParticipantsReturnsExactlyAllTheParticipantsOfTheGame(){
        List<Participant> expected = new ArrayList<>();

        // compares the content of arrays which are not necessarily ordered in the same way but should contain the same elements
        assertTrue(expected.containsAll(g.getParticipants()) && expected.size() == g.getParticipants().size());
        assertTrue(g.getParticipants().size() <= 2);

        assertDoesNotThrow(() -> g.addParticipant(p1));
        expected.add(p1);

        assertTrue(expected.containsAll(g.getParticipants()) && expected.size() == g.getParticipants().size());
        assertTrue(g.getParticipants().size() <= 2);

        assertDoesNotThrow(() -> g.addParticipant(p2));
        expected.add(p2);

        assertTrue(expected.containsAll(g.getParticipants()) && expected.size() == g.getParticipants().size());
        assertTrue(g.getParticipants().size() <= 2);

    }

    //getFollowingGame
    @Test
    void getFollowingGameReturnsAnEmptyOptionalIfThereIsNoFollowingGame(){
        Optional<Game> expected = Optional.empty();

        assertEquals(expected, g.getFollowingGame());
    }

    @Test
    void getFollowingGameReturnsTheFollowingGame(){
        Game followingGameToAdd = new GameImpl();
        Optional<Game> expected = Optional.of(followingGameToAdd);

        g.setFollowingGame(followingGameToAdd);

        assertEquals(expected, g.getFollowingGame());
    }
}
