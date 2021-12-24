package net.fabricmc.example;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.*;
import net.minecraft.text.LiteralText;
import net.minecraft.util.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.apache.logging.log4j.*;

public class ExampleMod implements ModInitializer {

	public static BlockEntityType<HockeyBlockEntity> HOCKEY_BLOCK_ENTITY;
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LogManager.getLogger("modid");

	public class SeattleKrakenMaterial implements ArmorMaterial {
		private static final int[] BASE_DURABILITY = new int[] {13, 15, 16, 11};
		private static final int[] PROTECTION_VALUES = new int[] {1, 2, 3, 1};
		@Override
		public int getDurability(EquipmentSlot slot) {
			return BASE_DURABILITY[slot.getEntitySlotId()] * 5;
		}
		@Override
		public int getProtectionAmount(EquipmentSlot slot) {
			return PROTECTION_VALUES[slot.getEntitySlotId()];
		}
		@Override
		public int getEnchantability() {
			return 1;
		}
		@Override
		public SoundEvent getEquipSound() {
			return SoundEvents.ITEM_ARMOR_EQUIP_LEATHER;
		}
		@Override
		public Ingredient getRepairIngredient() {
			return Ingredient.ofItems(Items.LEATHER);
		}
		@Override
		public String getName() {
			// Must be all lowercase
			return "seattle_kraken";
		}
		@Override
		public float getToughness() {
			return 1.0F;
		}
		@Override
		public float getKnockbackResistance() {
			return 0;
		}
	}

	public class HockeyBlockEntity extends BlockEntity {
		public HockeyBlockEntity(BlockPos pos, BlockState state) {
			super(ExampleMod.HOCKEY_BLOCK_ENTITY, pos, state);
		}
	}

	public class HockeyBlock extends Block implements BlockEntityProvider {
		public HockeyBlock(Settings settings) {
			super(settings);
		}

		@Override
		public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
			if (!world.isClient) {
				player.sendMessage(new LiteralText("Hello, world!"), false);
			}

			return ActionResult.SUCCESS;
		}

		@Override
		public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
			return new HockeyBlockEntity(pos, state);
		}
	}

	public final Block HOCKEY_BLOCK = new HockeyBlock(AbstractBlock.Settings.of(Material.METAL));

	public final ArmorMaterial SEATTLE_KRAKEN_MATERIAL = new SeattleKrakenMaterial();
	public final Item SEATTLE_KRAKEN_HELMET = new ArmorItem(SEATTLE_KRAKEN_MATERIAL, EquipmentSlot.HEAD, new Item.Settings());
	public final Item SEATTLE_KRAKEN_JERSEY = new ArmorItem(SEATTLE_KRAKEN_MATERIAL, EquipmentSlot.CHEST, new Item.Settings());
	public final Item SEATTLE_KRAKEN_PANTS = new ArmorItem(SEATTLE_KRAKEN_MATERIAL, EquipmentSlot.LEGS, new Item.Settings());
	//all hockey skates are the same, using ones from Kraken textures as default
	public final Item HOCKEY_BOOTS = new ArmorItem(SEATTLE_KRAKEN_MATERIAL, EquipmentSlot.FEET, new Item.Settings());


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
		LOGGER.info("Hello Fabric world!");
	}
}
