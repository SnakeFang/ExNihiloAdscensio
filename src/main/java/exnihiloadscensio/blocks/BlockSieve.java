package exnihiloadscensio.blocks;

import exnihiloadscensio.items.ItemMesh;
import exnihiloadscensio.tiles.TileSieve;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockSieve extends BlockBase implements ITileEntityProvider {
	
	public enum MeshType implements IStringSerializable {
		NONE(0, "none"), STRING(1, "string"), FLINT(2, "flint"), IRON(3, "iron"), DIAMOND(4, "diamond");

		private int id;
		private String name;
		
		private MeshType(int id, String name) {
			this.id = id;
			this.name = name;
		}
		
		@Override
		public String getName() {
			return name;
		}
		
		public int getID() {
			return id;
		}
		
		@Override
		public String toString() {
			return getName();
		}
		
		public static MeshType getMeshTypeByID(int meta) {
			switch (meta) {
			case 0:
				return NONE;
			case 1:
				return STRING;
			case 2:
				return FLINT;
			case 3:
				return IRON;
			case 4:
				return DIAMOND;
			}
			
			return NONE;
		}
	}
	
	public static final PropertyEnum<MeshType> MESH = PropertyEnum.create("mesh", MeshType.class);

	public BlockSieve() {
		super(Material.WOOD, "blockSieve");
		this.setDefaultState(this.blockState.getBaseState().withProperty(MESH, MeshType.NONE));
		this.setHardness(2.0f);
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		if (world.isRemote)
			return true;
		
		TileSieve te = (TileSieve) world.getTileEntity(pos);
		if (te != null && heldItem != null && heldItem.getItem() instanceof ItemMesh) {
			MeshType type = MeshType.getMeshTypeByID(heldItem.getItemDamage());
			boolean done = te.setMesh(type);
			if (done) {
				heldItem.stackSize--;
				world.setBlockState(pos, state.withProperty(MESH, type));
			}
		}
		
		return super.onBlockActivated(world, pos, state, player, hand, heldItem, side, hitX, hitY, hitZ);
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] {MESH});
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		MeshType type;
		switch (meta) {
		case 0:
			type = MeshType.NONE;
			break;
		case 1:
			type = MeshType.STRING;
			break;
		case 2:
			type = MeshType.FLINT;
			break;
		case 3:
			type = MeshType.IRON;
			break;
		case 4:
			type = MeshType.DIAMOND;
			break;
		default:
			type = MeshType.STRING;
			break;
		}
		return getDefaultState().withProperty(MESH, type);
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		MeshType type = (MeshType) state.getValue(MESH);
		return type.getID();
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileSieve();
	}
	
	@Override
	public boolean isFullyOpaque(IBlockState state)
	{
		return false;
	}
	
	@Override
	 public boolean isFullBlock(IBlockState state)
    {
        return false;
    }
	
	@Override
	public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }
	
	@Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

}
