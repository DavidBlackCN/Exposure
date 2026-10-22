package io.github.mortuusars.exposure.fabric;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class AdvancementResourcesTests {
    @Test
    void mergedAdvancementsUse263RecipeConditionsAndExistingRecipes() throws Exception {
        // Check the merged Fabric resources, including platform-specific recipes
        // and the camera stand advancement that are absent from common resources.
        Path resources = Path.of(System.getProperty("exposure.test.resources"));
        Path advancements = resources.resolve("data/exposure/advancement");
        int checked = 0;
        try (var paths = Files.walk(advancements)) {
            for (Path path : paths.filter(p -> p.toString().endsWith(".json")).toList()) {
                JsonObject advancement = JsonParser.parseString(Files.readString(path)).getAsJsonObject();
                for (JsonElement element : advancement.getAsJsonObject("criteria").asMap().values()) {
                    JsonObject criterion = element.getAsJsonObject();
                    if (!criterion.get("trigger").getAsString().equals("minecraft:recipe_unlocked")) continue;
                    JsonObject conditions = criterion.getAsJsonObject("conditions");
                    assertFalse(conditions.has("recipe"), path + ": obsolete singular recipe field");
                    assertTrue(conditions.has("recipes"), path + ": missing recipes field");
                    assertRecipesExist(resources, path, conditions.getAsJsonArray("recipes"));
                    checked++;
                }
                JsonObject rewards = advancement.getAsJsonObject("rewards");
                if (rewards != null && rewards.has("recipes")) {
                    assertRecipesExist(resources, path, rewards.getAsJsonArray("recipes"));
                }
            }
        }
        assertTrue(checked > 0, "No recipe unlock criteria were checked");
    }

    private static void assertRecipesExist(Path resources, Path advancement, JsonArray recipes) {
        assertFalse(recipes.isEmpty(), advancement + ": empty recipe list");
        for (JsonElement element : recipes) {
            String id = element.getAsString();
            if (id.startsWith("exposure:")) {
                Path recipe = resources.resolve("data/exposure/recipe/" + id.substring("exposure:".length()) + ".json");
                assertTrue(Files.isRegularFile(recipe), advancement + ": missing recipe " + id);
            }
        }
    }
}
