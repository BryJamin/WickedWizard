package com.byrjamin.wickedwizard.ecs.systems;

import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.byrjamin.wickedwizard.ecs.components.OnDeathComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;

/**
 * Created by Home on 01/04/2017.
 */

public class TutorialSystem extends BaseSystem {

    ComponentMapper<OnDeathComponent> odm;
    ComponentMapper<PositionComponent> pm;

    private boolean isTutorialStarted;
    private boolean isTutorialOver;

    private boolean isStateCompete;

    private enum STATE {
        GROUND, DASHING, SHOOTING, GRAPPLING, LEAVING
    }

    private STATE _state;

    @Override
    protected void processSystem() {

        if(!isTutorialOver) {


            if (isTutorialStarted && !isTutorialOver) {
                world.getSystem(LockSystem.class).lockDoors();
                if (_state == null) {
                    _state = STATE.GROUND;
                    isStateCompete = false;
                }

            }

            if(isStateCompete) {

                switch (_state) {
                    case GROUND:
                        _state = STATE.DASHING;
                        break;
                    case DASHING:
                        _state = STATE.SHOOTING;
                        break;
                    case SHOOTING:
                        _state = STATE.GRAPPLING;
                        break;
                    case GRAPPLING:
                        _state = STATE.LEAVING;
                        break;
                }


            }

            if (isTutorialOver) {
                world.getSystem(LockSystem.class).unlockDoors();
            }

        }

    }




    @Override
    protected boolean checkProcessing() {
        return true;
    }


    public void startTutorial(){

        if(!isTutorialStarted && !isTutorialOver) {
            isTutorialStarted = true;
        }
    }


}
