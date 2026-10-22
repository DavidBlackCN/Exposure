package io.github.mortuusars.exposure.client;

import com.mojang.blaze3d.platform.InputConstants;
import io.github.mortuusars.exposure.client.input.Key;
import io.github.mortuusars.exposure.client.input.Modifier;
import net.minecraft.client.input.KeyEvent;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class KeyBindingsTests {
    @Test
    void eitherSideOfEachModifierWorksAndLocksDoNotChangeShortcuts() {
        assertTrue(Modifier.matches(Modifier.SHIFT, 0x0001));
        assertTrue(Modifier.matches(Modifier.SHIFT, 0x0002));
        assertTrue(Modifier.matches(Modifier.CONTROL, 0x0040));
        assertTrue(Modifier.matches(Modifier.CONTROL, 0x0080));
        assertTrue(Modifier.matches(Modifier.CONTROL | Modifier.SHIFT, 0x0082));
        assertTrue(Modifier.matches(Modifier.CONTROL, 0x0040 | InputConstants.MOD_CAPS_LOCK | InputConstants.MOD_NUM_LOCK));
        assertFalse(Modifier.matches(Modifier.CONTROL, 0x0041));
        assertFalse(Modifier.matches(Modifier.NONE, 0x0100));
    }

    @Test
    void heldKeyRepeatsUsingSdlActionAndReleaseRunsOnce() {
        var key = Key.press(InputConstants.KEY_EQUALS);
        assertTrue(key.matches(InputConstants.KEY_EQUALS, '=', InputConstants.PRESS, 0));
        assertTrue(key.matches(InputConstants.KEY_EQUALS, '=', InputConstants.REPEAT, 0));
        assertFalse(key.matches(InputConstants.KEY_EQUALS, '=', InputConstants.RELEASE, 0));
        AtomicInteger calls = new AtomicInteger();
        var binding = Key.release(InputConstants.KEY_EQUALS).executes(() -> { calls.incrementAndGet(); });
        KeyEvent event = new KeyEvent(InputConstants.KEY_EQUALS, '=', 0);
        assertFalse(binding.keyPressed(event));
        assertTrue(binding.keyReleased(event));
        assertEquals(1, calls.get());
    }
}
