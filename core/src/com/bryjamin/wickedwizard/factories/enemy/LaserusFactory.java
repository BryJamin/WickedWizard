package com.bryjamin.wickedwizard.factories.enemy;


import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.IntMap;
import com.bryjamin.wickedwizard.ecs.components.Weapon;
import com.bryjamin.wickedwizard.ecs.components.WeaponComponent;
import com.bryjamin.wickedwizard.ecs.components.ai.FiringAIComponent;
import com.bryjamin.wickedwizard.ecs.components.ai.ProximityTriggerAIComponent;
import com.bryjamin.wickedwizard.ecs.components.ai.Task;
import com.bryjamin.wickedwizard.ecs.components.identifiers.ChildComponent;
import com.bryjamin.wickedwizard.ecs.components.identifiers.ParentComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.BounceComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.CollisionBoundComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.VelocityComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.AnimationComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.AnimationStateComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.bryjamin.wickedwizard.factories.weapons.enemy.LaserBeam;
import com.bryjamin.wickedwizard.utils.CenterMath;
import com.bryjamin.wickedwizard.utils.ComponentBag;
import com.bryjamin.wickedwizard.utils.Measure;

/**
 * Created by Home on 25/06/2017.
 */
public class LaserusFactory extends EnemyFactory {

    public float width = Measure.units(10f);
    public float height = Measure.units(10f);

    private final float hSpeed = Measure.units(20f);
    private final float vSpeed = Measure.units(15f);



    public float laserusHealth = 20f;

    private final static float laserCharge = 1f;
    private final static float fireRate = 1.5f;

    public LaserusFactory(AssetManager assetManager) {
        super(assetManager);
    }


    public ComponentBag laserus(float x, float y, boolean startsRight, boolean startsUp){

        x = x - width / 2;
        y = y - height / 2;

        ComponentBag bag = this.defaultEnemyBag(new ComponentBag(), x , y, laserusHealth);

        bag.add(new CollisionBoundComponent(new Rectangle(x,y, width, height), true));
        bag.add(new TextureRegionComponent(atlas.findRegion(com.bryjamin.wickedwizard.assets.TextureStrings.LASERUS),
                0, 0, width, height, TextureRegionComponent.ENEMY_LAYER_MIDDLE
        ));

        bag.add(new AnimationStateComponent(0));
        IntMap<Animation<TextureRegion>> animMap = new IntMap<Animation<TextureRegion>>();
        animMap.put(0, new Animation<TextureRegion>(0.15f / 1f, atlas.findRegions(com.bryjamin.wickedwizard.assets.TextureStrings.LASERUS), Animation.PlayMode.LOOP));
        animMap.put(AnimationStateComponent.FIRING, new Animation<TextureRegion>(0.10f / 1f, atlas.findRegions(com.bryjamin.wickedwizard.assets.TextureStrings.LASERUS_FIRING)));

        bag.add(new AnimationComponent(animMap));

        bag.add(new VelocityComponent(startsRight ? hSpeed : -hSpeed, startsUp ? vSpeed : - vSpeed));
        bag.add(new BounceComponent());

        bag.add(new ParentComponent());

        WeaponComponent wc = new WeaponComponent(
                new LazerusWeapon(assetManager));
        bag.add(wc);


        bag.add(new ProximityTriggerAIComponent(new Task() {
            @Override
            public void performAction(World world, Entity e) {
                e.edit().add(new FiringAIComponent());
            }

            @Override
            public void cleanUpAction(World world, Entity e) {
                e.edit().remove(FiringAIComponent.class);
            }
        }, true));

        return bag;

    }



    private class LazerusWeapon implements Weapon {


        private LaserBeam middleBeam;
        private LaserBeam leftBeam;
        private LaserBeam rightBeam;

        private float length = Measure.units(500f);
        private float girth = Measure.units(5f);


        public LazerusWeapon(AssetManager assetManager){

            LaserBeam.LaserBeamBuilder lbb = new LaserBeam.LaserBeamBuilder(assetManager)
                    .activeLaserHeight(length)
                    .activeLaserWidth(girth)
                    .chargingLaserHeight(length)
                    .chargingLaserWidth(girth)
                    .activeLaserTime(0.25f)
                    .activeLaserDisperseTime(0.5f)
                    .chargingLaserTime(laserCharge);

            middleBeam = lbb.build();

            lbb.activeLaserHeight(girth)
                    .activeLaserWidth(length)
                    .chargingLaserHeight(girth)
                    .chargingLaserWidth(length);

            leftBeam = lbb.build();
            rightBeam = lbb.build();

        }


        @Override
        public void fire(World world, Entity e, float x, float y, double angleInRadians) {

            CollisionBoundComponent cbc = e.getComponent(CollisionBoundComponent.class);

            float midOffsetX = CenterMath.offsetX(cbc.bound.getWidth(), middleBeam.getChargingLaserWidth());
            float midOffsetY = CenterMath.offsetY(cbc.bound.getHeight(), middleBeam.getChargingLaserHeight());

            Entity beam = middleBeam.createBeam(world,
                    cbc.bound.x + midOffsetX,
                    cbc.bound.y + midOffsetY);

            beam.edit().add(new com.bryjamin.wickedwizard.ecs.components.ai.FollowPositionComponent(e.getComponent(PositionComponent.class).position, midOffsetX, midOffsetY));
            beam.edit().add(new ChildComponent(e.getComponent(ParentComponent.class)));



            float leftOffsetX = CenterMath.offsetX(cbc.bound.getWidth(), leftBeam.getChargingLaserWidth());
            float leftOffsetY = CenterMath.offsetY(cbc.bound.getHeight(), leftBeam.getChargingLaserHeight());

            Entity leftBeam = this.leftBeam.createBeam(world,
                    cbc.bound.x + midOffsetX - length,
                    cbc.bound.y + leftOffsetY);

            leftBeam.edit().add(new com.bryjamin.wickedwizard.ecs.components.ai.FollowPositionComponent(e.getComponent(PositionComponent.class).position, midOffsetX - length, leftOffsetY));
            leftBeam.edit().add(new ChildComponent(e.getComponent(ParentComponent.class)));


            float rightOffsetY = CenterMath.offsetY(cbc.bound.getHeight(), rightBeam.getChargingLaserHeight());

            Entity rightBeam = this.rightBeam.createBeam(world,
                    cbc.bound.x + midOffsetX + girth,
                    cbc.bound.y + rightOffsetY);

            rightBeam.edit().add(new com.bryjamin.wickedwizard.ecs.components.ai.FollowPositionComponent(e.getComponent(PositionComponent.class).position, midOffsetX + girth, rightOffsetY));
            rightBeam.edit().add(new ChildComponent(e.getComponent(ParentComponent.class)));

        }

        @Override
        public float getBaseFireRate() {return fireRate;}

        @Override
        public float getBaseDamage() {return 0;}
    }


}
