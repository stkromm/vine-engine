package vine.window.test;

import static org.junit.Assert.assertTrue;

import java.awt.GraphicsEnvironment;

import org.junit.Before;
import org.junit.Test;

import vine.display.Display;
import vine.platform.lwjgl3.GLFWDisplay;

public class DisplayTest {

    Display display = null;

    @Before
    public void setup() {
        display = new GLFWDisplay();
    }

    @Test
    public void testRefreshRate() {
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        assertTrue(env.getDefaultScreenDevice().getDisplayMode().getRefreshRate() == display.getRefreshRate());
    }

    @Test
    public void testBitValues() {
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        assertTrue(env.getDefaultScreenDevice().getDisplayMode().getBitDepth() == display.getBlueBits()
                + display.getGreenBits() + display.getRedBits() + display.getRedBits());
    }
}