package trinsdar.ic2c_extras.tileentity;

import ic2.core.block.base.tile.TileEntityElectricBlock;
import ic2.core.platform.lang.components.base.LocaleComp;
import trinsdar.ic2c_extras.util.references.Ic2cExtrasLang;

public class TileEntityCesu extends TileEntityElectricBlock {

	public static final int MAX_OUTPUT = 128;
	public static final int MAX_STORAGE = 300000;
	
	public TileEntityCesu() {
		super(1, TileEntityCesu.MAX_OUTPUT, TileEntityCesu.MAX_STORAGE);		
		
	}
	
    @Override
    public LocaleComp getBlockName() {
        return Ic2cExtrasLang.CESU;
    }

	@Override
	public int getProcessRate() {
		// TODO Auto-generated method stub
		return 0;
	}
}
