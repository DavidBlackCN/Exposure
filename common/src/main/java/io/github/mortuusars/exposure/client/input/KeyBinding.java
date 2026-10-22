package io.github.mortuusars.exposure.client.input;

import com.mojang.blaze3d.platform.InputConstants;

import java.util.function.*;

public record KeyBinding(Key matcher, Supplier<Boolean> handler) {
    public boolean keyPressed(net.minecraft.client.input.KeyEvent event) {
        int keyCode = event.key();
        int shortcutKey = event.keycode();
        int modifiers = event.modifiers();
        return matchesPress(keyCode, shortcutKey, modifiers) && handler().get();
    }

    public boolean keyReleased(net.minecraft.client.input.KeyEvent event) {
        int keyCode = event.key();
        int shortcutKey = event.keycode();
        int modifiers = event.modifiers();
        return matchesRelease(keyCode, shortcutKey, modifiers) && handler().get();
    }

    public boolean matches(int keyCode, int shortcutKey, int press, int modifiers) {
        return matcher.matches(keyCode, shortcutKey, press, modifiers);
    }

    public boolean matchesPress(int keyCode, int shortcutKey, int modifiers) {
        return matcher.matches(keyCode, shortcutKey, InputConstants.PRESS, modifiers);
    }

    public boolean matchesRelease(int keyCode, int shortcutKey, int modifiers) {
        return matcher.matches(keyCode, shortcutKey, InputConstants.RELEASE, modifiers);
    }
}
