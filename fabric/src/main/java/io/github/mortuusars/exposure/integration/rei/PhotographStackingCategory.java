package io.github.mortuusars.exposure.integration.rei;

import io.github.mortuusars.exposure.Exposure;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

import java.util.ArrayList;
import java.util.List;

public class PhotographStackingCategory implements DisplayCategory<PhotographStackingDisplay> {
    private static final Identifier TEXTURE = Exposure.resource("textures/gui/jei/photograph_stacking.png");

    @Override
    public CategoryIdentifier<PhotographStackingDisplay> getCategoryIdentifier() { return PhotographStackingDisplay.CATEGORY; }

    @Override
    public Component getTitle() { return Component.translatable("jei.exposure.photograph_stacking.title"); }

    @Override
    public Renderer getIcon() { return EntryStacks.of(Exposure.Items.STACKED_PHOTOGRAPHS.get()); }

    @Override
    public int getDisplayWidth(PhotographStackingDisplay display) { return 119; }

    @Override
    public int getDisplayHeight() { return 48; }

    @Override
    public List<Widget> setupDisplay(PhotographStackingDisplay display, Rectangle bounds) {
        int x = bounds.x + 5;
        int y = bounds.y + 5;
        List<Widget> widgets = new ArrayList<>();
        widgets.add(Widgets.createRecipeBase(bounds));
        widgets.add(Widgets.createTexturedWidget(TEXTURE, new Rectangle(x, y, 109, 38)));
        widgets.add(Widgets.createSlot(new Point(x + 11, y + 11))
                .entries(display.getInputEntries().getFirst()).markInput().disableBackground());
        widgets.add(Widgets.createSlot(new Point(x + 82, y + 11))
                .entries(display.getOutputEntries().getFirst()).markOutput().disableBackground());
        widgets.add(Widgets.createTexturedWidget(TEXTURE,
                new Rectangle(x + (display.removing() ? 22 : 18), y + (display.removing() ? 15 : 5), 20, 20),
                109, display.removing() ? 38 : 0));
        widgets.add(Widgets.createTooltip(new Rectangle(x + 10, y + 4, 32, 31),
                Component.translatable(display.removing() ? "jei.exposure.photograph_stacking.removing.tooltip"
                        : "jei.exposure.photograph_stacking.stacking.tooltip")));
        return widgets;
    }
}
