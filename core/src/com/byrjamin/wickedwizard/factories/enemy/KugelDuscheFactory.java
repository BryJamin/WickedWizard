package com.byrjamin.wickedwizard.factories.enemy;

import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.World;
import com.artemis.utils.Bag;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.IntMap;
import com.byrjamin.wickedwizard.assets.TextureStrings;
import com.byrjamin.wickedwizard.ecs.components.BlinkComponent;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.EnemyComponent;
import com.byrjamin.wickedwizard.ecs.components.HealthComponent;
import com.byrjamin.wickedwizard.ecs.components.OnDeathComponent;
import com.byrjamin.wickedwizard.ecs.components.Weapon;
import com.byrjamin.wickedwizard.ecs.components.WeaponComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.FiringAIComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.Action;
import com.byrjamin.wickedwizard.ecs.components.ai.PhaseComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.LootComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.VelocityComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.AnimationComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.AnimationStateComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.byrjamin.wickedwizard.factories.AbstractFactory;
import com.byrjamin.wickedwizard.factories.BulletFactory;
import com.byrjamin.wickedwizard.factories.DeathFactory;
import com.byrjamin.wickedwizard.factories.items.ItemFactory;
import com.byrjamin.wickedwizard.factories.items.pickups.MoneyPlus1;
import com.byrjamin.wickedwizard.screens.PlayScreen;
import com.byrjamin.wickedwizard.utils.BagSearch;
import com.byrjamin.wickedwizard.utils.ComponentBag;
import com.byrjamin.wickedwizard.utils.Measure;

import java.util.Random;

/**
 * Created by Home on 13/04/2017.
 */

public class KugelDuscheFactory extends EnemyFactory {

    private final float width = Measure.units(9);
    private final float height = Measure.units(9f);

    private final float health = 11f;

    private final float textureWidth = Measure.units(12);
    private final float textureHeight = Measure.units(12);

    private final float textureOffsetX = -Measure.units(1.5f);
    private final float textureOffsetY = 0;

    private final float phaseTime = 0.15f;

    private DeathFactory df;
    private BulletFactory bf;

    public KugelDuscheFactory(AssetManager assetManager) {
        super(assetManager);
        this.df = new DeathFactory(assetManager);
        this.bf = new BulletFactory(assetManager);
    }


    public ComponentBag kugelDusche(float x, float y) {
        return kugelDusche(x, y, new Random().nextBoolean());
    };

    public ComponentBag kugelDusche(float x, float y, boolean isLeft){


        final boolean left = isLeft;

        x = x - width / 2;
        y = y - height / 2;

        ComponentBag bag = this.defaultEnemyBag(new ComponentBag(), x , y, width, height, 25);
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
        bag.add(new WeaponComponent(kugelWeapon(), 0.1f));


        Action p = new Action() {

            @Override
            public void performAction(World world, Entity e) {
                e.getComponent(FiringAIComponent.class).firingAngleInRadians += Math.toRadians((left) ? 7.5 : -7.5);
            }

            @Override
            public void cleanUpAction(World world, Entity e) {

            }
        };

        PhaseComponent pc = new PhaseComponent();
        pc.addPhase(phaseTime, p);
        pc.addPhaseSequence(0,0);

        bag.add(pc);
       // bag.add(df.basicOnDeathExplosion(new OnDeathComponent(), width, height, 0,0));



        return bag;


    }


    public Weapon kugelWeapon(){

        return new Weapon() {

            int[] angles = new int[] {0,180};

            @Override
            public void fire(World world,Entity e, float x, float y, double angle) {
                //Math.toRadians()
                for(int i : angles){
                    double angleOfTravel = angle + Math.toRadians(i);
                    Bag<Component> bag = bf.basicEnemyBulletBag(x, y, 1.7f);
                    bag.add(new VelocityComponent((float) (Measure.units(37) * Math.cos(angleOfTravel)), (float) (Measure.units(34) * Math.sin(angleOfTravel))));
                    BagSearch.getObjectOfTypeClass(TextureRegionComponent.class, bag).layer = TextureRegionComponent.ENEMY_LAYER_FAR;

                    Entity bullet = world.createEntity();
                    for(Component c : bag){
                        bullet.edit().add(c);
                    }
                }
            }

            @Override
            public float getBaseFireRate() {
                return 0.2f;
            }

            @Override
            public float getBaseDamage() {
                return 0;
            }
        };

    }


}
