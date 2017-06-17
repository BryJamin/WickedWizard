package com.byrjamin.wickedwizard.factories.enemy;

import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.IntMap;
import com.byrjamin.wickedwizard.assets.TextureStrings;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.ActionAfterTimeComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.Task;
import com.byrjamin.wickedwizard.ecs.components.identifiers.EnemyComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.FrictionComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.GravityComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.VelocityComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.AnimationComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.AnimationStateComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.byrjamin.wickedwizard.ecs.systems.FindPlayerSystem;
import com.byrjamin.wickedwizard.utils.ComponentBag;
import com.byrjamin.wickedwizard.utils.Measure;
import com.byrjamin.wickedwizard.utils.collider.HitBox;

/**
 * Created by Home on 17/06/2017.
 */

public class ModonFactory extends EnemyFactory{


    private final float width = Measure.units(15);
    private final float height = Measure.units(20);

    private final float textureWidth = Measure.units(20);
    private final float textureHeight = Measure.units(20);

    private final float textureOffsetX = -Measure.units(2.5f);
    private final float textureOffsetY = 0;

    private final float health = 20;

    public ModonFactory(AssetManager assetManager) {
        super(assetManager);
    }


    public ComponentBag modon(float x, float y){

        x = x - width / 2;
        y = y - height / 2;

        ComponentBag bag = this.defaultEnemyBag(new ComponentBag(), x, y, health);

        bag.add(new GravityComponent());
        bag.add(new VelocityComponent());


        CollisionBoundComponent cbc = new CollisionBoundComponent(new Rectangle(x,y,width,height));

        //lower
        cbc.hitBoxes.add(new HitBox(new Rectangle(x,y,width - Measure.units(1f), Measure.units(8f)), Measure.units(0.5f), 0));

        //mid
        cbc.hitBoxes.add(new HitBox(new Rectangle(x,y,width, Measure.units(8f)), 0, Measure.units(8f)));

        //upper
        cbc.hitBoxes.add(new HitBox(new Rectangle(x,y,width - Measure.units(4f), Measure.units(3f)), Measure.units(2f), Measure.units(16f)));

        bag.add(cbc);

        FrictionComponent fc = new FrictionComponent(true, false);
        fc.airFriction = false;

        bag.add(fc);

        bag.add(new AnimationStateComponent(0));
        IntMap<Animation<TextureRegion>> animMap = new IntMap<Animation<TextureRegion>>();
        animMap.put(0, new Animation<TextureRegion>(0.05f / 1f,
                atlas.findRegions(TextureStrings.MODON), Animation.PlayMode.LOOP));
        animMap.put(AnimationStateComponent.FIRING, new Animation<TextureRegion>(0.1f / 1f,
                atlas.findRegions(TextureStrings.MODON_FIRING)));
        bag.add(new AnimationComponent(animMap));

        TextureRegionComponent trc = new TextureRegionComponent(atlas.findRegion(TextureStrings.GOATWIZARD),
                textureOffsetX, 0, textureWidth, textureHeight, TextureRegionComponent.ENEMY_LAYER_MIDDLE);

        trc.color = new Color(91f / 255f,50f / 255f,86f / 255f, 1);
        trc.DEFAULT = new Color(91f / 255f,50f / 255f,86f / 255f, 1);

        bag.add(trc);


        bag.add(new ActionAfterTimeComponent(new Task() {
            @Override
            public void performAction(World world, Entity e) {
                CollisionBoundComponent pcbc = world.getSystem(FindPlayerSystem.class).getPC(CollisionBoundComponent.class);
                CollisionBoundComponent cbc = e.getComponent(CollisionBoundComponent.class);

                boolean isLeftOfPlayer = (cbc.getCenterX() < pcbc.getCenterX());

                e.getComponent(VelocityComponent.class).velocity.x = isLeftOfPlayer ? Measure.units(25f) : -Measure.units(25f);
                e.getComponent(VelocityComponent.class).velocity.y = Measure.units(85);

                e.getComponent(AnimationStateComponent.class).queueAnimationState(AnimationStateComponent.FIRING);


            }

            @Override
            public void cleanUpAction(World world, Entity e) {

            }
        }, 1.5f, true));


        return bag;







    }










}

