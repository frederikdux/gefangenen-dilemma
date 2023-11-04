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
module de.fhdw.gaming.GefangenenDilemma {
    exports de.fhdw.gaming.GefangenenDilemma.domain;
    exports de.fhdw.gaming.GefangenenDilemma.domain.factory;
    exports de.fhdw.gaming.GefangenenDilemma.moves;
    exports de.fhdw.gaming.GefangenenDilemma.moves.factory;

    requires transitive de.fhdw.gaming.core;

    uses de.fhdw.gaming.GefangenenDilemma.domain.factory.GefangenenDilemmaStrategyFactory;

    provides de.fhdw.gaming.GefangenenDilemma.domain.factory.GefangenenDilemmaStrategyFactory
            with de.fhdw.gaming.GefangenenDilemma.strategy.GefangenenDilemmaStaySilentStrategyFactory,
            de.fhdw.gaming.GefangenenDilemma.strategy.GefangenenDilemmaSnitchStrategyFactory;

    provides de.fhdw.gaming.core.domain.GameBuilderFactory
            with de.fhdw.gaming.GefangenenDilemma.domain.impl.GefangenenDilemmaGameBuilderFactoryImpl;

    opens de.fhdw.gaming.GefangenenDilemma.domain;
    opens de.fhdw.gaming.GefangenenDilemma.domain.impl;
    opens de.fhdw.gaming.GefangenenDilemma.moves;
    opens de.fhdw.gaming.GefangenenDilemma.moves.impl;
}
