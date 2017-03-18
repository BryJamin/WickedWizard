package com.byrjamin.wickedwizard.ecs.systems;

import com.artemis.Aspect;
import com.artemis.Component;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.PlayerComponent;
import com.byrjamin.wickedwizard.ecs.components.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.StateComponent;
import com.byrjamin.wickedwizard.ecs.components.TextureRegionComponent;
import com.byrjamin.wickedwizard.ecs.components.VelocityComponent;
import com.byrjamin.wickedwizard.ecs.components.WeaponComponent;
import com.byrjamin.wickedwizard.factories.EntityFactory;
import com.byrjamin.wickedwizard.helper.Measure;

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

    private Integer movementInputPoll = null;
    private Integer firingInputPoll = null;

    private float moveTarget;

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



        if(movementInputPoll != null) {

            if(Gdx.input.isTouched(movementInputPoll)) {
                Vector3 input = new Vector3(Gdx.input.getX(movementInputPoll), Gdx.input.getY(movementInputPoll), 0);
                gamecam.unproject(input);

                if(input.y < 290){
                    moveTarget = input.x;
                    hasTarget = true;
                }

            }

        }  else {
           // vc.velocity.x = 0;
        }


        if(hasTarget){
            if (moveTarget - 20 <= pc.getX() && pc.getX() < moveTarget + 20) {
                vc.velocity.x = 0;
                hasTarget = false;
            } else if (pc.getX() >= moveTarget) {
                vc.velocity.x = -Measure.units(115f);
            } else {
                vc.velocity.x = Measure.units(115f);
            }
        }


        if(firingInputPoll != null){
            if(Gdx.input.isTouched(firingInputPoll)) {
                if(wc.timer.isFinishedAndReset()) {
                    sc.setState(1);
                    Vector3 input = new Vector3(Gdx.input.getX(firingInputPoll), Gdx.input.getY(firingInputPoll), 0);
                    gamecam.unproject(input);

                    float x = pc.getX() + (cbc.bound.getWidth() / 2);
                    float y = pc.getY() + (cbc.bound.getHeight() / 2);

                    double angleOfTravel = (Math.atan2(input.y - y, input.x - x));

                    if(angleOfTravel >= 0) trc.scaleX = (angleOfTravel <= (Math.PI / 2)) ? 1 : -1;
                    else trc.scaleX = (angleOfTravel >= -(Math.PI / 2)) ? 1 : -1;

                    Entity bullet =  EntityFactory.createBullet(world, x, y, angleOfTravel);

                    for(Component c : wc.additionalComponenets) {
                        bullet.edit().add(c);
                    }
                }
            }
        } else {
            if(sc.getState() != 0) {
                sc.setState(0);
            }
        }
/*
        if(!canFire){
            if(sc.getState() != 0) {
                sc.setState(0);
            }
        }*/



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
            movementInputPoll = pointer;
            moveTarget = unprojectedInput.x;
        } else {
            canFire = true;
            firingInputPoll = pointer;
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {

        if(movementInputPoll != null) {
            movementInputPoll = (movementInputPoll == pointer) ? null : movementInputPoll;
        }

        if(firingInputPoll != null) {
            firingInputPoll = (firingInputPoll == pointer) ? null : firingInputPoll;
        }
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
