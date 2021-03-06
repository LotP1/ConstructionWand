package thetadev.constructionwand.basics;

import net.minecraft.item.Item;
import net.minecraft.item.ItemTier;
import net.minecraftforge.common.ForgeConfigSpec;
import thetadev.constructionwand.items.ModItems;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ConfigServer
{
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

	public static final ForgeConfigSpec.IntValue LIMIT_CREATIVE;
	public static final ForgeConfigSpec.IntValue MAX_RANGE;
	public static final ForgeConfigSpec.IntValue UNDO_HISTORY;
	public static final ForgeConfigSpec.BooleanValue ANGEL_FALLING;

	public static final ForgeConfigSpec.ConfigValue<List<?>> SIMILAR_BLOCKS;
	private static final String[] SIMILAR_BLOCKS_DEFAULT = {
			"minecraft:dirt;minecraft:grass_block;minecraft:coarse_dirt;minecraft:podzol;minecraft:mycelium;minecraft:farmland;minecraft:grass_path"
	};

	public static final ForgeConfigSpec.BooleanValue TE_WHITELIST;
	public static final ForgeConfigSpec.ConfigValue<List<?>> TE_LIST;
	private static final String[] TE_LIST_DEFAULT = {"chiselsandbits"};

	private static final HashMap<Item, WandProperties> wandProperties = new HashMap<>();

	public static WandProperties getWandProperties(Item wand) {
		return wandProperties.getOrDefault(wand, WandProperties.DEFAULT);
	}

	public static class WandProperties
	{
		public static final WandProperties DEFAULT = new WandProperties(null, null, null);

		private final ForgeConfigSpec.IntValue durability;
		private final ForgeConfigSpec.IntValue limit;
		private final ForgeConfigSpec.IntValue angel;

		private WandProperties(ForgeConfigSpec.IntValue durability, ForgeConfigSpec.IntValue limit, ForgeConfigSpec.IntValue angel) {
			this.durability = durability;
			this.limit = limit;
			this.angel = angel;
		}

		public WandProperties(ForgeConfigSpec.Builder builder, Item wand, int defDurability, int defLimit, int defAngel) {
			builder.push(wand.getRegistryName().getPath());

			if(defDurability > 0) {
				builder.comment("Wand durability");
				durability = builder.defineInRange("durability", defDurability, 1, Integer.MAX_VALUE);
			}
			else durability = null;
			builder.comment("Wand block limit");
			limit = builder.defineInRange("limit", defLimit, 1, Integer.MAX_VALUE);
			builder.comment("Max placement distance with angel mode (0 to disable angel mode)");
			angel = builder.defineInRange("angel", defAngel, 0, Integer.MAX_VALUE);
			builder.pop();

			wandProperties.put(wand, this);
		}

		public int getDurability() {
			return durability == null ? -1 : durability.get();
		}
		public int getLimit() {
			return limit == null ? 0 : limit.get();
		}
		public int getAngel() {
			return angel == null ? 0 : angel.get();
		}
	}

	static {
		BUILDER.comment("This is the Server config for ConstructionWand.",
				"If you're not familiar with Forge's new split client/server config, let me explain:",
				"Client config is stored in the /config folder and only contains client specific settings like graphics and keybinds.",
				"Mod behavior is configured in the Server config, which is world-specific and thus located",
				"in the /saves/myworld/serverconfig folder. If you want to change the serverconfig for all",
				"new worlds, copy the config files in the /defaultconfigs folder.");

		new WandProperties(BUILDER, ModItems.WAND_STONE, ItemTier.STONE.getMaxUses(), 9, 0);
		new WandProperties(BUILDER, ModItems.WAND_IRON, ItemTier.IRON.getMaxUses(), 27, 1);
		new WandProperties(BUILDER, ModItems.WAND_DIAMOND, ItemTier.DIAMOND.getMaxUses(), 128, 4);
		new WandProperties(BUILDER, ModItems.WAND_INFINITY, 0, 1024, 8);

		BUILDER.push("misc");
		BUILDER.comment("Block limit for Infinity Wand used in creative mode");
		LIMIT_CREATIVE = BUILDER.defineInRange("InfinityWandCreative", 2048, 1, Integer.MAX_VALUE);
		BUILDER.comment("Maximum placement range (0: unlimited). Affects all wands and is meant for lag prevention, not game balancing.");
		MAX_RANGE = BUILDER.defineInRange("MaxRange", 256, 0, Integer.MAX_VALUE);
		BUILDER.comment("Number of operations that can be undone");
		UNDO_HISTORY = BUILDER.defineInRange("UndoHistory", 3, 0, Integer.MAX_VALUE);
		BUILDER.comment("Place blocks below you while falling > 10 blocks with angel mode (Can be used to save you from drops/the void)");
		ANGEL_FALLING = BUILDER.define("AngelFalling", false);
		BUILDER.comment("Blocks to treat equally when in Similar mode. Enter block IDs seperated by ;");
		SIMILAR_BLOCKS = BUILDER.defineList("SimilarBlocks", Arrays.asList(SIMILAR_BLOCKS_DEFAULT), obj -> true);
		BUILDER.pop();

		BUILDER.push("tileentity");
		BUILDER.comment("White/Blacklist for Tile Entities. Allow/Prevent blocks with TEs from being placed by wand.",
				"You can either add block ids like minecraft:chest or mod ids like minecraft");
		TE_LIST = BUILDER.defineList("TEList", Arrays.asList(TE_LIST_DEFAULT), obj -> true);
		BUILDER.comment("If set to TRUE, treat TEList as a whitelist, otherwise blacklist");
		TE_WHITELIST = BUILDER.define("TEWhitelist", false);
		BUILDER.pop();
	}

	public static final ForgeConfigSpec SPEC = BUILDER.build();
}
