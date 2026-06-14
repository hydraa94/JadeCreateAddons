package com.hydraa.jadecreateaddon.mixin;

import com.zurrtum.create.content.equipment.armor.BacktankBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BacktankBlockEntity.class)
public interface BacktankBlockEntityAccess {
    @Accessor(value = "capacityEnchantLevel", remap = false)
    int getCapacityEnchantLevel();
}