package io.github.mortuusars.exposure.integration.rei;

import io.github.mortuusars.exposure.Exposure;
import io.github.mortuusars.exposure.world.camera.ExposureType;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

import java.util.ArrayList;
import java.util.List;

public class PhotographPrintingCategory implements DisplayCategory<PhotographPrintingDisplay> {
    private static final Identifier TEXTURE = Exposure.resource("textures/gui/jei/photograph_printing.png");

    @Override
    public CategoryIdentifier<PhotographPrintingDisplay> getCategoryIdentifier() { return PhotographPrintingDisplay.CATEGORY; }

    @Override
    public Component getTitle() { return Component.translatable("jei.exposure.photograph_printing.title"); }

    @Override
    public Renderer getIcon() { return EntryStacks.of(Exposure.Items.LIGHTROOM.get()); }

    @Override
    public int getDisplayWidth(PhotographPrintingDisplay display) { return 180; }

    @Override
    public int getDisplayHeight() { return 90; }

    @Override
    public List<Widget> setupDisplay(PhotographPrintingDisplay display, Rectangle bounds) {
        int x = bounds.x + 5;
        int y = bounds.y + 5;
        List<Widget> widgets = new ArrayList<>();
        widgets.add(Widgets.createRecipeBase(bounds));
        widgets.add(Widgets.createTexturedWidget(TEXTURE, new Rectangle(x, y, 170, 80)));
        widgets.add(Widgets.createTexturedWidget(TEXTURE, new Rectangle(x, y, 170, 80), 0, 80));
        List<EntryIngredient> inputs = display.getInputEntries();
        widgets.add(Widgets.createSlot(new Point(x + 78, y + 17)).entries(inputs.getFirst()).markInput().disableBackground());
        widgets.add(Widgets.createSlot(new Point(x + 6, y + 55)).entries(inputs.get(1)).markInput().disableBackground());
        if (display.type() == ExposureType.COLOR) {
            for (int i = 0; i < 3; i++) {
                widgets.add(Widgets.createSlot(new Point(x + 40 + i * 18, y + 55))
                        .entries(inputs.get(i + 2)).markInput().disableBackground());
            }
        }
        widgets.add(Widgets.createSlot(new Point(x + 94, y + 55)).entries(inputs.getLast()).markInput().disableBackground());
        widgets.add(Widgets.createSlot(new Point(x + 144, y + 55))
                .entries(display.getOutputEntries().getFirst()).markOutput().disableBackground());
        return widgets;
    }
}
