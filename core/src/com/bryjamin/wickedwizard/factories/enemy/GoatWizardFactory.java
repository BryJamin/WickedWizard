package com.bryjamin.wickedwizard.factories.enemy;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.IntMap;
import com.bryjamin.wickedwizard.assets.SoundFileStrings;
import com.bryjamin.wickedwizard.ecs.components.Weapon;
import com.bryjamin.wickedwizard.ecs.components.ai.OnDeathActionComponent;
import com.bryjamin.wickedwizard.ecs.components.ai.ProximityTriggerAIComponent;
import com.bryjamin.wickedwizard.ecs.components.ai.Task;
import com.bryjamin.wickedwizard.ecs.components.identifiers.BulletComponent;
import com.bryjamin.wickedwizard.ecs.components.identifiers.EnemyComponent;
import com.bryjamin.wickedwizard.ecs.components.identifiers.ParentComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.BounceComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.CollisionBoundComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.OrbitComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.AnimationComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.AnimationStateComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.BlinkOnHitComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.FadeComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.bryjamin.wickedwizard.ecs.systems.FindChildSystem;
import com.bryjamin.wickedwizard.ecs.systems.audio.SoundSystem;
import com.bryjamin.wickedwizard.factories.weapons.Giblets;
import com.bryjamin.wickedwizard.utils.ComponentBag;
import com.bryjamin.wickedwizard.utils.Measure;

/**
 * Created by Home on 17/05/2017.
 */

public class GoatWizardFactory extends EnemyFactory {


    private static final float height = Measure.units(20f);
    private static final float width = Measure.units(20f);

    private static final float texheight = Measure.units(15f);
    private static final float texwidth = Measure.units(15f);

    private static final float fireRate = 3f;


    private static final int GOAT_WIZARD_NORMAL_STATE = 0;
    private static final int GOAT_WIZARD_FIRING_STATE = 2;


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

        bag.add(new com.bryjamin.wickedwizard.ecs.components.movement.VelocityComponent(startsRight ? Measure.units(10f) : -Measure.units(10f), startsUp ? Measure.units(5f) : -Measure.units(5f)));
        bag.add(new BounceComponent());


        CollisionBoundComponent cbc = new CollisionBoundComponent();

        cbc.bound = new Rectangle(x , y , width, height);
        cbc.hitBoxes.add(new com.bryjamin.wickedwizard.utils.collider.HitBox(new Rectangle(x, y, Measure.units(7), Measure.units(7)), Measure.units(6.5f), Measure.units(7.5f)));

        bag.add(cbc);

        bag.add(new AnimationStateComponent(GOAT_WIZARD_NORMAL_STATE));
        IntMap<Animation<TextureRegion>> animMap = new IntMap<Animation<TextureRegion>>();
        animMap.put(GOAT_WIZARD_NORMAL_STATE, new Animation<TextureRegion>(0.15f / 1f,
                atlas.findRegions(com.bryjamin.wickedwizard.assets.TextureStrings.GOAT_WIZARD), Animation.PlayMode.LOOP));
        animMap.put(AnimationStateComponent.FIRING, new Animation<TextureRegion>(0.15f / 1f,
                atlas.findRegions(com.bryjamin.wickedwizard.assets.TextureStrings.GOAT_WIZARD_FIRING)));
        bag.add(new AnimationComponent(animMap));

        TextureRegionComponent trc = new TextureRegionComponent(atlas.findRegion(com.bryjamin.wickedwizard.assets.TextureStrings.GOAT_WIZARD),
                (width / 2) - (texwidth / 2), (height / 2) - (texheight / 2), texwidth, texheight, TextureRegionComponent.ENEMY_LAYER_MIDDLE);

        bag.add(trc);


        bag.add(new com.bryjamin.wickedwizard.ecs.components.WeaponComponent(new GoatWeapon(Measure.units(10f)), 0));
        bag.add(new ProximityTriggerAIComponent(new Task() {
            @Override
            public void performAction(World world, Entity e) {
                e.edit().add(new com.bryjamin.wickedwizard.ecs.components.ai.FiringAIComponent());
            }

            @Override
            public void cleanUpAction(World world, Entity e) {
                e.edit().remove(com.bryjamin.wickedwizard.ecs.components.ai.FiringAIComponent.class);
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


            world.getSystem(SoundSystem.class).playRandomSound(SoundFileStrings.enemyFireMegaMix);

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

                com.bryjamin.wickedwizard.ecs.components.movement.VelocityComponent vc = new com.bryjamin.wickedwizard.ecs.components.movement.VelocityComponent((float) (Measure.units(40) * Math.cos(angleInRadians)), (float) (Measure.units(40) * Math.sin(angleInRadians)));

                PositionComponent pc = e.getComponent(PositionComponent.class);

                Entity newOrbitCenter = world.createEntity();
                newOrbitCenter.edit().add(vc);
                newOrbitCenter.edit().add(new PositionComponent(pc.getX(), pc.getY()));


                for(com.bryjamin.wickedwizard.ecs.components.identifiers.ChildComponent c : e.getComponent(ParentComponent.class).children){
                    Entity child = world.getSystem(FindChildSystem.class).findChildEntity(c);

                    if(child == null) continue;

                    child.edit().add(new com.bryjamin.wickedwizard.ecs.components.movement.VelocityComponent(vc));
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
            e.edit().add(new BlinkOnHitComponent());
            e.edit().add(new com.bryjamin.wickedwizard.ecs.components.HealthComponent(3));
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

            com.bryjamin.wickedwizard.ecs.components.identifiers.ChildComponent c = new com.bryjamin.wickedwizard.ecs.components.identifiers.ChildComponent();
            pc.children.add(c);
            e.edit().add(c);

            TextureRegionComponent trc = new TextureRegionComponent(atlas.findRegion(com.bryjamin.wickedwizard.assets.TextureStrings.BLOCK), 0, 0, Measure.units(5), Measure.units(5), TextureRegionComponent.PLAYER_LAYER_FAR);
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

            com.bryjamin.wickedwizard.ecs.components.identifiers.ChildComponent c = new com.bryjamin.wickedwizard.ecs.components.identifiers.ChildComponent();
            pc.children.add(c);
            e.edit().add(c);

            TextureRegionComponent trc = new TextureRegionComponent(atlas.findRegion(com.bryjamin.wickedwizard.assets.TextureStrings.BLOCK), 0, 0, Measure.units(5), Measure.units(5), TextureRegionComponent.PLAYER_LAYER_FAR);
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
