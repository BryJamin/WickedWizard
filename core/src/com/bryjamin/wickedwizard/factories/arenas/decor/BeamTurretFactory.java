package com.bryjamin.wickedwizard.factories.arenas.decor;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.bryjamin.wickedwizard.assets.TextureStrings;
import com.bryjamin.wickedwizard.ecs.components.ai.Action;
import com.bryjamin.wickedwizard.ecs.components.ai.ActionAfterTimeComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.CollisionBoundComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.bryjamin.wickedwizard.ecs.components.object.WallComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.bryjamin.wickedwizard.factories.AbstractFactory;
import com.bryjamin.wickedwizard.factories.arenas.skins.ArenaSkin;
import com.bryjamin.wickedwizard.factories.weapons.enemy.LaserBeam;
import com.bryjamin.wickedwizard.factories.weapons.enemy.LaserOrbitalTask;
import com.bryjamin.wickedwizard.utils.CenterMath;
import com.bryjamin.wickedwizard.utils.ComponentBag;
import com.bryjamin.wickedwizard.utils.Measure;

import static com.bryjamin.wickedwizard.ecs.components.texture.TextureRegionComponent.PLAYER_LAYER_FAR;

/**
 * Created by BB on 09/08/2017.
 */

public class BeamTurretFactory extends AbstractFactory {

    private ArenaSkin arenaSkin;

    public BeamTurretFactory(AssetManager assetManager, ArenaSkin arenaSkin) {
        super(assetManager);
        this.arenaSkin = arenaSkin;
    }

    public ComponentBag laserChain(float x, float y, float scale, LaserOrbitalTask laserOrbitalTask){
        float width = Measure.units(5f) * scale;
        float height = Measure.units(5f) * scale;
        ComponentBag bag = new ComponentBag();
        bag.add(new PositionComponent(x,y));
        bag.add(new CollisionBoundComponent(new Rectangle(x,y, width, height)));
        bag.add(new WallComponent(new Rectangle(x,y,width, height)));
        bag.add(new TextureRegionComponent(atlas.findRegion(TextureStrings.BLOCK), width,height, PLAYER_LAYER_FAR, new Color(arenaSkin.getWallTint())));
        bag.add(new ActionAfterTimeComponent(laserOrbitalTask, 0));
        return bag;
    }



    public ComponentBag inCombatLaserChain(float x, float y, float scale, LaserOrbitalTask laserOrbitalTask){
        float width = Measure.units(5f) * scale;
        float height = Measure.units(5f) * scale;
        ComponentBag bag = new ComponentBag();
        bag.add(new PositionComponent(x,y));
        bag.add(new CollisionBoundComponent(new Rectangle(x,y, width, height)));
        bag.add(new WallComponent(new Rectangle(x,y,width, height)));
        bag.add(new TextureRegionComponent(atlas.findRegion(TextureStrings.BLOCK), width,height, PLAYER_LAYER_FAR, new Color(arenaSkin.getWallTint())));
        bag.add(new com.bryjamin.wickedwizard.ecs.components.ai.InCombatActionComponent(laserOrbitalTask));
        return bag;
    }



    public ComponentBag timedLaserChain(float x, float y, float scale, float timeTillReapeat, boolean isInstant, LaserOrbitalTask laserOrbitalTask){
        float width = Measure.units(5f) * scale;
        float height = Measure.units(5f) * scale;
        ComponentBag bag = new ComponentBag();
        bag.add(new PositionComponent(x,y));
        bag.add(new CollisionBoundComponent(new Rectangle(x,y, width, height)));
        bag.add(new WallComponent(new Rectangle(x,y,width, height)));
        bag.add(new TextureRegionComponent(atlas.findRegion(TextureStrings.BLOCK), width,height, PLAYER_LAYER_FAR, new Color(arenaSkin.getWallTint())));
        bag.add(new ActionAfterTimeComponent(laserOrbitalTask, isInstant ? 0 : timeTillReapeat, timeTillReapeat, true));
        return bag;
    }


    public ComponentBag timedLaserChain(float x, float y, float scale, float timeTillReapeat, LaserOrbitalTask laserOrbitalTask){
        return timedLaserChain(x,y,scale,timeTillReapeat, true, laserOrbitalTask);
    }


    public ComponentBag inCombatTimedLaserChain(float x, float y, float scale, final float timeTillReapeat, final boolean isInstant, final LaserOrbitalTask laserOrbitalTask){
        float width = Measure.units(5f) * scale;
        float height = Measure.units(5f) * scale;
        ComponentBag bag = new ComponentBag();
        bag.add(new PositionComponent(x,y));
        bag.add(new CollisionBoundComponent(new Rectangle(x,y, width, height)));
        bag.add(new WallComponent(new Rectangle(x,y,width, height)));
        bag.add(new TextureRegionComponent(atlas.findRegion(TextureStrings.BLOCK), width,height, PLAYER_LAYER_FAR, new Color(arenaSkin.getWallTint())));
        bag.add(new com.bryjamin.wickedwizard.ecs.components.ai.InCombatActionComponent(new com.bryjamin.wickedwizard.ecs.components.ai.Task() {
            @Override
            public void performAction(World world, Entity e) {
                e.edit().add(new ActionAfterTimeComponent(laserOrbitalTask, isInstant ? 0 : timeTillReapeat, timeTillReapeat, true));
            }

            @Override
            public void cleanUpAction(World world, Entity e) {
                laserOrbitalTask.cleanUpAction(world, e);
                e.edit().remove(ActionAfterTimeComponent.class);
            }
        }));
        return bag;
    }

    public ComponentBag inCombatTimedLaserChain(float x, float y, float scale, final float timeTillReapeat, final LaserOrbitalTask laserOrbitalTask){
        return inCombatTimedLaserChain(x,y,scale,timeTillReapeat,true, laserOrbitalTask);
    }


    public ComponentBag timedLaserBeam(float x, float y, float scale, float offset, final float timeTillReapeat, final LaserBeam laserBeam){
        return timedLaserBeam(x,y,scale, offset, timeTillReapeat,true, laserBeam);
    }



    public ComponentBag timedLaserBeam(float x, float y, float scale, final float offset, final float timeTillReapeat, final boolean isInstant, final LaserBeam laserBeam){

        float width = Measure.units(5f) * scale;
        float height = Measure.units(5f) * scale;
        ComponentBag bag = new ComponentBag();


        bag.add(new PositionComponent(x,y));
        bag.add(new CollisionBoundComponent(new Rectangle(x,y, width, height)));
        bag.add(new WallComponent(new Rectangle(x,y,width, height)));
        bag.add(new TextureRegionComponent(atlas.findRegion(TextureStrings.BLOCK), width,height, PLAYER_LAYER_FAR, new Color(arenaSkin.getWallTint())));

        bag.add(new ActionAfterTimeComponent(new Action() {
            @Override
            public void performAction(World world, Entity e) {
                CollisionBoundComponent cbc = e.getComponent(CollisionBoundComponent.class);

                laserBeam.createBeam(world,
                        cbc.bound.x +  (!laserBeam.isUseWidthAsCenter() ? offset : CenterMath.offsetX(cbc.bound.getWidth(), laserBeam.getChargingLaserWidth())),
                        cbc.bound.y + (laserBeam.isUseWidthAsCenter() ? offset : CenterMath.offsetY(cbc.bound.getHeight(), laserBeam.getChargingLaserHeight())));
            }
        }, isInstant ? 0 : timeTillReapeat, timeTillReapeat, true));

        return bag;



    }


    public ComponentBag inCombatTimedLaserBeam(float x, float y, float scale, final float offset, final float timeTillReapeat, final boolean isInstant, final LaserBeam laserBeam){
        float width = Measure.units(5f) * scale;
        float height = Measure.units(5f) * scale;
        ComponentBag bag = new ComponentBag();


        bag.add(new PositionComponent(x,y));
        bag.add(new CollisionBoundComponent(new Rectangle(x,y, width, height)));
        bag.add(new WallComponent(new Rectangle(x,y,width, height)));
        bag.add(new TextureRegionComponent(atlas.findRegion(TextureStrings.BLOCK), width,height, PLAYER_LAYER_FAR, new Color(arenaSkin.getWallTint())));
        bag.add(new com.bryjamin.wickedwizard.ecs.components.ai.InCombatActionComponent(new com.bryjamin.wickedwizard.ecs.components.ai.Task() {
            @Override
            public void performAction(World world, Entity e) {
                e.edit().add(new ActionAfterTimeComponent(new Action() {
                    @Override
                    public void performAction(World world, Entity e) {
                        CollisionBoundComponent cbc = e.getComponent(CollisionBoundComponent.class);

                        laserBeam.createBeam(world,
                                cbc.bound.x +  (!laserBeam.isUseWidthAsCenter() ? offset : CenterMath.offsetX(cbc.bound.getWidth(), laserBeam.getChargingLaserWidth())),
                                cbc.bound.y + (laserBeam.isUseWidthAsCenter() ? offset : CenterMath.offsetY(cbc.bound.getHeight(), laserBeam.getChargingLaserHeight())));
                    }
                }, isInstant ? 0 : timeTillReapeat, timeTillReapeat, true));
            }

            @Override
            public void cleanUpAction(World world, Entity e) {
                //laserOrbitalTask.cleanUpAction(world, e);
                e.edit().remove(ActionAfterTimeComponent.class);
            }
        }));
        return bag;
    }

    public ComponentBag inCombatTimedLaserBeam(float x, float y, float scale, float offset, final float timeTillReapeat, final LaserBeam laserBeam){
        return inCombatTimedLaserBeam(x,y,scale, offset, timeTillReapeat,true, laserBeam);
    }



}
