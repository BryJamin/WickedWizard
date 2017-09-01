package com.bryjamin.wickedwizard.ecs.components.identifiers;

import com.artemis.Component;

/**
 * Created by Home on 28/05/2017.
 */

public class BossTeleporterComponent extends Component{

    public LinkComponent link;

    public float offsetX;
    public float offsetY;


    public BossTeleporterComponent(){

    }

    public BossTeleporterComponent(LinkComponent link){
        this.link = link;
    }


}
