package io.github.mortuusars.exposure.integration.rei;

import io.github.mortuusars.exposure.world.item.crafting.recipe.ComponentTransferringRecipe;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.plugins.REICommonPlugin;
import me.shedaniel.rei.api.common.registry.display.ServerDisplayRegistry;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.plugin.common.displays.crafting.DefaultCustomShapelessDisplay;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/** Uses REI's existing serializer and sync for recipes hidden from the vanilla recipe book. */
public class ExposureReiCommonPlugin implements REICommonPlugin {
    @Override
    public void registerDisplays(ServerDisplayRegistry registry) {
        registry.beginRecipeFiller(ComponentTransferringRecipe.class).fill(ExposureReiCommonPlugin::display);
    }

    static DefaultCustomShapelessDisplay display(RecipeHolder<ComponentTransferringRecipe> holder) {
        ComponentTransferringRecipe recipe = holder.value();
        List<EntryIngredient> inputs = new ArrayList<>();
        inputs.add(EntryIngredients.ofIngredient(recipe.getSourceIngredient()));
        inputs.addAll(EntryIngredients.ofIngredients(recipe.getIngredients()));
        return new DefaultCustomShapelessDisplay(inputs,
                List.of(EntryIngredients.of(recipe.getResultTemplate())), Optional.of(holder.id().identifier()));
    }
}
