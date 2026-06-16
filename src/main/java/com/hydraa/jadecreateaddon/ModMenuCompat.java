package com.hydraa.jadecreateaddon;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.impl.WailaClientRegistration;

public class ModMenuCompat implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> WailaClientRegistration.instance().createPluginConfigScreen(parent, null);
    }
}