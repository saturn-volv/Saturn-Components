package com.github.saturnvolv.saturncomponents.mixin;

import com.github.saturnvolv.saturncomponents.component.DataComponentTypes;
import com.github.saturnvolv.saturncomponents.component.type.FoodPropertiesComponent;
import net.minecraft.item.*;
import net.minecraft.util.UseAction;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HoneyBottleItem.class)
public abstract class HoneyBottleItemMixin extends ItemMixin {
    @Inject(method = "getMaxUseTime", at = @At("HEAD"), cancellable = true)
    protected void getMaxUseTime( ItemStack stack, CallbackInfoReturnable<Integer> cir ) {
        super.getMaxUseTime(stack, cir);
        cir.cancel();
    }
    @Inject(method = "getUseAction", at = @At("HEAD"), cancellable = true)
    protected void getUseAction( ItemStack stack, CallbackInfoReturnable<UseAction> cir ) {
        super.getUseAction(stack, cir);
        cir.cancel();
    }

    @Override
    public int saturnComponents$timeToEat( FoodPropertiesComponent foodProperties ) {
        return 40;
    }
    @Override
    public boolean saturnComponents$isDrink( FoodPropertiesComponent foodProperties ) {
        return true;
    }
}
