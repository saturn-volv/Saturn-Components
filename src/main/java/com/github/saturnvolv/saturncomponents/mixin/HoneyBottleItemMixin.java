package com.github.saturnvolv.saturncomponents.mixin;

import com.github.saturnvolv.saturncomponents.component.DataComponentTypes;
import com.github.saturnvolv.saturncomponents.component.type.FoodPropertiesComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.*;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

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
    @Inject(method = "finishUsing", at = @At(value = "FIELD", target = "Lnet/minecraft/world/World;isClient:Z"), cancellable = true)
    public void stopMethod( ItemStack stack, World world, LivingEntity user, CallbackInfoReturnable<ItemStack> cir ) {
        super.finishUsing(stack, world, user, cir);
        cir.setReturnValue(stack);
    }

    @Override
    public Optional<ItemStack> resultItem( FoodPropertiesComponent foodProperties ) {
        return Optional.of(
                new ItemStack(Items.GLASS_BOTTLE)
        );
    }

    @Override
    public int timeToEat( FoodPropertiesComponent foodProperties ) {
        return 40;
    }
    @Override
    public boolean isDrink( FoodPropertiesComponent foodProperties ) {
        return true;
    }
}
