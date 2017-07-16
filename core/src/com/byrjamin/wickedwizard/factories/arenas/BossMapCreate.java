package com.byrjamin.wickedwizard.factories.arenas;

import com.byrjamin.wickedwizard.ecs.components.identifiers.BossTeleporterComponent;
import com.byrjamin.wickedwizard.ecs.systems.level.ArenaMap;
import com.byrjamin.wickedwizard.factories.items.Item;
import com.byrjamin.wickedwizard.utils.MapCoords;

/**
 * Created by Home on 16/07/2017.
 */

public interface BossMapCreate {
    ArenaMap createBossMap(BossTeleporterComponent btc, Item item);
}
