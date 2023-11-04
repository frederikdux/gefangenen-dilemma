package de.fhdw.gaming.GefangenenDilemma.domain.impl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.fhdw.gaming.GefangenenDilemma.domain.GefangenenDilemmaPlayer;
import de.fhdw.gaming.core.domain.PlayerState;

/**
 * Tests {@link GefangenenDilemmaPlayerImpl}.
 */
final class GefangenenDilemmaPlayerImplTest {

    /**
     * The default possible outcomes.
     */
    private Map<Boolean, Map<Boolean, Double>> defaultPossibleOutcomes;
    /**
     * The default player.
     */
    private GefangenenDilemmaPlayer defaultPlayer;

    /**
     * Initialises default test objects.
     */
    @BeforeEach
    void setUp() {
        this.defaultPossibleOutcomes = Map.of(
                false, Map.of(false, -2.0d, true, 0.0d),
                true, Map.of(false, 1.0d, true, 2.0d));
        this.defaultPlayer = new GefangenenDilemmaPlayerImpl("A", this.defaultPossibleOutcomes);
    }

    /**
     * Tests {@link GefangenenDilemmaPlayerImpl#toString()}.
     */
    @Test
    void testToString() {
        assertThat(this.defaultPlayer.toString(),
                is(equalTo("DemoPlayer[name=A, state=PLAYING, outcome=Optional.empty, answer=Optional.empty]")));
    }

    /**
     * Tests {@link GefangenenDilemmaPlayerImpl#equals(Object)}.
     */
    @Test
    void testEquals() {
        assertThat(this.defaultPlayer, is(equalTo(this.defaultPlayer)));
        final GefangenenDilemmaPlayer player = new GefangenenDilemmaPlayerImpl("A", this.defaultPossibleOutcomes);
        assertThat(player, is(equalTo(this.defaultPlayer)));

        assertThat(new GefangenenDilemmaPlayerImpl("B", this.defaultPossibleOutcomes),
                is(not(equalTo(this.defaultPlayer))));
        assertThat(new GefangenenDilemmaPlayerImpl("A", Collections.emptyMap()), is(not(equalTo(this.defaultPlayer))));

        player.setState(PlayerState.WON);
        assertThat(player, is(not(equalTo(this.defaultPlayer))));
    }

    /**
     * Tests {@link GefangenenDilemmaPlayerImpl#getPossibleOutcomes()}.
     */
    @Test
    void testGetPossibleOutcomes() {
        assertThat(this.defaultPlayer.getPossibleOutcomes(), is(equalTo(this.defaultPossibleOutcomes)));
    }

    /**
     * Tests {@link GefangenenDilemmaPlayerImpl#getAnswer()}.
     */
    @Test
    void testGetAnswer() {
        assertThat(this.defaultPlayer.getAnswer(), is(equalTo(Optional.empty())));
    }

    /**
     * Tests {@link GefangenenDilemmaPlayerImpl#setAnswer(boolean)}.
     */
    @Test
    void testSetAnswer() {
        this.defaultPlayer.setAnswer(false);
        assertThat(this.defaultPlayer.getAnswer(), is(equalTo(Optional.of(false))));

        Assertions.assertThrows(IllegalStateException.class, () -> this.defaultPlayer.setAnswer(true));
    }

    /**
     * Tests {@link GefangenenDilemmaPlayerImpl#deepCopy()}.
     */
    @Test
    void testDeepCopy() {
        final GefangenenDilemmaPlayer playerCopy = this.defaultPlayer.deepCopy();
        assertThat(playerCopy, is(equalTo(this.defaultPlayer)));

        playerCopy.setState(PlayerState.DRAW);
        assertThat(playerCopy, is(not(equalTo(this.defaultPlayer))));
        assertThat(this.defaultPlayer.getState(), is(equalTo(PlayerState.PLAYING)));

        this.defaultPlayer.setState(PlayerState.DRAW);
        assertThat(playerCopy, is(equalTo(this.defaultPlayer)));
        this.defaultPlayer.setOutcome(5.0d);
        assertThat(playerCopy, is(not(equalTo(this.defaultPlayer))));
        assertThat(playerCopy.getOutcome(), is(equalTo(Optional.of(0.0d))));
    }

    /**
     * Tests {@link GefangenenDilemmaPlayerImpl#getName()}.
     */
    @Test
    void testGetName() {
        assertThat(this.defaultPlayer.getName(), is(equalTo("A")));
    }

    /**
     * Tests {@link GefangenenDilemmaPlayerImpl#getState()}.
     */
    @Test
    void testGetState() {
        assertThat(this.defaultPlayer.getState(), is(equalTo(PlayerState.PLAYING)));
    }

    /**
     * Tests {@link GefangenenDilemmaPlayerImpl#setState(de.fhdw.gaming.core.domain.PlayerState)}.
     */
    @Test
    void testSetState() {
        this.defaultPlayer.setState(PlayerState.DRAW);
        assertThat(this.defaultPlayer.getState(), is(equalTo(PlayerState.DRAW)));
    }

    /**
     * Tests {@link GefangenenDilemmaPlayerImpl#getOutcome()}.
     */
    @Test
    void testGetOutcome() {
        assertThat(this.defaultPlayer.getOutcome(), is(equalTo(Optional.empty())));
        this.defaultPlayer.setState(PlayerState.RESIGNED);
        assertThat(this.defaultPlayer.getOutcome(), is(equalTo(Optional.of(-1.0d))));
        this.defaultPlayer.setState(PlayerState.DRAW);
        assertThat(this.defaultPlayer.getOutcome(), is(equalTo(Optional.of(0.0d))));
        this.defaultPlayer.setState(PlayerState.WON);
        assertThat(this.defaultPlayer.getOutcome(), is(equalTo(Optional.of(1.0d))));
        this.defaultPlayer.setState(PlayerState.LOST);
        assertThat(this.defaultPlayer.getOutcome(), is(equalTo(Optional.of(-1.0d))));
        this.defaultPlayer.setState(PlayerState.PLAYING);
        assertThat(this.defaultPlayer.getOutcome(), is(equalTo(Optional.empty())));
    }

    /**
     * Tests {@link GefangenenDilemmaPlayerImpl#setOutcome(double)}.
     */
    @Test
    void testSetOutcome() {
        this.defaultPlayer.setState(PlayerState.WON);
        this.defaultPlayer.setOutcome(2.0d);
        assertThat(this.defaultPlayer.getOutcome(), is(equalTo(Optional.of(2.0d))));

        this.defaultPlayer.setState(PlayerState.PLAYING);
        Assertions.assertThrows(IllegalStateException.class, () -> this.defaultPlayer.setOutcome(0.0d));
    }
}
