package com.hydraa.jadecreateaddon.create;

import com.hydraa.jadecreateaddon.mixin.KineticBlockEntityAccess;
import com.zurrtum.create.content.kinetics.base.KineticBlockEntity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;

import org.jspecify.annotations.Nullable;

import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IServerDataProvider;

public enum KineticDataProvider implements IServerDataProvider<BlockAccessor> {
    INSTANCE;

    @Override
    public void appendServerData(@Nullable CompoundTag data, BlockAccessor accessor) {
        if (data == null) return;
        if (!(accessor.getBlockEntity() instanceof KineticBlockEntity kinetic)) return;

        KineticBlockEntityAccess access = (KineticBlockEntityAccess) kinetic;
        data.putInt("rpm", Math.abs((int) kinetic.getSpeed()));
        data.putInt("stress", (int) access.getStress());
        data.putInt("capacity", (int) access.getCapacity());
    }

    @Override
    public @Nullable Identifier getUid() {
        return CreatePlugin.GOGGLES;
    }
}