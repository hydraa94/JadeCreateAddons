package com.hydraa.jadecreateaddon.create;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.zurrtum.create.content.contraptions.AbstractContraptionEntity;

import net.minecraft.ChatFormatting;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;

import org.jspecify.annotations.Nullable;

import snownee.jade.addon.core.ObjectNameProvider;
import snownee.jade.api.Accessor;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.EntityAccessor;
import snownee.jade.api.IEntityComponentProvider;
import snownee.jade.api.JadeIds;
import snownee.jade.api.TooltipPosition;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.theme.IThemeHelper;
import snownee.jade.api.ui.Element;
import snownee.jade.api.ui.JadeUI;
import snownee.jade.impl.WailaClientRegistration;
import snownee.jade.api.ui.TextElement;

import java.util.concurrent.TimeUnit;

public enum ContraptionExactBlockProvider implements IEntityComponentProvider {
    INSTANCE;

    private final Cache<Entity, Accessor<?>> accessorCache = CacheBuilder.newBuilder()
            .weakKeys()
            .expireAfterAccess(100, TimeUnit.MILLISECONDS)
            .build();

    @Override
    public @Nullable Element getIcon(EntityAccessor accessor, @Nullable IPluginConfig config, @Nullable Element currentIcon) {
        Accessor<?> exact = accessorCache.getIfPresent(accessor.getEntity());
        if (exact == null) return null;
        return WailaClientRegistration.instance().getAccessorHandler(exact.getAccessorType()).getIcon(exact);
    }

    @Override
    public void appendTooltip(@Nullable ITooltip tooltip, EntityAccessor accessor, @Nullable IPluginConfig config) {
        if (tooltip == null || config == null) return;
        Accessor<?> exact = accessorCache.getIfPresent(accessor.getEntity());
        if (exact == null) return;

        ITooltip dummy = JadeUI.tooltip();
        if (exact instanceof BlockAccessor blockAccessor) {
            ObjectNameProvider.ForBlock.INSTANCE.appendTooltip(dummy, blockAccessor, config);
        } else if (exact instanceof EntityAccessor entityAccessor) {
            ObjectNameProvider.ForEntity.INSTANCE.appendTooltip(dummy, entityAccessor, config);
        }

        if (!dummy.isEmpty()) {
            tooltip.remove(JadeIds.CORE_OBJECT_NAME);
            tooltip.add(0, dummy.get(JadeIds.CORE_OBJECT_NAME).stream()
                    .map(e -> e)
                    .toList());
        }
    }

    public void setHit(Entity entity, Accessor<?> accessor) {
        accessorCache.put(entity, accessor);
    }

    @Override
    public @Nullable Identifier getUid() {
        return CreatePlugin.CONTRAPTION_EXACT_BLOCK;
    }

    @Override
    public int getDefaultPriority() {
        return TooltipPosition.HEAD;
    }
}