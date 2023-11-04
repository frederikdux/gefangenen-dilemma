/*
 * Copyright © 2021-2023 Fachhochschule für die Wirtschaft (FHDW) Hannover
 *
 * This file is part of ipspiel24-demo.
 *
 * Ipspiel24-demo is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * Ipspiel24-demo is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with ipspiel24-demo. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package de.fhdw.gaming.GefangenenDilemma.domain.impl;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import de.fhdw.gaming.GefangenenDilemma.domain.GefangenenDilemmaPlayer;
import de.fhdw.gaming.GefangenenDilemma.domain.GefangenenDilemmaPlayerBuilder;
import de.fhdw.gaming.core.domain.GameException;

/**
 * Implements {@link GefangenenDilemmaPlayerBuilder}.
 */
final class GefangenenDilemmaPlayerBuilderImpl implements GefangenenDilemmaPlayerBuilder {

    /**
     * The name of the player.
     */
    private Optional<String> name;
    /**
     * The possible outcomes of this player. The key for the first-level map is the answer of the first player, the key
     * for the second-level map is the answer of the second player.
     */
    private Optional<Map<GefangenenDilemmaGameBuilderFactoryImpl.MOVES,
            Map<GefangenenDilemmaGameBuilderFactoryImpl.MOVES, Double>>> possibleOutcomes;

    /**
     * Creates an {@link GefangenenDilemmaPlayerBuilderImpl}.
     */
    GefangenenDilemmaPlayerBuilderImpl() {
        this.name = Optional.empty();
        this.possibleOutcomes = Optional.empty();
    }

    @Override
    public GefangenenDilemmaPlayerBuilderImpl changeName(final String newName) {
        this.name = Optional.of(newName);
        return this;
    }

    @Override
    public GefangenenDilemmaPlayerBuilder changePossibleOutcomes(
            final Map<GefangenenDilemmaGameBuilderFactoryImpl.MOVES,
                    Map<GefangenenDilemmaGameBuilderFactoryImpl.MOVES, Double>> newPossibleOutcomes) {
        this.possibleOutcomes = Optional.of(newPossibleOutcomes);
        return this;
    }

    @Override
    public GefangenenDilemmaPlayer build() throws GameException {
        return new GefangenenDilemmaPlayerImpl(
                this.name.orElseThrow(),
                this.checkPossibleOutcomes(this.possibleOutcomes.orElseThrow()));
    }

    /**
     * Checks if all possible outcomes are defined for a player.
     *
     * @param outcomes The possible outcomes for the player.
     */
    private Map<GefangenenDilemmaGameBuilderFactoryImpl.MOVES,
            Map<GefangenenDilemmaGameBuilderFactoryImpl.MOVES, Double>> checkPossibleOutcomes(
                    final Map<GefangenenDilemmaGameBuilderFactoryImpl.MOVES,
                            Map<GefangenenDilemmaGameBuilderFactoryImpl.MOVES, Double>> outcomes) {
        this.checkPossibleOutcome(outcomes, GefangenenDilemmaGameBuilderFactoryImpl.MOVES.SNITCH,
                GefangenenDilemmaGameBuilderFactoryImpl.MOVES.SNITCH);
        this.checkPossibleOutcome(outcomes, GefangenenDilemmaGameBuilderFactoryImpl.MOVES.SNITCH,
                GefangenenDilemmaGameBuilderFactoryImpl.MOVES.REMAINSILENT);
        this.checkPossibleOutcome(outcomes, GefangenenDilemmaGameBuilderFactoryImpl.MOVES.REMAINSILENT,
                GefangenenDilemmaGameBuilderFactoryImpl.MOVES.SNITCH);
        this.checkPossibleOutcome(outcomes, GefangenenDilemmaGameBuilderFactoryImpl.MOVES.REMAINSILENT,
                GefangenenDilemmaGameBuilderFactoryImpl.MOVES.REMAINSILENT);
        return outcomes;
    }

    /**
     * Checks if a given outcome is defined for a player.
     *
     * @param outcomes     The possible outcomes for the player.
     * @param firstChoice  The choice of the first player.
     * @param secondChoice The choice of the second player.
     */
    private void checkPossibleOutcome(
            final Map<GefangenenDilemmaGameBuilderFactoryImpl.MOVES,
                    Map<GefangenenDilemmaGameBuilderFactoryImpl.MOVES, Double>> outcomes,
            final GefangenenDilemmaGameBuilderFactoryImpl.MOVES firstChoice,
            final GefangenenDilemmaGameBuilderFactoryImpl.MOVES secondChoice) {
        if (outcomes.getOrDefault(firstChoice, Collections.emptyMap()).get(secondChoice) == null) {
            throw new IllegalArgumentException(
                    String.format(
                            "No outcome defined for player '%s' and combination %s/%s.",
                            this.name,
                            toString(firstChoice),
                            toString(secondChoice)));
        }
    }

    /**
     * Maps a GefangenenDilemmaGameBuilderFactoryImpl.MOVES value to a "yes" or "no" answer.
     *
     * @param value The value to be mapped.
     */
    private static String toString(final GefangenenDilemmaGameBuilderFactoryImpl.MOVES value) {
        switch (value) {
        case SNITCH:
            return "SNITCH";
        case REMAINSILENT:
            return "REMAINSILENT";
        default:
            return "NaN";
        }
    }
}
