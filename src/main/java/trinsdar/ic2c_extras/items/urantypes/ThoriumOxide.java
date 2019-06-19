package trinsdar.ic2c_extras.items.urantypes;

import ic2.core.item.reactor.uranTypes.UranBaseType;
import ic2.core.platform.lang.components.base.LocaleComp;
import ic2.core.platform.textures.Ic2Icons;
import ic2.core.util.misc.StackUtil;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import trinsdar.ic2c_extras.util.Registry;

import java.awt.*;

public class ThoriumOxide extends UranBaseType {

    @SideOnly(Side.CLIENT)
    @Override
    public TextureAtlasSprite getTexture(RodType type) {
        return Ic2Icons.getTextures("ic2c_extras_nuclear_cells")[this.getRodID(type) - 1100];
    }
    @Override
    public int getRow() {
        return 0;
    }

    @Override
    public int getMaxDurability() {
        return 20000;
    }

    @Override
    public float getEUPerPulse() {
        return 5.0f;
    }

    @Override
    public int getPulsesPerTick() {
        return 2;
    }

    @Override
    public int getPulsesForConnection() {
        return 3;
    }

    @Override
    public float getHeatModifier() {
        return 1.2f;
    }

    @Override
    public float getExplosionEffectModifier() {
        return 2.0f;
    }

    @Override
    public ItemStack getUraniumIngot() {
        return null;
    }

    @Override
    public ItemStack getRodType(RodType type) {
        switch(type) {
            case SingleRod:
                return new ItemStack(Registry.singleThoriumOxideCell).copy();
            case DualRod:
                return new ItemStack(Registry.doubleThoriumOxideCell).copy();
            case QuadRod:
                return new ItemStack(Registry.quadThoriumOxideCell).copy();
            case NearDepletedRod:
                return new ItemStack(Registry.nearDepletedThoriumOxideCell).copy();
            case IsotopicRod:
                return new ItemStack(Registry.isotopicThoriumOxideCell).copy();
            case ReEnrichedRod:
                return new ItemStack(Registry.reEnrichedThoriumOxideCell).copy();
            default:
                return ItemStack.EMPTY;
        }
    }

    @Override
    public ItemStack getNewIsotopicRod() {
        return StackUtil.copyWithDamage(this.getRodType(RodType.IsotopicRod), this.getMaxDurability() - 1);
    }

    @Override
    public short getRodID(RodType type) {
        switch(type) {
            case SingleRod:
                return 1104;
            case DualRod:
                return 1109;
            case QuadRod:
                return 1114;
            case ReEnrichedRod:
                return 1129;
            case NearDepletedRod:
                return 1119;
            case IsotopicRod:
                return 1124;
            default:
                return 0;
        }
    }

    @Override
    public LocaleComp getName(RodType rodType) {
        return null;
    }

    @Override
    public boolean isReEnrichedUran() {
        return false;
    }

    @Override
    public Color getReEnrichedColor() {
        return null;
    }

    @Override
    public ItemStack getIngridient() {
        return null;
    }

    @Override
    public int getIngrientPoints() {
        return 0;
    }

    @Override
    public int getIngridientCost() {
        return 0;
    }
}