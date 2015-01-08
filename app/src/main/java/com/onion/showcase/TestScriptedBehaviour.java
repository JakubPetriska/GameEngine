package com.onion.showcase;

import com.onion.api.Component;
import com.onion.api.GameObject;
import com.onion.api.Core;

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
