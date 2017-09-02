package com.bryjamin.wickedwizard.factories.chests;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.IntMap;
import com.bryjamin.wickedwizard.assets.ColorResource;
import com.bryjamin.wickedwizard.assets.SoundFileStrings;
import com.bryjamin.wickedwizard.ecs.components.ai.Action;
import com.bryjamin.wickedwizard.ecs.components.ai.OnDeathActionComponent;
import com.bryjamin.wickedwizard.ecs.components.identifiers.ChestComponent;
import com.bryjamin.wickedwizard.ecs.components.identifiers.LootComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.CollisionBoundComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.AnimationComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.AnimationStateComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.bryjamin.wickedwizard.factories.AbstractFactory;
import com.bryjamin.wickedwizard.factories.arenas.Arena;
import com.bryjamin.wickedwizard.utils.Measure;

/**
 * Created on 22/04/2017.
 *
 * Class used to created chest Entities within the game.
 *
 */
public class ChestFactory extends AbstractFactory {

    private com.bryjamin.wickedwizard.factories.weapons.Giblets giblets;

    public ChestFactory(AssetManager assetManager) {
        super(assetManager);

        this.giblets = new com.bryjamin.wickedwizard.factories.weapons.Giblets.GibletBuilder(assetManager)
                .numberOfGibletPairs(5)
                .expiryTime(0.4f)
                .minSpeed(5f)
                .maxSpeed(Measure.units(40))
                .size(Measure.units(1f))
                .colors(new Color(Color.BROWN), new Color(ColorResource.BOMB_YELLOW)) //Maybe change the color of this?
                .build();


    }

    public final float width = Measure.units(10f);
    public final float height = Measure.units(7f);

    public final float texWidth = Measure.units(10f);
    public final float texHeight = Measure.units(10f);


    public com.bryjamin.wickedwizard.utils.ComponentBag chestBag(float x, float y){

        com.bryjamin.wickedwizard.utils.ComponentBag bag = new com.bryjamin.wickedwizard.utils.ComponentBag();

        bag.add(new PositionComponent(x, y));
        bag.add(new CollisionBoundComponent(new Rectangle(x, y, width, height), true));
        bag.add(new com.bryjamin.wickedwizard.ecs.components.movement.VelocityComponent());
        bag.add(new LootComponent(LootComponent.TYPE.CHEST, 7, 2));
        bag.add(new ChestComponent());
        bag.add(new com.bryjamin.wickedwizard.ecs.components.movement.GravityComponent());
        bag.add(new com.bryjamin.wickedwizard.ecs.components.HealthComponent(3));
        bag.add(new com.bryjamin.wickedwizard.ecs.components.texture.BlinkOnHitComponent());

       // bag.add(new HitSoundComponent(SoundFileStrings.hitMegaMix));

        bag.add(new AnimationStateComponent(AnimationStateComponent.DEFAULT));
        IntMap<Animation<TextureRegion>> animMap = new IntMap<Animation<TextureRegion>>();

        animMap.put(AnimationStateComponent.DEFAULT, new Animation<TextureRegion>(1f / 7.5f, atlas.findRegions(com.bryjamin.wickedwizard.assets.TextureStrings.CHEST), Animation.PlayMode.LOOP));
        bag.add(new AnimationComponent(animMap));

        bag.add(new TextureRegionComponent(atlas.findRegion(com.bryjamin.wickedwizard.assets.TextureStrings.CHEST), texWidth, texHeight,
                TextureRegionComponent.BACKGROUND_LAYER_NEAR));

        bag.add(new OnDeathActionComponent(chestDeathAction()));

        return bag;
    }


    public com.bryjamin.wickedwizard.utils.ComponentBag centeredChestBag(float x, float y) {
        x = x - width / 2;
        y = y- height / 2;
        return chestBag(x, y);
    }

    public com.bryjamin.wickedwizard.utils.ComponentBag centeredChestBag(float x, float y, OnDeathActionComponent odac){
        x = x - width / 2;
        y = y- height / 2;
        return chestBag(x,y,odac);
    }



    public com.bryjamin.wickedwizard.utils.ComponentBag chestBag(float x, float y, OnDeathActionComponent odac) {
        com.bryjamin.wickedwizard.utils.ComponentBag bag = chestBag(x, y);
        com.bryjamin.wickedwizard.utils.BagSearch.removeObjectOfTypeClass(OnDeathActionComponent.class, bag);
        bag.add(odac);
        return bag;
    }



    public Action chestDeathAction(){
        return new Action() {
            @Override
            public void performAction(World world, Entity e) {
                giblets.performAction(world, e);
                world.getSystem(com.bryjamin.wickedwizard.ecs.systems.audio.SoundSystem.class).playRandomSound(SoundFileStrings.explosionMegaMix);
            }
        };
    }


    public OnDeathActionComponent trapODAC(){
        return new OnDeathActionComponent(new Action() {
            @Override
            public void performAction(World world, Entity e) {
                chestDeathAction().performAction(world, e);

                Arena arena = world.getSystem(com.bryjamin.wickedwizard.ecs.systems.level.RoomTransitionSystem.class).getCurrentArena();
                arena.arenaType = Arena.ArenaType.TRAP;

            }
        });
    }

}
