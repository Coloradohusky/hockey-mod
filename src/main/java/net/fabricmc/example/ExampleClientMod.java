package net.fabricmc.example;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;

@Environment(EnvType.CLIENT)
public class ExampleClientMod implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ScreenRegistry.<HockeyGuiDescription, HockeyScreen>register(ExampleMod.SCREEN_HANDLER_TYPE, (gui, inventory, title) -> new HockeyScreen(gui, inventory.player));
    }
}