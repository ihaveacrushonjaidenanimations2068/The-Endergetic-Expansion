package endergeticexpansion.api.util;

import org.apache.commons.lang3.ArrayUtils;

import endergeticexpansion.api.endimator.Endimation;
import endergeticexpansion.api.endimator.entity.IEndimatedEntity;
import endergeticexpansion.api.EndergeticAPI.ClientInfo;
import endergeticexpansion.common.entities.booflo.EntityBooflo;
import endergeticexpansion.common.network.entity.*;
import endergeticexpansion.common.network.entity.booflo.*;
import endergeticexpansion.common.network.nbt.*;
import endergeticexpansion.common.network.particle.*;
import endergeticexpansion.core.EndergeticExpansion;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.PacketDistributor;

/**
 * @author - SmellyModder(Luke Tonon)
 * This class holds a big list of useful network functions. Most are used in the mod
 */
public class NetworkUtil {
	/**
	 * @param stack - The stack that the message will update the nbt for
	 * Used for updating the server nbt from the client. For example it's used in booflo vest keybinds
	 */
	@OnlyIn(Dist.CLIENT)
	public static void updateSItemNBT(ItemStack stack) {
		EndergeticExpansion.CHANNEL.sendToServer(new MessageSUpdateNBTTag(stack));
	}
	
	/**
	 * @param stack - the stack that the message will update the nbt for
	 * Used for updating the client nbt from the server
	 */
	public static void updateCItemNBT(ItemStack stack) {
		EndergeticExpansion.CHANNEL.send(PacketDistributor.SERVER.noArg(), new MessageCUpdateNBTTag(stack));
	}
	
	/**
	 * @param stack - The stack that the message will set the cooldown for
	 * @param cooldown - The amount of time the cooldown will last; measured in ticks. 1 second = 20 ticks
	 * @param isVest - A boolean that specifies that the cooldown is for the Booflo Vest item
	 * Used for updating the client nbt from the server
	 */
	@OnlyIn(Dist.CLIENT)
	public static void setSItemCooldown(ItemStack stack, int cooldown, boolean isVest) {
		EndergeticExpansion.CHANNEL.sendToServer(new MessageSSetCooldown(stack, cooldown, isVest));
	}
	
	/**
	 * @param motion - The vector motion of the entity
	 * @param id - The Player's Entity Id
	 * Used for applying motion to the client from the server
	 */
	public static void setCVelocity(Entity entity, Vec3d motion) {
		EndergeticExpansion.CHANNEL.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), new MessageCSetVelocity(motion, entity.getEntityId()));
	}
	
	/**
	 * @param motion - The vector motion of the entity
	 * @param id - The Player's Entity Id
	 * Used for setting server side Entity Velocity from the client side
	 */
	@OnlyIn(Dist.CLIENT)
	public static void setSVelocity(Vec3d motion, int id) {
		EndergeticExpansion.CHANNEL.sendToServer(new MessageSSetVelocity(motion, id));
	}
	
	/**
	 * @param velX, velY, velZ - The velocity values for x, y, and z
	 * @param radius - The radius the blast will affect; measured in blocks
	 * Used for pushing entities back through the client for the server
	 */
	@OnlyIn(Dist.CLIENT)
	public static void SBoofEntity(float xzForce, float upperForce, int radius) {
		EndergeticExpansion.CHANNEL.sendToServer(new MessageSBoofEntity(xzForce, upperForce, radius));
	}
	
	/**
	 * @param entityId - Entity ID of the booflo
	 * Sends a message to the server to play the animation and inflate the booflo
	 */
	@OnlyIn(Dist.CLIENT)
	public static void inflateBooflo(int entityId) {
		Entity entity = ClientInfo.getClientPlayerWorld().getEntityByID(entityId);
		if(entity instanceof EntityBooflo) {
			EntityBooflo booflo = (EntityBooflo) entity;
			EndergeticExpansion.CHANNEL.sendToServer(new MessageSInflate(entityId));
			booflo.setBoofed(true);
			booflo.setDelayDecrementing(false);
			booflo.setDelayExpanding(true);
		}
	}
	
	/**
	 * @param entityId - Entity ID of the booflo
	 * Sends a message to the server to set the booflo to slam
	 */
	@OnlyIn(Dist.CLIENT)
	public static void slamBooflo(int entityId) {
		Entity entity = ClientInfo.getClientPlayerWorld().getEntityByID(entityId);
		if(entity instanceof EntityBooflo) {
			EndergeticExpansion.CHANNEL.sendToServer(new MessageSSlam(entityId));
		}
	}
	
	/**
	 * @param entityId - Entity ID of the booflo
	 * Sends a message to the server to increment the booflo bar
	 */
	@OnlyIn(Dist.CLIENT)
	public static void incrementBoofloBoostTimer(int entityId) {
		Entity entity = ClientInfo.getClientPlayerWorld().getEntityByID(entityId);
		if(entity instanceof EntityBooflo) {
			EndergeticExpansion.CHANNEL.sendToServer(new MessageSIncrementBoostDelay(entityId));
		}
	}
	
	/**
	 * @param entityId - Entity ID of the booflo
	 * Sends a message to the server to set the player not boosting
	 */
	@OnlyIn(Dist.CLIENT)
	public static void setPlayerNotBoosting(int entityId) {
		Entity entity = ClientInfo.getClientPlayerWorld().getEntityByID(entityId);
		if(entity instanceof EntityBooflo) {
			EndergeticExpansion.CHANNEL.sendToServer(new MessageSSetPlayerNotBoosting(entityId));
		}
	}
	
	/**
	 * @param entityId - The entity's id
	 * @param distance - The height of the fall
	 * Used for setting fall distance in booflo vests
	 */
	@OnlyIn(Dist.CLIENT)
	public static void setSFallDistance(int entityId, int distance) {
		EndergeticExpansion.CHANNEL.sendToServer(new MessageSSetFallDistance(entityId, distance));
	}
	
	/**
	 * @param stack - The stack do damage
	 * @param amount - The amount to damage
	 * Used for damaging items from the client side
	 */
	public static void damageItem(ItemStack stack, EquipmentSlotType type, int amount) {}
	
	/**
	 * @param name - The registry name of the particle
	 * All other parameters work same as world#addParticle
	 * Used for adding particles to the world from the server side
	 */
	public static void spawnParticle(String name, double posX, double posY, double posZ, double motionX, double motionY, double motionZ) {
		EndergeticExpansion.CHANNEL.send(PacketDistributor.ALL.with(() -> null), new MessageSpawnParticle(name, posX, posY, posZ, motionX, motionY, motionZ));
	}
	
	/**
	 * @param name - The registry name of the particle
	 * Used for adding particles to all the clients from the client
	 */
	public static void spawnParticleC2S2C(String name, double posX, double posY, double posZ, double motionX, double motionY, double motionZ) {
		EndergeticExpansion.CHANNEL.sendToServer(new MessageC2S2CSpawnParticle(name, posX, posY, posZ, motionX, motionY, motionZ));
	}
	
	public static void teleportEntity(Entity entity, double posX, double posY, double posZ) {
		entity.setLocationAndAngles(posX, posY, posZ, entity.rotationYaw, entity.rotationPitch);
		EndergeticExpansion.CHANNEL.send(PacketDistributor.ALL.with(() -> null), new MessageTeleport(entity.getEntityId(), posX, posY, posZ));
	}
	
	/**
	 * Sends an animation message to the clients to update an entity's animations
	 * @param entity - The Entity to send the packet for
	 * @param endimationToPlay - The endimation to play
	 */
	public static <E extends Entity & IEndimatedEntity> void setPlayingAnimationMessage(E entity, Endimation endimationToPlay) {
		if(!entity.world.isRemote) {
			EndergeticExpansion.CHANNEL.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), new MessageCAnimation(entity.getEntityId(), ArrayUtils.indexOf(entity.getEndimations(), endimationToPlay)));
			entity.setPlayingEndimation(endimationToPlay);
		}
	}
}