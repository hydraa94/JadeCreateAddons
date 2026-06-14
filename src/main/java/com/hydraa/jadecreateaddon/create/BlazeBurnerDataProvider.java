package com.hydraa.jadecreateaddon.create;

import com.zurrtum.create.content.processing.burner.BlazeBurnerBlockEntity;
import com.zurrtum.create.content.processing.burner.BlazeBurnerBlockEntity.FuelType;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;

import org.jspecify.annotations.Nullable;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IServerDataProvider;

public enum BlazeBurnerDataProvider implements IServerDataProvider<BlockAccessor> {
    INSTANCE;

    @Override
    public void appendServerData(@Nullable CompoundTag data, BlockAccessor accessor) {
        if (data == null) return;
        if (!(accessor.getBlockEntity() instanceof BlazeBurnerBlockEntity burner)) return;
        if (burner.isCreative()) {
            data.putBoolean("isCreative", true);
        } else if (burner.getActiveFuel() != FuelType.NONE) {
            data.putInt("fuelLevel", burner.getActiveFuel().ordinal());
            data.putInt("burnTimeRemaining", burner.getRemainingBurnTime());
        }
    }

    @Override
    public @Nullable Identifier getUid() {
        return CreatePlugin.BLAZE_BURNER;
    }
}