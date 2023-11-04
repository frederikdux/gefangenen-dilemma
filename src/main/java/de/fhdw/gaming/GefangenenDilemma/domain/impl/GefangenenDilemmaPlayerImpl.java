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
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import de.fhdw.gaming.GefangenenDilemma.domain.GefangenenDilemmaPlayer;
import de.fhdw.gaming.core.domain.AbstractPlayer;

/**
 * Implements {@link GefangenenDilemmaPlayer}.
 */
final class GefangenenDilemmaPlayerImpl
        extends AbstractPlayer<GefangenenDilemmaPlayer> implements GefangenenDilemmaPlayer {

    /**
     * The possible outcomes of this player. The key for the first-level map is the answer of the first player, the key
     * for the second-level map is the answer of the second player.
     */
    private final Map<GefangenenDilemmaGameBuilderFactoryImpl.MOVES,
            Map<GefangenenDilemmaGameBuilderFactoryImpl.MOVES, Double>> possibleOutcomes;
    /**
     * The answer of the player.
     */
    private Optional<GefangenenDilemmaGameBuilderFactoryImpl.MOVES> answer;

    /**
     * Creates a Demo player.
     *
     * @param name             The name of the player.
     * @param possibleOutcomes The possible outcomes of this player. The key for the first-level map is the answer of
     *                         the first player, the key for the second-level map is the answer of the second player.
     */
    GefangenenDilemmaPlayerImpl(final String name,
            final Map<GefangenenDilemmaGameBuilderFactoryImpl.MOVES,
                    Map<GefangenenDilemmaGameBuilderFactoryImpl.MOVES, Double>> possibleOutcomes) {
        super(name);
        this.possibleOutcomes = Collections
                .unmodifiableMap(new LinkedHashMap<>(Objects.requireNonNull(possibleOutcomes, "possibleOutcomes")));
        this.answer = Optional.empty();
    }

    /**
     * Creates a Demo player.
     *
     * @param source The {@link GefangenenDilemmaPlayer} to copy.
     */
    GefangenenDilemmaPlayerImpl(final GefangenenDilemmaPlayer source) {
        super(source);
        this.possibleOutcomes = source.getPossibleOutcomes();
        this.answer = source.getAnswer();
    }

    @Override
    public String toString() {
        return String
                .format("DemoPlayer[name=%s, state=%s, outcome=%s, answer=%s]", this.getName(), this.getState(),
                        this.getOutcome(),
                        this.answer);
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof GefangenenDilemmaPlayerImpl) {
            final GefangenenDilemmaPlayerImpl other = (GefangenenDilemmaPlayerImpl) obj;
            return super.equals(obj) && this.answer.equals(other.answer)
                    && this.possibleOutcomes.equals(other.possibleOutcomes);
        }
        return false;
    }

    @SuppressWarnings("PMD.UselessOverridingMethod")
    @Override
    public int hashCode() {
        return super.hashCode() ^ Objects.hash(this.answer, this.possibleOutcomes);
    }

    @Override
    public Map<GefangenenDilemmaGameBuilderFactoryImpl.MOVES,
            Map<GefangenenDilemmaGameBuilderFactoryImpl.MOVES, Double>> getPossibleOutcomes() {
        return this.possibleOutcomes;
    }

    @Override
    public Optional<GefangenenDilemmaGameBuilderFactoryImpl.MOVES> getAnswer() {
        return this.answer;
    }

    @Override
    public void setAnswer(final GefangenenDilemmaGameBuilderFactoryImpl.MOVES newAnswer) {
        if (this.answer.isPresent()) {
            throw new IllegalStateException(String.format("Player %s tried to change her answer.", this.getName()));
        }
        this.answer = Optional.of(newAnswer);
    }

    @Override
    public GefangenenDilemmaPlayer deepCopy() {
        return new GefangenenDilemmaPlayerImpl(this);
    }
}
