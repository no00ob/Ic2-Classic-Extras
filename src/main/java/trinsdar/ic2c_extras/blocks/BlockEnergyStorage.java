package trinsdar.ic2c_extras.blocks;

import ic2.core.IC2;
import ic2.core.block.base.BlockMultiID;
import ic2.core.block.base.tile.TileEntityBlock;
import ic2.core.block.base.tile.TileEntityElectricBlock;
import ic2.core.platform.lang.components.base.LocaleComp;
import ic2.core.platform.registry.Ic2Items;
import ic2.core.util.helpers.BlockStateContainerIC2;
import ic2.core.util.misc.StackUtil;
import ic2.core.util.obj.IItemContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
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
import net.minecraft.world.IWorldNameable;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import trinsdar.ic2c_extras.IC2CExtras;
import trinsdar.ic2c_extras.tileentity.TileEntityCesu;
import trinsdar.ic2c_extras.util.Icons;
import trinsdar.ic2c_extras.util.Registry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BlockEnergyStorage extends BlockMultiID {

    public BlockEnergyStorage(String name, LocaleComp comp) {
    	super(Material.IRON);
        this.setHardness(4.0F);
        this.setResistance(20.0F);
        this.setSoundType(SoundType.METAL);
        this.setCreativeTab(IC2CExtras.creativeTab);
        this.setRegistryName(IC2CExtras.MODID, name.toLowerCase());
        this.setUnlocalizedName(comp);
    }
    
    @Override
    public List<Integer> getValidMetas() {
        return Collections.singletonList(0);
    }
    
    @Override
    public TileEntityBlock createNewTileEntity(World worldIn, int meta) {
        if (this == Registry.cesu) {
            return new TileEntityCesu();
        } else {
            return null;
        }
    }
    
    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer,
                                ItemStack stack) {
        if (!IC2.platform.isRendering() && stack.getMetadata() < 3 || stack.getMetadata() == 5) {
            NBTTagCompound nbt = StackUtil.getOrCreateNbtData(stack);
            TileEntity tile = worldIn.getTileEntity(pos);
            if (tile instanceof TileEntityElectricBlock && nbt.hasKey("energy")) {
                ((TileEntityElectricBlock) tile).setStored(nbt.getInteger("energy"));
            }
        }
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
    }

    @Override
    public List<ItemStack> getWrenchDrops(World world, BlockPos pos, IBlockState state, TileEntity te, EntityPlayer player, int fortune) {
        int meta = this.getMetaFromState(state);
        if (meta < 3 || meta == 5){
            List<ItemStack> items = new ArrayList();
            ItemStack result = new ItemStack(this, 1, meta);
            NBTTagCompound nbt = StackUtil.getOrCreateNbtData(result);
            if (te instanceof IWorldNameable) {
                IWorldNameable name = (IWorldNameable)te;
                if (name.hasCustomName()) {
                    result.setStackDisplayName(name.getName());
                }
            }

            items.add(result);
            if (te instanceof IItemContainer) {
                items.addAll(((IItemContainer)te).getDrops());
            }
            if (te instanceof TileEntityElectricBlock){
                TileEntityElectricBlock block = (TileEntityElectricBlock)te;
                if (block.getStored() > 0){
                    nbt.setInteger("energy", (int)(.8f * block.getStored()));
                }
            }
            return items;
        }
        return super.getWrenchDrops(world, pos, state, te, player, fortune);
    }

    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        ArrayList<ItemStack> drops = new ArrayList<>();
        if (this == Registry.cesu) {
            drops.add(Ic2Items.advMachine);
        } else {
            drops.add(Ic2Items.machine);
        }
        return drops;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public TextureAtlasSprite[] getIconSheet(int meta) {
        return Icons.getTextureData(this);
    }

    @Override
    public int getMaxSheetSize(int meta) {
        return 1;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainerIC2(this, allFacings, active);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return 0;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState();
    }

    @Override
    public IBlockState getDefaultBlockState() {
        IBlockState state = this.getDefaultState().withProperty(active, false);
        if (this.hasFacing()) {
            state = state.withProperty(allFacings, EnumFacing.NORTH);
        }

        return state;
    }

    @Override
    public List<IBlockState> getValidStateList() {
        IBlockState def = getDefaultState();
        List<IBlockState> states = new ArrayList<IBlockState>();
        for (EnumFacing side : EnumFacing.VALUES) {
            states.add(def.withProperty(allFacings, side).withProperty(active, false));
            states.add(def.withProperty(allFacings, side).withProperty(active, true));
        }
        return states;
    }

    @Override
    public List<IBlockState> getValidStates() {
        return getBlockState().getValidStates();
    }
}
