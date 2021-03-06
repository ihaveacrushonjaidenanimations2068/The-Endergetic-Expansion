package endergeticexpansion.api.endimator.entity;

import endergeticexpansion.api.endimator.Endimation;

public interface IEndimatedEntity {
	public static final Endimation BLANK_ANIMATION = new Endimation();
	
	/**
	 * @return - The entity's animations
	 */
	Endimation[] getEndimations();
	
	/**
	 * @return - The endimation currently being played
	 */
	Endimation getPlayingEndimation();
	
	/**
	 * @return - The progress(in ticks) of the current playing endimation
	 */
	int getAnimationTick();
	
	/**
	 * Sets the progress of the current playing endimation
	 * @param animationTick - progress(in ticks)
	 */
	void setAnimationTick(int animationTick);
	
	/**
	 * @param endimationToPlay - The endimation to play
	 */
	void setPlayingEndimation(Endimation endimationToPlay);
	
	default void onEndimationStart(Endimation endimation) {};
	
	default void onEndimationEnd(Endimation endimation) {};
	
	default void resetEndimation() {
		this.setPlayingEndimation(BLANK_ANIMATION);
	}
	
	default void endimateTick() {
		if(!this.isNoEndimationPlaying()) {
			if(this.getAnimationTick() == 0) {
				this.onEndimationStart(this.getPlayingEndimation());
			}
			this.setAnimationTick(this.getAnimationTick() + 1);
			if(this.getAnimationTick() >= this.getPlayingEndimation().getAnimationTickDuration()) {
				this.onEndimationEnd(this.getPlayingEndimation());
				this.resetEndimation();
			}
		}
	}
	
	default boolean isNoEndimationPlaying() {
		return this.getPlayingEndimation() == BLANK_ANIMATION;
	}
	
	default boolean isEndimationPlaying(Endimation endimation) {
		return this.getPlayingEndimation() == endimation;
	}
}