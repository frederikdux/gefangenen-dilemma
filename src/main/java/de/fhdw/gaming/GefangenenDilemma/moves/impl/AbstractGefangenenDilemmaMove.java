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
package de.fhdw.gaming.GefangenenDilemma.moves.impl;

import de.fhdw.gaming.GefangenenDilemma.moves.GefangenenDilemmaMove;

/**
 * Represents a move allowed by the rules of the game.
 * <p>
 * The purpose of this class is solely to be able to check whether a {@link GefangenenDilemmaMove}
 * implementation is allowed by the
 * rules of the game. As this class is not public, custom strategies are unable to create {@link GefangenenDilemmaMove}
 * objects that
 * inherit from this class, so custom moves can be distinguished from possible moves easily.
 */
public abstract class AbstractGefangenenDilemmaMove implements GefangenenDilemmaMove {

    /**
     * Protected constructor.
     */
    protected AbstractGefangenenDilemmaMove() {
        // nothing to do
    }
}
