package trinsdar.ic2c_extras.blocks;

import java.util.Collections;
import java.util.List;

import com.zuxelus.zlib.blocks.FacingBlock;
import com.zuxelus.zlib.tileentities.TileEntityFacing;
import com.zuxelus.zlib.tileentities.TileEntityInventory;

import ic2.api.item.IC2Items;
import trinsdar.ic2c_extras.tileentity.TileEntityCesu;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockCesu extends FacingBlock {

	@Override
	protected TileEntityFacing createTileEntity() {
		return new TileEntityCesu();
	}

	@Override
	protected int getBlockGuiId() {
		return 14;
	}

	@Override
	public boolean canProvidePower(IBlockState state) {
		return true;
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		NBTTagCompound tag = stack.getTagCompound();
		if (tag == null)
			return;
		double energy = tag.getDouble("energy");
		TileEntity te = world.getTileEntity(pos);
		if (!(te instanceof TileEntityCesu) || energy == 0)
			return;
		((TileEntityCesu) te).setEnergy(energy);
	}

	@Override
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos) {
		if (!world.isRemote)
			world.notifyBlockUpdate(pos, state, state, 2);
	}

	@Override
	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		drops.add(IC2Items.getItem("te", "mfsu"));
	}

	@Override
	public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
		TileEntity te = blockAccess.getTileEntity(pos);
		if (!(te instanceof TileEntityCesu))
			return 0;
		return ((TileEntityCesu) te).getPowered() ? 15 : 0;
	}

	@Override
	public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> items) {
		items.add(getStackwithEnergy(0));
		items.add(getStackwithEnergy(TileEntityCesu.CAPACITY));
	}

	private ItemStack getStackwithEnergy(double energy) {
		ItemStack stack = new ItemStack(this);
		NBTTagCompound tag = new NBTTagCompound();
		stack.setTagCompound(tag);
		tag.setDouble("energy", energy);
		return stack;
	}

	//IWrenchable
	@Override
	public List<ItemStack> getWrenchDrops(World world, BlockPos pos, IBlockState state, TileEntity te, EntityPlayer player, int fortune) {
		if (!(te instanceof TileEntityCesu))
			return Collections.emptyList();
		List<ItemStack> list = ((TileEntityInventory) te).getDrops(fortune);
		list.add(getStackwithEnergy(((TileEntityCesu) te).getEnergy() * 0.8D));
		return list;
	}
}