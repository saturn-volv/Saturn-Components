package com.github.saturnvolv.saturncomponents.mixin;

import com.github.saturnvolv.saturncomponents.component.DataComponentTypes;
import com.github.saturnvolv.saturncomponents.component.FoodPropertiesImpl;
import com.github.saturnvolv.saturncomponents.component.type.FoodPropertiesComponent;
import com.mojang.datafixers.util.Pair;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.DataComponentType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Mixin(value = Item.class, priority = 1001)
abstract class ItemMixin implements FoodPropertiesImpl, ItemSettingsAccessor {
    @Shadow public abstract Item asItem();

    @Shadow @Nullable public abstract FoodComponent getFoodComponent();

    @Shadow @Final @Nullable private FoodComponent foodComponent;

    @Shadow public abstract ComponentMap getComponents();

    @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Item$Settings;getComponents()Lnet/minecraft/component/ComponentMap;"))
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
    @Inject( method = "getMaxUseTime(Lnet/minecraft/item/ItemStack;)I", at = @At("HEAD"), cancellable = true)
    protected void getMaxUseTime( ItemStack itemStack, CallbackInfoReturnable<Integer> cir) {
        if (this.hasFoodProperties(itemStack)) {
            FoodPropertiesComponent foodComponent = this.getFoodProperties(itemStack);
            assert foodComponent != null;
            cir.setReturnValue(
                    foodComponent.timeToEat() /
                   (foodComponent.behaviour().isSnack() ? 2 : 1)
            );
        }
    }
    @Inject( method = "getUseAction", at = @At("HEAD"), cancellable = true)
    protected void getUseAction( ItemStack itemStack, CallbackInfoReturnable<UseAction> cir ) {
        if (this.hasFoodProperties(itemStack))
            cir.setReturnValue(
                    this.getFoodProperties(itemStack).behaviour().getUseAction()
            );
        else
            cir.setReturnValue(UseAction.NONE);
    }
    @Inject(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Item;isFood()Z"), cancellable = true)
    protected void use( World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir ) {
        ItemStack stack = user.getStackInHand(hand);
        if (!this.hasFoodProperties(stack)) cir.setReturnValue(TypedActionResult.pass(stack));
        else if (user.canConsume(this.getFoodProperties(stack).behaviour().alwaysEdible())) {
            user.setCurrentHand(hand);
            cir.setReturnValue(TypedActionResult.consume(stack));
        } else
            cir.setReturnValue(TypedActionResult.fail(stack));
    }
    @Mixin(Item.Settings.class)
    public abstract static class ItemSettingsMixin {
        @Shadow public abstract <T> Item.Settings component( DataComponentType<T> type, T value );

        @Inject(method = "food", at = @At("HEAD"))
        private void applyFoodPropertiesComponent( FoodComponent foodComponent, CallbackInfoReturnable<Item.Settings> cir ) {
            if (foodComponent != null) {
                this.component(
                        DataComponentTypes.FOOD_PROPERTIES_CONTENT, FoodPropertiesComponent.fromFoodSettings(foodComponent)
                );
            }
        }
    }
}
