package thetadev.constructionwand.job;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import thetadev.constructionwand.basics.ConfigHandler;
import thetadev.constructionwand.basics.WandUtil;
import thetadev.constructionwand.basics.options.EnumMode;

public class AngelJob extends WandJob
{
	public AngelJob(PlayerEntity player, World world, ItemStack wand) {
		super(player, world, new BlockRayTraceResult(player.getLookVec(), fromVector(player.getLookVec()), player.getPosition(), false), wand);
	}

	private static Direction fromVector(Vec3d vector) {
		return Direction.getFacingFromVector(vector.x, vector.y, vector.z);
	}

	@Override
	protected void getBlockPositionList() {
		if(options.getOption(EnumMode.DEFAULT) != EnumMode.ANGEL || wandItem.angelDistance == 0) return;

		if(!player.isCreative() && !ConfigHandler.ANGEL_FALLING.get() && player.fallDistance > 10) return;

		Vec3d playerVec = WandUtil.entityPositionVec(player);
		Vec3d lookVec = player.getLookVec().mul(2, 2, 2);
		Vec3d placeVec = playerVec.add(lookVec);

		BlockPos currentPos = new BlockPos(placeVec);

		if(canPlace(currentPos)) {
			placeSnapshots.add(new PlaceSnapshot(currentPos, placeItem.getBlock().getDefaultState()));
		}
	}
}
