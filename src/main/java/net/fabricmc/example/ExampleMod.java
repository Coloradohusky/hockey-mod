package net.fabricmc.example;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ExampleMod implements ModInitializer {

	public static ScreenHandlerType<HockeyGuiDescription> SCREEN_HANDLER_TYPE;
	public static BlockEntityType<HockeyBlockEntity> HOCKEY_BLOCK_ENTITY;
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LogManager.getLogger("modid");

	public static final String MOD_ID = "example";
	public static final Identifier HOCKEY = new Identifier(MOD_ID, "hockey_block");

	public final Block HOCKEY_BLOCK = new HockeyBlock(AbstractBlock.Settings.of(Material.METAL));

	public final ArmorMaterial SEATTLE_KRAKEN_MATERIAL = new SeattleKrakenMaterial();
	public final Item SEATTLE_KRAKEN_HELMET = new ArmorItem(SEATTLE_KRAKEN_MATERIAL, EquipmentSlot.HEAD, new Item.Settings());
	public final Item SEATTLE_KRAKEN_JERSEY = new ArmorItem(SEATTLE_KRAKEN_MATERIAL, EquipmentSlot.CHEST, new Item.Settings());
	public final Item SEATTLE_KRAKEN_PANTS = new ArmorItem(SEATTLE_KRAKEN_MATERIAL, EquipmentSlot.LEGS, new Item.Settings());
	//all hockey skates are the same, using ones from Kraken textures as default
	public final Item HOCKEY_BOOTS = new ArmorItem(SEATTLE_KRAKEN_MATERIAL, EquipmentSlot.FEET, new Item.Settings());


	public static final ScreenHandlerType<HockeyScreenHandler> HOCKEY_SCREEN_HANDLER;
	static {
		//We use registerSimple here because our Entity is not an ExtendedScreenHandlerFactory
		//but a NamedScreenHandlerFactory.
		//In a later Tutorial you will see what ExtendedScreenHandlerFactory can do!
		HOCKEY_SCREEN_HANDLER = ScreenHandlerRegistry.registerSimple(HOCKEY, HockeyScreenHandler::new);
	}


	public final ItemGroup ITEM_GROUP = FabricItemGroupBuilder.create(
					new Identifier ("tutorial", "general"))
			.icon(() -> new ItemStack(Items.BOWL))
			.appendItems(stacks -> {
				stacks.add(new ItemStack(HOCKEY_BLOCK));
				stacks.add(new ItemStack(SEATTLE_KRAKEN_HELMET));
				stacks.add(new ItemStack(SEATTLE_KRAKEN_JERSEY));
				stacks.add(new ItemStack(SEATTLE_KRAKEN_PANTS));
				stacks.add(new ItemStack(HOCKEY_BOOTS));
			})
			.build();
	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		Registry.register(Registry.BLOCK, new Identifier("tutorial", "hockey_block"), HOCKEY_BLOCK);
		Registry.register(Registry.ITEM, new Identifier("tutorial", "hockey_block"), new BlockItem(HOCKEY_BLOCK, new FabricItemSettings()));
		Registry.register(Registry.ITEM, new Identifier("tutorial", "seattle_kraken_helmet"), SEATTLE_KRAKEN_HELMET);
		Registry.register(Registry.ITEM, new Identifier("tutorial", "seattle_kraken_jersey"), SEATTLE_KRAKEN_JERSEY);
		Registry.register(Registry.ITEM, new Identifier("tutorial", "seattle_kraken_pants"), SEATTLE_KRAKEN_PANTS);
		Registry.register(Registry.ITEM, new Identifier("tutorial", "hockey_boots"), HOCKEY_BOOTS);

		HOCKEY_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, "tutorial:hockey_block_entity", FabricBlockEntityTypeBuilder.create(HockeyBlockEntity::new, HOCKEY_BLOCK).build(null));

		SCREEN_HANDLER_TYPE = ScreenHandlerRegistry.registerSimple(new Identifier("tutorial:hockey_block"), (syncId, inventory) -> new HockeyGuiDescription(syncId, inventory, ScreenHandlerContext.EMPTY));

		LOGGER.info("Hello Fabric world!");
	}
}
