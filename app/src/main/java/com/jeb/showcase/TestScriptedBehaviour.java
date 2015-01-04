package com.jeb.showcase;

import com.jeb.api.Component;
import com.jeb.api.GameObject;
import com.jeb.engine.Core;

public class TestScriptedBehaviour extends Component {

    public TestScriptedBehaviour(Core core, GameObject owner) {
        super(core, owner);
    }

    @Override
    public void update() {
        gameObject.transform.position.x += 0.01;
        gameObject.transform.position.y -= 0.005;
    }

}
