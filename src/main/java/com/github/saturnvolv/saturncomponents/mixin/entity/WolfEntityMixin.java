package com.github.saturnvolv.saturncomponents.mixin.entity;

import com.github.saturnvolv.saturncomponents.component.FoodPropertiesImpl;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = WolfEntity.class, priority = 1001)
public class WolfEntityMixin implements FoodPropertiesImpl {
    @Inject(method = "isBreedingItem", at = @At("TAIL"), cancellable = true)
    private void isMeat( ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (!this.hasFoodProperties(stack)) cir.setReturnValue(false);
        else cir.setReturnValue(this.getFoodProperties(stack).behaviour().isMeat());
    }
}
