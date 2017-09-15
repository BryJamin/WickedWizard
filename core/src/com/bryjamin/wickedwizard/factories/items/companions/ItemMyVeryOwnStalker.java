package com.bryjamin.wickedwizard.factories.items.companions;

import com.artemis.Entity;
import com.artemis.World;
import com.bryjamin.wickedwizard.ecs.components.movement.CollisionBoundComponent;
import com.bryjamin.wickedwizard.factories.items.ItemLayout;
import com.bryjamin.wickedwizard.factories.items.ItemResource;

/**
 * Created by BB on 27/08/2017.
 */

public class ItemMyVeryOwnStalker implements com.bryjamin.wickedwizard.factories.items.Companion {


    @Override
    public boolean applyEffect(World world, Entity player) {

        com.bryjamin.wickedwizard.utils.BagToEntity.bagToEntity(world.createEntity(), new CompanionFactory(world.getSystem(com.bryjamin.wickedwizard.ecs.systems.graphical.RenderingSystem.class).assetManager)
                .myVeryOwnStalker(
                        player.getComponent(com.bryjamin.wickedwizard.ecs.components.identifiers.ParentComponent.class),
                        player.getComponent(com.bryjamin.wickedwizard.ecs.components.movement.PositionComponent.class),
                        player.getComponent(CollisionBoundComponent.class)));

        return true;
    }

    @Override
    public ItemLayout getValues() {
        return ItemResource.Companion.myVeryOwnStalker;
    }

}
