package com.byrjamin.wickedwizard.factories.chests;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.IntMap;
import com.byrjamin.wickedwizard.assets.ColorResource;
import com.byrjamin.wickedwizard.assets.SoundFileStrings;
import com.byrjamin.wickedwizard.assets.TextureStrings;
import com.byrjamin.wickedwizard.ecs.components.texture.BlinkOnHitComponent;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.HealthComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.Action;
import com.byrjamin.wickedwizard.ecs.components.ai.OnDeathActionComponent;
import com.byrjamin.wickedwizard.ecs.components.audio.HitSoundComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.ChestComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.LootComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.GravityComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.VelocityComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.AnimationComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.AnimationStateComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.byrjamin.wickedwizard.ecs.systems.audio.SoundSystem;
import com.byrjamin.wickedwizard.ecs.systems.level.RoomTransitionSystem;
import com.byrjamin.wickedwizard.factories.AbstractFactory;
import com.byrjamin.wickedwizard.factories.arenas.Arena;
import com.byrjamin.wickedwizard.factories.weapons.Giblets;
import com.byrjamin.wickedwizard.utils.BagSearch;
import com.byrjamin.wickedwizard.utils.ComponentBag;
import com.byrjamin.wickedwizard.utils.Measure;

/**
 * Created on 22/04/2017.
 *
 * Class used to created chest Entities within the game.
 *
 */
public class ChestFactory extends AbstractFactory {

    private Giblets giblets;

    public ChestFactory(AssetManager assetManager) {
        super(assetManager);

        this.giblets = new Giblets.GibletBuilder(assetManager)
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


    public ComponentBag chestBag(float x, float y){

        ComponentBag bag = new ComponentBag();

        bag.add(new PositionComponent(x, y));
        bag.add(new CollisionBoundComponent(new Rectangle(x, y, width, height), true));
        bag.add(new VelocityComponent());
        bag.add(new LootComponent(LootComponent.TYPE.CHEST, 7, 2));
        bag.add(new ChestComponent());
        bag.add(new GravityComponent());
        bag.add(new HealthComponent(3));
        bag.add(new BlinkOnHitComponent());

        bag.add(new HitSoundComponent(SoundFileStrings.hitMegaMix));

        bag.add(new AnimationStateComponent(AnimationStateComponent.DEFAULT));
        IntMap<Animation<TextureRegion>> animMap = new IntMap<Animation<TextureRegion>>();

        animMap.put(AnimationStateComponent.DEFAULT, new Animation<TextureRegion>(1f / 7.5f, atlas.findRegions(TextureStrings.CHEST), Animation.PlayMode.LOOP));
        bag.add(new AnimationComponent(animMap));

        bag.add(new TextureRegionComponent(atlas.findRegion(TextureStrings.CHEST), texWidth, texHeight,
                TextureRegionComponent.BACKGROUND_LAYER_NEAR));

        bag.add(new OnDeathActionComponent(chestDeathAction()));

        return bag;
    }


    public ComponentBag centeredChestBag(float x, float y) {
        x = x - width / 2;
        y = y- height / 2;
        return chestBag(x, y);
    }

    public ComponentBag centeredChestBag(float x, float y, OnDeathActionComponent odac){
        x = x - width / 2;
        y = y- height / 2;
        return chestBag(x,y,odac);
    }



    public ComponentBag chestBag(float x, float y, OnDeathActionComponent odac) {
        ComponentBag bag = chestBag(x, y);
        BagSearch.removeObjectOfTypeClass(OnDeathActionComponent.class, bag);
        bag.add(odac);
        return bag;
    }



    public Action chestDeathAction(){
        return new Action() {
            @Override
            public void performAction(World world, Entity e) {
                giblets.performAction(world, e);
                world.getSystem(SoundSystem.class).playRandomSound(SoundFileStrings.hitMegaMix);
            }
        };
    }


    public OnDeathActionComponent trapODAC(){
        return new OnDeathActionComponent(new Action() {
            @Override
            public void performAction(World world, Entity e) {
                chestDeathAction().performAction(world, e);

                Arena arena = world.getSystem(RoomTransitionSystem.class).getCurrentArena();
                arena.roomType = Arena.RoomType.TRAP;

            }
        });
    }

}
