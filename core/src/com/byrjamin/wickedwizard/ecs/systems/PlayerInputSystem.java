package com.byrjamin.wickedwizard.ecs.systems;

import com.artemis.Aspect;
import com.artemis.Component;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySubscription;
import com.artemis.systems.EntityProcessingSystem;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.byrjamin.wickedwizard.MainGame;
import com.byrjamin.wickedwizard.ecs.components.ChildComponent;
import com.byrjamin.wickedwizard.ecs.components.ParentComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.AccelerantComponent;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.DirectionalComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.GlideComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.GravityComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.JumpComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.MoveToComponent;
import com.byrjamin.wickedwizard.ecs.components.PlayerComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.AnimationStateComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.VelocityComponent;
import com.byrjamin.wickedwizard.ecs.components.WeaponComponent;
import com.byrjamin.wickedwizard.factories.BulletFactory;
import com.byrjamin.wickedwizard.factories.PlayerFactory;
import com.byrjamin.wickedwizard.utils.Measure;
import com.byrjamin.wickedwizard.utils.collider.Collider;
import com.byrjamin.wickedwizard.utils.enums.Direction;
import com.byrjamin.wickedwizard.utils.timer.StateTimer;

/**
 * Created by Home on 04/03/2017.
 */
public class PlayerInputSystem extends EntityProcessingSystem implements InputProcessor {

    public float MAX_GRAPPLE_LAUNCH = Measure.units(60f);
    private float MAX_GRAPPLE_MOVEMENT = Measure.units(150f);
    private float GRAPPLE_MOVEMENT = Measure.units(15f);


    ComponentMapper<PositionComponent> pm;
    ComponentMapper<ParentComponent> parm;
    ComponentMapper<ChildComponent> cm;
    ComponentMapper<VelocityComponent> vm;
    ComponentMapper<AccelerantComponent> am;
    ComponentMapper<MoveToComponent> mtm;
    ComponentMapper<CollisionBoundComponent> cbm;
    ComponentMapper<WeaponComponent> wm;
    ComponentMapper<AnimationStateComponent> sm;
    ComponentMapper<GravityComponent> gm;
    ComponentMapper<GlideComponent> glm;
    ComponentMapper<TextureRegionComponent> trm;
    ComponentMapper<JumpComponent> jm;

    OrthographicCamera gamecam;
    Viewport gameport;

    private StateTimer jumpTimer = new StateTimer(0.25f);

    private Integer movementInputPoll = null;
    private Integer firingInputPoll = null;
    public boolean activeGrapple;

    private Array<ChildComponent> wingChildren = new Array<ChildComponent>();

    public Rectangle movementArea;


    @SuppressWarnings("unchecked")
    public PlayerInputSystem(OrthographicCamera gamecam, Viewport gameport) {
        super(Aspect.all(PositionComponent.class, VelocityComponent.class, PlayerComponent.class, AnimationStateComponent.class));
        this.gamecam = gamecam;
        this.gameport = gameport;
        movementArea = new Rectangle(gamecam.position.x - gameport.getWorldWidth() / 2,
                gamecam.position.y - gameport.getWorldHeight() / 2,
                MainGame.GAME_WIDTH, Measure.units(10f));
    }


    public InputProcessor getInputProcessor(){
        return this;
    }

    @Override
    protected void process(Entity e) {

        PositionComponent pc = pm.get(e);
        VelocityComponent vc = vm.get(e);
        AccelerantComponent ac = am.get(e);
        CollisionBoundComponent cbc = cbm.get(e);
        WeaponComponent wc = wm.get(e);
        AnimationStateComponent sc = sm.get(e);
        TextureRegionComponent trc = trm.get(e);
        GravityComponent gc = gm.get(e);
        GlideComponent glc = glm.get(e);
        MoveToComponent mtc = mtm.get(e);

        jumpTimer.update(world.getDelta());

        if(mtm.get(e).targetX == null && mtm.get(e).targetY == null){
            activeGrapple = false;
            gc.ignoreGravity = false;
        }

        if(!activeGrapple) {

            if (movementInputPoll != null) {
                if (Gdx.input.isTouched(movementInputPoll)) {
                    Vector3 input = new Vector3(Gdx.input.getX(movementInputPoll), Gdx.input.getY(movementInputPoll), 0);
                    gameport.unproject(input);
                    if (input.y <= movementArea.y + movementArea.getHeight()) {
                        ac.accelX = Measure.units(15f);
                        ac.maxX = Measure.units(80f);
                        MoveToSystem.moveTo(input.x, cbc.getCenterX(), ac, vc);
                    }
                }
            } else if (!activeGrapple) {
                if (cbc.getRecentCollisions().contains(Collider.Collision.TOP, false)) {
                    MoveToSystem.decelerate(ac, vc);
                }


                if (movementInputPoll == null && glc.active && glc.gliding && mtc.targetX == null && mtc.targetY == null) {
                    MoveToSystem.decelerate(ac, vc);
                }

            }

            if (firingInputPoll != null) {

                if (Gdx.input.isTouched(firingInputPoll)) {

                    wc.timer.update(world.getDelta());

                    Vector3 input = new Vector3(Gdx.input.getX(firingInputPoll), Gdx.input.getY(firingInputPoll), 0);
                    gameport.unproject(input);
                    float x = pc.getX() + (cbc.bound.getWidth() / 2);
                    float y = pc.getY() + (cbc.bound.getHeight() / 2);
                    double angleOfTravel = (Math.atan2(input.y - y, input.x - x));


                    if(jumpTimer.isFinished()) {
                        if (angleOfTravel >= 0) {
                            if (angleOfTravel <= (Math.PI / 2)) {
                                DirectionalSystem.changeDirection(world, e, Direction.RIGHT, DirectionalComponent.PRIORITY.HIGH);
                            } else {
                                DirectionalSystem.changeDirection(world, e, Direction.LEFT, DirectionalComponent.PRIORITY.HIGH);
                            }
                        } else {
                            if (angleOfTravel >= -(Math.PI / 2)) {
                                DirectionalSystem.changeDirection(world, e, Direction.RIGHT, DirectionalComponent.PRIORITY.HIGH);
                            } else {
                                DirectionalSystem.changeDirection(world, e, Direction.LEFT, DirectionalComponent.PRIORITY.HIGH);
                            }
                        }
                    }

                    if (wc.timer.isFinishedAndReset()) {
                        sc.setState(1);

                        Entity bullet = BulletFactory.createBullet(world, x, y, angleOfTravel);

                        for (Component c : wc.additionalComponenets) {
                            bullet.edit().add(c);
                        }
                    }
                }
            } else {
                if (sc.getState() != 0) {
                    sc.setState(0);
                }
            }
        }

        if(cbc.getRecentCollisions().contains(Collider.Collision.TOP, false)){
            ParentComponent parc = world.getSystem(FindPlayerSystem.class).getPC(ParentComponent.class);
            turnOffGlide(glc, parc);
        }
    }


    public void turnOnGlide(GlideComponent glc, ParentComponent parc, PositionComponent pc){

        wingChildren.clear();

        Entity e = world.createEntity();
        for(Component c : PlayerFactory.rightWings(parc, pc)) {
            e.edit().add(c);
        }

        wingChildren.add(e.getComponent(ChildComponent.class));

        e = world.createEntity();
        for(Component c : PlayerFactory.leftWings(parc, pc)) {
            e.edit().add(c);
        }

        wingChildren.add(e.getComponent(ChildComponent.class));

        glc.gliding = true;
        glc.active = true;

    }

    public void turnOffGlide(GlideComponent glc, ParentComponent parc){

        EntitySubscription subscription = world.getAspectSubscriptionManager().get(Aspect.all(ChildComponent.class));
        IntBag entityIds = subscription.getEntities();


        for(int i = 0; i < entityIds.size(); i++){
            ChildComponent c = cm.get(entityIds.get(i));
            if(parc.children.contains(c, true)){
                parc.children.removeValue(c, true);
                world.delete(entityIds.get(i));
            }
        }

        glc.gliding = false;
        glc.active = false;
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
        Vector3 touchInput = new Vector3(screenX, screenY, 0);
        gameport.unproject(touchInput);

        if(!activeGrapple) {
            activeGrapple = world.getSystem(GrappleSystem.class).touchedGrapple(touchInput.x, touchInput.y);
        }
        if(!activeGrapple) {

            if (touchInput.y <= movementArea.y + movementArea.getHeight()) {
                movementInputPoll = pointer;
                world.getSystem(FindPlayerSystem.class).getPC(GravityComponent.class).ignoreGravity = false;
                //jumpTimer.reset(); //TODO figure out if touching the ground should disable the glide
            } else if(firingInputPoll == null){
                firingInputPoll = pointer;
                jumpTimer.reset();
            } else {
                jumpTimer.reset();
            }
        } else {

            //TODO a tad unsafe
            Rectangle r = world.getSystem(GrappleSystem.class).returnTouchedGrapple(touchInput.x, touchInput.y);;

            if(r != null) {
                MoveToComponent mtc = world.getSystem(FindPlayerSystem.class).getPC(MoveToComponent.class);
                CollisionBoundComponent cbc = world.getSystem(FindPlayerSystem.class).getPC(CollisionBoundComponent.class);
                AccelerantComponent ac = world.getSystem(FindPlayerSystem.class).getPC(AccelerantComponent.class);

                float x = r.x + r.getWidth() / 2;
                float y = r.y + r.getHeight() / 2;

                world.getSystem(MoveToSystem.class).flyToNoPathCheck(
                        Math.atan2(y - cbc.getCenterY(), x - cbc.getCenterX()),
                        x,
                        y,
                        GRAPPLE_MOVEMENT * 10,
                        mtc,
                        ac,
                        cbc);

                mtc.endSpeedX = 0;
                mtc.maxEndSpeedY = MAX_GRAPPLE_MOVEMENT / 2;
                // mtc.endSpeedY = GRAPPLE_MOVEMENT * 5;
                world.getSystem(FindPlayerSystem.class).getPC(GravityComponent.class).ignoreGravity = true;
                GlideComponent glc = world.getSystem(FindPlayerSystem.class).getPC(GlideComponent.class);
                ParentComponent parc = world.getSystem(FindPlayerSystem.class).getPC(ParentComponent.class);
                turnOffGlide(glc, parc);

                world.getSystem(FindPlayerSystem.class).getPC(VelocityComponent.class).velocity.y = 0;
                world.getSystem(FindPlayerSystem.class).getPC(VelocityComponent.class).velocity.x = 0;

            }
        }

        world.getSystem(ActiveOnTouchSystem.class).activeOnTouchTrigger(touchInput.x, touchInput.y);

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {


        if(!activeGrapple) {

            //TODO Find player get player should be player position or something if player can't be found
            if(!jumpTimer.isFinished() /*&& jm.get(world.getSystem(FindPlayerSystem.class).getPlayer()).jumps > 0*/) {


                Vector3 input = new Vector3(screenX, screenY, 0);
                gameport.unproject(input);


                CollisionBoundComponent cbc = world.getSystem(FindPlayerSystem.class).getPC(CollisionBoundComponent.class);
                VelocityComponent vc = world.getSystem(FindPlayerSystem.class).getPC(VelocityComponent.class);
                JumpComponent jc = world.getSystem(FindPlayerSystem.class).getPC(JumpComponent.class);

                if(input.y > cbc.bound.y + cbc.bound.getHeight()) {
                    if (jc.jumps > 0) {
                        vc.velocity.y = Measure.units(80f);
                        jc.jumps--;
                        PositionComponent pc = world.getSystem(FindPlayerSystem.class).getPC(PositionComponent.class);
                        ParentComponent parc = world.getSystem(FindPlayerSystem.class).getPC(ParentComponent.class);
                        GlideComponent glc = world.getSystem(FindPlayerSystem.class).getPC(GlideComponent.class);
                        turnOffGlide(glc, parc);
                        turnOnGlide(glc, parc, pc);
                    }

                } else {
                    GlideComponent glc = world.getSystem(FindPlayerSystem.class).getPC(GlideComponent.class);
                    ParentComponent parc = world.getSystem(FindPlayerSystem.class).getPC(ParentComponent.class);
                    turnOffGlide(glc, parc);
                }

                jumpTimer.reset();
            }


        }

        if(movementInputPoll != null) {
            movementInputPoll = (movementInputPoll == pointer) ? null : movementInputPoll;
        }

        if(firingInputPoll != null) {
            firingInputPoll = (firingInputPoll == pointer) ? null : firingInputPoll;

            if(firingInputPoll == null){
                world.getSystem(FindPlayerSystem.class).getPC(WeaponComponent.class).timer.reset();
            }

        }
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
