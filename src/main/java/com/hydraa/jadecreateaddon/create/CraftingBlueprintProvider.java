package com.hydraa.jadecreateaddon.create;

import com.hydraa.jadecreateaddon.mixin.BlueprintOverlayRendererAccess;
import com.zurrtum.create.content.equipment.blueprint.BlueprintEntity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;

import org.jspecify.annotations.Nullable;

import snownee.jade.api.Accessor;
import snownee.jade.api.EntityAccessor;
import snownee.jade.api.IEntityComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.view.ClientViewGroup;
import snownee.jade.api.view.IClientExtensionProvider;
import snownee.jade.api.view.IServerExtensionProvider;
import snownee.jade.api.view.ItemView;
import snownee.jade.api.view.ViewGroup;
import snownee.jade.api.ui.Element;
import snownee.jade.api.ui.JadeUI;

import java.util.List;

@Environment(EnvType.CLIENT)
public enum CraftingBlueprintProvider implements IEntityComponentProvider,
        IServerExtensionProvider<ItemStack>,
        IClientExtensionProvider<ItemStack, ItemView> {
    INSTANCE;

    public static List<ItemStack> getResults() {
        List<ItemStack> results = BlueprintOverlayRendererAccess.getResults();
        return results == null ? List.of() : results;
    }

    @Override
    public void appendTooltip(@Nullable ITooltip tooltip, EntityAccessor accessor, @Nullable IPluginConfig config) {
    }

    @Override
    public @Nullable Identifier getUid() {
        return CreatePlugin.CRAFTING_BLUEPRINT;
    }

    @Override
    public boolean isRequired() {
        return true;
    }

    @Override
    public List<ClientViewGroup<ItemView>> getClientGroups(Accessor<?> accessor, List<ViewGroup<ItemStack>> groups) {
        return ClientViewGroup.map(groups, ItemView::new, null);
    }

    @Override
    public @Nullable List<ViewGroup<ItemStack>> getGroups(Accessor<?> accessor) {
        return List.of(new ViewGroup<>(getResults()));
    }

    @Override
    public @Nullable Element getIcon(EntityAccessor accessor, @Nullable IPluginConfig config, @Nullable Element currentIcon) {
        List<ItemStack> results = getResults();
        if (!results.isEmpty()) {
            return JadeUI.item(results.getFirst());
        }
        return currentIcon;
    }
}