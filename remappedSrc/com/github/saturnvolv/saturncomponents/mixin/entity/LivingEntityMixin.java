package com.github.saturnvolv.saturncomponents.mixin.entity;

import com.github.saturnvolv.saturncomponents.component.type.FoodPropertiesComponent;
import com.github.saturnvolv.saturncomponents.component.FoodPropertiesImpl;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(value = LivingEntity.class, priority = 1001)
public abstract class LivingEntityMixin implements FoodPropertiesImpl {
    @Shadow public abstract boolean clearStatusEffects();

    @Inject(method = "applyFoodEffects", at = @At("HEAD"), cancellable = true)
    private void applyFoodEffects( ItemStack stack, World world, LivingEntity targetEntity, CallbackInfo ci ) {
        if (!this.hasFoodProperties(stack)) ci.cancel();
        FoodPropertiesComponent foodComponent = this.getFoodProperties(stack);
        List<FoodPropertiesComponent.FoodEffect> effects = foodComponent.statusEffects();
        for (FoodPropertiesComponent.FoodEffect foodEffect : effects) {
            if (!world.isClient && foodEffect.effect() != null && world.random.nextFloat() < foodEffect.chance()) {
                targetEntity.addStatusEffect(foodEffect.createStatusEffectInstance());
            }
        }
        ci.cancel();
    }
    @Redirect(method = "eatFood", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isFood()Z"))
    private boolean hasFoodComponent( ItemStack instance ) {
        return this.hasFoodProperties(instance);
    }
    @Inject(method = "eatFood", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;playSound(Lnet/minecraft/entity/player/PlayerEntity;DDDLnet/minecraft/sound/SoundEvent;Lnet/minecraft/sound/SoundCategory;FF)V"))
    private void applyClearEffects( World world, ItemStack stack, CallbackInfoReturnable<ItemStack> cir ) {
        if (this.getFoodProperties(stack).behaviour().clearsEffects() && !world.isClient)
            this.clearStatusEffects();
    }
}
