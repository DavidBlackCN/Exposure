package io.github.mortuusars.exposure.integration.rei;

import io.github.mortuusars.exposure.Exposure;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.display.DisplaySerializer;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public record PhotographStackingDisplay(boolean removing) implements Display {
    public static final CategoryIdentifier<PhotographStackingDisplay> CATEGORY =
            CategoryIdentifier.of(Exposure.resource("photograph_stacking"));

    @Override
    public List<EntryIngredient> getInputEntries() {
        return List.of(removing ? EntryIngredients.of(Exposure.Items.STACKED_PHOTOGRAPHS.get())
                : EntryIngredients.ofItems(List.of(Exposure.Items.PHOTOGRAPH.get(), Exposure.Items.STACKED_PHOTOGRAPHS.get())));
    }

    @Override
    public List<EntryIngredient> getOutputEntries() {
        return List.of(EntryIngredients.of(removing ? Exposure.Items.PHOTOGRAPH.get() : Exposure.Items.STACKED_PHOTOGRAPHS.get()));
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() { return CATEGORY; }

    @Override
    public Optional<Identifier> getDisplayLocation() { return Optional.empty(); }

    @Override
    public @Nullable DisplaySerializer<?> getSerializer() { return null; }
}
