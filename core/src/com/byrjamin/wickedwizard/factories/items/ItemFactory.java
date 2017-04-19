package com.byrjamin.wickedwizard.factories.items;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.EntitySubscription;
import com.artemis.World;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.ecs.components.CurrencyComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.ActionOnTouchComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.MoveToPlayerComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.IntangibleComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.AccelerantComponent;
import com.byrjamin.wickedwizard.ecs.components.object.AltarComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.ChildComponent;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.ExpireComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.FollowPositionComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.ParentComponent;
import com.byrjamin.wickedwizard.ecs.components.object.PickUpComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.PlayerComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.Action;
import com.byrjamin.wickedwizard.ecs.components.ai.ProximityTriggerAIComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.GravityComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.VelocityComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.HighlightComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureFontComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.byrjamin.wickedwizard.ecs.systems.FindChildSystem;
import com.byrjamin.wickedwizard.ecs.systems.graphical.MessageBannerSystem;
import com.byrjamin.wickedwizard.factories.items.passives.DamageUp;
import com.byrjamin.wickedwizard.factories.items.pickups.MoneyPlus1;
import com.byrjamin.wickedwizard.screens.PlayScreen;
import com.byrjamin.wickedwizard.utils.ComponentBag;
import com.byrjamin.wickedwizard.utils.Measure;

import java.util.Random;

/**
 * Created by Home on 09/04/2017.
 */

public class ItemFactory {


    public static ComponentBag createPickUpBag(float x, float y, PickUp pickUp){

        ComponentBag bag = new ComponentBag();
        bag.add(new PositionComponent(x,y));

        Random random = new Random();

        bag.add(new VelocityComponent(random.nextInt((int) Measure.units(60f)) -Measure.units(30f), Measure.units(30f)));
        bag.add(new GravityComponent());
        bag.add(new PickUpComponent(pickUp));
        bag.add(new CollisionBoundComponent(new Rectangle(x,y, Measure.units(5), Measure.units(5))));
        bag.add(new TextureRegionComponent(pickUp.getRegion(), Measure.units(5), Measure.units(5),
                TextureRegionComponent.PLAYER_LAYER_FAR));

        return bag;
    }

    public static ComponentBag createIntangibleFollowingPickUpBag(float x, float y, PickUp pickUp){

        ComponentBag bag = new ComponentBag();
        bag.add(new PositionComponent(x,y));

        Random random = new Random();

        bag.add(new VelocityComponent(random.nextInt((int) Measure.units(60f)) -Measure.units(30f), Measure.units(30f)));
        bag.add(new MoveToPlayerComponent());
        bag.add(new AccelerantComponent(Measure.units(5f),Measure.units(5f), Measure.units(50), Measure.units(50f)));
        bag.add(new IntangibleComponent());
        bag.add(new PickUpComponent(pickUp));
        bag.add(new CollisionBoundComponent(new Rectangle(x,y, Measure.units(5), Measure.units(5))));
        bag.add(new TextureRegionComponent(pickUp.getRegion(), Measure.units(5), Measure.units(5),
                TextureRegionComponent.FOREGROUND_LAYER_MIDDLE));

        return bag;
    }


    public static ComponentBag createFloatingItemBag(float x, float y, Item item){

        float width = Measure.units(8);
        float height = Measure.units(8);

        x = x - width / 2;
        y = y - height / 2;

        ComponentBag bag = new ComponentBag();
        bag.add(new PositionComponent(x,y));
        bag.add(new PickUpComponent(item));
        bag.add(new HighlightComponent());
        bag.add(new CollisionBoundComponent(new Rectangle(x,y, width, height)));
        bag.add(new TextureRegionComponent(item.getRegion(), width, height,
                TextureRegionComponent.PLAYER_LAYER_FAR));
        return bag;
    }


    public static Array<ComponentBag> createItemAltarBag(float x, float y, Item item){

        float width = Measure.units(15);
        float height = Measure.units(15);

        x = x - width / 2;
        y = y - height / 2;

        Array<ComponentBag> bags =  new Array<ComponentBag>();

        PositionComponent positionComponent = new PositionComponent(x,y);

        ComponentBag altarItemTexture = new ComponentBag();
        altarItemTexture.add(new PositionComponent());
        altarItemTexture.add(new TextureRegionComponent(item.getRegion(), Measure.units(5), Measure.units(5), TextureRegionComponent.FOREGROUND_LAYER_FAR));
        altarItemTexture.add(new FollowPositionComponent(positionComponent.position, width / 2 - Measure.units(2.5f), Measure.units(5)));

        ChildComponent c = new ChildComponent();
        altarItemTexture.add(c);

        ComponentBag bag = new ComponentBag();
        bag.add(new ParentComponent(c));
        bag.add(positionComponent);
        //bag.add(new PickUpComponent(item));
        bag.add(new AltarComponent(item));
        // bag.add(new HighlightComponent());
        bag.add(new VelocityComponent());
        bag.add(new GravityComponent());

        Rectangle bound = new Rectangle(new Rectangle(x,y, width, height / 3));

        bag.add(new CollisionBoundComponent(bound));
        bag.add(new TextureRegionComponent(PlayScreen.atlas.findRegion("altar"), width, height,
                TextureRegionComponent.PLAYER_LAYER_FAR));
        bag.add(new ProximityTriggerAIComponent(bound, activeAltar()));

        bags.add(altarItemTexture);
        bags.add(bag);





        return bags;
    }


    public static Array<ComponentBag> createShopItemBag(float x, float y, Item item, int money){

        float width = Measure.units(8);
        float height = Measure.units(8);

        x = x - width / 2;
        y = y - height / 2;

        Array<ComponentBag> bags =  new Array<ComponentBag>();

        ParentComponent pc = new ParentComponent();

        ComponentBag shopItemTexture = new ComponentBag();
        shopItemTexture.add(new PositionComponent(x , y));
        shopItemTexture.add(new TextureRegionComponent(item.getRegion(), width, height, TextureRegionComponent.FOREGROUND_LAYER_FAR));
        shopItemTexture.add(new CollisionBoundComponent(new Rectangle(x,y, width, height)));
        shopItemTexture.add(new AltarComponent(item));
        shopItemTexture.add(new ActionOnTouchComponent(activeAltar()));
        shopItemTexture.add(new CurrencyComponent(money));
        shopItemTexture.add(pc);
        bags.add(shopItemTexture);


        ComponentBag priceTag = new ComponentBag();
        priceTag.add(new PositionComponent(x, y - Measure.units(5)));
        priceTag.add(new TextureRegionComponent(new MoneyPlus1().getRegion(), width / 2, height / 2, TextureRegionComponent.FOREGROUND_LAYER_FAR));
        TextureFontComponent trc = new TextureFontComponent(""+money, 3);
        trc.width = width / 2;
        trc.offsetX = -Measure.units(45f);
        trc.offsetY =  Measure.units(2.8f);
        priceTag.add(trc);
        bags.add(priceTag);
        ChildComponent c = new ChildComponent();
        pc.children.add(c);
        priceTag.add(c);

        bags.add(priceTag);

        return bags;
    }


    private static Action buyItem(){
        return new Action() {
            @Override
            public void performAction(World w, Entity e) {
                //TODO If pickup just apply Item
                //TODO If Item do the raise above head thing that will be made into a static method
            }

            @Override
            public void cleanUpAction(World w, Entity e) {

            }
        };
    }



    private static Action activeAltar (){

        return new Action() {
            @Override
            public void performAction(World world, Entity e) {
                e.edit().add(new HighlightComponent());


                AltarComponent ac = e.getComponent(AltarComponent.class);

                if(ac.hasItem) {
                    EntitySubscription subscription = world.getAspectSubscriptionManager().get(Aspect.all(PlayerComponent.class));
                    IntBag entityIds = subscription.getEntities();

                    for (int i = 0; i < entityIds.size(); i++) {
                        Entity player = world.getEntity(entityIds.get(i));
                        ac.item.applyEffect(world, player);

                        CollisionBoundComponent pBound = player.getComponent(CollisionBoundComponent.class);

                        Entity itemHoverAffect = world.createEntity();
                        itemHoverAffect.edit().add(new PositionComponent());
                        itemHoverAffect.edit().add(new FollowPositionComponent(player.getComponent(PositionComponent.class).position,
                                0, pBound.bound.getHeight() + pBound.bound.getHeight() / 4));
                        itemHoverAffect.edit().add(new TextureRegionComponent(ac.item.getRegion(),
                                Measure.units(5), Measure.units(5), TextureRegionComponent.PLAYER_LAYER_FAR));
                        itemHoverAffect.edit().add(new ExpireComponent(0.9f));

                        world.getSystem(MessageBannerSystem.class).createBanner(ac.item.getName(), ac.item.getDescription());

                    }

                    ac.hasItem = false;
                    //TODO Previous the has ParenComponet was not here I re-used this for the shop I added it on
                    //TODO I would advise removing this once you wake back up
                    if(world.getMapper(ParentComponent.class).has(e))
                    {
                        world.getSystem(FindChildSystem.class).findChildEntity(e.getComponent(ParentComponent.class).children.first()).deleteFromWorld();
                    }
                }

                //TODO ditto
                if(world.getMapper(ParentComponent.class).has(e)) {
                    Entity child = world.getSystem(FindChildSystem.class).findChildEntity(e.getComponent(ParentComponent.class).children.first());
                    if (child != null) {
                        child.edit().add(new HighlightComponent());
                    }
                }
               // e.getComponent(ParentComponent.class).children.get(0)
            }

            @Override
            public void cleanUpAction(World w, Entity e) {
                e.edit().remove(HighlightComponent.class);

                Entity child = w.getSystem(FindChildSystem.class).findChildEntity(e.getComponent(ParentComponent.class).children.first());
                if(child != null){
                    child.edit().remove(HighlightComponent.class);
                }
            }
        };


    }



}
