package com.bryjamin.wickedwizard.ecs.systems.graphical;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.bryjamin.wickedwizard.ecs.components.graphics.CameraShakeComponent;
import com.bryjamin.wickedwizard.utils.Measure;

/**
 * Created by BB on 12/08/2017.
 */

public class CameraShakeSystem extends EntitySystem {


    private float shakeIntensity;

    private Camera gamecam;
    private Viewport gamePort;



    @SuppressWarnings("unchecked")
    public CameraShakeSystem(Viewport gamePort) {
        super(Aspect.all(CameraShakeComponent.class));
        this.gamecam = gamePort.getCamera();
        this.gamePort = gamePort;
    }

    @Override
    protected void processSystem() {

        if (this.getEntities().size() <= 0) return;

        for(Entity e : this.getEntities()) {

            float intensity = e.getComponent(CameraShakeComponent.class).intensity;
            shakeIntensity = shakeIntensity < intensity ? intensity : shakeIntensity;

        }


        float xMultiplier = gamecam.viewportWidth / gamecam.viewportHeight;




        float x = MathUtils.random.nextFloat() * Measure.units(shakeIntensity * xMultiplier) - Measure.units(shakeIntensity) / 2;

        float y = MathUtils.random.nextFloat() * Measure.units(shakeIntensity) - Measure.units(shakeIntensity) / 2;


      //  System.out.println(gamecam.position.x);

        gamecam.position.x += x;
        gamecam.position.y += y;

        gamecam.update();


      //  System.out.println(gamecam.position.x);
        //gamecam.position.add(x,0,0);

    }


}
