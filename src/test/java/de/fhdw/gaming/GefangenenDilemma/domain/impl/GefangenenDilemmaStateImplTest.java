package de.fhdw.gaming.GefangenenDilemma.domain.impl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.fhdw.gaming.GefangenenDilemma.domain.GefangenenDilemmaPlayer;
import de.fhdw.gaming.GefangenenDilemma.domain.GefangenenDilemmaState;
import de.fhdw.gaming.core.domain.PlayerState;

/**
 * Tests {@link GefangenenDilemmaStateImpl}.
 */
final class GefangenenDilemmaStateImplTest {

    /**
     * The default player A.
     */
    private GefangenenDilemmaPlayer defaultPlayerA;
    /**
     * The default player B.
     */
    private GefangenenDilemmaPlayer defaultPlayerB;
    /**
     * The default state.
     */
    private GefangenenDilemmaState defaultState;

    /**
     * Initialises default test objects.
     */
    @BeforeEach
    void setUp() throws Exception {
        final Map<Boolean, Map<Boolean, Double>> defaultPossibleOutcomesA = Map.of(
                false, Map.of(false, -2.0d, true, 0.0d),
                true, Map.of(false, 1.0d, true, 2.0d));
        this.defaultPlayerA = new GefangenenDilemmaPlayerImpl("A", defaultPossibleOutcomesA);
        final Map<Boolean, Map<Boolean, Double>> defaultPossibleOutcomesB = Map.of(
                false, Map.of(false, -2.0d, true, 0.0d),
                true, Map.of(false, -1.0d, true, 2.0d));
        this.defaultPlayerB = new GefangenenDilemmaPlayerImpl("B", defaultPossibleOutcomesB);
        this.defaultState = new GefangenenDilemmaStateImpl(this.defaultPlayerA, this.defaultPlayerB);
    }

    /**
     * Tests constructor exception when both players have the same name.
     */
    @Test
    void testCtorWithIdenticalNames() {
        final Map<Boolean, Map<Boolean, Double>> defaultPossibleOutcomesA = Map.of(
                false, Map.of(false, -2.0d, true, 0.0d),
                true, Map.of(false, 1.0d, true, 2.0d));
        final GefangenenDilemmaPlayer playerA1 = new GefangenenDilemmaPlayerImpl("A", defaultPossibleOutcomesA);
        final Map<Boolean, Map<Boolean, Double>> defaultPossibleOutcomesB = Map.of(
                false, Map.of(false, -2.0d, true, 0.0d),
                true, Map.of(false, -1.0d, true, 2.0d));
        final GefangenenDilemmaPlayerImpl playerA2 = new GefangenenDilemmaPlayerImpl("A", defaultPossibleOutcomesB);

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new GefangenenDilemmaStateImpl(this.defaultPlayerA, playerA1));
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new GefangenenDilemmaStateImpl(playerA1, playerA2));
    }

    /**
     * Tests {@link GefangenenDilemmaStateImpl#getFirstPlayer()}.
     */
    @Test
    void testGetFirstPlayer() {
        assertThat(this.defaultState.getFirstPlayer(), is(equalTo(this.defaultPlayerA)));
    }

    /**
     * Tests {@link GefangenenDilemmaStateImpl#getSecondPlayer()}.
     */
    @Test
    void testGetSecondPlayer() {
        assertThat(this.defaultState.getSecondPlayer(), is(equalTo(this.defaultPlayerB)));
    }

    /**
     * Tests {@link GefangenenDilemmaStateImpl#toString()}.
     */
    @Test
    void testToString() {
        assertThat(this.defaultState.toString(), is(equalTo(
                "DemoState["
                        + "firstPlayer=DemoPlayer["
                        + "name=A, state=PLAYING, outcome=Optional.empty, answer=Optional.empty"
                        + "], secondPlayer=DemoPlayer["
                        + "name=B, state=PLAYING, outcome=Optional.empty, answer=Optional.empty"
                        + "]]")));
    }

    /**
     * Tests {@link GefangenenDilemmaStateImpl#equals(Object)}.
     */
    @Test
    void testEqualsObject() throws Exception {
        assertThat(this.defaultState, is(equalTo(this.defaultState)));
        assertThat(new GefangenenDilemmaStateImpl(this.defaultPlayerA, this.defaultPlayerB),
                is(equalTo(this.defaultState)));
        assertThat(new GefangenenDilemmaStateImpl(this.defaultPlayerB, this.defaultPlayerA),
                is(not(equalTo(this.defaultState))));
    }

    /**
     * Tests {@link GefangenenDilemmaStateImpl#deepCopy()}.
     */
    @Test
    void testDeepCopy() {
        final GefangenenDilemmaState stateCopy = this.defaultState.deepCopy();
        assertThat(stateCopy, is(equalTo(this.defaultState)));

        stateCopy.getFirstPlayer().setAnswer(false);
        stateCopy.nextTurn();
        assertThat(stateCopy, is(not(equalTo(this.defaultState))));

        this.defaultPlayerA.setAnswer(false);
        this.defaultState.nextTurn();
        assertThat(stateCopy, is(equalTo(this.defaultState)));
        this.defaultPlayerB.setAnswer(true);
        this.defaultState.nextTurn();
        assertThat(stateCopy, is(not(equalTo(this.defaultState))));

        stateCopy.getSecondPlayer().setAnswer(true);
        stateCopy.nextTurn();
        assertThat(stateCopy, is(equalTo(this.defaultState)));
    }

    /**
     * Tests {@link GefangenenDilemmaStateImpl#getPlayers()}.
     */
    @Test
    void testGetPlayers() {
        assertThat(this.defaultState.getPlayers(), is(equalTo(
                Map.of("A", this.defaultPlayerA, "B", this.defaultPlayerB))));
    }

    /**
     * Tests {@link GefangenenDilemmaStateImpl#computeNextPlayers()}.
     */
    @Test
    void testComputeNextPlayers() {
        assertThat(this.defaultState.computeNextPlayers(), is(equalTo(
                Set.of(this.defaultPlayerA, this.defaultPlayerB))));
    }

    /**
     * Tests {@link GefangenenDilemmaStateImpl#nextTurn()}.
     */
    @Test
    void testNextTurn() {
        this.defaultPlayerA.setAnswer(true);
        this.defaultState.nextTurn();
        assertThat(this.defaultState.computeNextPlayers(), is(equalTo(Set.of(this.defaultPlayerB))));

        this.defaultPlayerB.setAnswer(false);
        this.defaultState.nextTurn();
        assertThat(this.defaultState.computeNextPlayers(), is(empty()));

        assertThat(this.defaultPlayerA.getState(), is(equalTo(PlayerState.WON)));
        assertThat(this.defaultPlayerA.getOutcome(), is(equalTo(Optional.of(1.0))));
        assertThat(this.defaultPlayerB.getState(), is(equalTo(PlayerState.LOST)));
        assertThat(this.defaultPlayerB.getOutcome(), is(equalTo(Optional.of(-1.0))));
    }
}
