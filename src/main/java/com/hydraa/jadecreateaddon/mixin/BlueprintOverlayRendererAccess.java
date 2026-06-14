package com.hydraa.jadecreateaddon.mixin;

import com.zurrtum.create.client.content.equipment.blueprint.BlueprintOverlayRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import net.minecraft.world.item.ItemStack;
import java.util.List;

@Mixin(BlueprintOverlayRenderer.class)
public interface BlueprintOverlayRendererAccess {
    @Accessor(value = "results", remap = false)
    static List<ItemStack> getResults() {
        throw new AssertionError();
    }
}