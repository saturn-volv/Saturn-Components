package com.github.saturnvolv.saturncomponents.component;

import com.github.saturnvolv.saturncomponents.component.type.FoodPropertiesComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public interface FoodPropertiesImpl {
    default int hunger( FoodPropertiesComponent foodProperties) {
        return foodProperties.hunger();
    }
    default float saturationModifier( FoodPropertiesComponent foodProperties) {
        return foodProperties.saturationModifier();
    }
    default int timeToEat( FoodPropertiesComponent foodProperties) {
        return foodProperties.timeToEat();
    }
    default boolean isMeat( FoodPropertiesComponent foodProperties) {
        return foodProperties.behaviour().isMeat();
    }
    default boolean alwaysEdible( FoodPropertiesComponent foodProperties) {
        return foodProperties.behaviour().alwaysEdible();
    }
    default boolean isSnack( FoodPropertiesComponent foodProperties) {
        return foodProperties.behaviour().isSnack();
    }
    default boolean isDrink( FoodPropertiesComponent foodProperties) {
        return foodProperties.behaviour().isDrink();
    }
    default boolean clearsEffects( FoodPropertiesComponent foodProperties) {
        return foodProperties.behaviour().clearsEffects();
    }
    default Optional<ItemStack> resultItem( FoodPropertiesComponent foodProperties) {
        if (foodProperties.hasResultItem())
            return foodProperties.resultItem();
        return Optional.empty();
    }
    default List<FoodPropertiesComponent.FoodEffect> statusEffects( FoodPropertiesComponent foodProperties) {
        return foodProperties.statusEffects();
    }

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
