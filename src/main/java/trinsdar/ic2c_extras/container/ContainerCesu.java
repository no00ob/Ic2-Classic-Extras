package trinsdar.ic2c_extras.container;

import com.zuxelus.zlib.containers.ContainerBase;
import com.zuxelus.zlib.containers.slots.SlotArmor;
import com.zuxelus.zlib.containers.slots.SlotChargeable;
import com.zuxelus.zlib.containers.slots.SlotDischargeable;
import com.zuxelus.energycontrol.network.NetworkHelper;
import trinsdar.ic2c_extras.tileentity.TileEntityCesu;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.IContainerListener;

public class ContainerCesu extends ContainerBase<TileEntityCesu> {
	private static final EntityEquipmentSlot[] armorSlots = getArmorSlots();
	private double lastEnergy = -1;

	public ContainerCesu(EntityPlayer player, TileEntityCesu te) {
		super(te);

		addSlotToContainer(new SlotChargeable(te, TileEntityCesu.SLOT_CHARGER, 26, 17));
		addSlotToContainer(new SlotDischargeable(te, TileEntityCesu.SLOT_DISCHARGER, 26, 53, TileEntityCesu.TIER));
		for (int col = 0; col < armorSlots.length; col++)
			addSlotToContainer(new SlotArmor(player.inventory, armorSlots[col], 8 + col * 18, 84));
		// inventory
		addPlayerInventorySlots(player, 196);
	}

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		double energy = te.getStoredEnergy();
		for (IContainerListener listener : listeners)
			if (lastEnergy != energy)
				NetworkHelper.updateClientTileEntity(listener, te.getPos(), 1, energy);
		lastEnergy = energy;
	}

	private static EntityEquipmentSlot[] getArmorSlots() {
		EntityEquipmentSlot[] values = EntityEquipmentSlot.values();
		int count = 0;
		for (EntityEquipmentSlot slot : values)
			if (slot.getSlotType() == EntityEquipmentSlot.Type.ARMOR)
				count++;
		EntityEquipmentSlot[] ret = new EntityEquipmentSlot[count];
		int i;
		for (i = 0; i < ret.length; i++)
			for (EntityEquipmentSlot slot : values)
				if (slot.getSlotType() == EntityEquipmentSlot.Type.ARMOR && slot.getIndex() == i) {
					ret[i] = slot;
					break;
				}
		for (i = 0; i < ret.length; i++)
			if (ret[i] == null)
				throw new RuntimeException("Can't find an armor mapping for idx " + i);
		return ret;
	}
}