package trinsdar.ic2c_extras.common.util;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import trinsdar.ic2c_extras.Ic2cExtras;
import trinsdar.ic2c_extras.common.blocks.*;
import trinsdar.ic2c_extras.common.tileentity.TileEntityAdvancedSteamTurbine;
import trinsdar.ic2c_extras.common.tileentity.TileEntityMetalPress;
import trinsdar.ic2c_extras.common.tileentity.TileEntityOreWashingPlant;
import trinsdar.ic2c_extras.common.tileentity.TileEntityThermalCentrifuge;

public class RegistryBlock
{
    public static final BlockMachine
    oreWashingPlant = new BlockMachine("oreWashingPlant"),
    thermalCentrifuge = new BlockMachine("thermalCentrifuge"),
    metalPress = new BlockMachine("metalPress");
    public static final BlockAdvancedSteamTurbine advancedSteamTurbine = new BlockAdvancedSteamTurbine("advancedSteamTurbine");
    public static final BlockMetal
    steelBlock = new BlockMetal("steelBlock", 0),
    refinedIronBlock = new BlockMetal("refinedIronBlock", 1);


    public static final Block[] blocks =
    {
            oreWashingPlant,
            advancedSteamTurbine,
            steelBlock,
            thermalCentrifuge,
            metalPress,
            refinedIronBlock
    };

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event)
    {
        IForgeRegistry registry = event.getRegistry();

        for (Block block : blocks)
        {
            registry.register(block);
        }

    }

    public static void registerTiles()
    {
         GameRegistry.registerTileEntity(TileEntityOreWashingPlant.class, new ResourceLocation(Ic2cExtras.MODID, "tileEntityOreWashingPlant"));
         GameRegistry.registerTileEntity(TileEntityAdvancedSteamTurbine.class, new ResourceLocation(Ic2cExtras.MODID, "tileEntityAdvancedSteamTurbine"));
         GameRegistry.registerTileEntity(TileEntityThermalCentrifuge.class, new ResourceLocation(Ic2cExtras.MODID, "tileEntityThermalCentrifuge"));
         GameRegistry.registerTileEntity(TileEntityMetalPress.class, new ResourceLocation(Ic2cExtras.MODID, "tileEntityMetalPress"));
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event)
    {
        IForgeRegistry registry = event.getRegistry();

        for (Block block : blocks)
        {
            registry.register(new ItemBlock(block).setRegistryName(block.getRegistryName()).setUnlocalizedName(block.getUnlocalizedName()));
        }
    }
}
