package com.hydraa.jadecreateaddon.mixin;

import com.zurrtum.create.content.kinetics.base.KineticBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(KineticBlockEntity.class)
public interface KineticBlockEntityAccess {
    @Accessor(value = "stress", remap = false)
    float getStress();

    @Accessor(value = "capacity", remap = false)
    float getCapacity();
}