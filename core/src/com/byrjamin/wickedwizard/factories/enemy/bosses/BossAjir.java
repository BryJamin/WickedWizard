package com.byrjamin.wickedwizard.factories.enemy.bosses;

import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.byrjamin.wickedwizard.assets.TextureStrings;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.Weapon;
import com.byrjamin.wickedwizard.ecs.components.ai.Action;
import com.byrjamin.wickedwizard.ecs.components.ai.ActionAfterTimeComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.OnDeathActionComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.PhaseComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.Task;
import com.byrjamin.wickedwizard.ecs.components.identifiers.EnemyComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.HazardComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.VelocityComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.FadeComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.byrjamin.wickedwizard.factories.BulletFactory;
import com.byrjamin.wickedwizard.factories.GibletFactory;
import com.byrjamin.wickedwizard.factories.enemy.EnemyFactory;
import com.byrjamin.wickedwizard.factories.weapons.enemy.MultiPistol;
import com.byrjamin.wickedwizard.utils.BulletMath;
import com.byrjamin.wickedwizard.utils.ComponentBag;
import com.byrjamin.wickedwizard.utils.Measure;
import com.byrjamin.wickedwizard.utils.collider.Collider;

import java.util.Random;

/**
 * Created by Home on 07/07/2017.
 */

public class BossAjir extends EnemyFactory{


    private static final float width = Measure.units(15f);
    private static final float height = Measure.units(25f);

    private static final float health = 80;

    private static final float timeBetweenSplitAction = 2.5f;

    private static final float shotSpeed = Measure.units(50f);

    private Random random;

    public BossAjir(AssetManager assetManager) {
        super(assetManager);
        this.random = new Random();
    }

    public ComponentBag ajir(float x, float y){

        x = x - width / 2;
        y = y - width / 2;

        ComponentBag bag = this.defaultEnemyBag(new ComponentBag(), x, y, health);

        bag.add(new CollisionBoundComponent(new Rectangle(x, y, width, height), true));

        bag.add(new TextureRegionComponent(atlas.findRegion(TextureStrings.BLOCK),
                width, height, TextureRegionComponent.ENEMY_LAYER_MIDDLE));


        PhaseComponent phaseComponent = new PhaseComponent();
        phaseComponent.addPhase(1f, new AjirSplitterWeaponPhase(random));
        phaseComponent.addPhase(50f, new AjirDeathFromAbovePhase(random));

        bag.add(phaseComponent);

        return bag;


    }


    private class AjirSplitterWeaponPhase implements Task {


        private Random random;
        private AjirSplitterWeapon ajirSplitterWeapon;

        //TODO change these to different angles once the art is finalized (Directly below is not fair)
        private int[] possibleAngles = new int[]{0, 45, 90, 135, 180, 215,325};

        public AjirSplitterWeaponPhase(Random random){
            this.random = random;
            this.ajirSplitterWeapon = new AjirSplitterWeapon(assetManager);
        }


        @Override
        public void performAction(World world, Entity e) {

            e.edit().add(new ActionAfterTimeComponent(new Action() {
                @Override
                public void performAction(World world, Entity e) {

                    CollisionBoundComponent cbc = e.getComponent(CollisionBoundComponent.class);
                    ajirSplitterWeapon.fire(world, e, cbc.getCenterX(), cbc.getCenterY(), Math.toRadians(possibleAngles[random.nextInt(possibleAngles.length)]));
                }
            }, timeBetweenSplitAction, true));
        }

        @Override
        public void cleanUpAction(World world, Entity e) {
            e.edit().remove(ActionAfterTimeComponent.class);
        }
    }




    private class AjirDeathFromAbovePhase implements Task {

        private Random random;

        public AjirDeathFromAbovePhase(Random random){
            this.random = random;
        }

        @Override
        public void performAction(World world, Entity e) {




            Entity beam = world.createEntity();

            float width = Measure.units(5f);
            float height = Measure.units(50f);

            beam.edit().add(new PositionComponent(Measure.units(10f), Measure.units(10f)));
            beam.edit().add(new CollisionBoundComponent(new Rectangle(Measure.units(10f), Measure.units(10f), Measure.units(5f), Measure.units(50f)), true));
            beam.edit().add(new HazardComponent());
            beam.edit().add(new FadeComponent(true, 5.0f, false, 0, 1f));
            beam.edit().add(new TextureRegionComponent(atlas.findRegion(TextureStrings.BLOCK), width, height, TextureRegionComponent.ENEMY_LAYER_MIDDLE,
                    new Color(Color.RED.r, Color.RED.g, Color.RED.b, 0)));





        }

        @Override
        public void cleanUpAction(World world, Entity e) {

        }
    }


    private class AjirSplitterWeapon implements Weapon {

        private BulletFactory bulletFactory;
        private GibletFactory gibletFactory;

        private Weapon weapoonFiredOnBulletDeath;

        public AjirSplitterWeapon(AssetManager assetManager){
            this.bulletFactory = new BulletFactory(assetManager);
            this.gibletFactory = new GibletFactory(assetManager);
            this.weapoonFiredOnBulletDeath = new MultiPistol.PistolBuilder(assetManager)
                    .angles(0,30,60,90,120,150,180,210,240,270,300,330)
                    .shotScale(2)
                    .expire(true)
                    //.expireRange(Measure.units(50f))
                    .build();
        }

        @Override
        public void fire(World world, Entity e, float x, float y, double angleInRadians) {

            Entity bullet = world.createEntity();

            for(Component c : bulletFactory.basicBulletBag(x, y, 10, atlas.findRegion(TextureStrings.BLOCK), new Color(Color.RED))) {
                bullet.edit().add(c);
            };

            bullet.edit().add(new EnemyComponent());
            bullet.edit().add(new VelocityComponent(BulletMath.velocityX(shotSpeed, angleInRadians),
                    BulletMath.velocityY(shotSpeed, angleInRadians)));


            bullet.edit().add(new OnDeathActionComponent(new Action() {
                @Override
                public void performAction(World world, Entity e) {

                    CollisionBoundComponent cbc = e.getComponent(CollisionBoundComponent.class);
                    gibletFactory.defaultGiblets(Color.RED).performAction(world, e);
                    weapoonFiredOnBulletDeath.fire(world, e , cbc.getCenterX(), cbc.getCenterY(), 0);
                }
            }));

        }

        @Override
        public float getBaseFireRate() {
            return 0;
        }

        @Override
        public float getBaseDamage() {
            return 0;
        }
    }






}
