package de.uks.ef.eclipse.tracking.configuration;

import java.beans.PropertyChangeListener;
import java.util.HashSet;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.core.services.log.Logger;

import de.uks.ef.core.model.Evaluation;
import de.uks.ef.core.model.TrackingEvent;
import de.uks.ef.core.model.TrackingManager;
import de.uks.ef.core.model.TrackingModul;
import de.uks.ef.eclipse.core.model.EclipseEvaluationManager;
import de.uks.ef.eclipse.tracking.listener.TrackingStateListener;
import de.uks.ef.eclipse.tracking.model.EclipseTrackingManager;

@SuppressWarnings("restriction")
@Creatable
@Singleton
public class TrackingService {
	@Inject
	private Logger LOGGER;

	private final HashSet<TrackingStateListener> listeners;

	@Inject
	private EclipseTrackingManager trackingManager;

	@Inject
	private EclipseEvaluationManager evaluationManager;

	@Inject
	private TrackingLoader trackingLoader;

	public TrackingService() {
		listeners = new HashSet<TrackingStateListener>();
	}

	@PostConstruct
	private void init() throws Exception {
		LOGGER.info("TrackingService injected.");
		trackingLoader.initialiseTracking();
		LOGGER.info("TrackingManager initialised.");
	}

	public TrackingManager getTrackingManager() {
		return trackingManager;
	}

	public HashSet<TrackingStateListener> getListeners() {
		return listeners;
	}

	public void showTracking() {
		for (final TrackingStateListener listener : listeners) {
			listener.trackingShown();
		}
	}

	public void registerTrackingStateListener(final TrackingStateListener trackingStateListener) {
		listeners.add(trackingStateListener);
	}

	public void unregisterTrackingStateListener(final TrackingStateListener trackingStateListener) {
		listeners.remove(trackingStateListener);
	}

	public void addTrackingEvent(final Evaluation activeEvaluation, final TrackingEvent trackingEvent) {
		if (activeEvaluation != null && activeEvaluation.isStarted()) {
			getTrackingManager().addTrackingEvent(activeEvaluation.getId(), trackingEvent);
			LOGGER.debug("TrackingEvent added: " + trackingEvent.getEncoding());
			showTracking();
		}
	}

	public void startTracking() {
		for (final TrackingModul trackingModul : evaluationManager.getTrackingManager().getTrackingModul().values()) {
			trackingModul.startTracking();
		}
		LOGGER.info("Tracking started.");
	}

	public void stopTracking() {
		for (final TrackingModul trackingModul : evaluationManager.getTrackingManager().getTrackingModul().values()) {
			trackingModul.stopTracking();
		}
		LOGGER.info("Tracking stopped.");
	}
}
