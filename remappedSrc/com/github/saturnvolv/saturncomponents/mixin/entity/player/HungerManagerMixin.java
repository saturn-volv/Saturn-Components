package com.github.saturnvolv.saturncomponents.mixin.entity.player;

import com.github.saturnvolv.saturncomponents.component.type.FoodPropertiesComponent;
import com.github.saturnvolv.saturncomponents.component.FoodPropertiesImpl;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HungerManager.class)
public abstract class HungerManagerMixin implements FoodPropertiesImpl {
    @Shadow public abstract void add( int food, float saturationModifier );

    @Inject(method = "eat", at = @At("HEAD"), cancellable = true)
    private void eat( Item item, ItemStack stack, CallbackInfo ci ) {
        if (!this.hasFoodProperties(stack)) ci.cancel();
        FoodPropertiesComponent foodComponent = this.getFoodProperties(stack);
        this.add(foodComponent.hunger(), foodComponent.saturationModifier());
        ci.cancel();
    }
}
