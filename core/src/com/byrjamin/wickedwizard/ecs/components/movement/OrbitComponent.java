package com.byrjamin.wickedwizard.ecs.components.movement;

import com.artemis.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.byrjamin.wickedwizard.utils.Measure;

/**
 * Created by Home on 16/05/2017.
 */

public class OrbitComponent extends Component {

    public Vector3 centerOfOrbit;

    public float radius;

    public float speedInDegrees;

    public float angle;

    public float offsetX;
    public float offsetY;

    public OrbitComponent(){
        centerOfOrbit = new Vector3();
        radius = Measure.units(5f);
        speedInDegrees = 1;
    }

    public OrbitComponent(Vector3 centerOfOrbit, float radius, float speedInDegrees, float angle){
        this(centerOfOrbit, radius, speedInDegrees, angle, 0, 0);
    }

    public OrbitComponent(Vector3 centerOfOrbit, float radius, float speedInDegrees, float angle, float offsetX, float offsetY){
        this.centerOfOrbit = centerOfOrbit;
        this.radius = radius;
        this.speedInDegrees = speedInDegrees;
        this.angle = angle;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }





}
