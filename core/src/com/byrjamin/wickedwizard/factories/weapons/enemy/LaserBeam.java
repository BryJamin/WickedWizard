package com.byrjamin.wickedwizard.factories.weapons.enemy;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.byrjamin.wickedwizard.assets.FileLocationStrings;
import com.byrjamin.wickedwizard.assets.SoundFileStrings;
import com.byrjamin.wickedwizard.assets.TextureStrings;
import com.byrjamin.wickedwizard.ecs.components.movement.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.Action;
import com.byrjamin.wickedwizard.ecs.components.ai.ActionAfterTimeComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.ExpireComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.OnDeathActionComponent;
import com.byrjamin.wickedwizard.ecs.components.audio.SoundEmitterComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.HazardComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.FadeComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.byrjamin.wickedwizard.utils.Measure;

/**
 * Created by Home on 08/07/2017.
 */

public class LaserBeam {

    private final AssetManager assetManager;
    private final TextureAtlas atlas;

    private final float chargingLaserWidth;
    private final float chargingLaserHeight;

    private final float activeLaserWidth;
    private final float activeLaserHeight;


    private final float activeLaserTime;
    private final float chargingLaserTime;

    private final float activeLaserDisperseTime;

    private final boolean useWidthAsCenter;

    private final int layer;

    private final Color color;

    public static class LaserBeamBuilder{

        //Required Parameters
        private final AssetManager assetManager;

        //Optional Parameters
        private float chargingLaserWidth =  Measure.units(5f);
        private float chargingLaserHeight = Measure.units(50f);

        private float activeLaserWidth = Measure.units(7.5f);
        private float activeLaserHeight = Measure.units(50f);

        private float activeLaserTime = 0.4f;
        private float chargingLaserTime = 0.8f;
        private float activeLaserDisperseTime = 0.3f;

        //Determines when creating the larger laser to center the new laser by the width or by the height
        private boolean centerLaserUsingWidth = true;

        private int layer = TextureRegionComponent.PLAYER_LAYER_MIDDLE;

        private Color color = new Color(Color.RED);

        public LaserBeamBuilder(AssetManager assetManager) {
            this.assetManager = assetManager;
        }

        public LaserBeamBuilder chargingLaserWidth(float val)
        { chargingLaserWidth = val; return this; }

        public LaserBeamBuilder chargingLaserHeight(float val)
        { chargingLaserHeight = val; return this; }

        public LaserBeamBuilder activeLaserWidth(float val)
        { activeLaserWidth = val; return this; }

        public LaserBeamBuilder activeLaserHeight(float val)
        { activeLaserHeight = val; return this; }

        public LaserBeamBuilder activeLaserTime(float val)
        { activeLaserTime = val; return this; }

        public LaserBeamBuilder centerLaserUsingWidth(boolean val)
        { centerLaserUsingWidth = val; return this;}

        public LaserBeamBuilder layer(int val)
        { layer = val; return this;}

        public LaserBeamBuilder chargingLaserTime(float val)
        { chargingLaserTime = val; return this; }


        public LaserBeamBuilder activeLaserDisperseTime(float val)
        { activeLaserDisperseTime = val; return this; }

        public LaserBeamBuilder color(Color val)
        { color = val; return this; }

        public LaserBeam build() {
            return new LaserBeam(this);
        }


    }

    public LaserBeam(LaserBeamBuilder lbb){
        this.assetManager = lbb.assetManager;
        this.atlas = assetManager.get(FileLocationStrings.spriteAtlas);

        this.chargingLaserWidth = lbb.chargingLaserWidth;
        this.chargingLaserHeight = lbb.chargingLaserHeight;
        this.activeLaserWidth = lbb.activeLaserWidth;
        this.activeLaserHeight = lbb.activeLaserHeight;
        this.activeLaserTime = lbb.activeLaserTime;
        this.chargingLaserTime = lbb.chargingLaserTime;
        this.activeLaserDisperseTime = lbb.activeLaserDisperseTime;
        this.useWidthAsCenter = lbb.centerLaserUsingWidth;
        this.layer = lbb.layer;
        this.color = lbb.color;
    }


    public float getChargingLaserWidth() {
        return chargingLaserWidth;
    }

    public float getChargingLaserHeight() {
        return chargingLaserHeight;
    }

    public boolean isUseWidthAsCenter() {
        return useWidthAsCenter;
    }

    public Entity createBeam(World world, float x, float y){

        Entity beam = world.createEntity();

        beam.edit().add(new PositionComponent(x, y));
        beam.edit().add(new CollisionBoundComponent(new Rectangle(x, y, chargingLaserWidth, chargingLaserHeight), true));
        //beam.edit().add(new HazardComponent());
        beam.edit().add(new FadeComponent(true, 0.5f, false, 0, 0.3f));

        beam.edit().add(new SoundEmitterComponent(SoundFileStrings.quietLaserMix));


        TextureRegionComponent trc = new TextureRegionComponent(atlas.findRegion(TextureStrings.BLOCK), chargingLaserWidth, chargingLaserHeight, TextureRegionComponent.ENEMY_LAYER_MIDDLE,
                new Color(color.r, color.g, color.b, 0));

        trc.layer = layer;

        beam.edit().add(trc);

        beam.edit().add(new ActionAfterTimeComponent(new Action() {
            @Override
            public void performAction(World world, Entity e) {

               // e.edit().remove(FollowPositionComponent.class);

                Entity activeBeam = world.createEntity();
                activeBeam.edit().add(new HazardComponent());
                activeBeam.edit().add(new SoundEmitterComponent(SoundFileStrings.laserMix));


                PositionComponent oldPC = e.getComponent(PositionComponent.class);
                oldPC.position.set(useWidthAsCenter ? oldPC.getX() - (activeLaserWidth / 2 - chargingLaserWidth / 2) : oldPC.getX(),
                        useWidthAsCenter ? oldPC.getY() : oldPC.getY() - (activeLaserHeight / 2 - chargingLaserHeight / 2), 0);
                activeBeam.edit().add(oldPC);
                activeBeam.edit().add(new CollisionBoundComponent(new Rectangle(oldPC.getX(), oldPC.getY(), activeLaserWidth, activeLaserHeight), true));

                TextureRegionComponent trc = e.getComponent(TextureRegionComponent.class);
                trc.width = activeLaserWidth;
                trc.height = activeLaserHeight;
                trc.layer = layer;
                //TextureRegionComponent trc = e.getComponent(TextureRegionComponent.class);
                trc.color.a = 1;

                activeBeam.edit().add(trc);

                activeBeam.edit().add(new ExpireComponent(activeLaserTime));

                activeBeam.edit().add(new OnDeathActionComponent(new Action() {
                    @Override
                    public void performAction(World world, Entity e) {

                        e.edit().remove(OnDeathActionComponent.class);
                        e.edit().remove(HazardComponent.class);
                        e.edit().remove(FadeComponent.class);
                        e.edit().add(new FadeComponent(false, activeLaserDisperseTime, false));
                        e.edit().add(new ExpireComponent(activeLaserDisperseTime + 0.1f));

/*
                        Entity fadingBeam = world.createEntity();
                        fadingBeam.edit().add(e.getComponent(PositionComponent.class));
                        fadingBeam.edit().add(e.getComponent(TextureRegionComponent.class));
                        fadingBeam.edit().add(new FadeComponent(false, activeLaserDisperseTime, false));
                        fadingBeam.edit().add(new ExpireComponent(activeLaserDisperseTime + 0.1f));*/
                    }
                }));


                e.deleteFromWorld();

            }
        }, chargingLaserTime));



     /*   beam.edit().add(new ActionAfterTimeComponent(new Action() {
            @Override
            public void performAction(World world, Entity e) {

                Entity activeBeam = world.createEntity();

                e.edit().remove(FadeComponent.class);

                e.edit().add(new HazardComponent());
                PositionComponent pc = e.getComponent(PositionComponent.class);
                pc.position.set(centerLaserUsingWidth ? pc.getX() - (activeLaserWidth / 2 - chargingLaserWidth / 2) : pc.getX(),
                        centerLaserUsingWidth ? pc.getY() : pc.getY() - (activeLaserHeight / 2 - chargingLaserHeight / 2), 0);
                e.edit().remove(CollisionBoundComponent.class);
                e.edit().add(new CollisionBoundComponent(new Rectangle(pc.getX(), pc.getY(), activeLaserWidth, activeLaserHeight), true));

                TextureRegionComponent trc = e.getComponent(TextureRegionComponent.class);
                trc.width = activeLaserWidth;
                trc.height = activeLaserHeight;
                trc.layer = layer;
                //TextureRegionComponent trc = e.getComponent(TextureRegionComponent.class);
                trc.color.a = 1;

                e.edit().add(new ExpireComponent(activeLaserTime));

                e.edit().add(new OnDeathActionComponent(new Action() {
                    @Override
                    public void performAction(World world, Entity e) {

                        PositionComponent pc = e.getComponent(PositionComponent.class);

                        Entity fadingBeam = world.createEntity();
                        fadingBeam.edit().add(e.getComponent(PositionComponent.class));
                        fadingBeam.edit().add(e.getComponent(TextureRegionComponent.class));
                        fadingBeam.edit().add(new FadeComponent(false, activeLaserDisperseTime, false));
                        fadingBeam.edit().add(new ExpireComponent(activeLaserDisperseTime + 0.1f));
                    }
                }));
            }
        }, chargingLaserTime));


*/


        return beam;


    }

}
