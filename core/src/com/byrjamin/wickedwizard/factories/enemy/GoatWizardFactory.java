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
import com.byrjamin.wickedwizard.ecs.components.ai.ExpiryRangeComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.FiringAIComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.OnDeathActionComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.ProximityTriggerAIComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.Task;
import com.byrjamin.wickedwizard.ecs.components.identifiers.BulletComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.ChildComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.EnemyComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.IntangibleComponent;
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
import com.byrjamin.wickedwizard.factories.weapons.Giblets;
import com.byrjamin.wickedwizard.utils.ComponentBag;
import com.byrjamin.wickedwizard.utils.Measure;
import com.byrjamin.wickedwizard.utils.collider.HitBox;

/**
 * Created by Home on 17/05/2017.
 */

public class GoatWizardFactory extends EnemyFactory {


    private static final float height = Measure.units(20f);
    private static final float width = Measure.units(20f);

    private static final float texheight = Measure.units(15f);
    private static final float texwidth = Measure.units(15f);

    private static final float fireRate = 3f;

    private final Giblets.GibletBuilder gibletBuilder;

    public GoatWizardFactory(AssetManager assetManager) {
        super(assetManager);
        this.gibletBuilder = new Giblets.GibletBuilder(assetManager);
    }

    public ComponentBag goatWizard(float x, float y, boolean startsRight, boolean startsUp){

        x = x - width / 2;
        y = y - height / 2;


        ComponentBag bag = new ComponentBag();
        bag = defaultEnemyBag(bag, x, y, 15);

        bag.add(new VelocityComponent(startsRight ? Measure.units(10f) : -Measure.units(10f), startsUp ? Measure.units(5f) : -Measure.units(5f)));
        bag.add(new BounceComponent());


        CollisionBoundComponent cbc = new CollisionBoundComponent();

        cbc.bound = new Rectangle(x , y , width, height);
        cbc.hitBoxes.add(new HitBox(new Rectangle(x, y, Measure.units(7), Measure.units(7)), Measure.units(6.5f), Measure.units(7.5f)));

        bag.add(cbc);

        bag.add(new AnimationStateComponent(0));
        IntMap<Animation<TextureRegion>> animMap = new IntMap<Animation<TextureRegion>>();
        animMap.put(0, new Animation<TextureRegion>(0.1f / 1f,
                atlas.findRegions(TextureStrings.GOATWIZARD), Animation.PlayMode.LOOP_RANDOM));
        bag.add(new AnimationComponent(animMap));

        TextureRegionComponent trc = new TextureRegionComponent(atlas.findRegion(TextureStrings.GOATWIZARD),
                (width / 2) - (texwidth / 2), (height / 2) - (texheight / 2), texwidth, texheight, TextureRegionComponent.ENEMY_LAYER_MIDDLE);

        trc.color = new Color(91f / 255f,50f / 255f,86f / 255f, 1);
        trc.DEFAULT = new Color(91f / 255f,50f / 255f,86f / 255f, 1);

        bag.add(trc);


        bag.add(new WeaponComponent(new GoatWeapon(Measure.units(10f)), 0));
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



    public ComponentBag goatSorcerer(float x, float y){

        float boundx = x;
        float boundy = y;

        x = x - width / 2;
        y = y - height / 2;


        ComponentBag bag = new ComponentBag();
        bag = defaultEnemyBag(bag, x, y, 15);

        bag.add(new VelocityComponent(Measure.units(10f), Measure.units(5f)));
        bag.add(new BounceComponent());


        CollisionBoundComponent cbc = new CollisionBoundComponent();

        cbc.bound = new Rectangle(x , y , width, height);
        cbc.hitBoxes.add(new HitBox(new Rectangle(x, y, Measure.units(7), Measure.units(7)), Measure.units(6.5f), Measure.units(7.5f)));

        bag.add(cbc);

        bag.add(new AnimationStateComponent(0));
        IntMap<Animation<TextureRegion>> animMap = new IntMap<Animation<TextureRegion>>();
        animMap.put(0, new Animation<TextureRegion>(0.1f / 1f,
                atlas.findRegions(TextureStrings.GOATWIZARD), Animation.PlayMode.LOOP_RANDOM));
        bag.add(new AnimationComponent(animMap));

        TextureRegionComponent trc = new TextureRegionComponent(atlas.findRegion(TextureStrings.GOATWIZARD),
                (width / 2) - (texwidth / 2), (height / 2) - (texheight / 2), texwidth, texheight, TextureRegionComponent.ENEMY_LAYER_MIDDLE);

        trc.color = new Color(91f / 255f,50f / 255f,86f / 255f, 1);
        trc.DEFAULT = new Color(91f / 255f,50f / 255f,86f / 255f, 1);

        bag.add(trc);


        bag.add(new WeaponComponent(new Weapon() {

            GoatWeapon first = new GoatWeapon(Measure.units(7.5f));
            GoatWeapon second = new GoatWeapon(Measure.units(12.5f));

            @Override
            public void fire(World world, Entity e, float x, float y, double angleInRadians) {
                first.fire(world, e, x, y, angleInRadians);
                second.fire(world, e, x,y,angleInRadians);
            }

            @Override
            public float getBaseFireRate() {
                return 2;
            }

            @Override
            public float getBaseDamage() {
                return 0;
            }
        }, 0));


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







    private class GoatWeapon implements Weapon {



        float [] angles = new float[] {0,45,90,135,180,225,270,315};

        private boolean isShield = true;
        private float radius;

        public GoatWeapon(float radius){
            this.radius = radius;
        }

        //TODO somehow track if any balls have been killed and re-summon after a count down period

        @Override
        public void fire(World world, Entity e, float x, float y, double angleInRadians) {

            if(isShield) {
                int count = 0;
                for (float f : angles) {
                    if(count % 2 == 0) {
                        createHealthyBlock(world, e.getComponent(ParentComponent.class), e.getComponent(PositionComponent.class).position, radius, f, new Color(Color.BLACK));
                    } else {
                        createBlock(world, e.getComponent(ParentComponent.class), e.getComponent(PositionComponent.class).position, radius, f, new Color(Color.RED));
                    }
                    count++;
                }
                isShield = false;
            } else {

                VelocityComponent vc = new VelocityComponent((float) (Measure.units(40) * Math.cos(angleInRadians)), (float) (Measure.units(40) * Math.sin(angleInRadians)));

                PositionComponent pc = e.getComponent(PositionComponent.class);

                Entity newOrbitCenter = world.createEntity();
                newOrbitCenter.edit().add(vc);
                newOrbitCenter.edit().add(new PositionComponent(pc.getX(), pc.getY()));


                for(ChildComponent c : e.getComponent(ParentComponent.class).children){
                    Entity child = world.getSystem(FindChildSystem.class).findChildEntity(c);

                    if(child == null) continue;

                    child.edit().add(new VelocityComponent(vc));
                    child.edit().add(new BulletComponent());
                    //child.edit().add(new IntangibleComponent());
                    //child.edit().add(new ExpiryRangeComponent(child.getComponent(PositionComponent.class).position, Measure.units(200f)));

                    child.getComponent(OrbitComponent.class).centerOfOrbit = newOrbitCenter.getComponent(PositionComponent.class).position;

                }
                e.getComponent(ParentComponent.class).children.clear();
                isShield = true;
            }
        }




        public void createHealthyBlock(World world, ParentComponent pc, Vector3 centerOfOrbit, float radius, float startAngle, Color color) {

            float x = (float) (centerOfOrbit.x + (radius * Math.cos(Math.toRadians(startAngle))));
            float y = (float) (centerOfOrbit.y + (radius * Math.sin(Math.toRadians(startAngle))));

            Entity e = world.createEntity();
            e.edit().add(new PositionComponent(x, y));
            e.edit().add(new CollisionBoundComponent(new Rectangle(x, y, Measure.units(5), Measure.units(5)), true));
            e.edit().add(new EnemyComponent());
            e.edit().add(new OrbitComponent(centerOfOrbit, radius, 2, startAngle, width / 2, height / 2));
            e.edit().add(new BlinkComponent());
            e.edit().add(new HealthComponent(3));
            e.edit().add(new FadeComponent(true, 1, false));
            e.edit().add(new OnDeathActionComponent(
                    new Giblets.GibletBuilder(assetManager)
                            .numberOfGibletPairs(3)
                            .size(Measure.units(0.5f))
                            .minSpeed(Measure.units(10f))
                            .maxSpeed(Measure.units(20f))
                            .colors(color)
                            .intangible(false)
                            .expiryTime(0.2f).build()));

            ChildComponent c = new ChildComponent();
            pc.children.add(c);
            e.edit().add(c);

            TextureRegionComponent trc = new TextureRegionComponent(atlas.findRegion(TextureStrings.BLOCK), 0, 0, Measure.units(5), Measure.units(5), TextureRegionComponent.PLAYER_LAYER_FAR);
            trc.DEFAULT = color;
            trc.color = color;
            e.edit().add(trc);

        }


        public void createBlock(World world, ParentComponent pc, Vector3 centerOfOrbit, float radius, float startAngle, Color color) {

            float x = (float) (centerOfOrbit.x + (radius * Math.cos(Math.toRadians(startAngle))));
            float y = (float) (centerOfOrbit.y + (radius * Math.sin(Math.toRadians(startAngle))));

            Entity e = world.createEntity();
            e.edit().add(new PositionComponent(x, y));
            e.edit().add(new CollisionBoundComponent(new Rectangle(x, y, Measure.units(5), Measure.units(5)), true));
            e.edit().add(new EnemyComponent());
            e.edit().add(new OrbitComponent(centerOfOrbit, radius, 6, startAngle, width / 2, height / 2));
            e.edit().add(new FadeComponent(true, 1, false));
            e.edit().add(new OnDeathActionComponent(new Giblets.GibletBuilder(assetManager)
                    .numberOfGibletPairs(3)
                    .size(Measure.units(0.5f))
                    .minSpeed(Measure.units(10f))
                    .maxSpeed(Measure.units(20f))
                    .colors(color)
                    .intangible(false)
                    .expiryTime(0.2f).build()));

            ChildComponent c = new ChildComponent();
            pc.children.add(c);
            e.edit().add(c);

            TextureRegionComponent trc = new TextureRegionComponent(atlas.findRegion(TextureStrings.BLOCK), 0, 0, Measure.units(5), Measure.units(5), TextureRegionComponent.PLAYER_LAYER_FAR);
            trc.DEFAULT = color;
            trc.color = color;
            e.edit().add(trc);

        }

        @Override
        public float getBaseFireRate() {
            return fireRate;
        }

        @Override
        public float getBaseDamage() {
            return 1;
        }
    }












}
