package com.byrjamin.wickedwizard.factories.enemy;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.IntMap;
import com.byrjamin.wickedwizard.assets.TextureStrings;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.Weapon;
import com.byrjamin.wickedwizard.ecs.components.WeaponComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.ActionAfterTimeComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.ExpiryRangeComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.FiringAIComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.OnDeathActionComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.ProximityTriggerAIComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.Task;
import com.byrjamin.wickedwizard.ecs.components.identifiers.BulletComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.EnemyComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.BounceComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.VelocityComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.AnimationComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.AnimationStateComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.FadeComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.byrjamin.wickedwizard.factories.weapons.Giblets;
import com.byrjamin.wickedwizard.utils.BulletMath;
import com.byrjamin.wickedwizard.utils.ComponentBag;
import com.byrjamin.wickedwizard.utils.Measure;
import com.byrjamin.wickedwizard.utils.collider.HitBox;

/**
 * Created by Home on 22/06/2017.
 */

public class AlurmFactory extends EnemyFactory {



    private final float width = Measure.units(5);
    private final float height = Measure.units(5);

    private final float hitboxWidth = Measure.units(40);
    private final float hitboxHeight = Measure.units(40);

    private final float health = 10;

    private final float Vspeed = Measure.units(15);
    private final float speed = Measure.units(10f);

    private final float textureWidth = Measure.units(20);
    private final float textureHeight = Measure.units(20);

    private final float textureOffsetX = -Measure.units(1f);
    private final float textureOffsetY = 0;

    public AlurmFactory(AssetManager assetManager) {
        super(assetManager);
    }


    public ComponentBag alurm(float x, float y, boolean startsRight, boolean startsUp){

        x = x - width / 2;
        y = y - width / 2;

        ComponentBag bag = this.defaultEnemyBag(new ComponentBag(), x,y, health);

        bag.add(new CollisionBoundComponent(new Rectangle(x, y, width, height), true));

        bag.add(new AnimationStateComponent(0));
        IntMap<Animation<TextureRegion>> animMap = new IntMap<Animation<TextureRegion>>();
        animMap.put(0, new Animation<TextureRegion>(0.15f / 1f,
                atlas.findRegions(TextureStrings.ALURM), Animation.PlayMode.LOOP));
        animMap.put(AnimationStateComponent.FIRING, new Animation<TextureRegion>(0.05f / 1f,
                atlas.findRegions(TextureStrings.ALURM_FIRING), Animation.PlayMode.LOOP));

        bag.add(new AnimationComponent(animMap));
        bag.add(new TextureRegionComponent(atlas.findRegion(TextureStrings.ALURM),
                (width / 2) - (textureWidth / 2), (height / 2) - (textureHeight / 2), textureWidth, textureHeight,
                TextureRegionComponent.ENEMY_LAYER_MIDDLE));


        bag.add(new BounceComponent());
        bag.add(new VelocityComponent(startsRight ? speed : -speed, startsUp ? Vspeed : -Vspeed ));
        bag.add(new FiringAIComponent(0));


        bag.add(new ProximityTriggerAIComponent(new Task() {
            @Override
            public void performAction(World world, Entity e) {
                e.getComponent(AnimationStateComponent.class).queueAnimationState(AnimationStateComponent.FIRING);
                e.edit().add(new WeaponComponent(new ShieldBlast(), 0.1f));
            }

            @Override
            public void cleanUpAction(World world, Entity e) {
                e.edit().remove(WeaponComponent.class);
            }
        }, new HitBox(new Rectangle(x,y,hitboxWidth,hitboxHeight), (width / 2) - (hitboxWidth / 2), (height / 2) - (hitboxHeight / 2))));

        return bag;




    }



    private class ShieldBlast implements Weapon {

        private float[] angles = new float[]{0, 45, 90, 135, 180, 225, 270, 315};


        @Override
        public void fire(World world, Entity e, float x, float y, double angleInRadians) {


            for(int i = 0; i < angles.length; i++) {
                createBlock(world, new Vector3(e.getComponent(CollisionBoundComponent.class).getCenterX(),
                        e.getComponent(CollisionBoundComponent.class).getCenterY(),
                        0), angles[i], new Color(Color.RED));
                angles[i] += 22.5f;
            }



        }

        @Override
        public float getBaseFireRate() {
            return 0.1f;
        }

        @Override
        public float getBaseDamage() {
            return 0;
        }


        public void createBlock(World world, Vector3 centerOfOrbit, final float startAngle, Color color){

            Entity e = world.createEntity();
            float radius = Measure.units(5f);

            float size = Measure.units(2.5f);

            final float x = (float) (centerOfOrbit.x + (radius * Math.cos(Math.toRadians(startAngle)))) - size / 2;
            final float y = (float) (centerOfOrbit.y + (radius * Math.sin(Math.toRadians(startAngle)))) - size / 2;

            e.edit().add(new PositionComponent(x, y));
            e.edit().add(new VelocityComponent());
            e.edit().add(new BulletComponent());
            e.edit().add(new EnemyComponent());
            e.edit().add(new CollisionBoundComponent(new Rectangle(x,y,size,size), true));
            e.edit().add(new ExpiryRangeComponent(new Vector3(x,y,0), Measure.units(20f)));
            //e.edit().add(new OrbitComponent(centerOfOrbit, radius, 2, startAngle, width / 2, height / 2));
            e.edit().add(new FadeComponent(true, 0.2f, false));
            e.edit().add(new OnDeathActionComponent(new Giblets.GibletBuilder(assetManager)
                    .numberOfGibletPairs(3)
                    .fadeChance(0.0f)
                    .size(Measure.units(0.5f))
                    .minSpeed(Measure.units(10f))
                    .maxSpeed(Measure.units(20f))
                    .colors(color)
                    .intangible(false)
                    .expiryTime(0.2f)
                    .build()));

            TextureRegionComponent trc = new TextureRegionComponent(atlas.findRegion("block"), size, size, TextureRegionComponent.PLAYER_LAYER_FAR);
            trc.DEFAULT = color;
            trc.color = color;
            e.edit().add(trc);


            e.edit().add(new ActionAfterTimeComponent(new Task() {
                @Override
                public void performAction(World world, Entity e) {
                    VelocityComponent vc = e.getComponent(VelocityComponent.class);
                    vc.velocity.x = BulletMath.velocityX(Measure.units(62.5f), Math.toRadians(startAngle));
                    vc.velocity.y = BulletMath.velocityY(Measure.units(62.5f), Math.toRadians(startAngle));

                    System.out.println("angle is :" + startAngle);

                }

                @Override
                public void cleanUpAction(World world, Entity e) {

                }
            }, 0.2f));







        }


    }



}
