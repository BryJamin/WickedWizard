package com.byrjamin.wickedwizard.ecs.systems.input;

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
import com.byrjamin.wickedwizard.ecs.systems.FindPlayerSystem;
import com.byrjamin.wickedwizard.ecs.systems.GrapplePointSystem;
import com.byrjamin.wickedwizard.ecs.systems.MoveToSystem;
import com.byrjamin.wickedwizard.factories.PlayerFactory;
import com.byrjamin.wickedwizard.utils.Measure;
import com.byrjamin.wickedwizard.utils.collider.Collider;
import com.byrjamin.wickedwizard.utils.enums.Direction;
import com.byrjamin.wickedwizard.utils.timer.StateTimer;

/**
 * Created by Home on 04/03/2017.
 */
public class PlayerInputSystem extends EntityProcessingSystem {

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


    private Integer tapInputPoll = null;

    private PlayerInput playerInput;

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

        playerInput = new PlayerInput(world, gameport, movementArea);
    }


    public InputProcessor getInputProcessor() {
        return playerInput;
    }

    public PlayerInput getPlayerInput() {
        return playerInput;
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

        playerInput.tapInputTimer.update(world.getDelta());

        if (mtm.get(e).targetX == null && mtm.get(e).targetY == null) {
            playerInput.activeGrapple = false;
            gc.ignoreGravity = false;
        }

        if (!playerInput.activeGrapple) {

            if (playerInput.movementInputPoll != null) {
                if (Gdx.input.isTouched(playerInput.movementInputPoll)) {
                    Vector3 input = new Vector3(Gdx.input.getX(playerInput.movementInputPoll), Gdx.input.getY(playerInput.movementInputPoll), 0);
                    gameport.unproject(input);
                    if (input.y <= movementArea.y + movementArea.getHeight()) {
                        ac.accelX = Measure.units(15f);
                        ac.maxX = Measure.units(80f);
                        MoveToSystem.moveTo(input.x, cbc.getCenterX(), ac, vc);
                    }
                }
            }

            if (playerInput.firingInputPoll != null) {

                if (Gdx.input.isTouched(playerInput.firingInputPoll)) {

                    wc.timer.update(world.getDelta());

                    Vector3 input = new Vector3(Gdx.input.getX(playerInput.firingInputPoll), Gdx.input.getY(playerInput.firingInputPoll), 0);
                    gameport.unproject(input);
                    float x = pc.getX() + (cbc.bound.getWidth() / 2);
                    float y = pc.getY() + (cbc.bound.getHeight() / 2);
                    double angleOfTravel = (Math.atan2(input.y - y, input.x - x));


                    if (playerInput.tapInputTimer.isFinished()) {
                        if (angleOfTravel >= 0) {
                            if (angleOfTravel <= (Math.PI / 2)) {
                                com.byrjamin.wickedwizard.ecs.systems.graphical.DirectionalSystem.changeDirection(world, e, Direction.RIGHT, DirectionalComponent.PRIORITY.HIGH);
                            } else {
                                com.byrjamin.wickedwizard.ecs.systems.graphical.DirectionalSystem.changeDirection(world, e, Direction.LEFT, DirectionalComponent.PRIORITY.HIGH);
                            }
                        } else {
                            if (angleOfTravel >= -(Math.PI / 2)) {
                                com.byrjamin.wickedwizard.ecs.systems.graphical.DirectionalSystem.changeDirection(world, e, Direction.RIGHT, DirectionalComponent.PRIORITY.HIGH);
                            } else {
                                com.byrjamin.wickedwizard.ecs.systems.graphical.DirectionalSystem.changeDirection(world, e, Direction.LEFT, DirectionalComponent.PRIORITY.HIGH);
                            }
                        }
                    }

                    if (wc.timer.isFinishedAndReset()) {
                        sc.setState(1);
                        wc.weapon.fire(world, x, y, angleOfTravel);
                    }
                }
            } else {
                if (sc.getState() != 0) {
                    sc.setState(0);
                }
            }
        }

        if (cbc.getRecentCollisions().contains(Collider.Collision.TOP, false)) {
            turnOffGlide();
        }
    }

    public void grappleTo(float grappleX, float grappleY) {

        Rectangle r = world.getSystem(GrapplePointSystem.class).returnTouchedGrapple(grappleX, grappleY);


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
                    cbc);

            mtc.endSpeedX = 0;
            mtc.maxEndSpeedY = MAX_GRAPPLE_MOVEMENT / 2;
            // mtc.endSpeedY = GRAPPLE_MOVEMENT * 5;
            world.getSystem(FindPlayerSystem.class).getPC(GravityComponent.class).ignoreGravity = true;
            turnOffGlide();

            world.getSystem(FindPlayerSystem.class).getPC(VelocityComponent.class).velocity.y = 0;
            world.getSystem(FindPlayerSystem.class).getPC(VelocityComponent.class).velocity.x = 0;

        }

    }


    public void turnOnGlide(){

        PositionComponent pc = world.getSystem(FindPlayerSystem.class).getPC(PositionComponent.class);
        ParentComponent parc = world.getSystem(FindPlayerSystem.class).getPC(ParentComponent.class);
        GlideComponent glc = world.getSystem(FindPlayerSystem.class).getPC(GlideComponent.class);

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

    public void turnOffGlide(){

        ParentComponent parc = world.getSystem(FindPlayerSystem.class).getPC(ParentComponent.class);
        GlideComponent glc = world.getSystem(FindPlayerSystem.class).getPC(GlideComponent.class);

        EntitySubscription subscription = world.getAspectSubscriptionManager().get(Aspect.all(ChildComponent.class));
        IntBag entityIds = subscription.getEntities();


        for(int i = 0; i < entityIds.size(); i++){
            ChildComponent c = world.getMapper(ChildComponent.class).get(entityIds.get(i));
            if(parc.children.contains(c, true)){
                parc.children.removeValue(c, true);
                world.delete(entityIds.get(i));
            }
        }

        glc.gliding = false;
        glc.active = false;
    }


}