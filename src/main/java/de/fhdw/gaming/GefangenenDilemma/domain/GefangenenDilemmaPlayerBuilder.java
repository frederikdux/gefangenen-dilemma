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

import java.util.Map;

import de.fhdw.gaming.GefangenenDilemma.domain.impl.GefangenenDilemmaGameBuilderFactoryImpl;
import de.fhdw.gaming.core.domain.GameException;

/**
 * A builder which allows to create a Demo player.
 */
public interface GefangenenDilemmaPlayerBuilder {

    /**
     * Changes the name of the player.
     * <p>
     * There is no default.
     *
     * @param newName The name of the player.
     * @return {@code this}
     */
    GefangenenDilemmaPlayerBuilder changeName(String newName);

    /**
     * Changes the possible outcomes of the player.
     * <p>
     * There is no default.
     *
     * @param possibleOutcomes The possible outcomes of the player. The key for the first-level map is the answer of the
     *                         first player, the key for the second-level map is the answer of the second player.
     */
    GefangenenDilemmaPlayerBuilder changePossibleOutcomes(
            @SuppressWarnings("exports") Map<GefangenenDilemmaGameBuilderFactoryImpl.MOVES,
                    Map<GefangenenDilemmaGameBuilderFactoryImpl.MOVES, Double>> possibleOutcomes);

    /**
     * Builds the player.
     *
     * @return The Demo player.
     * @throws GameException if creating the player is not allowed by the rules of the game.
     */
    GefangenenDilemmaPlayer build() throws GameException;
}
