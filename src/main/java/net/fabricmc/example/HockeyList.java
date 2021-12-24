package net.fabricmc.example;

import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import io.github.cottonmc.cotton.gui.widget.WButton;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;

public class HockeyList extends WPlainPanel {
    private static final Identifier TEAM_NAME = new Identifier("tutorial", "team_name");
    WButton button;
    public HockeyList() {
        button = new WButton(new LiteralText("Foo"));
        this.add(button, 0, 0, 150, 0);
    }
}
