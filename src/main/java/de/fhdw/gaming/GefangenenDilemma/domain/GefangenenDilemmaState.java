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

import de.fhdw.gaming.core.domain.State;

/**
 * Represents the state of a Demo game.
 */
public interface GefangenenDilemmaState extends State<GefangenenDilemmaPlayer, GefangenenDilemmaState> {

    /**
     * Returns the first player.
     */
    GefangenenDilemmaPlayer getFirstPlayer();

    /**
     * Returns the second player.
     */
    GefangenenDilemmaPlayer getSecondPlayer();
}
