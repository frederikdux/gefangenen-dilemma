package de.fhdw.gaming.GefangenenDilemma.domain.impl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.fhdw.gaming.GefangenenDilemma.domain.GefangenenDilemmaPlayer;
import de.fhdw.gaming.GefangenenDilemma.domain.GefangenenDilemmaState;
import de.fhdw.gaming.GefangenenDilemma.domain.GefangenenDilemmaStrategy;
import de.fhdw.gaming.GefangenenDilemma.moves.GefangenenDilemmaMove;
import de.fhdw.gaming.GefangenenDilemma.moves.factory.GefangenenDilemmaMoveFactory;
import de.fhdw.gaming.GefangenenDilemma.moves.impl.GefangenenDilemmaDefaultMoveFactory;
import de.fhdw.gaming.GefangenenDilemma.strategy.GefangenenDilemmaSnitchStrategyFactory;
import de.fhdw.gaming.GefangenenDilemma.strategy.GefangenenDilemmaStaySilentStrategyFactory;
import de.fhdw.gaming.core.domain.Game;

/**
 * Tests {@link GefangenenDilemmaMoveGeneratorImpl}.
 */
final class GefangenenDilemmaGameImplTest {

    /**
     * The default player A.
     */
    private GefangenenDilemmaPlayer playerA;
    /**
     * The default player A strategy.
     */
    private GefangenenDilemmaStrategy playerAStrategy;
    /**
     * The default player B.
     */
    private GefangenenDilemmaPlayer playerB;
    /**
     * The default player B strategy.
     */
    private GefangenenDilemmaStrategy playerBStrategy;
    /**
     * The default game.
     */
    private Game<GefangenenDilemmaPlayer, GefangenenDilemmaState, GefangenenDilemmaMove,
            GefangenenDilemmaStrategy> game;

    /**
     * Initialises default test objects.
     */
    @BeforeEach
    void setUp() throws Exception {
        final Map<Boolean, Map<Boolean, Double>> possibleOutcomes = Map.of(
                false, Map.of(false, -2.0d, true, 0.0d),
                true, Map.of(false, 1.0d, true, 2.0d));

        final GefangenenDilemmaMoveFactory moveFactory = new GefangenenDilemmaDefaultMoveFactory();
        this.playerA = new GefangenenDilemmaPlayerBuilderImpl().changeName("A").changePossibleOutcomes(possibleOutcomes)
                .build();
        this.playerB = new GefangenenDilemmaPlayerBuilderImpl().changeName("B").changePossibleOutcomes(possibleOutcomes)
                .build();
        this.playerAStrategy = new GefangenenDilemmaStaySilentStrategyFactory().create(moveFactory);
        this.playerBStrategy = new GefangenenDilemmaSnitchStrategyFactory().create(moveFactory);
        this.game = new GefangenenDilemmaGameBuilderImpl()
                .changeMaximumComputationTimePerMove(60)
                .addPlayer(this.playerA, this.playerAStrategy)
                .addPlayer(this.playerB, this.playerBStrategy)
                .build(1);
    }

    /**
     * Tests {@link GefangenenDilemmaMoveGeneratorImpl#toString()}.
     */
    @Test
    void testToString() {
        assertThat(this.game.toString(), is(equalTo("DefaultGame[id=1, state="
                + this.game.getState().toString() + ", strategies={A=DemoSayYesStrategy, B=DemoSayNoStrategy}]")));
    }

    /**
     * Tests {@link GefangenenDilemmaMoveGeneratorImpl#getId()}.
     */
    @Test
    void testGetId() {
        assertThat(this.game.getId(), is(equalTo(1)));
    }

    /**
     * Tests {@link GefangenenDilemmaMoveGeneratorImpl#getPlayers()}.
     */
    @Test
    void testGetPlayers() {
        assertThat(this.game.getPlayers(), is(equalTo(Map.of("A", this.playerA, "B", this.playerB))));
    }

    /**
     * Tests {@link GefangenenDilemmaMoveGeneratorImpl#getStrategies()}.
     */
    @Test
    void testGetStrategies() {
        assertThat(this.game.getStrategies(),
                is(equalTo(Map.of("A", this.playerAStrategy, "B", this.playerBStrategy))));
    }

    /**
     * Tests {@link GefangenenDilemmaMoveGeneratorImpl#getState()}.
     */
    @Test
    void testGetState() throws Exception {
        assertThat(this.game.getState(), is(equalTo(new GefangenenDilemmaStateImpl(this.playerA, this.playerB))));
    }

    /**
     * Tests a run of the game.
     */
    @Test
    void testRun() throws Exception {
        assertThat(this.game.isStarted(), is(equalTo(false)));
        assertThat(this.game.isFinished(), is(equalTo(false)));

        this.game.start();
        assertThat(this.game.isStarted(), is(equalTo(true)));
        assertThat(this.game.isFinished(), is(equalTo(false)));
        assertThat(this.game.getPlayers().get("A").getAnswer(), is(equalTo(Optional.empty())));
        assertThat(this.game.getPlayers().get("B").getAnswer(), is(equalTo(Optional.empty())));

        this.game.makeMove();
        assertThat(this.game.isStarted(), is(equalTo(true)));
        assertThat(this.game.isFinished(), is(equalTo(false)));
        assertThat(
                this.game.getPlayers().get("A").getAnswer().isPresent()
                        ^ this.game.getPlayers().get("B").getAnswer().isPresent(),
                is(equalTo(true)));

        this.game.makeMove();
        assertThat(this.game.isStarted(), is(equalTo(true)));
        assertThat(this.game.isFinished(), is(equalTo(true)));
        assertThat(this.game.getPlayers().get("A").getAnswer(), is(equalTo(Optional.of(true))));
        assertThat(this.game.getPlayers().get("B").getAnswer(), is(equalTo(Optional.of(false))));
    }
}
