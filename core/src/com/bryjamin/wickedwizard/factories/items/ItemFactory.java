package com.bryjamin.wickedwizard.factories.items;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.bryjamin.wickedwizard.assets.ColorResource;
import com.bryjamin.wickedwizard.assets.SoundFileStrings;
import com.bryjamin.wickedwizard.assets.TextureStrings;
import com.bryjamin.wickedwizard.ecs.components.CurrencyComponent;
import com.bryjamin.wickedwizard.ecs.components.ai.Action;
import com.bryjamin.wickedwizard.ecs.components.ai.ActionAfterTimeComponent;
import com.bryjamin.wickedwizard.ecs.components.ai.ActionOnTouchComponent;
import com.bryjamin.wickedwizard.ecs.components.ai.Condition;
import com.bryjamin.wickedwizard.ecs.components.ai.ConditionalActionComponent;
import com.bryjamin.wickedwizard.ecs.components.ai.DuringRoomLoadActionComponent;
import com.bryjamin.wickedwizard.ecs.components.ai.MoveToPlayerComponent;
import com.bryjamin.wickedwizard.ecs.components.ai.OnDeathActionComponent;
import com.bryjamin.wickedwizard.ecs.components.ai.Task;
import com.bryjamin.wickedwizard.ecs.components.identifiers.ChildComponent;
import com.bryjamin.wickedwizard.ecs.components.identifiers.IntangibleComponent;
import com.bryjamin.wickedwizard.ecs.components.identifiers.OffScreenPickUpComponent;
import com.bryjamin.wickedwizard.ecs.components.identifiers.ParentComponent;
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
import com.bryjamin.wickedwizard.ecs.systems.ai.OnDeathSystem;
import com.bryjamin.wickedwizard.ecs.systems.level.ChangeLevelSystem;
import com.bryjamin.wickedwizard.factories.AbstractFactory;
import com.bryjamin.wickedwizard.factories.arenas.skins.ArenaSkin;
import com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory;
import com.bryjamin.wickedwizard.factories.items.pickups.MoneyPlus1;
import com.bryjamin.wickedwizard.factories.weapons.Giblets;
import com.bryjamin.wickedwizard.utils.BagToEntity;
import com.bryjamin.wickedwizard.utils.BulletMath;
import com.bryjamin.wickedwizard.utils.CenterMath;
import com.bryjamin.wickedwizard.utils.ComponentBag;
import com.bryjamin.wickedwizard.utils.Measure;

import java.util.Random;

import static com.bryjamin.wickedwizard.factories.items.ItemInteractionTasks.buyItem;


/**
 * Created by Home on 09/04/2017.
 */

public class ItemFactory extends AbstractFactory {


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
        shopItemTexture.add(new ActionOnTouchComponent(ItemInteractionTasks.buyItem()));
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




}
