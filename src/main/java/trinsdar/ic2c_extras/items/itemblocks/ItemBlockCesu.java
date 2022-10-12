package trinsdar.ic2c_extras.items.itemblocks;

import com.zuxelus.energycontrol.api.ItemStackHelper;
import trinsdar.ic2c_extras.tileentity.TileEntityCesu;
import ic2.api.energy.EnergyNet;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemBlockCesu extends ItemBlock {

	public ItemBlockCesu(Block block) {
		super(block);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag) {
		super.addInformation(stack, world, tooltip, flag);
		tooltip.add(I18n.format("ic2.item.tooltip.PowerTier",TileEntityCesu.TIER));
		tooltip.add(String.format("%s %.0f %s %s %d M %s", I18n.format("ic2.item.tooltip.Output"), EnergyNet.instance.getPowerFromTier(TileEntityCesu.TIER),
				I18n.format("ic2.generic.text.EUt"), I18n.format("ic2.item.tooltip.Capacity"), TileEntityCesu.CAPACITY / 1000000, I18n.format("ic2.generic.text.EU")));
		tooltip.add(I18n.format("ic2.item.tooltip.Store") + " " + (long) ItemStackHelper.getTagCompound(stack).getDouble("energy") + " " + I18n.format("ic2.generic.text.EU"));
	}

	@Override
	public EnumRarity getRarity(ItemStack stack) {
		return EnumRarity.COMMON;
	}

	@Override
	public void onCreated(ItemStack stack, World world, EntityPlayer player) {
		NBTTagCompound tag = new NBTTagCompound();
		stack.setTagCompound(tag);
		tag.setDouble("energy", 0);
	}
}