package com.byrjamin.wickedwizard.factories.enemy.bosses;

import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.Rectangle;
import com.byrjamin.wickedwizard.assets.TextureStrings;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
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
import com.byrjamin.wickedwizard.factories.enemy.EnemyFactory;
import com.byrjamin.wickedwizard.utils.BulletMath;
import com.byrjamin.wickedwizard.utils.CenterMath;
import com.byrjamin.wickedwizard.utils.ComponentBag;
import com.byrjamin.wickedwizard.utils.Measure;
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


    private static final float speed = Measure.units(10f);

    private static final float legHeight = Measure.units(60f);
    private static final float legWidth = Measure.units(20f);
    private static final float legHealth = 20;

    private static final float legOffSetX = -Measure.units(40f);
    private static final float rightlegOffSetX = Measure.units(60f);


    public BossArchnoid(AssetManager assetManager) {
        super(assetManager);
    }




    public ComponentBag archnoid(float x, float y){

        ComponentBag bag = this.defaultEnemyBag(new ComponentBag(), x, y, health);

        bag.add(new CollisionBoundComponent(new Rectangle(x, y, bodyWidth, bodyHeight), true));


        bag.add(new VelocityComponent(speed, 0));

        bag.add(new IntangibleComponent());

        bag.add(pushBackCondition(0));

        bag.add(new TextureRegionComponent(atlas.findRegion(TextureStrings.BLOCK),
                CenterMath.offsetX(bodyWidth, bodyTextureWidth),
                CenterMath.offsetY(bodyHeight, bodyTextureHeight),
                bodyTextureWidth,
                bodyTextureHeight,
                TextureRegionComponent.FOREGROUND_LAYER_MIDDLE));



        return bag;
    }




    public ConditionalActionComponent pushBackCondition(final float pushAngleInDegrees){
        return (new ConditionalActionComponent(new Condition() {
            @Override
            public boolean condition(World world, Entity entity) {
                return entity.getComponent(CollisionBoundComponent.class).bound.overlaps(world.getSystem(FindPlayerSystem.class).getPC(CollisionBoundComponent.class).bound);

            }


        }, new Task() {
            @Override
            public void performAction(World world, Entity e) {
                VelocityComponent vc = world.getSystem(FindPlayerSystem.class).getPC(VelocityComponent.class);
                vc.velocity.x = BulletMath.velocityX(Measure.units(50f), Math.toRadians(pushAngleInDegrees));
                vc.velocity.y = BulletMath.velocityY(Measure.units(50f), Math.toRadians(pushAngleInDegrees));
            }

            @Override
            public void cleanUpAction(World world, Entity e) {

            }
        }));
    }




}
