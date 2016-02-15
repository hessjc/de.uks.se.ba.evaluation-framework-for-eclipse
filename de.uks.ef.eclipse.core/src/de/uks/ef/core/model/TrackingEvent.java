package de.uks.ef.core.model;

public interface TrackingEvent {
	public EvaluationStep geEvaluationStep();

	public String getTimestamp();
	
	public String getStepId();
	
	public String getEncoding();

	/**
	 * <pre>
	 *           0..*     0..1
	 * TrackingEvent ------------------------- TrackingModul
	 *           trackingEvent        &lt;       trackingModul
	 * </pre>
	 */
	public void setTrackingModul(TrackingModul value);

	public TrackingModul getTrackingModul();

	/**
	 * <pre>
	 *           0..*     0..1
	 * TrackingEvent ------------------------- TrackingManager
	 *           trackingEvent        &lt;       trackingManager
	 * </pre>
	 */
	public void setTrackingManager(TrackingManager trackingManager);

	public TrackingManager getTrackingManager();
}