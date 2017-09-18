package com.bryjamin.wickedwizard.factories.items;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.EntitySubscription;
import com.artemis.World;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.bryjamin.wickedwizard.assets.ColorResource;
import com.bryjamin.wickedwizard.assets.SoundFileStrings;
import com.bryjamin.wickedwizard.assets.TextureStrings;
import com.bryjamin.wickedwizard.ecs.components.CurrencyComponent;
import com.bryjamin.wickedwizard.ecs.components.StatComponent;
import com.bryjamin.wickedwizard.ecs.components.ai.Action;
import com.bryjamin.wickedwizard.ecs.components.ai.ActionAfterTimeComponent;
import com.bryjamin.wickedwizard.ecs.components.ai.ActionOnTouchComponent;
import com.bryjamin.wickedwizard.ecs.components.ai.Condition;
import com.bryjamin.wickedwizard.ecs.components.ai.ConditionalActionComponent;
import com.bryjamin.wickedwizard.ecs.components.ai.FollowPositionComponent;
import com.bryjamin.wickedwizard.ecs.components.ai.MoveToPlayerComponent;
import com.bryjamin.wickedwizard.ecs.components.ai.OnDeathActionComponent;
import com.bryjamin.wickedwizard.ecs.components.ai.DuringRoomLoadActionComponent;
import com.bryjamin.wickedwizard.ecs.components.ai.ProximityTriggerAIComponent;
import com.bryjamin.wickedwizard.ecs.components.ai.Task;
import com.bryjamin.wickedwizard.ecs.components.identifiers.ChildComponent;
import com.bryjamin.wickedwizard.ecs.components.identifiers.IntangibleComponent;
import com.bryjamin.wickedwizard.ecs.components.identifiers.OffScreenPickUpComponent;
import com.bryjamin.wickedwizard.ecs.components.identifiers.ParentComponent;
import com.bryjamin.wickedwizard.ecs.components.identifiers.PlayerComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.AccelerantComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.CollisionBoundComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.FrictionComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.GravityComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.VelocityComponent;
import com.bryjamin.wickedwizard.ecs.components.object.AltarComponent;
import com.bryjamin.wickedwizard.ecs.components.object.PickUpComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.FadeComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.TextureFontComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.bryjamin.wickedwizard.ecs.systems.FindChildSystem;
import com.bryjamin.wickedwizard.ecs.systems.FindPlayerSystem;
import com.bryjamin.wickedwizard.ecs.systems.LuckSystem;
import com.bryjamin.wickedwizard.ecs.systems.PickUpSystem;
import com.bryjamin.wickedwizard.ecs.systems.ai.OnDeathSystem;
import com.bryjamin.wickedwizard.ecs.systems.level.ChangeLevelSystem;
import com.bryjamin.wickedwizard.factories.AbstractFactory;
import com.bryjamin.wickedwizard.factories.arenas.skins.ArenaSkin;
import com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory;
import com.bryjamin.wickedwizard.factories.items.passives.accuracy.ItemErraticFire;
import com.bryjamin.wickedwizard.factories.items.pickups.MoneyPlus1;
import com.bryjamin.wickedwizard.factories.weapons.Giblets;
import com.bryjamin.wickedwizard.utils.BagToEntity;
import com.bryjamin.wickedwizard.utils.BulletMath;
import com.bryjamin.wickedwizard.utils.CenterMath;
import com.bryjamin.wickedwizard.utils.ComponentBag;
import com.bryjamin.wickedwizard.utils.Measure;
import com.bryjamin.wickedwizard.utils.collider.HitBox;
import com.bryjamin.wickedwizard.utils.enums.ItemType;

import java.util.Random;


/**
 * Created by Home on 09/04/2017.
 */

public class ItemFactory extends AbstractFactory {

    private static final float altarWidth = Measure.units(15f);
    private static final float altarHeight = Measure.units(15f);
    private static final float altarItemWidth = Measure.units(5f);
    private static final float altarItemHeight = Measure.units(5f);


    private static final float width = Measure.units(10f);
    private static final float height = Measure.units(10f);
    private static final float textureWidth = Measure.units(5f);
    private static final float textureHeight = Measure.units(5f);

    private static final float goldWidth = Measure.units(2.5f);
    private static final float goldHeight = Measure.units(2.5f);



    public ItemFactory(AssetManager assetManager) {
        super(assetManager);
    }

    public ComponentBag createPickUpBag(float x, float y, PickUp pickUp){

        ComponentBag bag = new ComponentBag();
        bag.add(new PositionComponent(x,y));

        Random random = new Random();

        bag.add(new VelocityComponent(random.nextInt((int) Measure.units(60f)) - Measure.units(30f), Measure.units(30f)));
        bag.add(new GravityComponent());
        bag.add(new PickUpComponent(pickUp));
        bag.add(new CollisionBoundComponent(new Rectangle(x,y, Measure.units(5), Measure.units(5))));
        bag.add(new TextureRegionComponent(atlas.findRegion(pickUp.getValues().getRegion().getLeft(), pickUp.getValues().getRegion().getRight()), Measure.units(5), Measure.units(5),
                TextureRegionComponent.PLAYER_LAYER_FAR));
        bag.add(new FrictionComponent());
        return bag;
    }

    public ComponentBag createMoneyPickUpBag(float x, float y, PickUp pickUp){

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
        bag.add(new TextureRegionComponent(atlas.findRegion(pickUp.getValues().getRegion().getLeft(), pickUp.getValues().getRegion().getRight()), Measure.units(2), Measure.units(2),
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
        bag.add(new TextureRegionComponent(atlas.findRegion(item.getValues().getRegion().getLeft(), item.getValues().getRegion().getRight()),
                Measure.units(5), Measure.units(5),
                TextureRegionComponent.ENEMY_LAYER_FAR,
                item.getValues().getTextureColor()));
        bag.add(followPositionComponent);
        ChildComponent c = new ChildComponent(pc);
        bag.add(c);

        return bag;


    }

    public ComponentBag createCenteredItemAltarBag(float x, float y, Color color, ItemType... itemTypes){

        x = x - altarWidth / 2;
        y = y - altarHeight / 2;

        return createItemAltarBag(x, y, color, itemTypes);
    }


    private ComponentBag emptyAltar(float x, float y, Color color){


        ComponentBag altarBag = new ComponentBag();

        altarBag.add(new ParentComponent());
        altarBag.add(new PositionComponent(x,y));
        altarBag.add(new AltarComponent());
        altarBag.add(new VelocityComponent());
        altarBag.add(new GravityComponent());

        Rectangle bound = new Rectangle(new Rectangle(x,y, altarWidth, altarHeight / 3));
        altarBag.add(new CollisionBoundComponent(bound));
        altarBag.add(new ProximityTriggerAIComponent(activeAltar(), new HitBox(bound)));
        altarBag.add(new TextureRegionComponent(atlas.findRegion(TextureStrings.ALTAR), altarWidth, altarHeight,
                TextureRegionComponent.PLAYER_LAYER_FAR, new Color(color)));

        return altarBag;

    }




    public ComponentBag createItemAltarBag(float x, float y, Color color, final ItemType... itemTypes){

        ComponentBag altarBag = emptyAltar(x, y, color);

        altarBag.add(new DuringRoomLoadActionComponent(new Action() {
            @Override
            public void performAction(World world, Entity e) {


                Item altarItem = world.getSystem(ChangeLevelSystem.class).getJigsawGenerator().getItemStore().generateRoomItem(itemTypes);
                //altarItem = new ItemBigShot();

                e.getComponent(AltarComponent.class).pickUp = altarItem;

                BagToEntity.bagToEntity(world.createEntity(), altarItemTexture(altarItem, e.getComponent(ParentComponent.class),
                        new FollowPositionComponent(e.getComponent(PositionComponent.class).position, CenterMath.offsetX(altarWidth, altarItemWidth), Measure.units(5f))));

                e.edit().remove(DuringRoomLoadActionComponent.class);

            }
        }));

        return  altarBag;
    }



    public ComponentBag createCenteredPresetItemAltarBag(float x, float y, Color color, Item item){
        x = x - altarWidth / 2;
        y = y - altarHeight / 2;
        return createPresetItemAltarBag(x, y, color, item);
    }


    public ComponentBag createPresetItemAltarBag(float x, float y, Color color, final Item item){

        ComponentBag altarBag = emptyAltar(x, y, color);

        altarBag.add(new DuringRoomLoadActionComponent(new Action() {
            @Override
            public void performAction(World world, Entity e) {

                Item item = new ItemErraticFire();

                e.getComponent(AltarComponent.class).pickUp = item;

                BagToEntity.bagToEntity(world.createEntity(), altarItemTexture(item, e.getComponent(ParentComponent.class),
                        new FollowPositionComponent(e.getComponent(PositionComponent.class).position, CenterMath.offsetX(altarWidth, altarItemWidth), Measure.units(5f))));

                e.edit().remove(DuringRoomLoadActionComponent.class);

            }
        }));

        return altarBag;
    }


    public Array<ComponentBag> createShopItemBag(float x, float y, final int money){

        x = x - width / 2;
        y = y - height / 2;

        Array<ComponentBag> bags =  new Array<ComponentBag>();

        ParentComponent pc = new ParentComponent();

        ComponentBag shopItemTexture = new ComponentBag();
        shopItemTexture.add(new PositionComponent(x , y));
        shopItemTexture.add(new CollisionBoundComponent(new Rectangle(x,y, width, height)));
        shopItemTexture.add(new AltarComponent());
        shopItemTexture.add(new ActionOnTouchComponent(buyItem()));
        shopItemTexture.add(new CurrencyComponent(money));
        shopItemTexture.add(pc);

        shopItemTexture.add(new DuringRoomLoadActionComponent(new Action() {
            @Override
            public void performAction(World world, Entity e) {

                Item item = world.getSystem(ChangeLevelSystem.class).getJigsawGenerator().getItemStore().generateShopRoomItem();
                e.getComponent(AltarComponent.class).pickUp = item;

                e.edit().add(new TextureRegionComponent(atlas.findRegion(item.getValues().getRegion().getLeft(), item.getValues().getRegion().getRight()),
                        (width / 2) - (textureWidth / 2),
                        (height / 2) - (textureHeight / 2),
                        textureWidth,
                        textureHeight,
                        TextureRegionComponent.ENEMY_LAYER_FAR,
                        item.getValues().getTextureColor()));

                Vector3 position = e.getComponent(PositionComponent.class).position;

                BagToEntity.bagToEntity(world.createEntity(), priceTag(position.x, position.y, money, e.getComponent(ParentComponent.class)));


            }
        }));

        bags.add(shopItemTexture);

        return bags;
    }

    public ComponentBag priceTag(float x, float y, final int money, ParentComponent parentComponent){

        ComponentBag priceTag = new ComponentBag();
        priceTag.add(new PositionComponent(x + Measure.units(2f), y - Measure.units(1.5f)));
        priceTag.add(new TextureRegionComponent(atlas.findRegion(new MoneyPlus1().getValues().getRegion().getLeft(), new MoneyPlus1().getValues().getRegion().getRight()),
                goldWidth,
                goldHeight,
                TextureRegionComponent.ENEMY_LAYER_FAR));
        TextureFontComponent tfc = new TextureFontComponent(""+money);
        //tfc.width = width / 2;
        tfc.offsetX = Measure.units(5);
        tfc.offsetY = Measure.units(2.5f);
        priceTag.add(tfc);
        ChildComponent c = new ChildComponent(parentComponent);
        priceTag.add(c);


        final Color affordColor = new Color(Color.WHITE);
        final Color expensiveColor = new Color(Color.RED);


        ConditionalActionComponent conditionalActionComponent = new ConditionalActionComponent();

        conditionalActionComponent.add(new Condition() {
            @Override
            public boolean condition(World world, Entity entity) {
                return money <= world.getSystem(FindPlayerSystem.class).getPlayerComponent(CurrencyComponent.class).money;
            }
        }, new Action() {
            @Override
            public void performAction(World world, Entity e) {
                e.getComponent(TextureFontComponent.class).color = affordColor;
                e.getComponent(TextureFontComponent.class).DEFAULT = affordColor;
            }
        });


        conditionalActionComponent.add(new Condition() {
            @Override
            public boolean condition(World world, Entity entity) {
                return money > world.getSystem(FindPlayerSystem.class).getPlayerComponent(CurrencyComponent.class).money;
            }
        }, new Action() {
            @Override
            public void performAction(World world, Entity e) {
                e.getComponent(TextureFontComponent.class).color = expensiveColor;
                e.getComponent(TextureFontComponent.class).DEFAULT = expensiveColor;
            }
        });

        priceTag.add(conditionalActionComponent);

        return priceTag;

    }



    public Array<ComponentBag> createShopPickUpBag(float x, float y, PickUp pickUp, final int money){

        x = x - width / 2;
        y = y - height / 2;

        Array<ComponentBag> bags =  new Array<ComponentBag>();

        ParentComponent pc = new ParentComponent();

        ComponentBag shopItemTexture = new ComponentBag();
        shopItemTexture.add(new PositionComponent(x , y));
        shopItemTexture.add(new TextureRegionComponent(atlas.findRegion(pickUp.getValues().getRegion().getLeft(), pickUp.getValues().getRegion().getRight()),
                CenterMath.offsetX(width, textureWidth),
                CenterMath.offsetY(height, textureHeight),
                textureWidth,
                textureHeight,
                TextureRegionComponent.ENEMY_LAYER_FAR, pickUp.getValues().getTextureColor()));
        shopItemTexture.add(new CollisionBoundComponent(new Rectangle(x,y, width, height)));
        shopItemTexture.add(new AltarComponent(pickUp));
        shopItemTexture.add(new ActionOnTouchComponent(buyItem()));
        shopItemTexture.add(new CurrencyComponent(money));
        shopItemTexture.add(pc);

        shopItemTexture.add(new DuringRoomLoadActionComponent(new Action() {
            @Override
            public void performAction(World world, Entity e) {
                Vector3 position = e.getComponent(PositionComponent.class).position;
                BagToEntity.bagToEntity(world.createEntity(), priceTag(position.x, position.y, money, e.getComponent(ParentComponent.class)));
            }
        }));

        bags.add(shopItemTexture);

        return bags;
    }







    public ComponentBag randomizerShopOption(final float x, final float y, final int money, final ArenaSkin arenaSkin){


        ParentComponent pc = new ParentComponent();

        final ComponentBag shopItemTexture = new ComponentBag();
        shopItemTexture.add(new PositionComponent(x - width / 2, y - height / 2));
        shopItemTexture.add(new CollisionBoundComponent(new Rectangle(x,y, width, height)));
        shopItemTexture.add(new CurrencyComponent(money));
        shopItemTexture.add(pc);
        shopItemTexture.add(new TextureRegionComponent(atlas.findRegion(TextureStrings.SETTINGS_QUESTION_MARK),
                CenterMath.offsetX(width, textureWidth),
                CenterMath.offsetY(height, textureHeight),
                textureWidth,
                textureHeight,
                TextureRegionComponent.ENEMY_LAYER_FAR, new Color(Color.BLACK)));

        shopItemTexture.add(new DuringRoomLoadActionComponent(new Action() {
            @Override
            public void performAction(World world, Entity e) {
                Vector3 position = e.getComponent(PositionComponent.class).position;
                BagToEntity.bagToEntity(world.createEntity(), priceTag(position.x, position.y, money, e.getComponent(ParentComponent.class)));
            }
        }));


        shopItemTexture.add(new ActionOnTouchComponent(new Action() {


            int count = 0;

            FadeComponent fadeIn = new FadeComponent(true, 0.25f, false);
            FadeComponent fadeOut = new FadeComponent(false, 0.25f, false);

            @Override
            public void performAction(World world, Entity e) {

                CurrencyComponent playerMoney = world.getSystem(FindPlayerSystem.class).getPlayerComponent(CurrencyComponent.class);
                CurrencyComponent itemPrice = e.getComponent(CurrencyComponent.class);


                if(playerMoney.money - itemPrice.money >= 0) {

                    count++;
                    playerMoney.money -= itemPrice.money;

                    float spawnTime = 1f;

                    Entity spawner = BagToEntity.bagToEntity(world.createEntity(), new SpawnerFactory.SpawnerBuilder(assetManager, arenaSkin)
                            .spawnTime(spawnTime)
                            .build()
                            .spawnerBag(x, y));


                    spawner.edit().add(new OnDeathActionComponent());

                    CollisionBoundComponent cbc = spawner.getComponent(CollisionBoundComponent.class);

                    boolean bool;

                    if(count <= 10) {
                        bool = world.getSystem(LuckSystem.class).randomizerItemSpawn(spawner.getComponent(OnDeathActionComponent.class),
                                cbc.getCenterX(),
                                cbc.getCenterY(),
                                arenaSkin.getWallTint());
                    } else {
                        bool = true;
                        world.getSystem(LuckSystem.class).createRandomizerItem(LuckSystem.RandomizerOptions.ITEM,
                                spawner.getComponent(OnDeathActionComponent.class),
                                cbc.getCenterX(),
                                cbc.getCenterY(),
                                arenaSkin.getWallTint());
                    }

                    final boolean isItem = bool;

                    e.getComponent(ActionOnTouchComponent.class).isEnabled = false;

                    e.edit().add(fadeOut);

                    for(ChildComponent c : e.getComponent(ParentComponent.class).children){
                        Entity child = world.getSystem(FindChildSystem.class).findChildEntity(c);
                        if(child != null) child.edit().add(fadeOut);
                    }



                    e.edit().add(new ActionAfterTimeComponent(new Action() {
                        @Override
                        public void performAction(World world, Entity e) {

                            if(!isItem) {
                                e.edit().add(fadeIn);
                                e.getComponent(ActionOnTouchComponent.class).isEnabled = true;

                                for(ChildComponent c : e.getComponent(ParentComponent.class).children){
                                    Entity child = world.getSystem(FindChildSystem.class).findChildEntity(c);
                                    if(child != null) child.edit().add(fadeIn);
                                }

                            } else {
                                world.getSystem(OnDeathSystem.class).kill(e);
                            }
                        }
                    }, spawnTime));

                }


            }
        }));

        return shopItemTexture;



    }






    private Action buyItem(){
        return new Action() {
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
                        world.getSystem(com.bryjamin.wickedwizard.ecs.systems.graphical.MessageBannerSystem.class).createItemBanner(item.getValues().getName(), item.getValues().getDescription());

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
