package com.byrjamin.wickedwizard.ecs.components.identifiers;

import com.artemis.Component;
import com.byrjamin.wickedwizard.ecs.systems.level.ArenaMap;

/**
 * Created by Home on 28/05/2017.
 */

public class BossTeleporterComponent extends Component{

    public LinkComponent link;

    public ArenaMap From;
    public ArenaMap To;


    public BossTeleporterComponent(){

    }

    public BossTeleporterComponent(LinkComponent link){
        this.link = link;
    }


}
