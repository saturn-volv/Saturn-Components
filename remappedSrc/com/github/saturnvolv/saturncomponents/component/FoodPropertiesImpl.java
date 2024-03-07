package com.github.saturnvolv.saturncomponents.component;

import com.github.saturnvolv.saturncomponents.component.type.FoodPropertiesComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public interface FoodPropertiesImpl {
    @Nullable
    default FoodPropertiesComponent getFoodProperties( ItemStack itemStack) {
        return itemStack.getComponents().get(DataComponentTypes.FOOD_PROPERTIES_CONTENT);
    }
    default boolean hasFoodProperties( ItemStack itemStack ) {
        return itemStack.getComponents().contains(DataComponentTypes.FOOD_PROPERTIES_CONTENT);
    }

    @Nullable
    default FoodPropertiesComponent getFoodProperties( Item item) {
        return item.getComponents().get(DataComponentTypes.FOOD_PROPERTIES_CONTENT);
    }
    default boolean hasFoodProperties( Item item ) {
        return item.getComponents().contains(DataComponentTypes.FOOD_PROPERTIES_CONTENT);
    }
}
