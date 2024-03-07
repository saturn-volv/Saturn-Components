package com.github.saturnvolv.saturncomponents.mixin;

import net.minecraft.component.ComponentMap;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Item.Settings.class)
public interface ItemSettingsAccessor {
    @Accessor
    FoodComponent getFoodComponent();

    @Invoker
    ComponentMap callGetComponents();
}