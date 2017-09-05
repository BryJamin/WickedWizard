package com.bryjamin.wickedwizard.ecs.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySubscription;
import com.artemis.systems.EntityProcessingSystem;
import com.artemis.utils.IntBag;
import com.bryjamin.wickedwizard.ecs.components.ai.ExpireComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.CollisionBoundComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.FadeComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.bryjamin.wickedwizard.factories.items.Item;
import com.bryjamin.wickedwizard.screens.DataSave;
import com.bryjamin.wickedwizard.utils.Measure;

/**
 * Created by Home on 09/04/2017.
 */

public class PickUpSystem extends EntityProcessingSystem {

    ComponentMapper<com.bryjamin.wickedwizard.ecs.components.object.PickUpComponent> im;
    ComponentMapper<CollisionBoundComponent> cbm;

    public PickUpSystem() {
        super(Aspect.all(com.bryjamin.wickedwizard.ecs.components.identifiers.PlayerComponent.class, CollisionBoundComponent.class));
    }

    @Override
    protected void process(Entity e) {

        EntitySubscription subscription = world.getAspectSubscriptionManager().
                get(Aspect.all(com.bryjamin.wickedwizard.ecs.components.object.PickUpComponent.class, CollisionBoundComponent.class));
        IntBag itemsIds = subscription.getEntities();


        CollisionBoundComponent player = cbm.get(e);
        CollisionBoundComponent item;

        for(int i = 0; i < itemsIds.size(); i++){


            int itemEntity = itemsIds.get(i);
            item = cbm.get(itemEntity);

            if(player.bound.overlaps(item.bound)){
                com.bryjamin.wickedwizard.ecs.components.object.PickUpComponent ic = im.get(itemEntity);
                if(ic.getPickUp().applyEffect(world, e)) {;
                    world.getSystem(com.bryjamin.wickedwizard.ecs.systems.ai.OnDeathSystem.class).kill(world.getEntity(itemEntity));
                }
            }

        }


    }


    public void itemOverHead(Entity player, Item item){

        CollisionBoundComponent pBound = player.getComponent(CollisionBoundComponent.class);

        Entity itemHoverAffect = world.createEntity();
        itemHoverAffect.edit().add(new com.bryjamin.wickedwizard.ecs.components.movement.PositionComponent());
        itemHoverAffect.edit().add(new com.bryjamin.wickedwizard.ecs.components.ai.FollowPositionComponent(player.getComponent(com.bryjamin.wickedwizard.ecs.components.movement.PositionComponent.class).position,
                0, pBound.bound.getHeight() + pBound.bound.getHeight() / 4));
        itemHoverAffect.edit().add(new TextureRegionComponent(world.getSystem(com.bryjamin.wickedwizard.ecs.systems.graphical.RenderingSystem.class).atlas.findRegion(item.getValues().region.getLeft(), item.getValues().region.getRight()),
                Measure.units(5), Measure.units(5), TextureRegionComponent.PLAYER_LAYER_FAR));
        itemHoverAffect.edit().add(new ExpireComponent(1.2f));

        FadeComponent fc = new FadeComponent(false, 1.0f, false);

        itemHoverAffect.edit().add(fc);


        DataSave.saveItemData(item.getValues().id);




    }




}
