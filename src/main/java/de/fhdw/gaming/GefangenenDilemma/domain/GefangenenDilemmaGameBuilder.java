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
package de.fhdw.gaming.GefangenenDilemma.domain;

import de.fhdw.gaming.GefangenenDilemma.moves.GefangenenDilemmaMove;
import de.fhdw.gaming.core.domain.Game;
import de.fhdw.gaming.core.domain.GameBuilder;
import de.fhdw.gaming.core.domain.GameException;
import de.fhdw.gaming.core.domain.ObserverFactoryProvider;

/**
 * A builder which allows to create a Demo game.
 */
public interface GefangenenDilemmaGameBuilder extends GameBuilder {

    /**
     * Creates an {@link GefangenenDilemmaPlayerBuilder} which allows to create and add a player to the game together
     * with her
     * strategy.
     */
    GefangenenDilemmaPlayerBuilder createPlayerBuilder();

    /**
     * Adds a player and her corresponding strategy.
     *
     * @param player   The player.
     * @param strategy The player's strategy.
     * @throws GameException if adding the player is not allowed by the rules of the game.
     */
    GefangenenDilemmaGameBuilder addPlayer(GefangenenDilemmaPlayer player, GefangenenDilemmaStrategy strategy)
            throws GameException;

    /**
     * Changes the {@link ObserverFactoryProvider}.
     *
     * @param newObserverFactoryProvider The new {@link ObserverFactoryProvider}.
     * @return {@code this}
     */
    GefangenenDilemmaGameBuilder changeObserverFactoryProvider(ObserverFactoryProvider newObserverFactoryProvider);

    @Override
    Game<GefangenenDilemmaPlayer, GefangenenDilemmaState, GefangenenDilemmaMove, GefangenenDilemmaStrategy> build(
            int id) throws GameException, InterruptedException;
}
