package com.byrjamin.wickedwizard.factories.items;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.EntitySubscription;
import com.artemis.World;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.assets.ColorResource;
import com.byrjamin.wickedwizard.assets.SoundFileStrings;
import com.byrjamin.wickedwizard.assets.TextureStrings;
import com.byrjamin.wickedwizard.ecs.components.BackPackComponent;
import com.byrjamin.wickedwizard.ecs.components.CurrencyComponent;
import com.byrjamin.wickedwizard.ecs.components.StatComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.Action;
import com.byrjamin.wickedwizard.ecs.components.ai.ActionAfterTimeComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.ActionOnTouchComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.MoveToPlayerComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.OnDeathActionComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.OnRoomLoadActionComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.Task;
import com.byrjamin.wickedwizard.ecs.components.identifiers.IntangibleComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.OffScreenPickUpComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.AccelerantComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.FrictionComponent;
import com.byrjamin.wickedwizard.ecs.components.object.AltarComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.ChildComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.FollowPositionComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.ParentComponent;
import com.byrjamin.wickedwizard.ecs.components.object.PickUpComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.PlayerComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.ProximityTriggerAIComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.GravityComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.VelocityComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureFontComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.byrjamin.wickedwizard.ecs.systems.FindChildSystem;
import com.byrjamin.wickedwizard.ecs.systems.FindPlayerSystem;
import com.byrjamin.wickedwizard.ecs.systems.PickUpSystem;
import com.byrjamin.wickedwizard.ecs.systems.ai.OnDeathSystem;
import com.byrjamin.wickedwizard.ecs.systems.audio.SoundSystem;
import com.byrjamin.wickedwizard.ecs.systems.graphical.MessageBannerSystem;
import com.byrjamin.wickedwizard.ecs.systems.level.ChangeLevelSystem;
import com.byrjamin.wickedwizard.factories.AbstractFactory;
import com.byrjamin.wickedwizard.factories.items.pickups.MoneyPlus1;
import com.byrjamin.wickedwizard.factories.weapons.Giblets;
import com.byrjamin.wickedwizard.utils.BagToEntity;
import com.byrjamin.wickedwizard.utils.BulletMath;
import com.byrjamin.wickedwizard.utils.CenterMath;
import com.byrjamin.wickedwizard.utils.ComponentBag;
import com.byrjamin.wickedwizard.utils.Measure;
import com.byrjamin.wickedwizard.utils.collider.HitBox;

import java.util.Random;

/**
 * Created by Home on 09/04/2017.
 */

public class ItemFactory extends AbstractFactory {

    private static final float altarWidth = Measure.units(15f);
    private static final float altarHeight = Measure.units(15f);
    private static final float altarItemWidth = Measure.units(5f);
    private static final float altarItemHeight = Measure.units(5f);

    public ItemFactory(AssetManager assetManager) {
        super(assetManager);
    }

    public ComponentBag createPickUpBag(float x, float y, PickUp pickUp){

        ComponentBag bag = new ComponentBag();
        bag.add(new PositionComponent(x,y));

        Random random = new Random();

        bag.add(new VelocityComponent(random.nextInt((int) Measure.units(60f)) -Measure.units(30f), Measure.units(30f)));
        bag.add(new GravityComponent());
        bag.add(new PickUpComponent(pickUp));
        bag.add(new CollisionBoundComponent(new Rectangle(x,y, Measure.units(5), Measure.units(5))));
        bag.add(new TextureRegionComponent(atlas.findRegion(pickUp.getValues().region.getLeft(), pickUp.getValues().region.getRight()), Measure.units(5), Measure.units(5),
                TextureRegionComponent.PLAYER_LAYER_FAR));
        bag.add(new FrictionComponent());
        return bag;
    }

    public ComponentBag createIntangibleFollowingPickUpBag(float x, float y, PickUp pickUp){

        ComponentBag bag = new ComponentBag();
        bag.add(new PositionComponent(x,y));

        Random random = new Random();


        float angle = random.nextFloat() * (360);

        float speed = random.nextFloat() * (Measure.units(100f) - Measure.units(50f)) + Measure.units(50f);

        bag.add(new VelocityComponent(BulletMath.velocityX(speed, Math.toRadians(angle)), BulletMath.velocityY(speed, Math.toRadians(angle))));
        //TODO the way tracking should work is similar to if (pos + velocity > target etc, then don't move there).

        bag.add(new IntangibleComponent());
        bag.add(new OffScreenPickUpComponent(pickUp));
        bag.add(new PickUpComponent(pickUp));
        bag.add(new CollisionBoundComponent(new Rectangle(x,y, Measure.units(2), Measure.units(2))));
        bag.add(new TextureRegionComponent(atlas.findRegion(pickUp.getValues().region.getLeft(), pickUp.getValues().region.getRight()), Measure.units(2), Measure.units(2),
                TextureRegionComponent.FOREGROUND_LAYER_MIDDLE));


        bag.add(new FrictionComponent(true, true));
        bag.add(new OnDeathActionComponent(new Task() {
            @Override
            public void performAction(World world, Entity e) {

                new Giblets.GibletBuilder(assetManager)
                        .numberOfGibletPairs(5)
                        .expiryTime(0.2f)
                        .maxSpeed(Measure.units(50f))
                        .fadeChance(0.25f)
                        .size(Measure.units(1f))
                        .mixes(SoundFileStrings.coinPickUpMix)
                        .colors(new Color(ColorResource.MONEY_YELLOW))
                        .build()
                .performAction(world, e);
                
            }

            @Override
            public void cleanUpAction(World world, Entity e) {

            }
        }));


        bag.add(new ActionAfterTimeComponent(new Task() {
            @Override
            public void performAction(World world, Entity e) {
                //e.edit().add(new VelocityComponent());
                e.edit().add(new MoveToPlayerComponent());
                e.edit().add(new AccelerantComponent(Measure.units(125f), Measure.units(125f)));
                e.edit().add(new IntangibleComponent());
                e.edit().remove(FrictionComponent.class);
            }

            @Override
            public void cleanUpAction(World world, Entity e) {

            }
        }, 0.5f));

        return bag;
    }


    private ComponentBag altarItemTexture(Item item, ParentComponent pc, FollowPositionComponent followPositionComponent){

        ComponentBag bag = new ComponentBag();

        bag.add(new PositionComponent());
        bag.add(new TextureRegionComponent(atlas.findRegion(item.getValues().region.getLeft(), item.getValues().region.getRight()), Measure.units(5), Measure.units(5), TextureRegionComponent.FOREGROUND_LAYER_FAR));
        bag.add(followPositionComponent);
        ChildComponent c = new ChildComponent(pc);
        bag.add(c);

        return bag;


    }




    public ComponentBag createItemAltarBag(float x, float y, Color color){

        PositionComponent positionComponent = new PositionComponent(x,y);

        ComponentBag altarBag = new ComponentBag();
        ParentComponent pc = new ParentComponent();
        altarBag.add(pc);
        altarBag.add(positionComponent);
        altarBag.add(new AltarComponent());
        altarBag.add(new VelocityComponent());
        altarBag.add(new GravityComponent());

        Rectangle bound = new Rectangle(new Rectangle(x,y, altarWidth, altarHeight / 3));
        altarBag.add(new CollisionBoundComponent(bound));
        altarBag.add(new ProximityTriggerAIComponent(activeAltar(), new HitBox(bound)));



        altarBag.add(new OnRoomLoadActionComponent(new Action() {
            @Override
            public void performAction(World world, Entity e) {

                Item item = world.getSystem(ChangeLevelSystem.class).getJigsawGenerator().getItemStore().generateItemRoomItem();
                e.getComponent(AltarComponent.class).pickUp = item;

                BagToEntity.bagToEntity(world.createEntity(), altarItemTexture(item, e.getComponent(ParentComponent.class),
                        new FollowPositionComponent(e.getComponent(PositionComponent.class).position, CenterMath.offsetX(altarWidth, altarItemWidth), Measure.units(5f))));

            }
        }));


        TextureRegionComponent altarTexture = new TextureRegionComponent(atlas.findRegion(TextureStrings.ALTAR), altarWidth, altarHeight,
                TextureRegionComponent.PLAYER_LAYER_FAR, new Color(color));

        altarBag.add(altarTexture);

        return altarBag;
    }


    public Array<ComponentBag> createShopItemBag(float x, float y, int money){

        float width = Measure.units(10);
        float height = Measure.units(10);

        float textureWidth = Measure.units(5f);
        float textureHeight = Measure.units(5f);

        float goldWidth = Measure.units(2.5f);
        float goldHeight = Measure.units(2.5f);


        x = x - width / 2;
        y = y - height / 2;

        Array<ComponentBag> bags =  new Array<ComponentBag>();

        ParentComponent pc = new ParentComponent();

        ComponentBag shopItemTexture = new ComponentBag();
        shopItemTexture.add(new PositionComponent(x , y));
        shopItemTexture.add(new TextureRegionComponent(atlas.findRegion(TextureStrings.BLOCK),
                (width / 2) - (textureWidth / 2),
                (height / 2) - (textureHeight / 2),
                textureWidth,
                textureHeight,
                TextureRegionComponent.FOREGROUND_LAYER_FAR));
        shopItemTexture.add(new CollisionBoundComponent(new Rectangle(x,y, width, height)));
        shopItemTexture.add(new AltarComponent());
        shopItemTexture.add(new ActionOnTouchComponent(buyItem()));
        shopItemTexture.add(new CurrencyComponent(money));
        shopItemTexture.add(pc);

        shopItemTexture.add(new OnRoomLoadActionComponent(new Action() {
            @Override
            public void performAction(World world, Entity e) {

                Item item = world.getSystem(ChangeLevelSystem.class).getJigsawGenerator().getItemStore().generateItemRoomItem();
                e.getComponent(AltarComponent.class).pickUp = item;
                e.getComponent(TextureRegionComponent.class).region = atlas.findRegion(item.getValues().region.getLeft(), item.getValues().region.getRight());

            }
        }));

        bags.add(shopItemTexture);


        ComponentBag priceTag = new ComponentBag();
        priceTag.add(new PositionComponent(x + Measure.units(2f), y - Measure.units(1.5f)));
        priceTag.add(new TextureRegionComponent(atlas.findRegion(new MoneyPlus1().getValues().region.getLeft(), new MoneyPlus1().getValues().region.getRight()), goldWidth, goldHeight, TextureRegionComponent.FOREGROUND_LAYER_FAR));
        TextureFontComponent tfc = new TextureFontComponent(""+money);
        //tfc.width = width / 2;
        tfc.offsetX = Measure.units(5);
        tfc.offsetY = Measure.units(2.5f);
        priceTag.add(tfc);
        ChildComponent c = new ChildComponent();
        pc.children.add(c);
        priceTag.add(c);

        bags.add(priceTag);

        return bags;
    }



    public Array<ComponentBag> createShopPickUpBag(float x, float y, PickUp pickUp, int money){

        float width = Measure.units(10);
        float height = Measure.units(10);

        float textureWidth = Measure.units(5f);
        float textureHeight = Measure.units(5f);

        float goldWidth = Measure.units(2.5f);
        float goldHeight = Measure.units(2.5f);


        x = x - width / 2;
        y = y - height / 2;

        Array<ComponentBag> bags =  new Array<ComponentBag>();

        ParentComponent pc = new ParentComponent();

        ComponentBag shopItemTexture = new ComponentBag();
        shopItemTexture.add(new PositionComponent(x , y));
        shopItemTexture.add(new TextureRegionComponent(atlas.findRegion(pickUp.getValues().region.getLeft(), pickUp.getValues().region.getRight()),
                (width / 2) - (textureWidth / 2),
                (height / 2) - (textureHeight / 2),
                textureWidth,
                textureHeight,
                TextureRegionComponent.FOREGROUND_LAYER_FAR));
        shopItemTexture.add(new CollisionBoundComponent(new Rectangle(x,y, width, height)));
        shopItemTexture.add(new AltarComponent(pickUp));
        shopItemTexture.add(new ActionOnTouchComponent(buyItem()));
        shopItemTexture.add(new CurrencyComponent(money));
        shopItemTexture.add(pc);

        shopItemTexture.add(new OnRoomLoadActionComponent(new Action() {
            @Override
            public void performAction(World world, Entity e) {

            }
        }));

        bags.add(shopItemTexture);


        ComponentBag priceTag = new ComponentBag();
        priceTag.add(new PositionComponent(x + Measure.units(2f), y - Measure.units(1.5f)));
        priceTag.add(new TextureRegionComponent(atlas.findRegion(new MoneyPlus1().getValues().region.getLeft(), new MoneyPlus1().getValues().region.getRight()), goldWidth, goldHeight, TextureRegionComponent.FOREGROUND_LAYER_FAR));
        TextureFontComponent tfc = new TextureFontComponent(""+money);
        //tfc.width = width / 2;
        tfc.offsetX = Measure.units(5);
        tfc.offsetY = Measure.units(2.5f);
        priceTag.add(tfc);
        ChildComponent c = new ChildComponent();
        pc.children.add(c);
        priceTag.add(c);

        bags.add(priceTag);

        return bags;
    }


    private Task buyItem(){
        return new Task() {
            @Override
            public void performAction(World world, Entity e) {

                CurrencyComponent playerMoney = world.getSystem(FindPlayerSystem.class).getPlayerComponent(CurrencyComponent.class);
                CurrencyComponent itemPrice = e.getComponent(CurrencyComponent.class);


                if(playerMoney.money - itemPrice.money >= 0) {

                    AltarComponent ac = e.getComponent(AltarComponent.class);

                    //TODO if item do this
                    if(ac.pickUp instanceof Item) {
                        activeAltar().performAction(world, e);
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

            @Override
            public void cleanUpAction(World world, Entity e) {

            }
        };
    }



    private Task activeAltar (){

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




                        world.getSystem(PickUpSystem.class).itemOverHead(player, item);
                        world.getSystem(MessageBannerSystem.class).createItemBanner(item.getValues().name, item.getValues().description );

                        ac.hasItem = false;
                        if (world.getMapper(ParentComponent.class).has(e)) {
                            world.getSystem(FindChildSystem.class).findChildEntity(e.getComponent(ParentComponent.class).children.first()).deleteFromWorld();
                        }

                        world.getSystem(SoundSystem.class).playRandomSound(SoundFileStrings.itemPickUpMegaMix);
                    }

                }

            }

            @Override
            public void cleanUpAction(World world, Entity e) {
            }
        };


    }



}
