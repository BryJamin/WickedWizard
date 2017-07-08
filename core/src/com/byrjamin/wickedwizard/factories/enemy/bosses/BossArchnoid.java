package com.byrjamin.wickedwizard.factories.enemy.bosses;

import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.byrjamin.wickedwizard.assets.TextureStrings;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.HealthComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.Action;
import com.byrjamin.wickedwizard.ecs.components.ai.ActionAfterTimeComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.Condition;
import com.byrjamin.wickedwizard.ecs.components.ai.ConditionalActionComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.FollowPositionComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.Task;
import com.byrjamin.wickedwizard.ecs.components.identifiers.IntangibleComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.BounceComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.GravityComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.VelocityComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.byrjamin.wickedwizard.ecs.systems.FindPlayerSystem;
import com.byrjamin.wickedwizard.ecs.systems.ai.OnDeathSystem;
import com.byrjamin.wickedwizard.factories.enemy.EnemyFactory;
import com.byrjamin.wickedwizard.utils.BulletMath;
import com.byrjamin.wickedwizard.utils.CenterMath;
import com.byrjamin.wickedwizard.utils.ComponentBag;
import com.byrjamin.wickedwizard.utils.Measure;
import com.byrjamin.wickedwizard.utils.Pair;
import com.byrjamin.wickedwizard.utils.collider.HitBox;

/**
 * Created by Home on 08/07/2017.
 */

public class BossArchnoid extends EnemyFactory {

    private static final float health = 100;

    private static final float bodyWidth = Measure.units(70f);
    private static final float bodyHeight = Measure.units(90f);

    private static final float bodyTextureWidth = Measure.units(70f);
    private static final float bodyTextureHeight = Measure.units(90f);

    private static final float enrageDistance = Measure.units(140f);


    private static final float speed = Measure.units(10f);

    private static final float legHeight = Measure.units(60f);
    private static final float legWidth = Measure.units(20f);
    private static final float legHealth = 20;

    private static final float legOffSetX = -Measure.units(40f);
    private static final float rightlegOffSetX = Measure.units(60f);


    public BossArchnoid(AssetManager assetManager) {
        super(assetManager);
    }




    public ComponentBag archnoid(final float x, final float y){

        ComponentBag bag = this.defaultEnemyBag(new ComponentBag(), x, y, health);

        bag.add(new CollisionBoundComponent(new Rectangle(x, y, bodyWidth, bodyHeight), true));


        bag.add(new VelocityComponent(speed, 0));

        bag.add(new IntangibleComponent());

        ConditionalActionComponent cac = new ConditionalActionComponent();
        cac.add(new Condition() {
                    @Override
                    public boolean condition(World world, Entity entity) {
                        return entity.getComponent(CollisionBoundComponent.class).bound.overlaps(world.getSystem(FindPlayerSystem.class).getPC(CollisionBoundComponent.class).bound);

                    }
                }, new Task() {
                    @Override
                    public void performAction(World world, Entity e) {
                        VelocityComponent vc = world.getSystem(FindPlayerSystem.class).getPC(VelocityComponent.class);
                        vc.velocity.x = BulletMath.velocityX(Measure.units(50f), Math.toRadians(0));
                        vc.velocity.y = BulletMath.velocityY(Measure.units(50f), Math.toRadians(0));
                    }

                    @Override
                    public void cleanUpAction(World world, Entity e) {

                    }
                });



        cac.add(new Condition() {

            private Vector3 startPosition = new Vector3(x, y, 0);

            private float distanceTravelled;


            @Override
            public boolean condition(World world, Entity entity) {

                PositionComponent positionComponent = entity.getComponent(PositionComponent.class);


                float distance = startPosition.dst(positionComponent.position);

                distanceTravelled += Math.abs(distance);
                startPosition.set(positionComponent.position);

                if(distanceTravelled > enrageDistance){
                    return true;
                }

                return false;
            }
        }, new Task() {
            @Override
            public void performAction(World world, Entity e) {
                e.getComponent(VelocityComponent.class).velocity.x = 0;
                world.getSystem(FindPlayerSystem.class).getPC(HealthComponent.class).health = 1;
                world.getSystem(FindPlayerSystem.class).getPC(HealthComponent.class).applyDamage(2);
            }

            @Override
            public void cleanUpAction(World world, Entity e) {

            }
        });


        bag.add(cac);

        bag.add(new TextureRegionComponent(atlas.findRegion(TextureStrings.BLOCK),
                CenterMath.offsetX(bodyWidth, bodyTextureWidth),
                CenterMath.offsetY(bodyHeight, bodyTextureHeight),
                bodyTextureWidth,
                bodyTextureHeight,
                TextureRegionComponent.FOREGROUND_LAYER_MIDDLE));



        return bag;
    }




}
