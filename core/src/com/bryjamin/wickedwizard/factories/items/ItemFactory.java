package com.bryjamin.wickedwizard.factories.items;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.EntitySubscription;
import com.artemis.World;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.bryjamin.wickedwizard.assets.ColorResource;
import com.bryjamin.wickedwizard.assets.SoundFileStrings;
import com.bryjamin.wickedwizard.ecs.components.CurrencyComponent;
import com.bryjamin.wickedwizard.ecs.components.ai.Action;
import com.bryjamin.wickedwizard.ecs.components.ai.ActionAfterTimeComponent;
import com.bryjamin.wickedwizard.ecs.components.ai.MoveToPlayerComponent;
import com.bryjamin.wickedwizard.ecs.components.ai.OnDeathActionComponent;
import com.bryjamin.wickedwizard.ecs.components.identifiers.IntangibleComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.AccelerantComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.FrictionComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.CollisionBoundComponent;
import com.bryjamin.wickedwizard.ecs.components.ai.ProximityTriggerAIComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.TextureFontComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.bryjamin.wickedwizard.ecs.systems.PickUpSystem;
import com.bryjamin.wickedwizard.factories.AbstractFactory;
import com.bryjamin.wickedwizard.utils.BagToEntity;
import com.bryjamin.wickedwizard.utils.BulletMath;
import com.bryjamin.wickedwizard.utils.CenterMath;

import java.util.Random;

/**
 * Created by Home on 09/04/2017.
 */

public class ItemFactory extends AbstractFactory {

    private static final float altarWidth = com.bryjamin.wickedwizard.utils.Measure.units(15f);
    private static final float altarHeight = com.bryjamin.wickedwizard.utils.Measure.units(15f);
    private static final float altarItemWidth = com.bryjamin.wickedwizard.utils.Measure.units(5f);
    private static final float altarItemHeight = com.bryjamin.wickedwizard.utils.Measure.units(5f);

    public ItemFactory(AssetManager assetManager) {
        super(assetManager);
    }

    public com.bryjamin.wickedwizard.utils.ComponentBag createPickUpBag(float x, float y, PickUp pickUp){

        com.bryjamin.wickedwizard.utils.ComponentBag bag = new com.bryjamin.wickedwizard.utils.ComponentBag();
        bag.add(new PositionComponent(x,y));

        Random random = new Random();

        bag.add(new com.bryjamin.wickedwizard.ecs.components.movement.VelocityComponent(random.nextInt((int) com.bryjamin.wickedwizard.utils.Measure.units(60f)) - com.bryjamin.wickedwizard.utils.Measure.units(30f), com.bryjamin.wickedwizard.utils.Measure.units(30f)));
        bag.add(new com.bryjamin.wickedwizard.ecs.components.movement.GravityComponent());
        bag.add(new com.bryjamin.wickedwizard.ecs.components.object.PickUpComponent(pickUp));
        bag.add(new CollisionBoundComponent(new Rectangle(x,y, com.bryjamin.wickedwizard.utils.Measure.units(5), com.bryjamin.wickedwizard.utils.Measure.units(5))));
        bag.add(new TextureRegionComponent(atlas.findRegion(pickUp.getValues().region.getLeft(), pickUp.getValues().region.getRight()), com.bryjamin.wickedwizard.utils.Measure.units(5), com.bryjamin.wickedwizard.utils.Measure.units(5),
                TextureRegionComponent.PLAYER_LAYER_FAR));
        bag.add(new FrictionComponent());
        return bag;
    }

    public com.bryjamin.wickedwizard.utils.ComponentBag createMoneyPickUpBag(float x, float y, PickUp pickUp){

        com.bryjamin.wickedwizard.utils.ComponentBag bag = new com.bryjamin.wickedwizard.utils.ComponentBag();
        bag.add(new PositionComponent(x,y));

        Random random = new Random();


        float angle = random.nextFloat() * (360);

        float speed = random.nextFloat() * (com.bryjamin.wickedwizard.utils.Measure.units(100f) - com.bryjamin.wickedwizard.utils.Measure.units(50f)) + com.bryjamin.wickedwizard.utils.Measure.units(50f);

        bag.add(new com.bryjamin.wickedwizard.ecs.components.movement.VelocityComponent(BulletMath.velocityX(speed, Math.toRadians(angle)), BulletMath.velocityY(speed, Math.toRadians(angle))));
        //TODO the way tracking should work is similar to if (pos + velocity > target etc, then don't move there).

        bag.add(new IntangibleComponent());
        bag.add(new com.bryjamin.wickedwizard.ecs.components.identifiers.OffScreenPickUpComponent(pickUp));
        bag.add(new com.bryjamin.wickedwizard.ecs.components.object.PickUpComponent(pickUp));
        bag.add(new CollisionBoundComponent(new Rectangle(x,y, com.bryjamin.wickedwizard.utils.Measure.units(2), com.bryjamin.wickedwizard.utils.Measure.units(2))));
        bag.add(new TextureRegionComponent(atlas.findRegion(pickUp.getValues().region.getLeft(), pickUp.getValues().region.getRight()), com.bryjamin.wickedwizard.utils.Measure.units(2), com.bryjamin.wickedwizard.utils.Measure.units(2),
                TextureRegionComponent.FOREGROUND_LAYER_MIDDLE));


        bag.add(new FrictionComponent(true, true));
        bag.add(new OnDeathActionComponent(new com.bryjamin.wickedwizard.ecs.components.ai.Task() {
            @Override
            public void performAction(World world, Entity e) {

                new com.bryjamin.wickedwizard.factories.weapons.Giblets.GibletBuilder(assetManager)
                        .numberOfGibletPairs(5)
                        .expiryTime(0.2f)
                        .maxSpeed(com.bryjamin.wickedwizard.utils.Measure.units(50f))
                        .fadeChance(0.25f)
                        .size(com.bryjamin.wickedwizard.utils.Measure.units(1f))
                        .mixes(SoundFileStrings.coinPickUpMix)
                        .colors(new Color(ColorResource.MONEY_YELLOW))
                        .build()
                .performAction(world, e);
                
            }

            @Override
            public void cleanUpAction(World world, Entity e) {

            }
        }));


        bag.add(new ActionAfterTimeComponent(new com.bryjamin.wickedwizard.ecs.components.ai.Task() {
            @Override
            public void performAction(World world, Entity e) {
                //e.edit().add(new VelocityComponent());
                e.edit().add(new MoveToPlayerComponent());
                e.edit().add(new AccelerantComponent(com.bryjamin.wickedwizard.utils.Measure.units(125f), com.bryjamin.wickedwizard.utils.Measure.units(125f)));
                e.edit().add(new IntangibleComponent());
                e.edit().remove(FrictionComponent.class);
            }

            @Override
            public void cleanUpAction(World world, Entity e) {

            }
        }, 0.5f));

        return bag;
    }


    private com.bryjamin.wickedwizard.utils.ComponentBag altarItemTexture(Item item, com.bryjamin.wickedwizard.ecs.components.identifiers.ParentComponent pc, com.bryjamin.wickedwizard.ecs.components.ai.FollowPositionComponent followPositionComponent){

        com.bryjamin.wickedwizard.utils.ComponentBag bag = new com.bryjamin.wickedwizard.utils.ComponentBag();

        bag.add(new PositionComponent());
        bag.add(new TextureRegionComponent(atlas.findRegion(item.getValues().region.getLeft(), item.getValues().region.getRight()),
                com.bryjamin.wickedwizard.utils.Measure.units(5), com.bryjamin.wickedwizard.utils.Measure.units(5),
                TextureRegionComponent.FOREGROUND_LAYER_FAR,
                item.getValues().textureColor));
        bag.add(followPositionComponent);
        com.bryjamin.wickedwizard.ecs.components.identifiers.ChildComponent c = new com.bryjamin.wickedwizard.ecs.components.identifiers.ChildComponent(pc);
        bag.add(c);

        return bag;


    }


    public com.bryjamin.wickedwizard.utils.ComponentBag createItemAltarBag(float x, float y, Color color){
        return createItemAltarBag(x, y, color, null);
    }



    public com.bryjamin.wickedwizard.utils.ComponentBag createItemAltarBag(float x, float y, Color color, final Item item){

        PositionComponent positionComponent = new PositionComponent(x,y);

        com.bryjamin.wickedwizard.utils.ComponentBag altarBag = new com.bryjamin.wickedwizard.utils.ComponentBag();
        com.bryjamin.wickedwizard.ecs.components.identifiers.ParentComponent pc = new com.bryjamin.wickedwizard.ecs.components.identifiers.ParentComponent();
        altarBag.add(pc);
        altarBag.add(positionComponent);
        altarBag.add(new com.bryjamin.wickedwizard.ecs.components.object.AltarComponent());
        altarBag.add(new com.bryjamin.wickedwizard.ecs.components.movement.VelocityComponent());
        altarBag.add(new com.bryjamin.wickedwizard.ecs.components.movement.GravityComponent());

        Rectangle bound = new Rectangle(new Rectangle(x,y, altarWidth, altarHeight / 3));
        altarBag.add(new CollisionBoundComponent(bound));
        altarBag.add(new ProximityTriggerAIComponent(activeAltar(), new com.bryjamin.wickedwizard.utils.collider.HitBox(bound)));



        altarBag.add(new com.bryjamin.wickedwizard.ecs.components.ai.OnRoomLoadActionComponent(new Action() {
            @Override
            public void performAction(World world, Entity e) {

                Item altarItem;

                if(item == null){
                    altarItem = world.getSystem(com.bryjamin.wickedwizard.ecs.systems.level.ChangeLevelSystem.class).getJigsawGenerator().getItemStore().generateItemRoomItem();
                } else {
                    altarItem = item;
                }

                e.getComponent(com.bryjamin.wickedwizard.ecs.components.object.AltarComponent.class).pickUp = altarItem;

                BagToEntity.bagToEntity(world.createEntity(), altarItemTexture(altarItem, e.getComponent(com.bryjamin.wickedwizard.ecs.components.identifiers.ParentComponent.class),
                        new com.bryjamin.wickedwizard.ecs.components.ai.FollowPositionComponent(e.getComponent(PositionComponent.class).position, CenterMath.offsetX(altarWidth, altarItemWidth), com.bryjamin.wickedwizard.utils.Measure.units(5f))));

            }
        }));


        TextureRegionComponent altarTexture = new TextureRegionComponent(atlas.findRegion(com.bryjamin.wickedwizard.assets.TextureStrings.ALTAR), altarWidth, altarHeight,
                TextureRegionComponent.PLAYER_LAYER_FAR, new Color(color));

        altarBag.add(altarTexture);

        return altarBag;
    }


    public Array<com.bryjamin.wickedwizard.utils.ComponentBag> createShopItemBag(float x, float y, int money){

        final float width = com.bryjamin.wickedwizard.utils.Measure.units(10);
        final float height = com.bryjamin.wickedwizard.utils.Measure.units(10);

        final float textureWidth = com.bryjamin.wickedwizard.utils.Measure.units(5f);
        final float textureHeight = com.bryjamin.wickedwizard.utils.Measure.units(5f);

        float goldWidth = com.bryjamin.wickedwizard.utils.Measure.units(2.5f);
        float goldHeight = com.bryjamin.wickedwizard.utils.Measure.units(2.5f);


        x = x - width / 2;
        y = y - height / 2;

        Array<com.bryjamin.wickedwizard.utils.ComponentBag> bags =  new Array<com.bryjamin.wickedwizard.utils.ComponentBag>();

        com.bryjamin.wickedwizard.ecs.components.identifiers.ParentComponent pc = new com.bryjamin.wickedwizard.ecs.components.identifiers.ParentComponent();

        com.bryjamin.wickedwizard.utils.ComponentBag shopItemTexture = new com.bryjamin.wickedwizard.utils.ComponentBag();
        shopItemTexture.add(new PositionComponent(x , y));
        shopItemTexture.add(new CollisionBoundComponent(new Rectangle(x,y, width, height)));
        shopItemTexture.add(new com.bryjamin.wickedwizard.ecs.components.object.AltarComponent());
        shopItemTexture.add(new com.bryjamin.wickedwizard.ecs.components.ai.ActionOnTouchComponent(buyItem()));
        shopItemTexture.add(new CurrencyComponent(money));
        shopItemTexture.add(pc);

        shopItemTexture.add(new com.bryjamin.wickedwizard.ecs.components.ai.OnRoomLoadActionComponent(new Action() {
            @Override
            public void performAction(World world, Entity e) {

                Item item = world.getSystem(com.bryjamin.wickedwizard.ecs.systems.level.ChangeLevelSystem.class).getJigsawGenerator().getItemStore().generateItemRoomItem();
                e.getComponent(com.bryjamin.wickedwizard.ecs.components.object.AltarComponent.class).pickUp = item;




                e.edit().add(new TextureRegionComponent(atlas.findRegion(item.getValues().region.getLeft(), item.getValues().region.getRight()),
                        (width / 2) - (textureWidth / 2),
                        (height / 2) - (textureHeight / 2),
                        textureWidth,
                        textureHeight,
                        TextureRegionComponent.FOREGROUND_LAYER_FAR,
                        item.getValues().textureColor));

            }
        }));

        bags.add(shopItemTexture);


        com.bryjamin.wickedwizard.utils.ComponentBag priceTag = new com.bryjamin.wickedwizard.utils.ComponentBag();
        priceTag.add(new PositionComponent(x + com.bryjamin.wickedwizard.utils.Measure.units(2f), y - com.bryjamin.wickedwizard.utils.Measure.units(1.5f)));
        priceTag.add(new TextureRegionComponent(atlas.findRegion(new com.bryjamin.wickedwizard.factories.items.pickups.MoneyPlus1().getValues().region.getLeft(), new com.bryjamin.wickedwizard.factories.items.pickups.MoneyPlus1().getValues().region.getRight()), goldWidth, goldHeight, TextureRegionComponent.FOREGROUND_LAYER_FAR));
        TextureFontComponent tfc = new TextureFontComponent(""+money);
        //tfc.width = width / 2;
        tfc.offsetX = com.bryjamin.wickedwizard.utils.Measure.units(5);
        tfc.offsetY = com.bryjamin.wickedwizard.utils.Measure.units(2.5f);
        priceTag.add(tfc);
        com.bryjamin.wickedwizard.ecs.components.identifiers.ChildComponent c = new com.bryjamin.wickedwizard.ecs.components.identifiers.ChildComponent();
        pc.children.add(c);
        priceTag.add(c);

        bags.add(priceTag);

        return bags;
    }



    public Array<com.bryjamin.wickedwizard.utils.ComponentBag> createShopPickUpBag(float x, float y, PickUp pickUp, int money){

        float width = com.bryjamin.wickedwizard.utils.Measure.units(10);
        float height = com.bryjamin.wickedwizard.utils.Measure.units(10);

        float textureWidth = com.bryjamin.wickedwizard.utils.Measure.units(5f);
        float textureHeight = com.bryjamin.wickedwizard.utils.Measure.units(5f);

        float goldWidth = com.bryjamin.wickedwizard.utils.Measure.units(2.5f);
        float goldHeight = com.bryjamin.wickedwizard.utils.Measure.units(2.5f);


        x = x - width / 2;
        y = y - height / 2;

        Array<com.bryjamin.wickedwizard.utils.ComponentBag> bags =  new Array<com.bryjamin.wickedwizard.utils.ComponentBag>();

        com.bryjamin.wickedwizard.ecs.components.identifiers.ParentComponent pc = new com.bryjamin.wickedwizard.ecs.components.identifiers.ParentComponent();

        com.bryjamin.wickedwizard.utils.ComponentBag shopItemTexture = new com.bryjamin.wickedwizard.utils.ComponentBag();
        shopItemTexture.add(new PositionComponent(x , y));
        shopItemTexture.add(new TextureRegionComponent(atlas.findRegion(pickUp.getValues().region.getLeft(), pickUp.getValues().region.getRight()),
                (width / 2) - (textureWidth / 2),
                (height / 2) - (textureHeight / 2),
                textureWidth,
                textureHeight,
                TextureRegionComponent.FOREGROUND_LAYER_FAR, pickUp.getValues().textureColor));
        shopItemTexture.add(new CollisionBoundComponent(new Rectangle(x,y, width, height)));
        shopItemTexture.add(new com.bryjamin.wickedwizard.ecs.components.object.AltarComponent(pickUp));
        shopItemTexture.add(new com.bryjamin.wickedwizard.ecs.components.ai.ActionOnTouchComponent(buyItem()));
        shopItemTexture.add(new CurrencyComponent(money));
        shopItemTexture.add(pc);

        shopItemTexture.add(new com.bryjamin.wickedwizard.ecs.components.ai.OnRoomLoadActionComponent(new Action() {
            @Override
            public void performAction(World world, Entity e) {

            }
        }));

        bags.add(shopItemTexture);


        com.bryjamin.wickedwizard.utils.ComponentBag priceTag = new com.bryjamin.wickedwizard.utils.ComponentBag();
        priceTag.add(new PositionComponent(x + com.bryjamin.wickedwizard.utils.Measure.units(2f), y - com.bryjamin.wickedwizard.utils.Measure.units(1.5f)));
        priceTag.add(new TextureRegionComponent(atlas.findRegion(new com.bryjamin.wickedwizard.factories.items.pickups.MoneyPlus1().getValues().region.getLeft(), new com.bryjamin.wickedwizard.factories.items.pickups.MoneyPlus1().getValues().region.getRight()), goldWidth, goldHeight, TextureRegionComponent.FOREGROUND_LAYER_FAR));
        TextureFontComponent tfc = new TextureFontComponent(""+money);
        //tfc.width = width / 2;
        tfc.offsetX = com.bryjamin.wickedwizard.utils.Measure.units(5);
        tfc.offsetY = com.bryjamin.wickedwizard.utils.Measure.units(2.5f);
        priceTag.add(tfc);
        com.bryjamin.wickedwizard.ecs.components.identifiers.ChildComponent c = new com.bryjamin.wickedwizard.ecs.components.identifiers.ChildComponent();
        pc.children.add(c);
        priceTag.add(c);

        bags.add(priceTag);

        return bags;
    }


    private com.bryjamin.wickedwizard.ecs.components.ai.Task buyItem(){
        return new com.bryjamin.wickedwizard.ecs.components.ai.Task() {
            @Override
            public void performAction(World world, Entity e) {

                CurrencyComponent playerMoney = world.getSystem(com.bryjamin.wickedwizard.ecs.systems.FindPlayerSystem.class).getPlayerComponent(CurrencyComponent.class);
                CurrencyComponent itemPrice = e.getComponent(CurrencyComponent.class);


                if(playerMoney.money - itemPrice.money >= 0) {

                    com.bryjamin.wickedwizard.ecs.components.object.AltarComponent ac = e.getComponent(com.bryjamin.wickedwizard.ecs.components.object.AltarComponent.class);

                    //TODO if item do this
                    if(ac.pickUp instanceof Item) {
                        activeAltar().performAction(world, e);
                    } else {
                        EntitySubscription subscription = world.getAspectSubscriptionManager().get(Aspect.all(com.bryjamin.wickedwizard.ecs.components.identifiers.PlayerComponent.class));
                        IntBag entityIds = subscription.getEntities();

                        for (int i = 0; i < entityIds.size(); i++) {
                            Entity player = world.getEntity(entityIds.get(i));
                            if(!ac.pickUp.applyEffect(world, player)) return;
                        }
                    }

                    playerMoney.money -= itemPrice.money;

                    if (world.getMapper(com.bryjamin.wickedwizard.ecs.components.identifiers.ParentComponent.class).has(e)) {
                        world.getSystem(com.bryjamin.wickedwizard.ecs.systems.ai.OnDeathSystem.class).killChildComponentsIgnoreOnDeath(world.getMapper(com.bryjamin.wickedwizard.ecs.components.identifiers.ParentComponent.class).get(e));
                    }


                    e.deleteFromWorld();
                }

            }

            @Override
            public void cleanUpAction(World world, Entity e) {

            }
        };
    }



    private com.bryjamin.wickedwizard.ecs.components.ai.Task activeAltar (){

        return new com.bryjamin.wickedwizard.ecs.components.ai.Task() {
            @Override
            public void performAction(World world, Entity e) {

                com.bryjamin.wickedwizard.ecs.components.object.AltarComponent ac = e.getComponent(com.bryjamin.wickedwizard.ecs.components.object.AltarComponent.class);

                if(ac.pickUp instanceof Item) {

                    Item item = (Item) ac.pickUp;

                    if (ac.hasItem) {

                        Entity player = world.getSystem(com.bryjamin.wickedwizard.ecs.systems.FindPlayerSystem.class).getPlayerEntity();
                        item.applyEffect(world, player);
                        player.getComponent(com.bryjamin.wickedwizard.ecs.components.StatComponent.class).collectedItems.add(item);




                        world.getSystem(PickUpSystem.class).itemOverHead(player, item);
                        world.getSystem(com.bryjamin.wickedwizard.ecs.systems.graphical.MessageBannerSystem.class).createItemBanner(item.getValues().name, item.getValues().description );

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
