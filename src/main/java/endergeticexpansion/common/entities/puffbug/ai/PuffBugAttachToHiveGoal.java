package endergeticexpansion.common.entities.puffbug.ai;

import endergeticexpansion.common.entities.puffbug.EntityPuffBug;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.Direction;
import net.minecraft.util.math.Vec3d;

public class PuffBugAttachToHiveGoal extends Goal {
	private EntityPuffBug puffbug;
	private int ticksPassed;
	
	public PuffBugAttachToHiveGoal(EntityPuffBug puffbug) {
		this.puffbug = puffbug;
	}

	@Override
	public boolean shouldExecute() {
		Direction side = this.puffbug.getDesiredHiveSide();
		if(side != null && this.puffbug.getHive() != null) {
			if(this.puffbug.isAtCorrectRestLocation(side)) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public void startExecuting() {
		this.puffbug.getNavigator().clearPath();
		this.puffbug.setAIMoveSpeed(0.0F);
		
		this.puffbug.setMotion(Vec3d.ZERO);
	}
	
	@Override
	public void tick() {
		this.ticksPassed++;
		
		this.puffbug.getNavigator().clearPath();
		this.puffbug.setAIMoveSpeed(0.0F);
		
		this.puffbug.setMotion(this.puffbug.getMotion().mul(1.0F, 0.0F, 1.0F));
		
		if(this.ticksPassed > 25) {
			this.puffbug.setAttachedHiveSide(this.puffbug.getDesiredHiveSide());
			this.puffbug.setDesiredHiveSide(null);
		}
	}
	
	@Override
	public void resetTask() {
		this.ticksPassed = 0;
	}
}