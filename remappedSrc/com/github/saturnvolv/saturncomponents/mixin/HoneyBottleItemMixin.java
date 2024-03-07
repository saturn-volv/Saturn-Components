package com.github.saturnvolv.saturncomponents.mixin;

import com.github.saturnvolv.saturncomponents.component.DataComponentTypes;
import com.github.saturnvolv.saturncomponents.component.type.FoodPropertiesComponent;
import net.minecraft.item.*;
import net.minecraft.util.UseAction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HoneyBottleItem.class)
public abstract class HoneyBottleItemMixin extends ItemMixin{

    @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Item;<init>(Lnet/minecraft/item/Item$Settings;)V"))
    protected void changeFoodComponentData( Item.Settings settings, CallbackInfo ci ) {
        FoodPropertiesComponent component = null;
        FoodComponent foodComponent = getFoodComponent();
        boolean isHoneyBottle = this.asItem() instanceof HoneyBottleItem;
        boolean isMilk = this.asItem() instanceof MilkBucketItem;
        if (!isHoneyBottle && !isMilk) return;
        if (isMilk) component = FoodPropertiesComponent.DEFAULT;
        if (foodComponent != null)
            component = FoodPropertiesComponent.fromFoodSettings(foodComponent);
        if (component != null) {
            FoodPropertiesComponent.FoodBehaviour behaviour = component.behaviour();
            behaviour = new FoodPropertiesComponent.FoodBehaviour(
                    behaviour.isMeat(), behaviour.alwaysEdible(), behaviour.isSnack(), true, behaviour.clearsEffects()
            );
            System.out.println(behaviour);
            settings.component(DataComponentTypes.FOOD_PROPERTIES_CONTENT, new FoodPropertiesComponent(
                    component.hunger(),
                    component.saturationModifier(),
                    isHoneyBottle ? 40 : component.timeToEat(),
                    behaviour,
                    component.resultItem(),
                    component.statusEffects()
            ));
        }
    }
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
}
