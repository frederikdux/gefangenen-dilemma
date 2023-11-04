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

import de.fhdw.gaming.GefangenenDilemma.domain.GefangenenDilemmaPlayer;
import de.fhdw.gaming.GefangenenDilemma.domain.GefangenenDilemmaState;
import de.fhdw.gaming.GefangenenDilemma.domain.impl.GefangenenDilemmaGameBuilderFactoryImpl;
import de.fhdw.gaming.core.domain.GameException;

/**
 * Says "yes".
 */
public class GefangenenDilemmaStaySilentMove extends AbstractGefangenenDilemmaMove {

    @Override
    public void applyTo(final GefangenenDilemmaState state, final GefangenenDilemmaPlayer player) throws GameException {
        player.setAnswer(GefangenenDilemmaGameBuilderFactoryImpl.MOVES.REMAINSILENT);
    }

    @Override
    public String toString() {
        return "Stay silent";
    }
}