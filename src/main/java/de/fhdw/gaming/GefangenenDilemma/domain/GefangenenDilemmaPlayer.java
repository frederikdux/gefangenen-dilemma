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
import java.util.Optional;

import de.fhdw.gaming.GefangenenDilemma.domain.impl.GefangenenDilemmaGameBuilderFactoryImpl;
import de.fhdw.gaming.core.domain.Player;

/**
 * Represents a Demo player.
 */
public interface GefangenenDilemmaPlayer extends Player<GefangenenDilemmaPlayer> {

    /**
     * Returns the possible outcomes of this player. The key for the first-level map is the answer of the first player,
     * the key for the second-level map is the answer of the second player.
     */
    Map<GefangenenDilemmaGameBuilderFactoryImpl.MOVES,
            Map<GefangenenDilemmaGameBuilderFactoryImpl.MOVES, Double>> getPossibleOutcomes();

    /**
     * Returns the answer of this player.
     */
    Optional<GefangenenDilemmaGameBuilderFactoryImpl.MOVES> getAnswer();

    /**
     * Sets the answer of this player.
     *
     * @param newAnswer The answer to set. {@code true} means "yes", {@code false} means "no"
     * @throws IllegalStateException if an answer has already been set.
     */
    void setAnswer(GefangenenDilemmaGameBuilderFactoryImpl.MOVES newAnswer);

    @Override
    GefangenenDilemmaPlayer deepCopy();
}
