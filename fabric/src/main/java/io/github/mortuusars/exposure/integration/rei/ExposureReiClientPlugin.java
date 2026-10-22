package io.github.mortuusars.exposure.integration.rei;

import io.github.mortuusars.exposure.Config;
import io.github.mortuusars.exposure.Exposure;
import io.github.mortuusars.exposure.client.gui.RecipeViewerSupport;
import io.github.mortuusars.exposure.client.gui.screen.ItemRenameScreen;
import io.github.mortuusars.exposure.client.gui.screen.LightroomScreen;
import io.github.mortuusars.exposure.client.gui.screen.album.AlbumScreen;
import io.github.mortuusars.exposure.world.camera.ExposureType;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.client.registry.screen.ExclusionZones;
import me.shedaniel.rei.api.common.plugins.PluginManager;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;
import me.shedaniel.rei.plugin.common.BuiltinPlugin;
import me.shedaniel.rei.plugin.common.displays.DefaultInformationDisplay;
import me.shedaniel.rei.plugin.common.displays.crafting.DefaultCustomShapelessDisplay;
import net.minecraft.network.chat.Component;

import java.util.List;

public class ExposureReiClientPlugin implements REIClientPlugin {
    @Override
    public void registerCategories(CategoryRegistry registry) {
        registry.add(new PhotographPrintingCategory(), config -> config.setQuickCraftingEnabledByDefault(false));
        registry.add(new PhotographStackingCategory(), config -> config.setQuickCraftingEnabledByDefault(false));
        registry.addWorkstations(PhotographPrintingDisplay.CATEGORY, EntryStacks.of(Exposure.Items.LIGHTROOM.get()));
        registry.addWorkstations(PhotographStackingDisplay.CATEGORY, EntryStacks.of(Exposure.Items.STACKED_PHOTOGRAPHS.get()));
    }

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        registry.add(new PhotographPrintingDisplay(ExposureType.BLACK_AND_WHITE));
        registry.add(new PhotographPrintingDisplay(ExposureType.COLOR));
        registry.add(new PhotographStackingDisplay(false));
        registry.add(new PhotographStackingDisplay(true));
        if (Config.Client.SHOW_REI_INFORMATION.get()) {
            registry.add(DefaultInformationDisplay.createFromEntries(
                    EntryIngredients.ofItems(List.of(Exposure.Items.PHOTOGRAPH_FRAME.get(), Exposure.Items.CLEAR_PHOTOGRAPH_FRAME.get())),
                    Component.translatable("item.exposure.photograph_frame"))
                    .line(Component.translatable("exposure.jei.info.photograph_frame")));
        }

        RecipeViewerSupport.setReiRecipeLookup(stack -> !PluginManager.areAnyReloading()
                && registry.get(BuiltinPlugin.CRAFTING).stream()
                .filter(DefaultCustomShapelessDisplay.class::isInstance)
                .anyMatch(display -> !display.getInputEntries().isEmpty()
                        && EntryIngredients.testFuzzy(display.getInputEntries().getFirst(), EntryStacks.of(stack))));
    }

    @Override
    public void registerExclusionZones(ExclusionZones zones) {
        zones.register(LightroomScreen.class, screen -> {
            var area = screen.getFilmSlotTabArea();
            return List.of(new Rectangle(area.getX(), area.getY(), area.getWidth(), area.getHeight()));
        });
        zones.register(AlbumScreen.class, screen -> List.of(new Rectangle(0, 0, screen.width, screen.height)));
        zones.register(ItemRenameScreen.class, screen -> List.of(new Rectangle(0, 0, screen.width, screen.height)));
    }
}
