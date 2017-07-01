package com.byrjamin.wickedwizard.factories.enemy.bosses;

import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.World;
import com.artemis.utils.Bag;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntMap;
import com.byrjamin.wickedwizard.assets.TextureStrings;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.Weapon;
import com.byrjamin.wickedwizard.ecs.components.WeaponComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.Task;
import com.byrjamin.wickedwizard.ecs.components.ai.ActionAfterTimeComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.ExpireComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.FiringAIComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.PhaseComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.ChildComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.EnemyComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.IntangibleComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.ParentComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.OrbitComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.VelocityComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.AnimationComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.AnimationStateComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.FadeComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.byrjamin.wickedwizard.ecs.systems.FindChildSystem;
import com.byrjamin.wickedwizard.factories.BulletFactory;
import com.byrjamin.wickedwizard.factories.DeathFactory;
import com.byrjamin.wickedwizard.factories.enemy.EnemyFactory;
import com.byrjamin.wickedwizard.utils.BagSearch;
import com.byrjamin.wickedwizard.utils.ComponentBag;
import com.byrjamin.wickedwizard.utils.Measure;

import java.util.Random;

/**
 * Created by Home on 27/05/2017.
 */

public class BossGurner extends EnemyFactory{

    private DeathFactory df;
    private BulletFactory bf;

    private final float width = Measure.units(30f);
    private final float height = Measure.units(30f);


    public BossGurner(AssetManager assetManager) {
        super(assetManager);
        this.df = new DeathFactory(assetManager);
        this.bf = new BulletFactory(assetManager);
    }


    public ComponentBag giantKugelDusche(float x, float y) {
        return giantKugelDusche(x, y, new Random().nextBoolean());
    };

    public ComponentBag giantKugelDusche(float x, float y, boolean isLeft){

        final boolean left = isLeft;

        x = x - width / 2;
        y = y - height / 2;

        ComponentBag bag = this.defaultEnemyBag(new ComponentBag(), x , y, 90);
        bag.add(new CollisionBoundComponent(new Rectangle(x, y, width, height), true));


        bag.add(new AnimationStateComponent(0));
        IntMap<Animation<TextureRegion>> animMap = new IntMap<Animation<TextureRegion>>();
        animMap.put(0, new Animation<TextureRegion>(0.1f / 1f, atlas.findRegions(TextureStrings.KUGELDUSCHE_EMPTY),
                (left) ? Animation.PlayMode.LOOP : Animation.PlayMode.LOOP_REVERSED));


        bag.add(new AnimationComponent(animMap));


        TextureRegionComponent trc = new TextureRegionComponent(atlas.findRegion(TextureStrings.KUGELDUSCHE_EMPTY),
                width, height, TextureRegionComponent.ENEMY_LAYER_MIDDLE);

        trc.color = new Color(Color.BLACK);
        trc.DEFAULT = new Color(Color.BLACK);

        bag.add(trc);
        bag.add(new FiringAIComponent(0));
       // bag.add(new WeaponComponent(new KugelWeapon(left), 0.5f));


        Task p = new Task() {

            @Override
            public void performAction(World world, Entity e) {
                e.edit().add(new WeaponComponent(new KugelWeapon(left), 0.5f));
            }

            @Override
            public void cleanUpAction(World world, Entity e) {
                e.edit().remove(WeaponComponent.class);
            }
        };


        PhaseComponent pc = new PhaseComponent();
        pc.addPhase(10f, p);
        pc.addPhase(15f, new Orbitals(Measure.units(50f), left));
        pc.addPhase(10f, p);
        pc.addPhase(15f, new Orbitals(Measure.units(50f), !left));
        bag.add(pc);
        // bag.add(df.basicOnDeathExplosion(new OnDeathComponent(), width, height, 0,0));



        return bag;


    }


    private class Orbitals implements Task {


        int[] angles = new int[] {-10,80,170, 260};
        float range;
        private boolean isLeft;

        public Orbitals(float range, boolean isLeft){
            this.range = range;
            this.isLeft = isLeft;
        }

        @Override
        public void performAction(World world, Entity e) {

            PositionComponent current = e.getComponent(PositionComponent.class);
            ParentComponent pc = new ParentComponent();

            float bulletSize = Measure.units(7.5f);
            float angleSpeed = isLeft ? 0.5f : -0.5f;

            for(int i = 0; i < 15; i ++) {

                for (int angle : angles) {
                    Entity orbital = world.createEntity();
                    orbital.edit().add(new PositionComponent());
                    //orbital.edit().add(new EnemyComponent());
                    orbital.edit().add(new CollisionBoundComponent(new Rectangle(0, 0, bulletSize,bulletSize), true));
                    orbital.edit().add(new OrbitComponent(
                            new Vector3(current.getX(), current.getY(), 0f), i * bulletSize, angleSpeed, angle, width / 2, height / 2
                    ));

                    orbital.edit().add(new TextureRegionComponent(atlas.findRegion("block"),
                            bulletSize,bulletSize, TextureRegionComponent.ENEMY_LAYER_FAR, new Color(Color.RED)));

                    orbital.edit().add(new FadeComponent(true, 1.25f, false));

                    orbital.edit().add(new IntangibleComponent());

                    ChildComponent c = new ChildComponent();
                    pc.children.add(c);
                    orbital.edit().add(c);


                    orbital.edit().add(new ActionAfterTimeComponent(new Task() {
                        @Override
                        public void performAction(World world, Entity e) {
                            e.edit().add(new EnemyComponent());
                        }

                        @Override
                        public void cleanUpAction(World world, Entity e) {

                        }
                    }, 1.0f));


                }

                e.edit().add(pc);
            }


        }

        @Override
        public void cleanUpAction(World world, Entity e) {


            Array<ChildComponent> childComponents  = new Array<ChildComponent>();
            childComponents.addAll(e.getComponent(ParentComponent.class).children);

            for(ChildComponent c : childComponents) {

                Entity child = world.getSystem(FindChildSystem.class).findChildEntity(c);


                if(child != null){
                    child.edit().remove(EnemyComponent.class);
                    child.edit().add(new FadeComponent(false, 0.5f, false));
                    child.edit().add(new ExpireComponent(0.6f));
                    //world.getSystem(OnDeathSystem.class).kill(child);
                }
            };

            e.edit().remove(ParentComponent.class);
        }
    }




    private class KugelWeapon implements Weapon{


        int[] angles = new int[] {0,45,90,135,180,225,270,315};
        private boolean left;

        KugelWeapon(boolean left){
            this.left = left;
        }

        @Override
        public void fire(World world, Entity e, float x, float y, double angleInRadians) {
            for(int i : angles){
                double angleOfTravel = angleInRadians + Math.toRadians(i);
                Bag<Component> bag = bf.basicEnemyBulletBag(x, y, 4f);
                bag.add(new VelocityComponent((float) (Measure.units(37) * Math.cos(angleOfTravel)), (float) (Measure.units(34) * Math.sin(angleOfTravel))));
                BagSearch.getObjectOfTypeClass(TextureRegionComponent.class, bag).layer = TextureRegionComponent.ENEMY_LAYER_FAR;

                Entity bullet = world.createEntity();
                for(Component c : bag){
                    bullet.edit().add(c);
                }
            }

            e.getComponent(FiringAIComponent.class).firingAngleInRadians += Math.toRadians((left) ? 25 : -25);

        }

        @Override
        public float getBaseFireRate() {
            return 0.5f;
        }

        @Override
        public float getBaseDamage() {
            return 0;
        }


        public void setLeft(boolean left){
            this.left = left;
        }
    }









}
