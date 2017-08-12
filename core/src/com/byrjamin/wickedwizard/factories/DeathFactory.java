package com.byrjamin.wickedwizard.factories;

import com.badlogic.gdx.assets.AssetManager;

/**
 * Created by Home on 01/04/2017.
 */

public class DeathFactory extends AbstractFactory {

    private final static int aniNumber = 1;

    public DeathFactory(AssetManager assetManager) {
        super(assetManager);
    }



  /*  public OnDeathComponent giblets(OnDeathComponent fillodc, int numberOfGiblets, Color color){

        ComponentBag bag;

        Random random = new Random();

        for(int i = 0; i < numberOfGiblets; i++) {

            int vx = random.nextInt((int) Measure.units(100f)) + 50;
            int vy = random.nextInt((int) Measure.units(100f)) + 50;
            vx = random.nextBoolean() ? vx : -vx;
            vy = random.nextBoolean() ? vy : -vy;

            bag = new ComponentBag();
            bag.add(new PositionComponent());
            bag.add(new VelocityComponent(vx, vy));
            bag.add(new BounceComponent());
            bag.add(new CollisionBoundComponent(new Rectangle(0, 0, Measure.units(1f), Measure.units(1f))));
            bag.add(new ExpireComponent(0.4f));

            FadeComponent fc = new FadeComponent();
            fc.fadeIn = false;
            fc.alphaTimeLimit = 0.4f;
            fc.alphaTimer = 0.4f;
            fc.isEndless = false;

            bag.add(fc);

            TextureRegionComponent trc = new TextureRegionComponent(atlas.findRegion("block"), Measure.units(1f), Measure.units(1f),
                    TextureRegionComponent.ENEMY_LAYER_MIDDLE);
            trc.DEFAULT = color;
            trc.color = color;

            bag.add(trc);

            fillodc.getComponentBags().add(bag);

        }

        return fillodc;



    }*/

}
