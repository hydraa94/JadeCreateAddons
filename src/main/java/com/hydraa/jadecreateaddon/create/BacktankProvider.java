package com.hydraa.jadecreateaddon.create;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

import org.jspecify.annotations.Nullable;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;


@Environment(EnvType.CLIENT)
public enum BacktankProvider implements IBlockComponentProvider {
    INSTANCE;

    //Air level helper
    private static String formatAir(int air) {
        int minutes = air / 60;
        int seconds = air % 60;
        return String.format("%d:%02d", minutes, seconds);
    }

    @Override
    public void appendTooltip(@Nullable ITooltip tooltip, BlockAccessor accessor, @Nullable IPluginConfig config) {
        if (tooltip == null) return;
        CompoundTag data = accessor.getServerData();

        int airLevel = data.getInt("airLevel").orElse(0);
        int maxAir = data.getInt("maxAir").orElse(0);
        if (maxAir == 0) return;

        tooltip.add(Component.translatable("jade_create_addon.backtank_air",
                formatAir(airLevel),
                formatAir(maxAir)));
    }

    @Override
    public @Nullable Identifier getUid() {
        return CreatePlugin.BACKTANK_CAPACITY;
    }
}