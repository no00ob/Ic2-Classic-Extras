package trinsdar.ic2c_extras.proxy;

import ic2.core.IC2;
import net.minecraftforge.common.config.Configuration;
import trinsdar.ic2c_extras.Config;
import trinsdar.ic2c_extras.util.Ic2cExtrasOredict;
import trinsdar.ic2c_extras.util.Ic2cExtrasRecipes;
import trinsdar.ic2c_extras.util.Registry;


public class CommonProxy
{
    public static Configuration config;

    public void preInit()
    {
        Config.init();
        Registry.init();
        Registry.registerTiles();
    }

    public void init()
    {
        if (!IC2.config.getFlag("NonRadiation")){
            Ic2cExtrasOredict.init();
            Ic2cExtrasRecipes.init();
        }
    }

    public void postInit()
    {
        Ic2cExtrasRecipes.initRailcraftRecipes();
    }
}


