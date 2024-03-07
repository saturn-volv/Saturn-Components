package com.github.saturnvolv.saturncomponents.component;

import com.github.saturnvolv.saturncomponents.component.type.FoodPropertiesComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface FoodPropertiesImpl {
    default int saturnComponents$hunger( FoodPropertiesComponent foodProperties) {
        return foodProperties.hunger();
    }
    default float saturnComponents$saturationModifier( FoodPropertiesComponent foodProperties) {
        return foodProperties.saturationModifier();
    }
    default int saturnComponents$timeToEat( FoodPropertiesComponent foodProperties) {
        return foodProperties.timeToEat();
    }
    default boolean saturnComponents$isMeat( FoodPropertiesComponent foodProperties) {
        return foodProperties.behaviour().isMeat();
    }
    default boolean saturnComponents$alwaysEdible( FoodPropertiesComponent foodProperties) {
        return foodProperties.behaviour().alwaysEdible();
    }
    default boolean saturnComponents$isSnack( FoodPropertiesComponent foodProperties) {
        return foodProperties.behaviour().isSnack();
    }
    default boolean saturnComponents$isDrink( FoodPropertiesComponent foodProperties) {
        return foodProperties.behaviour().isDrink();
    }
    default boolean saturnComponents$clearsEffects( FoodPropertiesComponent foodProperties) {
        return foodProperties.behaviour().clearsEffects();
    }
    default ItemStack saturnComponents$resultItem( FoodPropertiesComponent foodProperties) {
        if (foodProperties.hasResultItem())
            return foodProperties.getResult();
        return null;
    }
    default List<FoodPropertiesComponent.FoodEffect> saturnComponents$statusEffects( FoodPropertiesComponent foodProperties) {
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
