package com.onion.showcase;

import android.text.TextUtils;
import android.util.Log;

import com.onion.api.Component;
import com.onion.api.GameObject;
import com.onion.api.Core;
import com.onion.api.Touch;

public class TestScriptedBehaviour extends Component {

    public TestScriptedBehaviour(Core core, GameObject owner) {
        super(core, owner);
    }

    @Override
    public void update() {
        gameObject.transform.position.x += 0.01;
        gameObject.transform.position.y -= 0.005;

        StringBuilder builder = new StringBuilder();
        for(Touch touch : core.touchInput.getTouches()) {
            builder.append(touch.getState() + " - [" + touch.getX() + ", " + touch.getY() + "], ");
        }
        String touchesDesc = builder.toString();
        if(!TextUtils.isEmpty(touchesDesc)) {
            Log.d("DEBUG", touchesDesc);
        }
    }

}
