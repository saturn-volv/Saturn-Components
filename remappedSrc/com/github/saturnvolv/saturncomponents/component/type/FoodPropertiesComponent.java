package com.github.saturnvolv.saturncomponents.component.type;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.UseAction;
import net.minecraft.util.dynamic.Codecs;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public record FoodPropertiesComponent(
        int hunger,
        float saturationModifier,
        int timeToEat,
        FoodBehaviour behaviour,
        Optional<ItemStack> resultItem,
        List<FoodEffect> statusEffects
) {
    public static final FoodPropertiesComponent DEFAULT = new FoodPropertiesComponent(0, 0.0f, 32, FoodBehaviour.DEFAULT, Optional.empty(), List.of());

    public static final Codec<FoodPropertiesComponent> CODEC;
    public static final PacketCodec<RegistryByteBuf, FoodPropertiesComponent> PACKET_CODEC;

    public boolean hasResultItem() {
        return this.resultItem.isPresent();
    }
    public @Nullable ItemStack getResult() {
        return this.hasResultItem() ? this.resultItem.get() : null;
    }

    public static FoodPropertiesComponent fromFoodSettings( FoodComponent foodComponent ) {
        List<FoodPropertiesComponent.FoodEffect> foodEffects = new ArrayList<>();
        for (Pair<StatusEffectInstance, Float> pair : foodComponent.getStatusEffects()) {
            FoodPropertiesComponent.FoodEffect foodEffect = new FoodPropertiesComponent.FoodEffect(
                    pair.getFirst().getEffectType(),
                    pair.getFirst().getDuration(),
                    pair.getFirst().getAmplifier(),
                    pair.getFirst().isAmbient(),
                    pair.getFirst().shouldShowParticles(),
                    pair.getSecond()
            );
            foodEffects.add(foodEffect);
        }
        // Mixin to all subclasses where these might change, so I don't have to manually do it all here :D
        return new FoodPropertiesComponent(
                foodComponent.getHunger(),
                foodComponent.getSaturationModifier(),
                foodComponent.isSnack() ? 16 : 32,
                new FoodBehaviour(
                        foodComponent.isMeat()
                        ,foodComponent.isAlwaysEdible(),
                        foodComponent.isSnack(),
                        false,
                        false
                ),
                Optional.empty(),
                foodEffects
        );
    }

    static {
        CODEC = RecordCodecBuilder.create((instance) -> {
           return instance.group(
                   Codec.INT.fieldOf("hunger").forGetter(FoodPropertiesComponent::hunger),
                   Codec.FLOAT.optionalFieldOf("saturation_modifier", 1.0f).forGetter(FoodPropertiesComponent::saturationModifier),
                   Codec.INT.optionalFieldOf("time_to_eat", 32).forGetter(FoodPropertiesComponent::timeToEat),
                   FoodBehaviour.CODEC.optionalFieldOf( "food_behaviour", FoodBehaviour.DEFAULT).forGetter(FoodPropertiesComponent::behaviour),
                   ItemStack.CODEC.optionalFieldOf("result_item").forGetter(FoodPropertiesComponent::resultItem),
                   Codecs.createStrictOptionalFieldCodec(FoodEffect.CODEC.listOf(), "custom_effects", List.of()).forGetter(FoodPropertiesComponent::statusEffects)

           ).apply(instance, FoodPropertiesComponent::new);
        });

        PACKET_CODEC = PacketCodec.tuple(
                PacketCodecs.VAR_INT, FoodPropertiesComponent::hunger,
                PacketCodecs.FLOAT, FoodPropertiesComponent::saturationModifier,
                PacketCodecs.VAR_INT, FoodPropertiesComponent::timeToEat,
                FoodBehaviour.PACKET_CODEC, FoodPropertiesComponent::behaviour,
                ItemStack.PACKET_CODEC.collect(PacketCodecs::optional), FoodPropertiesComponent::resultItem,
                FoodEffect.PACKET_CODEC.collect(PacketCodecs.toList()), FoodPropertiesComponent::statusEffects,
                FoodPropertiesComponent::new
        );
    }
    public record FoodBehaviour(boolean isMeat, boolean alwaysEdible, boolean isSnack, boolean isDrink, boolean clearsEffects) {
        public static final Codec<FoodBehaviour> CODEC;
        public static final PacketCodec<RegistryByteBuf, FoodBehaviour> PACKET_CODEC;
        public static final FoodBehaviour DEFAULT = new FoodBehaviour(false, false, false, false, false);

        public UseAction getUseAction() {
            System.out.println(isDrink);
            return this.isDrink ? UseAction.DRINK : UseAction.EAT;
        }

        static {
            CODEC = RecordCodecBuilder.create(instance -> {
               return instance.group(
                       Codec.BOOL.optionalFieldOf("is_meat", false).forGetter(FoodBehaviour::isMeat),
                       Codec.BOOL.optionalFieldOf("always_edible", false).forGetter(FoodBehaviour::alwaysEdible),
                       Codec.BOOL.optionalFieldOf("is_snack", false).forGetter(FoodBehaviour::isSnack),
                       Codec.BOOL.optionalFieldOf("is_drink", false).forGetter(FoodBehaviour::isDrink),
                       Codec.BOOL.optionalFieldOf("clear_effects", false).forGetter(FoodBehaviour::clearsEffects)
               ).apply(instance, FoodBehaviour::new);
            });

            PACKET_CODEC = PacketCodec.tuple(
                    PacketCodecs.BOOL, FoodBehaviour::isMeat,
                    PacketCodecs.BOOL, FoodBehaviour::alwaysEdible,
                    PacketCodecs.BOOL, FoodBehaviour::isSnack,
                    PacketCodecs.BOOL, FoodBehaviour::isDrink,
                    PacketCodecs.BOOL, FoodBehaviour::clearsEffects,
                    FoodBehaviour::new
            );
        }
    }
    public record FoodEffect(RegistryEntry<StatusEffect> effect, int duration, int amplifier, boolean ambient, boolean showParticles, float chance) {
        public static final Codec<FoodEffect> CODEC;
        public static final PacketCodec<RegistryByteBuf, FoodEffect> PACKET_CODEC;
        public StatusEffectInstance createStatusEffectInstance() {
            return new StatusEffectInstance(this.effect, this.duration, this.amplifier, this.ambient, !this.showParticles, this.showParticles);
        }
        static {
            CODEC = RecordCodecBuilder.create((instance) -> {
                return instance.group(
                        Registries.STATUS_EFFECT.getEntryCodec().fieldOf("id").forGetter(FoodEffect::effect),
                        Codec.INT.optionalFieldOf("duration", 160).forGetter(FoodEffect::duration),
                        Codec.INT.optionalFieldOf("amplifier", 0).forGetter(FoodEffect::amplifier),
                        Codec.BOOL.optionalFieldOf("ambient", false).forGetter(FoodEffect::ambient),
                        Codec.BOOL.optionalFieldOf("show_particles", true).forGetter(FoodEffect::showParticles),
                        Codec.FLOAT.optionalFieldOf("chance", 1.0f).forGetter(FoodEffect::chance)
                ).apply(instance, FoodEffect::new);
            });
            PACKET_CODEC = PacketCodec.tuple(
                    PacketCodecs.registryEntry(RegistryKeys.STATUS_EFFECT), FoodEffect::effect,
                    PacketCodecs.VAR_INT, FoodEffect::duration,
                    PacketCodecs.VAR_INT, FoodEffect::amplifier,
                    PacketCodecs.BOOL, FoodEffect::ambient,
                    PacketCodecs.BOOL, FoodEffect::showParticles,
                    PacketCodecs.FLOAT, FoodEffect::chance,
                    FoodEffect::new
            );
        }
    }
}
