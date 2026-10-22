package io.github.mortuusars.exposure.integration.rei;

import io.github.mortuusars.exposure.Exposure;
import io.github.mortuusars.exposure.world.camera.ExposureType;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.display.DisplaySerializer;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public record PhotographPrintingDisplay(ExposureType type) implements Display {
    public static final CategoryIdentifier<PhotographPrintingDisplay> CATEGORY =
            CategoryIdentifier.of(Exposure.resource("photograph_printing"));

    @Override
    public List<EntryIngredient> getInputEntries() {
        List<EntryIngredient> inputs = new ArrayList<>();
        inputs.add(EntryIngredients.of(type == ExposureType.COLOR
                ? Exposure.Items.DEVELOPED_COLOR_FILM.get() : Exposure.Items.DEVELOPED_BLACK_AND_WHITE_FILM.get()));
        inputs.add(EntryIngredients.ofItemTag(Exposure.Tags.Items.PHOTO_PAPERS));
        if (type == ExposureType.COLOR) {
            inputs.add(EntryIngredients.ofItemTag(Exposure.Tags.Items.CYAN_PRINTING_DYES));
            inputs.add(EntryIngredients.ofItemTag(Exposure.Tags.Items.MAGENTA_PRINTING_DYES));
            inputs.add(EntryIngredients.ofItemTag(Exposure.Tags.Items.YELLOW_PRINTING_DYES));
        }
        inputs.add(EntryIngredients.ofItemTag(Exposure.Tags.Items.BLACK_PRINTING_DYES));
        return inputs;
    }

    @Override
    public List<EntryIngredient> getOutputEntries() {
        ItemStack photograph = new ItemStack(Exposure.Items.PHOTOGRAPH.get());
        photograph.set(Exposure.DataComponents.PHOTOGRAPH_TYPE, type);
        return List.of(EntryIngredients.of(photograph));
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() { return CATEGORY; }

    @Override
    public Optional<Identifier> getDisplayLocation() { return Optional.empty(); }

    // A local instruction display; actual server recipes use REI's crafting serializer.
    @Override
    public @Nullable DisplaySerializer<?> getSerializer() { return null; }
}
