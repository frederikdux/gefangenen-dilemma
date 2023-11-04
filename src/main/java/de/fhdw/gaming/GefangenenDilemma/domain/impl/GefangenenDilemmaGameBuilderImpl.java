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
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import de.fhdw.gaming.GefangenenDilemma.domain.GefangenenDilemmaGameBuilder;
import de.fhdw.gaming.GefangenenDilemma.domain.GefangenenDilemmaPlayer;
import de.fhdw.gaming.GefangenenDilemma.domain.GefangenenDilemmaPlayerBuilder;
import de.fhdw.gaming.GefangenenDilemma.domain.GefangenenDilemmaState;
import de.fhdw.gaming.GefangenenDilemma.domain.GefangenenDilemmaStrategy;
import de.fhdw.gaming.GefangenenDilemma.moves.GefangenenDilemmaMove;
import de.fhdw.gaming.GefangenenDilemma.moves.impl.AbstractGefangenenDilemmaMove;
import de.fhdw.gaming.core.domain.DefaultGame;
import de.fhdw.gaming.core.domain.DefaultObserverFactoryProvider;
import de.fhdw.gaming.core.domain.Game;
import de.fhdw.gaming.core.domain.GameBuilder;
import de.fhdw.gaming.core.domain.GameException;
import de.fhdw.gaming.core.domain.ObserverFactoryProvider;

/**
 * Implements {@link GefangenenDilemmaGameBuilder}.
 */
final class GefangenenDilemmaGameBuilderImpl implements GefangenenDilemmaGameBuilder {

    /**
     * The {@link ObserverFactoryProvider}.
     */
    private ObserverFactoryProvider observerFactoryProvider;
    /**
     * The player using black tokens.
     */
    private Optional<GefangenenDilemmaPlayer> firstPlayer;
    /**
     * The strategy of the player using black tokens.
     */
    private Optional<GefangenenDilemmaStrategy> firstPlayerStrategy;
    /**
     * The player using white tokens.
     */
    private Optional<GefangenenDilemmaPlayer> secondPlayer;
    /**
     * The strategy of the player using white tokens.
     */
    private Optional<GefangenenDilemmaStrategy> secondPlayerStrategy;
    /**
     * The maximum computation time per move in seconds.
     */
    private int maxComputationTimePerMove;

    /**
     * Creates a Demo game builder.
     */
    GefangenenDilemmaGameBuilderImpl() {
        this.observerFactoryProvider = new DefaultObserverFactoryProvider();
        this.firstPlayer = Optional.empty();
        this.firstPlayerStrategy = Optional.empty();
        this.secondPlayer = Optional.empty();
        this.secondPlayerStrategy = Optional.empty();
        this.maxComputationTimePerMove = GameBuilder.DEFAULT_MAX_COMPUTATION_TIME_PER_MOVE;
    }

    @Override
    public GefangenenDilemmaPlayerBuilder createPlayerBuilder() {
        return new GefangenenDilemmaPlayerBuilderImpl();
    }

    @Override
    public GefangenenDilemmaGameBuilder addPlayer(final GefangenenDilemmaPlayer player,
            final GefangenenDilemmaStrategy strategy)
            throws GameException {

        if (this.firstPlayer.isEmpty()) {
            this.firstPlayer = Optional.of(Objects.requireNonNull(player, "player"));
            this.firstPlayerStrategy = Optional.of(Objects.requireNonNull(strategy, "firstPlayerStrategy"));
        } else if (this.secondPlayer.isEmpty()) {
            this.secondPlayer = Optional.of(Objects.requireNonNull(player, "player"));
            this.secondPlayerStrategy = Optional.of(Objects.requireNonNull(strategy, "secondPlayerStrategy"));
        } else {
            throw new GameException(String.format("More than two players are now allowed."));
        }
        return this;
    }

    @Override
    public GefangenenDilemmaGameBuilder changeMaximumComputationTimePerMove(final int newMaxComputationTimePerMove) {
        this.maxComputationTimePerMove = newMaxComputationTimePerMove;
        return this;
    }

    @Override
    public GefangenenDilemmaGameBuilder changeObserverFactoryProvider(
            final ObserverFactoryProvider newObserverFactoryProvider) {
        this.observerFactoryProvider = newObserverFactoryProvider;
        return this;
    }

    @Override
    public Game<GefangenenDilemmaPlayer, GefangenenDilemmaState, GefangenenDilemmaMove,
            GefangenenDilemmaStrategy> build(final int id)
                    throws GameException, InterruptedException {
        if (!this.firstPlayer.isPresent() || !this.secondPlayer.isPresent()) {
            throw new GameException("A Demo game needs two players.");
        }

        final GefangenenDilemmaStateImpl initialState = new GefangenenDilemmaStateImpl(this.firstPlayer.get(),
                this.secondPlayer.get());

        final Map<String, GefangenenDilemmaStrategy> strategies = new LinkedHashMap<>();
        strategies.put(initialState.getFirstPlayer().getName(), this.firstPlayerStrategy.orElseThrow());
        strategies.put(initialState.getSecondPlayer().getName(), this.secondPlayerStrategy.orElseThrow());
        return new DefaultGame<>(
                id,
                initialState,
                strategies,
                this.maxComputationTimePerMove,
                AbstractGefangenenDilemmaMove.class::isInstance,
                new GefangenenDilemmaMoveGeneratorImpl(),
                this.observerFactoryProvider);
    }
}
