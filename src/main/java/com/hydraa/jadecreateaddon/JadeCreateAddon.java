package com.hydraa.jadecreateaddon;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JadeCreateAddon implements ModInitializer {
    public static final String MOD_ID = "jade-create-addon";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        String version = net.fabricmc.loader.api.FabricLoader.getInstance()
                .getModContainer(MOD_ID)
                .map(mod -> mod.getMetadata().getVersion().getFriendlyString())
                .orElse("unknown");

        LOGGER.info("[Jade Create Addon] v{} initialized.", version);
    }
}