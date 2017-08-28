package com.byrjamin.wickedwizard.factories.items.companions;

import com.artemis.Entity;
import com.artemis.World;
import com.byrjamin.wickedwizard.ecs.components.identifiers.ParentComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.ecs.systems.graphical.RenderingSystem;
import com.byrjamin.wickedwizard.factories.items.Companion;
import com.byrjamin.wickedwizard.factories.items.ItemResource;
import com.byrjamin.wickedwizard.utils.BagToEntity;

/**
 * Created by BB on 27/08/2017.
 */

public class ItemMyVeryOwnStalker implements Companion {


    @Override
    public boolean applyEffect(World world, Entity player) {

        BagToEntity.bagToEntity(world.createEntity(), new CompanionFactory(world.getSystem(RenderingSystem.class).assetManager)
                .myVeryOwnStalker(
                        player.getComponent(ParentComponent.class),
                        player.getComponent(PositionComponent.class),
                        player.getComponent(CollisionBoundComponent.class)));

        return true;
    }

    @Override
    public ItemResource.ItemValues getValues() {
        return ItemResource.Companion.myVeryOwnStalker;
    }

}
