package com.bryjamin.wickedwizard.factories.enemy;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.IntMap;

/**
 * Created by Home on 11/06/2017.
 */

public class JigFactory extends EnemyFactory{

    private float width = com.bryjamin.wickedwizard.utils.Measure.units(15f);
    private float height = com.bryjamin.wickedwizard.utils.Measure.units(10f);

    private float texwidth = com.bryjamin.wickedwizard.utils.Measure.units(15f);
    private float texheight = com.bryjamin.wickedwizard.utils.Measure.units(15f);

    private float hitBoxWidth = com.bryjamin.wickedwizard.utils.Measure.units(10f);
    private float hitBoxHeight = com.bryjamin.wickedwizard.utils.Measure.units(6.5f);

    private float bodyHitBoxWidth = com.bryjamin.wickedwizard.utils.Measure.units(15f);
    private float bodyHitBoxHeight = com.bryjamin.wickedwizard.utils.Measure.units(2.5f);

    private static final float initialChargeTime = 0.5f;
    private static final float fireRate = 0.75f;

    private static final float speed = com.bryjamin.wickedwizard.utils.Measure.units(10f);

    private static final float health = 15;

    public JigFactory(AssetManager assetManager) {
        super(assetManager);
    }


    public com.bryjamin.wickedwizard.utils.ComponentBag stationaryJig(float x, float y){

        x = x - width / 2;
        y = y - height / 2;

        com.bryjamin.wickedwizard.utils.ComponentBag bag = new com.bryjamin.wickedwizard.utils.ComponentBag();
        this.defaultEnemyBag(bag, x,y,health);


        bag.add(new com.bryjamin.wickedwizard.ecs.components.movement.CollisionBoundComponent(new Rectangle(x,y,width,height),
                new com.bryjamin.wickedwizard.utils.collider.HitBox(new Rectangle(x,y,hitBoxWidth,hitBoxHeight),
                        com.bryjamin.wickedwizard.utils.CenterMath.offsetX(width, hitBoxWidth),
                        com.bryjamin.wickedwizard.utils.CenterMath.offsetY(height, hitBoxHeight)),
                new com.bryjamin.wickedwizard.utils.collider.HitBox(new Rectangle(x,y,bodyHitBoxWidth,bodyHitBoxHeight),
                        com.bryjamin.wickedwizard.utils.CenterMath.offsetX(width, bodyHitBoxWidth),
                        com.bryjamin.wickedwizard.utils.CenterMath.offsetY(height, bodyHitBoxHeight))
                ));



        com.bryjamin.wickedwizard.ecs.components.texture.TextureRegionComponent trc = new com.bryjamin.wickedwizard.ecs.components.texture.TextureRegionComponent(atlas.findRegion(com.bryjamin.wickedwizard.assets.TextureStrings.JIG_FIRING),
                com.bryjamin.wickedwizard.utils.CenterMath.offsetX(width, texwidth), com.bryjamin.wickedwizard.utils.CenterMath.offsetY(height, texheight), texwidth, texheight, com.bryjamin.wickedwizard.ecs.components.texture.TextureRegionComponent.ENEMY_LAYER_MIDDLE);

        bag.add(trc);


        bag.add(new com.bryjamin.wickedwizard.ecs.components.texture.AnimationStateComponent(com.bryjamin.wickedwizard.ecs.components.texture.AnimationStateComponent.DEFAULT));
        IntMap<Animation<TextureRegion>> animMap = new IntMap<Animation<TextureRegion>>();
        animMap.put(com.bryjamin.wickedwizard.ecs.components.texture.AnimationStateComponent.DEFAULT, new Animation<TextureRegion>(0.3f / 1f,
                atlas.findRegions(com.bryjamin.wickedwizard.assets.TextureStrings.JIG_FIRING), Animation.PlayMode.LOOP));
        animMap.put(com.bryjamin.wickedwizard.ecs.components.texture.AnimationStateComponent.FIRING, new Animation<TextureRegion>(0.1f / 1f,
                atlas.findRegions(com.bryjamin.wickedwizard.assets.TextureStrings.JIG_FIRING), Animation.PlayMode.REVERSED));
        bag.add(new com.bryjamin.wickedwizard.ecs.components.texture.AnimationComponent(animMap));

        bag.add(new com.bryjamin.wickedwizard.ecs.components.ai.FiringAIComponent(0,0));

        bag.add(new com.bryjamin.wickedwizard.ecs.components.ai.ActionAfterTimeComponent(new com.bryjamin.wickedwizard.ecs.components.ai.Action() {
            @Override
            public void performAction(World world, Entity e) {
                e.getComponent(com.bryjamin.wickedwizard.ecs.components.ai.FiringAIComponent.class).firingAngleInRadians += Math.toRadians(30f);
            }
        }, fireRate, true));


        com.bryjamin.wickedwizard.factories.weapons.enemy.MultiPistol multiPistol = new com.bryjamin.wickedwizard.factories.weapons.enemy.MultiPistol.PistolBuilder(assetManager)
                .angles(0, 60,120,180,240,300)
                .shotSpeed(com.bryjamin.wickedwizard.utils.Measure.units(30f))
                .fireRate(fireRate)
                .shotScale(3)
                .build();

        bag.add(new com.bryjamin.wickedwizard.ecs.components.WeaponComponent(multiPistol, initialChargeTime));

        return bag;


    }


    public com.bryjamin.wickedwizard.utils.ComponentBag movingJig(float x, float y, boolean startsRight){

        com.bryjamin.wickedwizard.utils.ComponentBag bag = stationaryJig(x,y);
        bag.add(new com.bryjamin.wickedwizard.ecs.components.movement.VelocityComponent(startsRight ? speed : -speed, 0));
        bag.add(new com.bryjamin.wickedwizard.ecs.components.movement.BounceComponent());

        return bag;




    }







}
