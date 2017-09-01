package com.bryjamin.wickedwizard.factories.arenas;

import com.bryjamin.wickedwizard.ecs.components.identifiers.BossTeleporterComponent;
import com.bryjamin.wickedwizard.ecs.systems.level.ArenaMap;

/**
 * Created by Home on 16/07/2017.
 */

public interface BossMapCreate {
    ArenaMap createBossMap(BossTeleporterComponent btc);
}
