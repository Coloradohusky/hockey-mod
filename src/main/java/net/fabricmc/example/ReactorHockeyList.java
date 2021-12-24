package net.fabricmc.example;

import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import io.github.cottonmc.cotton.gui.widget.WButton;
import net.minecraft.text.LiteralText;

public class ReactorHockeyList extends WPlainPanel {
    WButton button;
    public ReactorHockeyList() {
        button = new WButton(new LiteralText("foo"));
        button.setOnClick(() -> {
            System.out.println(button.getWidth());
            System.out.println(button.getHeight());
        });
        this.add(button, 0, 0, 75, 18);
    }
}
