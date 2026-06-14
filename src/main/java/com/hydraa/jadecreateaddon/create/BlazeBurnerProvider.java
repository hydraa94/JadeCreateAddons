package com.hydraa.jadecreateaddon.create;

import com.zurrtum.create.content.processing.basin.BasinBlockEntity;
import com.zurrtum.create.content.processing.burner.BlazeBurnerBlock;
import com.zurrtum.create.content.processing.burner.BlazeBurnerBlock.HeatLevel;
import com.zurrtum.create.content.processing.burner.BlazeBurnerBlockEntity;
import com.zurrtum.create.content.processing.burner.BlazeBurnerBlockEntity.FuelType;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import org.jspecify.annotations.Nullable;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.theme.IThemeHelper;
import snownee.jade.api.ui.JadeUI;

@Environment(EnvType.CLIENT)
public enum BlazeBurnerProvider implements IBlockComponentProvider {
    INSTANCE;

    @Override
    public void appendTooltip(@Nullable ITooltip tooltip, BlockAccessor accessor, @Nullable IPluginConfig config) {
        if (tooltip == null) return;
        CompoundTag data = accessor.getServerData();

        FuelType activeFuel = FuelType.NONE;
        boolean isCreative = data.getBoolean("isCreative").orElse(false);
        if (isCreative) {
            HeatLevel heatLevel = BasinBlockEntity.getHeatLevelOf(accessor.getBlockState());
            if (heatLevel == HeatLevel.SEETHING) {
                activeFuel = FuelType.SPECIAL;
            } else if (heatLevel != HeatLevel.NONE) {
                activeFuel = FuelType.NORMAL;
            }
        } else {
            activeFuel = FuelType.values()[data.getInt("fuelLevel").orElse(0)];
        }
        if (activeFuel == FuelType.NONE) return;

        ItemStack item = new ItemStack(activeFuel == FuelType.SPECIAL ? Items.SOUL_CAMPFIRE : Items.CAMPFIRE);
        tooltip.add(JadeUI.smallItem(item));
        if (isCreative) {
            tooltip.append(IThemeHelper.get().info(Component.translatable("jade.infinity")));
        } else {
            int ticks = data.getInt("burnTimeRemaining").orElse(0);
            int totalSeconds = ticks/20;
            int minutes = totalSeconds / 60;
            int seconds = totalSeconds % 60;
            tooltip.append(IThemeHelper.get().info(Component.literal(String.format("%d:%02d", minutes, seconds))));
        }
    }

    @Override
    public Identifier getUid() {
        return CreatePlugin.BLAZE_BURNER;
    }
}