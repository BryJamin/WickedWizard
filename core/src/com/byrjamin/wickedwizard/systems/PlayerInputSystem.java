package com.byrjamin.wickedwizard.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.byrjamin.wickedwizard.components.PlayerComponent;
import com.byrjamin.wickedwizard.components.PositionComponent;
import com.byrjamin.wickedwizard.components.VelocityComponent;
import com.byrjamin.wickedwizard.helper.AbstractGestureDectector;
import com.byrjamin.wickedwizard.helper.Measure;

/**
 * Created by Home on 04/03/2017.
 */
public class PlayerInputSystem extends EntityProcessingSystem implements InputProcessor {

    ComponentMapper<PositionComponent> pm;
    ComponentMapper<VelocityComponent> vm;
    OrthographicCamera gamecam;
    Vector3 touchInput;
    Vector3 unprojectedInput;

    private boolean hasTarget;

    @SuppressWarnings("unchecked")
    public PlayerInputSystem(OrthographicCamera gamecam) {
        super(Aspect.all(PositionComponent.class, VelocityComponent.class, PlayerComponent.class));
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

        if(hasTarget) {

            if(unprojectedInput.x -20 <= pc.getX() && pc.getX() < unprojectedInput.x + 20) {
                hasTarget = false;
                vc.velocity.x = 0;
            } else if(pc.getX() >= unprojectedInput.x){
                vc.velocity.x = -Measure.units(115f);
            } else {
                vc.velocity.x = Measure.units(115f);
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
        if(unprojectedInput.y <= 300) {
            hasTarget = true;
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
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
