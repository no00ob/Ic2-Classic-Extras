package trinsdar.ic2c_extras.events;

import ic2.core.IC2;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import trinsdar.ic2c_extras.util.IReactorPlated;

import java.util.UUID;

public class Bear989Event {
    int ticker = 0;

    public boolean hasFullPlatedSuit(EntityPlayer player) {
        for (int i = 0; i < 4; i++) {
            ItemStack stack = player.inventory.armorInventory.get(i);
            Item item = stack.getItem();
            if (!(item instanceof IReactorPlated) || !((IReactorPlated)item).hasReactorPlate(stack)) {
                return false;
            }
        }
        return true;
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        EntityPlayer player = event.player;
        if (IC2.platform.isSimulating() && event.phase == TickEvent.Phase.END) {
            if (player.getUniqueID().equals(new UUID(0x1964e3d1650040e7L, 0x9ff2e6161d41a8c2L))) {
                if (hasFullPlatedSuit(player)){
                    player.addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("slowness"), 10, 3, false, false));
                }
                int count = 0;
                for (int i = 0; i < player.inventory.mainInventory.size(); i++) {
                    if (player.inventory.getStackInSlot(i).getCount() > 0) {
                        count += 1;
                    }
                }
                int emptySlots = player.inventory.mainInventory.size() - count;
                if (ticker == 0) {
                    switch (emptySlots) {
                        case 3:
                            IC2.platform.messagePlayer(player, "Bear, your inventory starts to get full.");
                            ticker = 2400;
                            break;
                        case 2:
                            IC2.platform.messagePlayer(player, "You should clean up your Inventory, Bear!");
                            ticker = 2400;
                            break;
                        case 1:
                            IC2.platform.messagePlayer(player, "Your Inventory is almost full, Bear!!");
                            ticker = 2400;
                            break;
                        case 0:
                            IC2.platform.messagePlayer(player, "You are full of shit, Bear!!!");
                            ticker = 2400;
                            break;
                        default:
                            break;
                    }
                }
                if (ticker > 0) {
                    if (emptySlots < 6) {
                        ticker -= 1;
                    } else {
                        ticker = 0;
                    }
                }
            }
        }
    }
}
