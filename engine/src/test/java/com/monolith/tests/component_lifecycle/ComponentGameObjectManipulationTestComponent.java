package com.monolith.tests.component_lifecycle;

import com.monolith.api.Component;
import com.monolith.api.GameObject;

/**
 * Created by Jakub Petriska on 26. 2. 2015.
 * <p/>
 * This class tests adding of GameObjects and Components.
 */
public class ComponentGameObjectManipulationTestComponent extends Component {

    private int mUpdateCount;

    private GameObject mFirstChild;
    private GameObject mSecondChild;
    private GameObject mThirdChild;
    private GameObject mChildOfFirstChild;
    private GameObject mReattachedChild;

    // TODO add changing object's parent

    @Override
    public void update() {
        mUpdateCount++;

        switch (mUpdateCount) {
            case 1:
                mFirstChild = new GameObject(getGameObject());
                break;
            case 2:
                mSecondChild = new GameObject(getGameObject());
                mSecondChild.addComponent(new LifecycleAssertingComponent("SecondChild"));
                break;
            case 3:
                mFirstChild.addComponent(new LifecycleAssertingComponent("FirstChild"));
                break;
            case 4:
                // In one frame add object, add component to it and remove this component
                mThirdChild = new GameObject(getGameObject());
                Component component = new LifecycleAssertingComponent("ThirdChild");
                mThirdChild.addComponent(component);
                mThirdChild.removeComponent(component);
                break;
            case 5:
                mChildOfFirstChild = new GameObject(mFirstChild);
                mChildOfFirstChild.addComponent(new LifecycleAssertingComponent("ChildOfFirstChild"));

                mReattachedChild = new GameObject(mFirstChild);
                mReattachedChild.addComponent(new LifecycleAssertingComponent("ReattachedChild"));
                break;
            case 6:
                mFirstChild.removeChild(mChildOfFirstChild);
                break;
            case 7:
                getGameObject().removeChild(mSecondChild);
                break;
            case 8:
                mReattachedChild.setParent(mThirdChild);
                break;
        }
    }
}
