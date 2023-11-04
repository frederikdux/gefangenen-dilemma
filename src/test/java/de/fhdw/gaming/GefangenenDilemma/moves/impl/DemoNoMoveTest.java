package de.fhdw.gaming.GefangenenDilemma.moves.impl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.fhdw.gaming.GefangenenDilemma.domain.GefangenenDilemmaGameBuilder;
import de.fhdw.gaming.GefangenenDilemma.domain.GefangenenDilemmaGameBuilderFactory;
import de.fhdw.gaming.GefangenenDilemma.domain.GefangenenDilemmaPlayer;
import de.fhdw.gaming.GefangenenDilemma.domain.GefangenenDilemmaState;
import de.fhdw.gaming.GefangenenDilemma.domain.GefangenenDilemmaStrategy;
import de.fhdw.gaming.GefangenenDilemma.domain.impl.GefangenenDilemmaGameBuilderFactoryImpl;
import de.fhdw.gaming.GefangenenDilemma.moves.GefangenenDilemmaMove;
import de.fhdw.gaming.GefangenenDilemma.moves.factory.GefangenenDilemmaMoveFactory;
import de.fhdw.gaming.GefangenenDilemma.strategy.GefangenenDilemmaSnitchStrategyFactory;
import de.fhdw.gaming.core.domain.Game;
import de.fhdw.gaming.core.domain.GameBuilderFactory;
import de.fhdw.gaming.core.domain.util.FunctionE;
import de.fhdw.gaming.core.ui.InputProvider;
import de.fhdw.gaming.core.ui.InputProviderException;
import de.fhdw.gaming.core.ui.util.ChainedInputProvider;
import de.fhdw.gaming.core.ui.util.NonInteractiveInputProvider;

/**
 * Tests {@link GefangenenDilemmaSnitchMove}.
 */
class DemoSNITCHMoveTest {

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
        final GefangenenDilemmaMoveFactory moveFactory = new GefangenenDilemmaDefaultMoveFactory();
        final GefangenenDilemmaStrategy playerAStrategy = new GefangenenDilemmaSnitchStrategyFactory()
                .create(moveFactory);
        final GefangenenDilemmaStrategy playerBStrategy = new GefangenenDilemmaSnitchStrategyFactory()
                .create(moveFactory);

        final GefangenenDilemmaGameBuilderFactory factory = new GefangenenDilemmaGameBuilderFactoryImpl();
        final FunctionE<Map<String, Object>, InputProvider, InputProviderException> nextInputProvider = (
                final Map<String, Object> gameDataSet) -> new ChainedInputProvider(
                        new NonInteractiveInputProvider()
                                .fixedString(GameBuilderFactory.PARAM_PLAYER_NAME, "A")
                                .fixedObject(GameBuilderFactory.PARAM_PLAYER_STRATEGY, playerAStrategy)
                                .fixedInteger(
                                        GefangenenDilemmaGameBuilderFactory.PARAM_PLAYER_OUTCOME_ON_SNITCH_SNITCH,
                                        -8)
                                .fixedInteger(
                                        GefangenenDilemmaGameBuilderFactory.PARAM_PLAYER_OUTCOME_ON_SNITCH_STAYSILENT,
                                        0)
                                .fixedInteger(
                                        GefangenenDilemmaGameBuilderFactory.PARAM_PLAYER_OUTCOME_ON_STAYSILENT_SNITCH,
                                        -10)
                                .fixedInteger(
                                        GefangenenDilemmaGameBuilderFactory.PARAM_PLAYER_OUTCOME_ON_STAYSILENT_STAYSILENT,
                                        -1),
                        (final Map<String, Object> firstPlayerDataSet) -> new NonInteractiveInputProvider()
                                .fixedString(GameBuilderFactory.PARAM_PLAYER_NAME, "B")
                                .fixedObject(GameBuilderFactory.PARAM_PLAYER_STRATEGY, playerBStrategy)
                                .fixedInteger(
                                        GefangenenDilemmaGameBuilderFactory.PARAM_PLAYER_OUTCOME_ON_SNITCH_SNITCH,
                                        -8)
                                .fixedInteger(
                                        GefangenenDilemmaGameBuilderFactory.PARAM_PLAYER_OUTCOME_ON_SNITCH_STAYSILENT,
                                        0)
                                .fixedInteger(
                                        GefangenenDilemmaGameBuilderFactory.PARAM_PLAYER_OUTCOME_ON_STAYSILENT_SNITCH,
                                        -10)
                                .fixedInteger(
                                        GefangenenDilemmaGameBuilderFactory.PARAM_PLAYER_OUTCOME_ON_STAYSILENT_STAYSILENT,
                                        -1));
        final GefangenenDilemmaGameBuilder gameBuilder = factory.createGameBuilder(
                new ChainedInputProvider(
                        new NonInteractiveInputProvider()
                                .fixedInteger(GameBuilderFactory.PARAM_MAX_COMPUTATION_TIME_PER_MOVE, 60),
                        nextInputProvider));
        this.game = gameBuilder.build(1);
    }

    /**
     * Tests
     * {@link GefangenenDilemmaSnitchMove#applyTo
     * (de.fhdw.gaming.GefangenenDilemma.domain.GefangenenDilemmaState, GefangenenDilemmaPlayer)}.
     */
    @Test
    void testApplyTo() throws Exception {
        assertThat(this.getPlayerA().getAnswer(), is(equalTo(Optional.empty())));
        new GefangenenDilemmaSnitchMove().applyTo(this.game.getState(), this.getPlayerA());
        assertThat(this.getPlayerA().getAnswer(), is(equalTo(Optional.of(false))));
        new GefangenenDilemmaSnitchMove().applyTo(this.game.getState(), this.getPlayerB());
        assertThat(this.getPlayerB().getAnswer(), is(equalTo(Optional.of(false))));
    }

    /**
     * Tests {@link GefangenenDilemmaSnitchMove#toString()}.
     */
    @Test
    void testToString() {
        assertThat(new GefangenenDilemmaSnitchMove().toString(), is(equalTo("Saying no")));
    }

    /**
     * Returns player A.
     */
    private GefangenenDilemmaPlayer getPlayerA() {
        return this.game.getPlayers().get("A");
    }

    /**
     * Returns player B.
     */
    private GefangenenDilemmaPlayer getPlayerB() {
        return this.game.getPlayers().get("B");
    }
}
