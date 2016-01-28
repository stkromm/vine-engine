package vine.game.test;

import vine.game.GameObject;

/**
 * @author Steffen
 *
 */
public class MyTestObject extends GameObject {

    /**
     * 
     */
    public MyTestObject() {

    }

    @Override
    public void update(float delta) {
        super.update(delta);
        onDestroy();
    }

    @Override
    public void begin() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void construct(int x) {
        System.out.println("Custom constructor implementation");
    }

}