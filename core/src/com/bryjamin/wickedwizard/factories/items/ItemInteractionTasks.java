package com.bryjamin.wickedwizard.factories.items;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.EntitySubscription;
import com.artemis.World;
import com.artemis.utils.IntBag;
import com.bryjamin.wickedwizard.assets.SoundFileStrings;
import com.bryjamin.wickedwizard.ecs.components.CurrencyComponent;
import com.bryjamin.wickedwizard.ecs.components.StatComponent;
import com.bryjamin.wickedwizard.ecs.components.ai.Action;
import com.bryjamin.wickedwizard.ecs.components.ai.ExpireComponent;
import com.bryjamin.wickedwizard.ecs.components.ai.FollowPositionComponent;
import com.bryjamin.wickedwizard.ecs.components.ai.Task;
import com.bryjamin.wickedwizard.ecs.components.identifiers.ParentComponent;
import com.bryjamin.wickedwizard.ecs.components.identifiers.PlayerComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.CollisionBoundComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.bryjamin.wickedwizard.ecs.components.object.AltarComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.FadeComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.bryjamin.wickedwizard.ecs.systems.FindPlayerSystem;
import com.bryjamin.wickedwizard.ecs.systems.ai.OnDeathSystem;
import com.bryjamin.wickedwizard.ecs.systems.graphical.RenderingSystem;
import com.bryjamin.wickedwizard.screens.DataSave;
import com.bryjamin.wickedwizard.utils.Measure;

/**
 * Created by BB on 28/09/2017.
 */

public class ItemInteractionTasks {


    public static void itemOverHead(World world, Entity player, Item item){

        CollisionBoundComponent pBound = player.getComponent(CollisionBoundComponent.class);

        Entity itemHoverAffect = world.createEntity();
        itemHoverAffect.edit().add(new PositionComponent());
        itemHoverAffect.edit().add(new FollowPositionComponent(player.getComponent(PositionComponent.class).position,
                0, pBound.bound.getHeight() + pBound.bound.getHeight() / 4));
        itemHoverAffect.edit().add(new TextureRegionComponent(world.getSystem(RenderingSystem.class).atlas.findRegion(item.getValues().getRegion().getLeft(), item.getValues().getRegion().getRight()),
                Measure.units(5), Measure.units(5), TextureRegionComponent.PLAYER_LAYER_FAR));
        itemHoverAffect.edit().add(new ExpireComponent(1.2f));

        FadeComponent fc = new FadeComponent(false, 1.0f, false);

        itemHoverAffect.edit().add(fc);


        DataSave.saveItemData(item.getValues().getId());

    }



    public static Action buyItem(){
        return new Action() {
            @Override
            public void performAction(World world, Entity e) {

                CurrencyComponent playerMoney = world.getSystem(FindPlayerSystem.class).getPlayerComponent(CurrencyComponent.class);
                CurrencyComponent itemPrice = e.getComponent(CurrencyComponent.class);


                if(playerMoney.money - itemPrice.money >= 0) {

                    AltarComponent ac = e.getComponent(AltarComponent.class);

                    //TODO if item do this
                    if(ac.pickUp instanceof Item) {
                        pickUpItem().performAction(world, e);
                    } else {
                        EntitySubscription subscription = world.getAspectSubscriptionManager().get(Aspect.all(PlayerComponent.class));
                        IntBag entityIds = subscription.getEntities();

                        for (int i = 0; i < entityIds.size(); i++) {
                            Entity player = world.getEntity(entityIds.get(i));
                            if(!ac.pickUp.applyEffect(world, player)) return;
                        }
                    }

                    playerMoney.money -= itemPrice.money;

                    if (world.getMapper(ParentComponent.class).has(e)) {
                        world.getSystem(OnDeathSystem.class).killChildComponentsIgnoreOnDeath(world.getMapper(ParentComponent.class).get(e));
                    }


                    e.deleteFromWorld();
                }

            }

        };
    }



    public static Task pickUpItem(){

        return new Task() {
            @Override
            public void performAction(World world, Entity e) {

                AltarComponent ac = e.getComponent(AltarComponent.class);

                if(ac.pickUp instanceof Item) {

                    Item item = (Item) ac.pickUp;

                    if (ac.hasItem) {

                        Entity player = world.getSystem(FindPlayerSystem.class).getPlayerEntity();
                        item.applyEffect(world, player);
                        player.getComponent(StatComponent.class).collectedItems.add(item);

                        itemOverHead(world, player, item);

                        world.getSystem(com.bryjamin.wickedwizard.ecs.systems.graphical.MessageBannerSystem.class).createItemBanner(item.getValues().getName(),
                                item.getValues().getDescription(),
                                Measure.units(52.5f));

                        ac.hasItem = false;
                        if (world.getMapper(com.bryjamin.wickedwizard.ecs.components.identifiers.ParentComponent.class).has(e)) {
                            world.getSystem(com.bryjamin.wickedwizard.ecs.systems.FindChildSystem.class).findChildEntity(e.getComponent(com.bryjamin.wickedwizard.ecs.components.identifiers.ParentComponent.class).children.first()).deleteFromWorld();
                        }

                        world.getSystem(com.bryjamin.wickedwizard.ecs.systems.audio.SoundSystem.class).playRandomSound(SoundFileStrings.itemPickUpMegaMix);
                    }

                }

            }

            @Override
            public void cleanUpAction(World world, Entity e) {
            }
        };


    }

















}
