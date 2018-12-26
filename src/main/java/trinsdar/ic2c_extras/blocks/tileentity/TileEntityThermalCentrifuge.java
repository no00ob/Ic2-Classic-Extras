package trinsdar.ic2c_extras.blocks.tileentity;

import ic2.api.classic.item.IMachineUpgradeItem;
import ic2.api.classic.recipe.machine.IMachineRecipeList;
import ic2.api.classic.recipe.machine.MachineOutput;
import ic2.api.classic.tile.MachineType;
import ic2.api.recipe.IRecipeInput;
import ic2.core.RotationList;
import ic2.core.block.base.tile.TileEntityBasicElectricMachine;
import ic2.core.inventory.container.ContainerIC2;
import ic2.core.inventory.filters.ArrayFilter;
import ic2.core.inventory.filters.BasicItemFilter;
import ic2.core.inventory.filters.CommonFilters;
import ic2.core.inventory.management.AccessRule;
import ic2.core.inventory.management.InventoryHandler;
import ic2.core.inventory.management.SlotType;
import ic2.core.platform.lang.components.base.LocaleComp;
import ic2.core.platform.registry.Ic2Items;
import ic2.core.platform.registry.Ic2Sounds;
import ic2.core.util.helpers.FilteredList;
import ic2.core.util.misc.StackUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.ResourceLocation;
import trinsdar.ic2c_extras.blocks.container.ContainerThermalCentrifuge;
import trinsdar.ic2c_extras.util.references.Ic2cExtrasResourceLocations;
import trinsdar.ic2c_extras.util.references.Ic2cExtrasLang;

import java.util.List;

import static trinsdar.ic2c_extras.util.Ic2cExtrasRecipes.thermalCentrifuge;

public class TileEntityThermalCentrifuge extends TileEntityBasicElectricMachine
{
    public static int maxHeat = 500;
    public int heat;

    public static final String neededHeat = "neededHeat";
    public TileEntityThermalCentrifuge() {
        super( 5, 48, 400, 128);
        this.addGuiFields("heat");
    }

    public float getHeat() {
        return (float)this.heat;
    }

    public float getMaxHeat() { return (float)this.maxHeat; }

    @Override
    public IMachineRecipeList.RecipeEntry getOutputFor(ItemStack input) {
        return thermalCentrifuge.getRecipeInAndOutput(input, false);
    }

    @Override
    public ResourceLocation getGuiTexture() {
        return Ic2cExtrasResourceLocations.thermalCentrifuge;
    }

    public static final int slotInput = 0;
    public static final int slotFuel = 1;
    public static final int slotOutput = 2;
    public static final int slotOutput2 = 3;
    public static final int slotOutput3 = 4;

    @Override
    protected void addSlots(InventoryHandler handler)
    {
        handler.registerDefaultSideAccess(AccessRule.Both, RotationList.ALL);
        handler.registerDefaultSlotAccess(AccessRule.Both, slotFuel);
        handler.registerDefaultSlotAccess(AccessRule.Import, slotInput);
        handler.registerDefaultSlotAccess(AccessRule.Export, slotOutput, slotOutput2, slotOutput3);
        handler.registerDefaultSlotsForSide(RotationList.UP.getOppositeList(), 0, 2, 4);
        handler.registerDefaultSlotsForSide(RotationList.DOWN.getOppositeList(), 1, 3);
        handler.registerInputFilter(new ArrayFilter(CommonFilters.DischargeEU, new BasicItemFilter(Items.REDSTONE), new BasicItemFilter(Ic2Items.suBattery)), slotFuel);
        handler.registerOutputFilter(CommonFilters.NotDischargeEU, slotFuel);
        handler.registerSlotType(SlotType.Fuel, slotFuel);
        handler.registerSlotType(SlotType.Input, slotInput);
        handler.registerSlotType(SlotType.Output, slotOutput, slotOutput2, slotOutput3);
    }

    @Override
    public ResourceLocation getStartSoundFile()
    {
        return Ic2Sounds.extractorOp;
    }

    @Override
    public ResourceLocation getInterruptSoundFile()
    {
        return Ic2Sounds.interruptingSound;
    }


    @Override
    public ContainerIC2 getGuiContainer(EntityPlayer player) {
        return new ContainerThermalCentrifuge(player.inventory, this);
    }

    @Override
    public IMachineRecipeList getRecipeList() {
        return thermalCentrifuge;
    }

    @Override
    public MachineType getType() {
        return null;
    }


    @Override
    public LocaleComp getBlockName() {
        return Ic2cExtrasLang.thermalCentrifuge;
    }

    @Override
    protected EnumActionResult canFillRecipeIntoOutputs(MachineOutput output) {
        List<ItemStack> result = output.getAllOutputs();
        for (int i = 0; i < result.size() && i < 3; i++) {
            ItemStack stack = getStackInSlot(slotOutput + i);
            ItemStack extra = result.get(i);
            if ((!stack.isEmpty() && !StackUtil.isStackEqual(stack, extra, false, true))
                    || stack.getCount() + extra.getCount() > extra.getMaxStackSize()) {
                return EnumActionResult.PASS;
            }
        }
        return EnumActionResult.SUCCESS;
    }

    @Override
    public boolean canWork()
    {
        if(super.canWork())
        {
            return heat == maxHeat;
        }
        return false;
    }

    @Override
    public void update() {
        super.update();

        if ((isRedstonePowered() || (lastRecipe != null && !this.inventory.get(slotInput).isEmpty())) && this.energy > 0) {
            if (this.heat < maxHeat) {
                ++this.heat;
                this.getNetwork().updateTileGuiField(this, "heat");
            }

            this.useEnergy(1);
        } else if (this.heat > 0) {
            this.heat -= Math.min(this.heat, 4);
            this.getNetwork().updateTileGuiField(this, "heat");
        }
    }

    @Override
    public void operate(IMachineRecipeList.RecipeEntry entry) {

        IRecipeInput input = entry.getInput();
        MachineOutput output = entry.getOutput().copy();

        for (int i = 0; i < 4; ++i)
        {
            ItemStack itemStack = this.inventory.get(i + this.inventory.size() - 4);
            if (itemStack.getItem() instanceof IMachineUpgradeItem)
            {
                IMachineUpgradeItem item = (IMachineUpgradeItem) itemStack.getItem();
                item.onProcessEndPre(itemStack, this, output);
            }
        }

        List<ItemStack> list = new FilteredList();
        this.operateOnce(input, output, list);

        for (int i = 0; i < 4; ++i)
        {
            ItemStack itemStack = this.inventory.get(i + this.inventory.size() - 4);
            if (itemStack.getItem() instanceof IMachineUpgradeItem)
            {
                IMachineUpgradeItem item = (IMachineUpgradeItem) itemStack.getItem();
                item.onProcessEndPost(itemStack, this, input, output, list);
            }
        }

        if (list.size() > 0)
        {
            for (int i = 0; i < 3 && i < list.size(); i++) {
                // Dangerous thing here. Might dupe items if there is random rolls
                ItemStack toAdd = list.get(i);
                if (toAdd.isEmpty()) {
                    continue;
                }
                if (getStackInSlot(slotOutput + i).isEmpty()) {
                    setStackInSlot(slotOutput + i, toAdd);
                } else {
                    getStackInSlot(slotOutput + i).grow(toAdd.getCount());
                }
            }
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.heat = nbt.getInteger("Heat");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setInteger("Heat", this.heat);
        return nbt;
    }

    public static int getRequiredHeat(MachineOutput output) {
        if (output == null || output.getMetadata() == null) {
            return 0;
        }
        return output.getMetadata().getInteger(neededHeat);
    }

    protected static NBTTagCompound createNeededHeat(int amount) {
        maxHeat = amount;
        if (amount <= 0) {
            return null;
        }
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setInteger(neededHeat, amount);
        return nbt;
    }

    public static void addRecipe(IRecipeInput input, MachineOutput output)
    {
        thermalCentrifuge.addRecipe(input, output, input.getInputs().get(0).getDisplayName());
    }
}