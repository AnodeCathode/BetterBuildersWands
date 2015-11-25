package portablejim.bbw.core.items;

import cpw.mods.fml.common.FMLLog;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import portablejim.bbw.BetterBuildersWandsMod;
import portablejim.bbw.basics.EnumLock;
import portablejim.bbw.basics.Point3d;
import portablejim.bbw.core.WandUtility;
import portablejim.bbw.core.WandWorker;
import portablejim.bbw.core.wands.UnbreakingWand;
import portablejim.bbw.shims.BasicPlayerShim;
import portablejim.bbw.shims.BasicWorldShim;
import portablejim.bbw.shims.CreativePlayerShim;
import portablejim.bbw.shims.IPlayerShim;
import portablejim.bbw.shims.IWorldShim;

import java.util.LinkedList;
import java.util.List;

/**
 * Class to cover common functions between wands.
 */
public abstract class ItemBasicWand extends Item implements IWandItem{
    public ItemBasicWand() {
        super();
        this.setCreativeTab(CreativeTabs.tabTools);
    }

    public boolean onItemUse(ItemStack itemstack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        IPlayerShim playerShim = new BasicPlayerShim(player);
        if(player.capabilities.isCreativeMode) {
            playerShim = new CreativePlayerShim(player);
        }
        IWorldShim worldShim = new BasicWorldShim(world);
        UnbreakingWand unbreakingWand = new UnbreakingWand(itemstack);

        WandWorker worker = new WandWorker(unbreakingWand, playerShim, worldShim);

        Point3d clickedPos = new Point3d(x, y, z);

        ItemStack targetItemstack = worker.getEquivalentItemStack(clickedPos);
        int numBlocks = Math.min(unbreakingWand.getMaxBlocks(), playerShim.countItems(targetItemstack));
        FMLLog.info("Max blocks: %d (%d|%d", numBlocks, unbreakingWand.getMaxBlocks(), playerShim.countItems(targetItemstack));

        LinkedList<Point3d> blocks = worker.getBlockPositionList(clickedPos, ForgeDirection.getOrientation(side), numBlocks, getLock(itemstack), getFaceLock(itemstack));

        worker.placeBlocks(blocks, clickedPos);

        return true;
    }

    @SuppressWarnings("unchecked")
    public void addInformation(ItemStack itemstack, EntityPlayer player, List lines, boolean extraInfo) {

        EnumLock mode = WandUtility.getMode(itemstack);
        switch (mode) {
            case NORTHSOUTH:
                lines.add(StatCollector.translateToLocal(BetterBuildersWandsMod.MODID + ".hover.mode.northsouth"));
                break;
            case VERTICAL:
                lines.add(StatCollector.translateToLocal(BetterBuildersWandsMod.MODID + ".hover.mode.vertical"));
                break;
            case VERTICALEASTWEST:
                lines.add(StatCollector.translateToLocal(BetterBuildersWandsMod.MODID + ".hover.mode.vertialeastwest"));
                break;
            case EASTWEST:
                lines.add(StatCollector.translateToLocal(BetterBuildersWandsMod.MODID + ".hover.mode.eastwest"));
                break;
            case HORIZONTAL:
                lines.add(StatCollector.translateToLocal(BetterBuildersWandsMod.MODID + ".hover.mode.horizontal"));
                break;
            case VERTICALNORTHSOUTH:
                lines.add(StatCollector.translateToLocal(BetterBuildersWandsMod.MODID + ".hover.mode.vertialnorthsouth"));
                break;
            case NOLOCK:
                lines.add(StatCollector.translateToLocal(BetterBuildersWandsMod.MODID + ".hover.mode.nolock"));
                break;
        }
    }
}
