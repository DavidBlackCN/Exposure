package io.github.mortuusars.exposure.client.input;

import com.mojang.blaze3d.platform.InputConstants;

public interface Modifier {
    int NONE = 0;
    int SHIFT = InputConstants.MOD_SHIFT;
    int CONTROL = InputConstants.MOD_CONTROL;
    int ALT = InputConstants.MOD_ALT;
    int SUPER = InputConstants.MOD_SUPER;

    static boolean matches(int expected, int actual) {
        // SDL has separate bits for the left/right modifier keys. Lock keys do
        // not alter a binding, and either side satisfies the requested modifier.
        return ((expected & SHIFT) != 0) == ((actual & SHIFT) != 0)
                && ((expected & CONTROL) != 0) == ((actual & CONTROL) != 0)
                && ((expected & ALT) != 0) == ((actual & ALT) != 0)
                && ((expected & SUPER) != 0) == ((actual & SUPER) != 0);
    }
}
