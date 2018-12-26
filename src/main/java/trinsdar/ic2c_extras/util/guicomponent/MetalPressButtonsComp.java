package trinsdar.ic2c_extras.util.guicomponent;

import ic2.core.inventory.gui.GuiIC2;
import ic2.core.inventory.gui.buttons.IconButton;
import ic2.core.inventory.gui.components.GuiComponent;
import ic2.core.platform.registry.Ic2GuiComp;
import ic2.core.platform.registry.Ic2Items;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import trinsdar.ic2c_extras.blocks.tileentity.TileEntityMetalPress;
import trinsdar.ic2c_extras.util.registry.RegistryItem;

import java.util.Arrays;
import java.util.List;

public class MetalPressButtonsComp extends GuiComponent {
    TileEntityMetalPress block;

    public IconButton rollingButton;
    public IconButton extrudingButton;
    public IconButton cuttingButton;

    public MetalPressButtonsComp(TileEntityMetalPress tile) {
        super(Ic2GuiComp.nullBox);
        this.block = tile;
    }

    @Override
    public List<ActionRequest> getNeededRequests() {
        return Arrays.asList(ActionRequest.GuiInit, ActionRequest.ButtonNotify);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onGuiInit(GuiIC2 gui) {
        rollingButton = new IconButton(0, gui.getXOffset() + 81, gui.getYOffset() + 52, 20, 20);
        gui.registerButton((rollingButton).setItemStack(new ItemStack(RegistryItem.ironCasing, 1)).addText("rolling mode"));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onGuiTick(GuiIC2 gui) {
        gui.getButton(0).enabled = true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onButtonClick(GuiIC2 gui, GuiButton button) {

        rollingButton.setItemStack(Ic2Items.copperCable);
//        rollingButton.setItemStack(new ItemStack(Items.SHEARS));
    }
}