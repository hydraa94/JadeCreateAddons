package com.hydraa.jadecreateaddon.create;

import java.util.Map;
import java.util.LinkedHashMap;

import com.zurrtum.create.content.contraptions.AbstractContraptionEntity;
import com.zurrtum.create.infrastructure.items.CombinedInvWrapper;

import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;

import org.jspecify.annotations.Nullable;

import snownee.jade.api.Accessor;
import snownee.jade.api.view.ClientViewGroup;
import snownee.jade.api.view.IClientExtensionProvider;
import snownee.jade.api.view.IServerExtensionProvider;
import snownee.jade.api.view.ItemView;
import snownee.jade.api.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public enum ContraptionItemStorageProvider implements IServerExtensionProvider<ItemStack>,
        IClientExtensionProvider<ItemStack, ItemView> {
    INSTANCE;

    @Override
    public @Nullable Identifier getUid() {
        return CreatePlugin.CONTRAPTION_INVENTORY;
    }

    @Override
    public @Nullable List<ViewGroup<ItemStack>> getGroups(Accessor<?> accessor) {
        if (!(accessor.getTarget() instanceof AbstractContraptionEntity entity)) return null;
        CombinedInvWrapper items = entity.getContraption().getStorage().getAllItems();

        Map<Item, ItemStack> merged = new LinkedHashMap<>();
        for (int i = 0; i < items.getContainerSize(); i++) {
            ItemStack stack = items.getItem(i);
            if (stack.isEmpty()) continue;
            merged.merge(stack.getItem(), stack.copy(), (a, b) -> {
                a.grow(b.getCount());
                return a;
            });
        }

        if (merged.isEmpty()) return null;
        return List.of(new ViewGroup<>(new ArrayList<>(merged.values())));
    }

    @Override
    public List<ClientViewGroup<ItemView>> getClientGroups(Accessor<?> accessor, List<ViewGroup<ItemStack>> groups) {
        return ClientViewGroup.map(groups, ItemView::new, null);
    }
}