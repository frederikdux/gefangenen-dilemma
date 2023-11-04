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

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import de.fhdw.gaming.GefangenenDilemma.domain.GefangenenDilemmaPlayer;
import de.fhdw.gaming.GefangenenDilemma.domain.GefangenenDilemmaState;
import de.fhdw.gaming.core.domain.GameException;
import de.fhdw.gaming.core.domain.PlayerState;

/**
 * Implements {@link GefangenenDilemmaState}.
 */
final class GefangenenDilemmaStateImpl implements GefangenenDilemmaState {

    /**
     * The first player.
     */
    private final GefangenenDilemmaPlayer firstPlayer;
    /**
     * The second player.
     */
    private final GefangenenDilemmaPlayer secondPlayer;

    /**
     * Creates a Demo state.
     *
     * @param firstPlayer  The first player.
     * @param secondPlayer The second player.
     * @throws GameException if the state cannot be created according to the rules of the game.
     */
    GefangenenDilemmaStateImpl(final GefangenenDilemmaPlayer firstPlayer, final GefangenenDilemmaPlayer secondPlayer)
            throws GameException {

        this.firstPlayer = Objects.requireNonNull(firstPlayer, "firstPlayer");
        this.secondPlayer = Objects.requireNonNull(secondPlayer, "secondPlayer");

        if (this.firstPlayer.getName().equals(this.secondPlayer.getName())) {
            throw new IllegalArgumentException(
                    String.format("Both players have the same name '%s'.", this.firstPlayer.getName()));
        }
    }

    /**
     * Creates a Demo state by copying an existing one.
     *
     * @param source The state to copy.
     */
    GefangenenDilemmaStateImpl(final GefangenenDilemmaStateImpl source) {
        this.firstPlayer = source.firstPlayer.deepCopy();
        this.secondPlayer = source.secondPlayer.deepCopy();
    }

    /**
     * Returns the first player.
     */
    @Override
    public GefangenenDilemmaPlayer getFirstPlayer() {
        return this.firstPlayer;
    }

    /**
     * Returns the second player.
     */
    @Override
    public GefangenenDilemmaPlayer getSecondPlayer() {
        return this.secondPlayer;
    }

    @Override
    public String toString() {
        return String.format("DemoState[firstPlayer=%s, secondPlayer=%s]", this.firstPlayer, this.secondPlayer);
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof GefangenenDilemmaStateImpl) {
            final GefangenenDilemmaStateImpl other = (GefangenenDilemmaStateImpl) obj;
            return this.firstPlayer.equals(other.firstPlayer) && this.secondPlayer.equals(other.secondPlayer);
        }
        return false;
    }

    @Override
    public GefangenenDilemmaState deepCopy() {
        return new GefangenenDilemmaStateImpl(this);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.firstPlayer, this.secondPlayer);
    }

    @Override
    public Map<String, GefangenenDilemmaPlayer> getPlayers() {
        final Map<String, GefangenenDilemmaPlayer> result = new LinkedHashMap<>();
        result.put(this.firstPlayer.getName(), this.firstPlayer);
        result.put(this.secondPlayer.getName(), this.secondPlayer);
        return result;
    }

    @Override
    public Set<GefangenenDilemmaPlayer> computeNextPlayers() {
        final Set<GefangenenDilemmaPlayer> playersWithoutMove = new LinkedHashSet<>();
        if (this.firstPlayer.getAnswer().isEmpty()) {
            playersWithoutMove.add(this.firstPlayer);
        }
        if (this.secondPlayer.getAnswer().isEmpty()) {
            playersWithoutMove.add(this.secondPlayer);
        }
        return playersWithoutMove;
    }

    @Override
    public void nextTurn() {
        final Set<GefangenenDilemmaPlayer> playersWithoutMove = this.computeNextPlayers();
        if (playersWithoutMove.isEmpty()) {
            final GefangenenDilemmaGameBuilderFactoryImpl.MOVES answerOfFirstPlayer = this.firstPlayer.getAnswer()
                    .orElseThrow();
            final GefangenenDilemmaGameBuilderFactoryImpl.MOVES answerOfSecondPlayer = this.secondPlayer.getAnswer()
                    .orElseThrow();

            final Double outcomeOfFirstPlayer = this.firstPlayer.getPossibleOutcomes().get(answerOfFirstPlayer)
                    .get(answerOfSecondPlayer);
            this.firstPlayer.setState(outcomeToState(outcomeOfFirstPlayer));
            this.firstPlayer.setOutcome(outcomeOfFirstPlayer);

            final Double outcomeOfSecondPlayer = this.secondPlayer.getPossibleOutcomes().get(answerOfFirstPlayer)
                    .get(answerOfSecondPlayer);
            this.secondPlayer.setState(outcomeToState(outcomeOfSecondPlayer));
            this.secondPlayer.setOutcome(outcomeOfSecondPlayer);
        }
    }

    /**
     * Computes a player state from an outcome.
     *
     * @param outcome The player's outcome.
     */
    private static PlayerState outcomeToState(final Double outcome) {
        return outcome > 0.0 ? PlayerState.WON : outcome < 0.0 ? PlayerState.LOST : PlayerState.DRAW;
    }
}
