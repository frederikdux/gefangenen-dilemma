package de.fhdw.gaming.GefangenenDilemma.domain.impl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.util.Map;

import org.junit.jupiter.api.Test;

import de.fhdw.gaming.GefangenenDilemma.domain.GefangenenDilemmaPlayer;

/**
 * Tests {@link GefangenenDilemmaPlayerBuilderImpl}.
 */
final class GefangenenDilemmaPlayerBuilderImplTest {

    /**
     * Tests {@link GefangenenDilemmaPlayerBuilderImpl#build()}.
     */
    @Test
    void testBuild() throws Exception {
        final Map<Boolean, Map<Boolean, Double>> possibleOutcomes = Map.of(
                false, Map.of(false, -2.0d, true, 0.0d),
                true, Map.of(false, 1.0d, true, 2.0d));

        final GefangenenDilemmaPlayer player = new GefangenenDilemmaPlayerBuilderImpl().changeName("A")
                .changePossibleOutcomes(possibleOutcomes)
                .build();
        assertThat(player, is(equalTo(new GefangenenDilemmaPlayerImpl("A", possibleOutcomes))));
    }
}
