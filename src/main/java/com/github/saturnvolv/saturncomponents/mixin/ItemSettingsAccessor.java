package com.github.saturnvolv.saturncomponents.mixin;

import net.minecraft.component.ComponentMap;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.util.Rarity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Item.Settings.class)
public interface ItemSettingsAccessor {
    @Accessor
    @Nullable
    FoodComponent getFoodComponent();

    @Accessor
    int getMaxCount();

    @Accessor
    int getMaxDamage();
    @Accessor
    Rarity getRarity();

    @Accessor
    boolean isFireproof();

    @Accessor
    Item getRecipeRemainder();
}
