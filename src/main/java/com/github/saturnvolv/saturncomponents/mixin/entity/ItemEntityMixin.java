package com.github.saturnvolv.saturncomponents.mixin.entity;

import com.github.saturnvolv.saturncomponents.component.DataComponentTypes;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.DamageTypeTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin {
    @Shadow public abstract ItemStack getStack();

    @Inject(method = "isFireImmune", at = @At("HEAD"))
    private void getStackInstance( CallbackInfoReturnable<Boolean> cir, @Share("fireproof_item") LocalRef<ItemStack> fireproofItem) {
        fireproofItem.set(this.getStack());
    }
    @Redirect(method = "isFireImmune", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Item;isFireproof()Z"))
    private boolean redirectItem( Item instance, @Share("fireproof_item") LocalRef<ItemStack> fireproofItem ) {
        ItemStack itemStack = fireproofItem.get();
        return itemStack.getComponents().contains(DataComponentTypes.IS_FIREPROOF);
    }
    @Redirect(method = "damage",at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Item;damage(Lnet/minecraft/entity/damage/DamageSource;)Z"))
    private boolean getStackAwareDamage( Item instance, DamageSource source ) {
        boolean isFireproof = this.getStack().getComponents().contains(DataComponentTypes.IS_FIREPROOF);
        return !isFireproof || !source.isIn(DamageTypeTags.IS_FIRE);
    }
}
