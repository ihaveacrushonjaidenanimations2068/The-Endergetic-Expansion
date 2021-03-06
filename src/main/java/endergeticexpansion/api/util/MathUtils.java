package endergeticexpansion.api.util;

import java.util.Random;

import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;

/**
 * @author - SmellyModder(Luke Tonon)
 */
public class MathUtils {
	
	/*
	 * Used to calculate center's with percents of AxisAllignedBBs
	 * To get a default middle value use 0.5D for all multipliers
	 */
	public static Vec3d getCenterAdjusted(AxisAlignedBB bb, double xMultiplier, double yMultiplier, double zMultiplier) {
		return new Vec3d(bb.minX + (bb.maxX - bb.minX) * xMultiplier, bb.minY + (bb.maxY - bb.minY) * yMultiplier, bb.minZ + (bb.maxZ - bb.minZ) * zMultiplier);
	}
    
	public static double distanceBetweenPoints2d(double x1, double y1, double x2, double y2) {
		return Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
	}
	
	public static double makeNegativeRandomly(double value, Random rand) {
		return rand.nextBoolean() ? -value : value;
	}
	
	public static int getLowestValueInIntArray(int[] array) {
		int currentLowest = Integer.MAX_VALUE;
		
		for(int i = 0; i < array.length; i++) {
			if(array[i] <= currentLowest) {
				currentLowest = array[i];
			}
		}
		return currentLowest;
	}
    
	public interface ComputableEquation {
		public double compute(double theta);
	}
	
}
