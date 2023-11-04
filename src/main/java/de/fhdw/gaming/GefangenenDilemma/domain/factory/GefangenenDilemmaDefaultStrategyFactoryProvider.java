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
package de.fhdw.gaming.GefangenenDilemma.domain.factory;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

/**
 * Implements {@link GefangenenDilemmaStrategyFactoryProvider} by using the {@link ServiceLoader}.
 */
public final class GefangenenDilemmaDefaultStrategyFactoryProvider implements GefangenenDilemmaStrategyFactoryProvider {

    @Override
    public List<GefangenenDilemmaStrategyFactory> getStrategyFactories() {
        final ServiceLoader<
                GefangenenDilemmaStrategyFactory> services = ServiceLoader.load(GefangenenDilemmaStrategyFactory.class);
        final List<GefangenenDilemmaStrategyFactory> list = new ArrayList<>();
        services.iterator().forEachRemaining(list::add);
        return list;
    }
}
