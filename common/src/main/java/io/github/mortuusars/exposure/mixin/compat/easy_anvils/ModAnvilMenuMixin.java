package io.github.mortuusars.exposure.mixin.compat.easy_anvils;

import io.github.mortuusars.exposure.Config;
import io.github.mortuusars.exposure.world.item.BrokenInterplanarProjectorItem;
import io.github.mortuusars.exposure.world.item.InterplanarProjectorItem;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AnvilMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Pseudo
@Mixin(targets = "fuzs.easyanvils.common.world.inventory.ModAnvilMenu", remap = false)
public abstract class ModAnvilMenuMixin extends AnvilMenu {
    public ModAnvilMenuMixin(int containerId, Inventory playerInventory) {
        super(containerId, playerInventory);
    }

    @ModifyConstant(method = "setItemName", constant = @Constant(intValue = 50))
    private int onSetItemName(int constant) {
        if (Config.Server.SPEC.isLoaded()
              && Config.Server.INTERPLANAR_PROJECTOR_LARGER_RENAMING_LIMIT.get()
              && (inputSlots.getItem(0).getItem() instanceof InterplanarProjectorItem
                || inputSlots.getItem(0).getItem() instanceof BrokenInterplanarProjectorItem)) {
            return 150;
        }
        return 50;
    }
}
