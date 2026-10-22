package io.github.mortuusars.exposure.client.gui;

import net.minecraft.world.item.ItemStack;

import java.util.function.Predicate;

/** The optional plugin installs this callback without exposing REI classes to core code. */
public class RecipeViewerSupport {
    private static Predicate<ItemStack> reiRecipeAvailable = stack -> false;

    public static void setReiRecipeLookup(Predicate<ItemStack> lookup) {
        reiRecipeAvailable = lookup;
    }

    public static boolean hasReiRecipe(ItemStack stack) {
        return reiRecipeAvailable.test(stack);
    }
}
