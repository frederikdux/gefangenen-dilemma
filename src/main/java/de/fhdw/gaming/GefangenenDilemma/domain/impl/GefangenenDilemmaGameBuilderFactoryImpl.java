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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;

import de.fhdw.gaming.GefangenenDilemma.domain.GefangenenDilemmaGameBuilder;
import de.fhdw.gaming.GefangenenDilemma.domain.GefangenenDilemmaGameBuilderFactory;
import de.fhdw.gaming.GefangenenDilemma.domain.GefangenenDilemmaPlayer;
import de.fhdw.gaming.GefangenenDilemma.domain.GefangenenDilemmaPlayerBuilder;
import de.fhdw.gaming.GefangenenDilemma.domain.GefangenenDilemmaStrategy;
import de.fhdw.gaming.GefangenenDilemma.domain.factory.GefangenenDilemmaDefaultStrategyFactoryProvider;
import de.fhdw.gaming.GefangenenDilemma.domain.factory.GefangenenDilemmaStrategyFactory;
import de.fhdw.gaming.GefangenenDilemma.domain.factory.GefangenenDilemmaStrategyFactoryProvider;
import de.fhdw.gaming.GefangenenDilemma.moves.factory.GefangenenDilemmaMoveFactory;
import de.fhdw.gaming.GefangenenDilemma.moves.impl.GefangenenDilemmaDefaultMoveFactory;
import de.fhdw.gaming.core.domain.GameBuilder;
import de.fhdw.gaming.core.domain.GameBuilderFactory;
import de.fhdw.gaming.core.domain.GameException;
import de.fhdw.gaming.core.domain.Strategy;
import de.fhdw.gaming.core.ui.InputProvider;
import de.fhdw.gaming.core.ui.InputProviderException;
import de.fhdw.gaming.core.ui.type.validator.MaxValueValidator;
import de.fhdw.gaming.core.ui.type.validator.MinValueValidator;
import de.fhdw.gaming.core.ui.type.validator.PatternValidator;

/**
 * Implements {@link GameBuilderFactory} by creating a Demo game builder.
 */
public final class GefangenenDilemmaGameBuilderFactoryImpl implements GefangenenDilemmaGameBuilderFactory {

    /**
     * The number of players.
     */
    private static final int NUMBER_OF_PLAYERS = 2;
    /**
     * Smallest allowed maximum computation time per move in seconds.
     */
    private static final int MIN_MAX_COMPUTATION_TIME_PER_MOVE = 1;
    /**
     * Largest allowed maximum computation time per move in seconds.
     */
    private static final int MAX_MAX_COMPUTATION_TIME_PER_MOVE = 3600;

    /**
     * Implements {@link MOVES}.
     */
    public static enum MOVES {
        /**
         * Implements{@link SNITCH_MOVE}.
         */
        SNITCH,
        /**
         * Implements{@link REMAINSILENT_MOVE}.
         */
        REMAINSILENT
    }

    /**
     * All available Demo strategies.
     */
    private final Set<GefangenenDilemmaStrategy> strategies;

    /**
     * Creates a Demo game factory. Demo strategies are loaded by using the {@link java.util.ServiceLoader}.
     * <p>
     * This constructor is meant to be used by the {@link java.util.ServiceLoader}.
     */
    public GefangenenDilemmaGameBuilderFactoryImpl() {
        this(new GefangenenDilemmaDefaultStrategyFactoryProvider());
    }

    /**
     * Creates a Demo game factory.
     *
     * @param strategyFactoryProvider The {@link GefangenenDilemmaStrategyFactoryProvider} for loading Demo strategies.
     */
    GefangenenDilemmaGameBuilderFactoryImpl(final GefangenenDilemmaStrategyFactoryProvider strategyFactoryProvider) {
        final GefangenenDilemmaMoveFactory moveFactory = new GefangenenDilemmaDefaultMoveFactory();

        final List<GefangenenDilemmaStrategyFactory> factories = strategyFactoryProvider.getStrategyFactories();
        this.strategies = new LinkedHashSet<>();
        for (final GefangenenDilemmaStrategyFactory factory : factories) {
            this.strategies.add(factory.create(moveFactory));
        }
    }

    @Override
    public String getName() {
        return "GefangenenDilemma";
    }

    @Override
    public int getMinimumNumberOfPlayers() {
        return GefangenenDilemmaGameBuilderFactoryImpl.NUMBER_OF_PLAYERS;
    }

    @Override
    public int getMaximumNumberOfPlayers() {
        return GefangenenDilemmaGameBuilderFactoryImpl.NUMBER_OF_PLAYERS;
    }

    @Override
    public List<? extends Strategy<?, ?, ?>> getStrategies() {
        return new ArrayList<>(this.strategies);
    }

    @Override
    public GefangenenDilemmaGameBuilder createGameBuilder(final InputProvider inputProvider) throws GameException {
        try {
            final GefangenenDilemmaGameBuilder gameBuilder = new GefangenenDilemmaGameBuilderImpl();

            @SuppressWarnings("unchecked")
            final Map<String,
                    Object> gameData = inputProvider.needInteger(
                            GameBuilderFactory.PARAM_MAX_COMPUTATION_TIME_PER_MOVE,
                            "Maximum computation time per move in seconds",
                            Optional.of(GameBuilder.DEFAULT_MAX_COMPUTATION_TIME_PER_MOVE),
                            new MinValueValidator<>(
                                    GefangenenDilemmaGameBuilderFactoryImpl.MIN_MAX_COMPUTATION_TIME_PER_MOVE),
                            new MaxValueValidator<>(
                                    GefangenenDilemmaGameBuilderFactoryImpl.MAX_MAX_COMPUTATION_TIME_PER_MOVE))
                            .requestData("Game properties");

            gameBuilder.changeMaximumComputationTimePerMove(
                    (Integer) gameData.get(GameBuilderFactory.PARAM_MAX_COMPUTATION_TIME_PER_MOVE));

            final InputProvider firstPlayerInputProvider = inputProvider.getNext(gameData);
            final Map<String, Object> firstPlayerData = this.requestPlayerData(firstPlayerInputProvider, "Player 1");
            final GefangenenDilemmaPlayer firstPlayer = this.createPlayer(gameBuilder.createPlayerBuilder(),
                    firstPlayerData);
            final GefangenenDilemmaStrategy firstPlayerStrategy = this.getStrategy(firstPlayerData);
            gameBuilder.addPlayer(firstPlayer, firstPlayerStrategy);

            final InputProvider secondPlayerInputProvider = firstPlayerInputProvider.getNext(firstPlayerData);
            final Map<String, Object> secondPlayerData = this.requestPlayerData(secondPlayerInputProvider, "Player 2");
            final GefangenenDilemmaPlayer secondPlayer = this.createPlayer(gameBuilder.createPlayerBuilder(),
                    secondPlayerData);
            final GefangenenDilemmaStrategy secondPlayerStrategy = this.getStrategy(secondPlayerData);
            gameBuilder.addPlayer(secondPlayer, secondPlayerStrategy);

            return gameBuilder;
        } catch (final InputProviderException e) {
            throw new GameException(String.format("Creating Demo game was aborted: %s", e.getMessage()), e);
        }
    }

    /**
     * Returns data for a player builder.
     *
     * @param inputProvider The input provider.
     * @param title         The title for the UI.
     * @throws InputProviderException if the operation has been aborted prematurely (e.g. if the user cancelled a
     *                                dialog).
     */
    @SuppressWarnings("unchecked")
    private Map<String, Object> requestPlayerData(final InputProvider inputProvider, final String title)
            throws GameException, InputProviderException {

        inputProvider
                .needString(
                        GameBuilderFactory.PARAM_PLAYER_NAME,
                        "Name",
                        Optional.empty(),
                        new PatternValidator(Pattern.compile("\\S+(\\s+\\S+)*")))
                .needInteger(
                        GefangenenDilemmaGameBuilderFactory.PARAM_PLAYER_OUTCOME_ON_SNITCH_SNITCH,
                        "Player's outcome on No/No",
                        Optional.of(-8))
                .needInteger(
                        GefangenenDilemmaGameBuilderFactory.PARAM_PLAYER_OUTCOME_ON_SNITCH_STAYSILENT,
                        "Player's outcome on No/Yes",
                        Optional.of(0))
                .needInteger(
                        GefangenenDilemmaGameBuilderFactory.PARAM_PLAYER_OUTCOME_ON_STAYSILENT_SNITCH,
                        "Player's outcome on Yes/No",
                        Optional.of(-10))
                .needInteger(
                        GefangenenDilemmaGameBuilderFactory.PARAM_PLAYER_OUTCOME_ON_STAYSILENT_STAYSILENT,
                        "Player's outcome on Yes/Yes",
                        Optional.of(-1))
                .needObject(GameBuilderFactory.PARAM_PLAYER_STRATEGY, "Strategy", Optional.empty(), this.strategies);

        return inputProvider.requestData(title);
    }

    /**
     * Creates a Demo player.
     *
     * @param playerBuilder The player builder.
     * @param playerData    The requested player data.
     * @return The created {@link GefangenenDilemmaPlayer}.
     * @throws InputProviderException if the operation has been aborted prematurely (e.g. if the user cancelled a
     *                                dialog).
     */
    private GefangenenDilemmaPlayer createPlayer(final GefangenenDilemmaPlayerBuilder playerBuilder,
            final Map<String, Object> playerData) throws GameException, InputProviderException {

        final Map<MOVES, Map<MOVES, Double>> possibleOutcomes = new LinkedHashMap<>();

        final Map<MOVES, Double> possibleOutcomesSnitch = new LinkedHashMap<>();
        possibleOutcomesSnitch.put(
                MOVES.SNITCH,
                (double) (Integer) playerData
                        .get(GefangenenDilemmaGameBuilderFactory.PARAM_PLAYER_OUTCOME_ON_SNITCH_SNITCH));
        possibleOutcomesSnitch.put(
                MOVES.REMAINSILENT,
                (double) (Integer) playerData
                        .get(GefangenenDilemmaGameBuilderFactory.PARAM_PLAYER_OUTCOME_ON_SNITCH_STAYSILENT));
        possibleOutcomes.put(MOVES.SNITCH, possibleOutcomesSnitch);

        final Map<MOVES, Double> possibleOutcomesStaySilent = new LinkedHashMap<>();
        possibleOutcomesStaySilent.put(
                MOVES.SNITCH,
                (double) (Integer) playerData
                        .get(GefangenenDilemmaGameBuilderFactory.PARAM_PLAYER_OUTCOME_ON_STAYSILENT_SNITCH));
        possibleOutcomesStaySilent.put(
                MOVES.REMAINSILENT,
                (double) (Integer) playerData
                        .get(GefangenenDilemmaGameBuilderFactory.PARAM_PLAYER_OUTCOME_ON_STAYSILENT_STAYSILENT));
        possibleOutcomes.put(MOVES.REMAINSILENT, possibleOutcomesStaySilent);
        return playerBuilder.changeName((String) playerData.get(GameBuilderFactory.PARAM_PLAYER_NAME))
                .changePossibleOutcomes(possibleOutcomes).build();
    }

    /**
     * Returns a Demo strategy.
     *
     * @param playerData The requested player data.
     * @return The Demo strategy.
     */
    private GefangenenDilemmaStrategy getStrategy(final Map<String, Object> playerData) {
        return (GefangenenDilemmaStrategy) playerData.get(GameBuilderFactory.PARAM_PLAYER_STRATEGY);
    }
}
