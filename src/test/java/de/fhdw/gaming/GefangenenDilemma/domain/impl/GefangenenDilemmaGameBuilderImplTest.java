package de.fhdw.gaming.GefangenenDilemma.domain.impl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.util.Map;

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
 * Tests {@link GefangenenDilemmaGameBuilderImpl}.
 */
final class GefangenenDilemmaGameBuilderImplTest {

    /**
     * Tests {@link GefangenenDilemmaGameBuilderImpl#build()}.
     */
    @Test
    void testBuild() throws Exception {
        final Map<Boolean, Map<Boolean, Double>> possibleOutcomes = Map.of(
                false, Map.of(false, -2.0d, true, 0.0d),
                true, Map.of(false, 1.0d, true, 2.0d));

        final GefangenenDilemmaMoveFactory moveFactory = new GefangenenDilemmaDefaultMoveFactory();
        final GefangenenDilemmaPlayer playerA = new GefangenenDilemmaPlayerBuilderImpl().changeName("A")
                .changePossibleOutcomes(possibleOutcomes)
                .build();
        final GefangenenDilemmaPlayer playerB = new GefangenenDilemmaPlayerBuilderImpl().changeName("B")
                .changePossibleOutcomes(possibleOutcomes)
                .build();
        final GefangenenDilemmaStrategy playerAStrategy = new GefangenenDilemmaStaySilentStrategyFactory()
                .create(moveFactory);
        final GefangenenDilemmaStrategy playerBStrategy = new GefangenenDilemmaSnitchStrategyFactory()
                .create(moveFactory);
        try (Game<GefangenenDilemmaPlayer, GefangenenDilemmaState, GefangenenDilemmaMove,
                GefangenenDilemmaStrategy> game = new GefangenenDilemmaGameBuilderImpl()
                        .changeMaximumComputationTimePerMove(60)
                        .addPlayer(playerA, playerAStrategy)
                        .addPlayer(playerB, playerBStrategy)
                        .build(1)) {
            assertThat(game.getId(), is(equalTo(1)));
            assertThat(game.getState(), is(equalTo(new GefangenenDilemmaStateImpl(playerA, playerB))));
            assertThat(game.getPlayers(), is(equalTo(Map.of("A", playerA, "B", playerB))));
            assertThat(game.getStrategies(), is(equalTo(Map.of("A", playerAStrategy, "B", playerBStrategy))));
        }
    }
}
