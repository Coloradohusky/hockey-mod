package net.fabricmc.example;

import io.github.cottonmc.cotton.gui.SyncedGuiDescription;
import io.github.cottonmc.cotton.gui.client.BackgroundPainter;
import io.github.cottonmc.cotton.gui.networking.NetworkSide;
import io.github.cottonmc.cotton.gui.networking.ScreenNetworking;
import io.github.cottonmc.cotton.gui.widget.*;
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import io.github.cottonmc.cotton.gui.widget.icon.ItemIcon;
import io.github.cottonmc.cotton.gui.widget.icon.TextureIcon;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;
import net.minidev.json.parser.ParseException;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;

public class HockeyGuiDescription extends SyncedGuiDescription {
    private static final int INVENTORY_SIZE = 1;
    private static final Identifier TEAM_NAME = new Identifier("tutorial", "team_name");

    public HockeyGuiDescription(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(ExampleMod.SCREEN_HANDLER_TYPE, syncId, playerInventory, getBlockInventory(context, INVENTORY_SIZE), getBlockPropertyDelegate(context));

        WPlainPanel root = new WPlainPanel();
        setRootPanel(root);
        root.setSize(300, 200);
        root.setInsets(Insets.ROOT_PANEL);
        root.add(this.createPlayerInventoryPanel(), 0, 40*3);
        WLabel mainTitle = new WLabel(new LiteralText("Creator"));
        mainTitle.setHorizontalAlignment(HorizontalAlignment.RIGHT);
        root.add(mainTitle, 65*3, 0);

        ArrayList fullData = null;
        try {
            fullData = JerseyJSONParser.getData();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Set<String> testSet = new LinkedHashSet<String>();
        for (Object i : fullData) {
            String test = (String) i;
            String[] splitTest = test.split("/");
            testSet.add(splitTest[0] + "/" + splitTest[1]);
        }
        List<String> onlyTeams = new ArrayList<String>();
        onlyTeams.addAll(testSet);

        Set<String> yetAnotherTestSet = new LinkedHashSet<String>();
        for (Object i : onlyTeams) {
            String test = (String) i;
            String[] splitTest = test.split("/");
            yetAnotherTestSet.add(splitTest[0]);
        }
        List<String> onlyLeagues = new ArrayList<String>();
        onlyLeagues.addAll(yetAnotherTestSet);

        //league/team/jersey/*.png
        WTabPanel leagueTabs = new WTabPanel();
        for (Object league: onlyLeagues) {
            List<String> currentLeagueTeams = new ArrayList<String>();
            for (Object team : onlyTeams) {
                String currentTeamLeague = ((String)team).split("/")[0];
                if ( currentTeamLeague.equals(league) ) {
                    currentLeagueTeams.add((String)team);
                }
            }
            BiConsumer<String, HockeyList> configurator = (String fullName, HockeyList destination) -> {
                System.out.println(fullName);
                String convertedText = fullName.toLowerCase().replace(" ", "_");
                destination.button.setLabel(new LiteralText(fullName.split("/")[1]));
                destination.button.setIcon(new TextureIcon(new Identifier("tutorial:textures/item/" + convertedText + "/home/" + convertedText.split("/")[1] + "_jersey.png")));
                destination.button.setOnClick(() -> {
                    ScreenNetworking.of(this, NetworkSide.CLIENT).send(TEAM_NAME, buf -> buf.writeString(fullName));
                });
            };

            WListPanel<String, HockeyList> list = new WListPanel<>(currentLeagueTeams, HockeyList::new, configurator);
            list.setListItemHeight(24);
            list.setSize(165, 85);
            list.setLocation(0, 5);
            leagueTabs.add(list, tab -> tab.icon(new ItemIcon(new ItemStack(Items.APPLE))));
        }
        root.add(leagueTabs, 0, 0, 55, 32);


        ArrayList finalFullData = fullData;
        /*
        Is supposed to change reactorHockeyPanel based on the team that is clicked.
        Receives TEAM_NAME, sets reactorNames as it should, but reactorHockeyPanel doesn't show up
        */
        ScreenNetworking.of(this, NetworkSide.SERVER).receive(TEAM_NAME, buf -> {
            BiConsumer<String, ReactorHockeyList> reactorConfigurator = (String fullName, ReactorHockeyList destination) -> {
                System.out.println(fullName);
                String convertedText = fullName.toLowerCase().replace(" ", "_");
                String[] splitName = fullName.split("/");
                String[] splitConvertedName = convertedText.split("/");
                String texturePath = "tutorial:textures/item/" + splitConvertedName[0] + "/" + splitConvertedName[1] + "/" + splitConvertedName[2] + "/" + splitConvertedName[1] + "_jersey.png";
                destination.button.setLabel(new LiteralText(splitName[2]));
                destination.button.setIcon(new TextureIcon(new Identifier(texturePath)));
                destination.button.setOnClick(() -> {
                    System.out.println(texturePath);
                });
            };
            List<String> reactorNames = new ArrayList<String>();
            Set<String> tempReactorNames = new LinkedHashSet<String>();
            tempReactorNames.addAll(finalFullData);
            String teamName = buf.readString(100);
            tempReactorNames.removeIf(s -> !s.contains(teamName));
            reactorNames.addAll(tempReactorNames);
            System.out.println(teamName);
            System.out.println(reactorNames);
            WListPanel<String, ReactorHockeyList> reactorHockeyPanel = new WListPanel<>(reactorNames, ReactorHockeyList::new, reactorConfigurator);
            reactorHockeyPanel.setSize(150, 85);
            root.add(reactorHockeyPanel, 180, 20, 100, 64);
        });

        root.validate(this);
    }
}
