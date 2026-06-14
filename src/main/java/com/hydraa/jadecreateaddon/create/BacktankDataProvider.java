package com.hydraa.jadecreateaddon.create;

import com.hydraa.jadecreateaddon.mixin.BacktankBlockEntityAccess;
import com.zurrtum.create.content.equipment.armor.BacktankBlockEntity;
import com.zurrtum.create.content.equipment.armor.BacktankUtil;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;

import org.jspecify.annotations.Nullable;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IServerDataProvider;

public enum BacktankDataProvider implements IServerDataProvider<BlockAccessor> {
    INSTANCE;

    @Override
    public void appendServerData(@Nullable CompoundTag data, BlockAccessor accessor) {
        if (data == null) return;
        if (!(accessor.getBlockEntity() instanceof BacktankBlockEntity backtank)) return;

        int airLevel = backtank.airLevel;
        int maxAir = BacktankUtil.maxAir(((BacktankBlockEntityAccess) backtank).getCapacityEnchantLevel());

        data.putInt("airLevel", airLevel);
        data.putInt("maxAir", maxAir);
    }

    @Override
    public @Nullable Identifier getUid() {
        return CreatePlugin.BACKTANK_CAPACITY;
    }
}