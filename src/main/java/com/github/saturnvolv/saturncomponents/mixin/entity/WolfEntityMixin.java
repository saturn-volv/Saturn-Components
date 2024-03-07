package com.github.saturnvolv.saturncomponents.mixin.entity;

import com.github.saturnvolv.saturncomponents.component.FoodPropertiesImpl;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = WolfEntity.class, priority = 1001)
public class WolfEntityMixin implements FoodPropertiesImpl {
    @Inject(method = "isBreedingItem", at = @At("TAIL"), cancellable = true)
    private void isMeat( ItemStack stack, CallbackInfoReturnable<Boolean> cir ) {
        if (!this.hasFoodProperties(stack)) cir.setReturnValue(false);
        else cir.setReturnValue(this.getFoodProperties(stack).behaviour().isMeat());
    }
}
