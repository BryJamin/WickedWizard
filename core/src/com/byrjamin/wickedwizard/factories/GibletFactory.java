package com.byrjamin.wickedwizard.factories;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.OnDeathComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.ExpireComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.IntangibleComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.BounceComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.VelocityComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.FadeComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.byrjamin.wickedwizard.utils.ComponentBag;
import com.byrjamin.wickedwizard.utils.Measure;

import java.util.Random;

/**
 * Created by Home on 14/05/2017.
 */

public class GibletFactory extends AbstractFactory {

    private final static int aniNumber = 1;
    private final Random random = new Random();

    public GibletFactory(AssetManager assetManager) {
        super(assetManager);
    }


    public OnDeathComponent defaultGiblets(OnDeathComponent fillodc, Color color){
        return  giblets(fillodc, 5, 0.2f, (int) Measure.units(10f), (int) Measure.units(20f),Measure.units(0.5f), color);
    }

    public OnDeathComponent giblets(OnDeathComponent fillodc, int numberOfGiblets,float life,float minSpeed,float maxSpeed, float size, Color color){

        ComponentBag bag;

        for(int i = 0; i < numberOfGiblets; i++) {

            float vx = random.nextFloat() * (maxSpeed - minSpeed) + minSpeed;
            float vy = random.nextFloat() * (maxSpeed - minSpeed) + minSpeed;
            vx = random.nextBoolean() ? vx : -vx;
            vy = random.nextBoolean() ? vy : -vy;

            bag = new ComponentBag();
            bag.add(new PositionComponent());
            bag.add(new VelocityComponent(vx, vy));
            bag.add(new BounceComponent());
            bag.add(new CollisionBoundComponent(new Rectangle(0, 0, size, size)));
            bag.add(new ExpireComponent(life));

            //FadeComponent fc = new FadeComponent(false, life, false);
            //bag.add(fc);

            TextureRegionComponent trc = new TextureRegionComponent(atlas.findRegion("block"), size, size,
                    TextureRegionComponent.PLAYER_LAYER_NEAR);
            trc.DEFAULT = color;
            trc.color = color;

            bag.add(trc);

            fillodc.getComponentBags().add(bag);

        }

        return fillodc;



    }


    public OnDeathComponent bombGiblets(OnDeathComponent fillodc, int numberOfGiblets,float life,float minSpeed,float maxSpeed, float size, Color color){

        ComponentBag bag;

        Random random = new Random();

        for(int i = 0; i < numberOfGiblets; i++) {

            float vx = random.nextInt((int) maxSpeed - (int) minSpeed) + minSpeed;
            float vy = random.nextInt((int) maxSpeed - (int) minSpeed) + minSpeed;

            float fadeLife = random.nextFloat() * (life * 2 - life / 2) + life / 2;

            vx = random.nextBoolean() ? vx : -vx;
            vy = random.nextBoolean() ? vy : -vy;

            bag = new ComponentBag();
            bag.add(new PositionComponent());
            bag.add(new VelocityComponent(vx, vy));
           // bag.add(new BounceComponent());
            bag.add(new CollisionBoundComponent(new Rectangle(0, 0, size, size)));
            bag.add(new ExpireComponent(life));
            bag.add(new IntangibleComponent());
            // bag.add(new ExpiryRangeComponent(new Vector3()maxSpeed))


            if(random.nextBoolean() && random.nextBoolean()) {

            } else {
                FadeComponent fc = new FadeComponent(false, fadeLife, false);
                bag.add(fc);
            }

            TextureRegionComponent trc = new TextureRegionComponent(atlas.findRegion("block"), size, size,
                    TextureRegionComponent.ENEMY_LAYER_MIDDLE);
            trc.DEFAULT = color;
            trc.color = color;

            bag.add(trc);

            fillodc.getComponentBags().add(bag);

        }

        return fillodc;



    }



}
