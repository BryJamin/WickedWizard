package com.byrjamin.wickedwizard.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.byrjamin.wickedwizard.components.BulletComponent;
import com.byrjamin.wickedwizard.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.components.GravityComponent;
import com.byrjamin.wickedwizard.components.PlayerComponent;
import com.byrjamin.wickedwizard.components.PositionComponent;
import com.byrjamin.wickedwizard.components.StateComponent;
import com.byrjamin.wickedwizard.components.TextureRegionComponent;
import com.byrjamin.wickedwizard.components.VelocityComponent;
import com.byrjamin.wickedwizard.components.WeaponComponent;
import com.byrjamin.wickedwizard.factories.EntityFactory;
import com.byrjamin.wickedwizard.helper.AbstractGestureDectector;
import com.byrjamin.wickedwizard.helper.Measure;
import com.byrjamin.wickedwizard.screens.PlayScreen;

/**
 * Created by Home on 04/03/2017.
 */
public class PlayerInputSystem extends EntityProcessingSystem implements InputProcessor {

    ComponentMapper<PositionComponent> pm;
    ComponentMapper<VelocityComponent> vm;
    ComponentMapper<CollisionBoundComponent> cbm;
    ComponentMapper<WeaponComponent> wm;
    ComponentMapper<StateComponent> sm;
    ComponentMapper<TextureRegionComponent> trm;


    OrthographicCamera gamecam;
    Vector3 touchInput;
    Vector3 unprojectedInput;

    private boolean hasTarget;
    private boolean canFire;

    @SuppressWarnings("unchecked")
    public PlayerInputSystem(OrthographicCamera gamecam) {
        super(Aspect.all(PositionComponent.class, VelocityComponent.class, PlayerComponent.class, StateComponent.class));
        this.gamecam = gamecam;
    }

    @Override
    protected void initialize() {
        Gdx.input.setInputProcessor(this);
    }

    @Override
    protected void process(Entity e) {

        PositionComponent pc = pm.get(e);
        VelocityComponent vc = vm.get(e);
        CollisionBoundComponent cbc = cbm.get(e);
        WeaponComponent wc = wm.get(e);
        StateComponent sc = sm.get(e);
        TextureRegionComponent trc = trm.get(e);


        wc.timer.update(world.getDelta());

        if(hasTarget) {
            Vector3 input = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            gamecam.unproject(input);

            if (input.x - 20 <= pc.getX() && pc.getX() < unprojectedInput.x + 20) {
                hasTarget = false;
                vc.velocity.x = 0;
            } else if (pc.getX() >= unprojectedInput.x) {
                vc.velocity.x = -Measure.units(115f);
            } else {
                vc.velocity.x = Measure.units(115f);
            }
        } else if(canFire){
            if(Gdx.input.isTouched()) {
                if(wc.timer.isFinishedAndReset()) {
                    sc.setState(1);
                    Vector3 input = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
                    gamecam.unproject(input);

                    float x = pc.getX() + (cbc.bound.getWidth() / 2);
                    float y = pc.getY() + (cbc.bound.getHeight() / 2);

                    double angleOfTravel = (Math.atan2(input.y - y, input.x - x));

                    if(angleOfTravel >= 0) trc.scaleX = (angleOfTravel <= (Math.PI / 2)) ? 1 : -1;
                    else trc.scaleX = (angleOfTravel >= -(Math.PI / 2)) ? 1 : -1;

                    System.out.println(Math.toDegrees(angleOfTravel));
                    EntityFactory.createBullet(world, x, y, angleOfTravel);
                }
            }

        }

        if(!canFire){
            if(sc.getState() != 0) {
                sc.setState(0);
            }
        }



    }


    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        touchInput = new Vector3(screenX, screenY, 0);
        unprojectedInput = gamecam.unproject(touchInput);
        if(unprojectedInput.y <= 290) {
            hasTarget = true;
        } else {
            canFire = true;
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {

        canFire = false;
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
