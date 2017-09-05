package com.bryjamin.wickedwizard.ecs.components.movement;

import com.artemis.Component;
import com.badlogic.gdx.math.Vector3;
import com.bryjamin.wickedwizard.utils.Measure;

/**
 * Created by Home on 16/05/2017.
 */

public class OrbitComponent extends Component {

    public Vector3 centerOfOrbit;

    public float radius;

    public float speedInDegrees;

    public float currentAngleInDegrees;

    public float offsetX;
    public float offsetY;

    public OrbitComponent(){
        centerOfOrbit = new Vector3();
        radius = Measure.units(5f);
        speedInDegrees = 1;
    }

    public OrbitComponent(Vector3 centerOfOrbit, float radius, float speedInDegrees, float currentAngleInDegrees){
        this(centerOfOrbit, radius, speedInDegrees, currentAngleInDegrees, 0, 0);
    }

    public OrbitComponent(Vector3 centerOfOrbit, float radius, float speedInDegrees, float currentAngleInDegrees, float offsetX, float offsetY){
        this.centerOfOrbit = centerOfOrbit;
        this.radius = radius;
        this.speedInDegrees = speedInDegrees;
        this.currentAngleInDegrees = currentAngleInDegrees;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }





}
