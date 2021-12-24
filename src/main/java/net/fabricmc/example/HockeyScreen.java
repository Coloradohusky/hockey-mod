package net.fabricmc.example;

import io.github.cottonmc.cotton.gui.client.CottonInventoryScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

public class HockeyScreen extends CottonInventoryScreen<HockeyGuiDescription> {
    public HockeyScreen(HockeyGuiDescription gui, PlayerEntity player) {
        super(gui, player);
    }
}
