package com.bryjamin.wickedwizard.factories.items.companions;

import com.artemis.Entity;
import com.artemis.World;
import com.bryjamin.wickedwizard.ecs.components.movement.CollisionBoundComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.bryjamin.wickedwizard.ecs.systems.graphical.RenderingSystem;
import com.bryjamin.wickedwizard.factories.items.Companion;
import com.bryjamin.wickedwizard.factories.items.ItemLayout;
import com.bryjamin.wickedwizard.factories.items.ItemResource;
import com.bryjamin.wickedwizard.utils.BagToEntity;

/**
 * Created by BB on 16/09/2017.
 */

public class ItemGhastlyWail implements Companion {


    @Override
    public boolean applyEffect(World world, Entity player) {

        BagToEntity.bagToEntity(world.createEntity(), new CompanionFactory(world.getSystem(RenderingSystem.class).assetManager)
                .ghastlyWailCompanion(
                        player,
                        player.getComponent(PositionComponent.class),
                        player.getComponent(CollisionBoundComponent.class)));

        return true;
    }

    @Override
    public ItemLayout getValues() {
        return ItemResource.Companion.ghastlyWail;
    }

}
