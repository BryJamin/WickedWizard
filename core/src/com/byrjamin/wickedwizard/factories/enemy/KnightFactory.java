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
import com.byrjamin.wickedwizard.ecs.components.BlinkComponent;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.HealthComponent;
import com.byrjamin.wickedwizard.ecs.components.Weapon;
import com.byrjamin.wickedwizard.ecs.components.WeaponComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.ActionAfterTimeComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.FiringAIComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.OnDeathActionComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.ProximityTriggerAIComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.Task;
import com.byrjamin.wickedwizard.ecs.components.identifiers.BulletComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.ChildComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.EnemyComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.ParentComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.BounceComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.OrbitComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.VelocityComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.AnimationComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.AnimationStateComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.FadeComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.byrjamin.wickedwizard.ecs.systems.FindChildSystem;
import com.byrjamin.wickedwizard.factories.GibletFactory;
import com.byrjamin.wickedwizard.utils.BulletMath;
import com.byrjamin.wickedwizard.utils.ComponentBag;
import com.byrjamin.wickedwizard.utils.Measure;
import com.byrjamin.wickedwizard.utils.collider.HitBox;

/**
 * Created by Home on 23/06/2017.
 */

public class KnightFactory extends EnemyFactory {


    private static final float height = Measure.units(20f);
    private static final float width = Measure.units(20f);

    private static final float texheight = Measure.units(15f);
    private static final float texwidth = Measure.units(15f);


    //TODO may add a hitbox for it's little arms?

    private static final float hitboxHeight = Measure.units(8);
    private static final float hitboxOffsetY = Measure.units(7.5f);
    private static final float hitboxWidth = Measure.units(11f);

    private static final float health = 20;

    private static final float vSpeed = Measure.units(5f);
    private static final float hSpeed = Measure.units(10f);

    private final GibletFactory gibletFactory;


    public KnightFactory(AssetManager assetManager) {
        super(assetManager);
        this.gibletFactory = new GibletFactory(assetManager);





    }



    public ComponentBag knightBag(float x, float y, boolean startsRight, boolean startsUp){

        x = x - width / 2;
        y = y - height / 2;

        ComponentBag bag = new ComponentBag();
        bag = defaultEnemyBag(bag, x, y, health);

        bag.add(new VelocityComponent(startsRight ? hSpeed : -hSpeed,
                startsUp ? vSpeed  : -vSpeed));
        bag.add(new BounceComponent());


        CollisionBoundComponent cbc = new CollisionBoundComponent();

        cbc.bound = new Rectangle(x , y , width, height);
        cbc.hitBoxes.add(new HitBox(new Rectangle(x, y, hitboxWidth, hitboxHeight), (width / 2) - (hitboxWidth / 2), hitboxOffsetY));

        bag.add(cbc);

        bag.add(new AnimationStateComponent(0));
        IntMap<Animation<TextureRegion>> animMap = new IntMap<Animation<TextureRegion>>();
        animMap.put(0, new Animation<TextureRegion>(0.1f / 1f,
                atlas.findRegions(TextureStrings.KNIGHT), Animation.PlayMode.LOOP_RANDOM));
        animMap.put(AnimationStateComponent.FIRING, new Animation<TextureRegion>(0.1f / 1f,
                atlas.findRegions(TextureStrings.KNIGHT_FIRING), Animation.PlayMode.LOOP_RANDOM));
        bag.add(new AnimationComponent(animMap));

        TextureRegionComponent trc = new TextureRegionComponent(atlas.findRegion(TextureStrings.GOATWIZARD),
                (width / 2) - (texwidth / 2), (height / 2) - (texheight / 2), texwidth, texheight, TextureRegionComponent.ENEMY_LAYER_MIDDLE);

        trc.color = new Color(91f / 255f,50f / 255f,86f / 255f, 1);
        trc.DEFAULT = new Color(91f / 255f,50f / 255f,86f / 255f, 1);

        bag.add(trc);


        bag.add(new WeaponComponent(new KnightWeapon(), 0));
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
        bag.add(new ParentComponent());

        return bag;
    }




















    private class KnightWeapon implements Weapon {


        float [] angles = new float[] {0,45,90,135,180,225,270,315};

        private boolean isShield = true;

        //TODO somehow track if any balls have been killed and re-summon after a count down period

        @Override
        public void fire(World world, Entity e, float x, float y, double angleInRadians) {

            if(isShield) {
                for (float f : angles) {
                    createBlock(world, e.getComponent(ParentComponent.class), e.getComponent(PositionComponent.class).position, f, new Color(Color.BLACK));
                }
                isShield = false;
            } else {

                for(ChildComponent c : e.getComponent(ParentComponent.class).children){
                    Entity child = world.getSystem(FindChildSystem.class).findChildEntity(c);

                    if(child == null) continue;

                    OrbitComponent oc = child.getComponent(OrbitComponent.class);

                    double angle = Math.toRadians(oc.angleInDegrees);

                    child.edit().add(new VelocityComponent(
                            BulletMath.velocityX(Measure.units(75f), angle),
                            BulletMath.velocityY(Measure.units(75f), angle)));
                    child.edit().add(new BulletComponent());

                    child.edit().remove(OrbitComponent.class);
                    child.edit().remove(ActionAfterTimeComponent.class);

                }
                e.getComponent(ParentComponent.class).children.clear();

                isShield = true;



            }
        }




        public void createBlock(World world, ParentComponent pc, Vector3 centerOfOrbit, float startAngle, Color color){


            float radius = Measure.units(2);



            float x = (float) (centerOfOrbit.x + (radius * Math.cos(Math.toRadians(startAngle))));
            float y = (float) (centerOfOrbit.y + (radius * Math.sin(Math.toRadians(startAngle))));

            Entity e = world.createEntity();
            e.edit().add(new PositionComponent(x,y));
            e.edit().add(new CollisionBoundComponent(new Rectangle(x,y, Measure.units(5), Measure.units(5)), true));
            e.edit().add(new EnemyComponent());
            e.edit().add(new OrbitComponent(centerOfOrbit, radius, 4, startAngle, width / 2, height / 2));
            e.edit().add(new BlinkComponent());
            e.edit().add(new HealthComponent(3));
            e.edit().add(new FadeComponent(true, 1, false));
            //e.edit().add(new VelocityComponent());
            //e.edit().add(new BulletComponent());
            e.edit().add(new OnDeathActionComponent(gibletFactory.defaultGiblets(new Color(color))));

            ChildComponent c = new ChildComponent();
            pc.children.add(c);
            e.edit().add(c);

            TextureRegionComponent trc = new TextureRegionComponent(atlas.findRegion("block"), 0, 0,  Measure.units(5), Measure.units(5), TextureRegionComponent.PLAYER_LAYER_FAR);
            trc.DEFAULT = color;
            trc.color = color;
            e.edit().add(trc);


            e.edit().add(new ActionAfterTimeComponent(new Task() {
                @Override
                public void performAction(World world, Entity e) {
                    e.getComponent(OrbitComponent.class).radius += e.getComponent(OrbitComponent.class).radius >= Measure.units(10f) ? 0 :  Measure.units(0.1f);
                }

                @Override
                public void cleanUpAction(World world, Entity e) {

                }
            }, 0f, true));

        }

        @Override
        public float getBaseFireRate() {
            return 1.5f;
        }

        @Override
        public float getBaseDamage() {
            return 1;
        }
    }




}