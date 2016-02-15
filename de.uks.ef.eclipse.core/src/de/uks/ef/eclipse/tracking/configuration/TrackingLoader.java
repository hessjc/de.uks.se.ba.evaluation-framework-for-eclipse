package de.uks.ef.eclipse.tracking.configuration;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

import javax.inject.Inject;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.ISafeRunnable;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.InjectionException;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.core.services.log.Logger;

import de.uks.ef.eclipse.tracking.model.EclipseTrackingManager;
import de.uks.ef.eclipse.tracking.model.EclipseTrackingModul;

@SuppressWarnings("restriction")
@Creatable
public class TrackingLoader {
	private final String EXTENSION_POINT_TRACKINGMANAGER = "de.uks.ef.TrackingManager"; //$NON-NLS-1$

	@Inject
	private IEclipseContext context;

	@Inject
	private Logger LOGGER;

	@Inject
	EclipseTrackingManager trackingManager;
	
	public void initialiseTracking() throws CoreException {
		new Job("Initialize Tracking Loader") {
			@Override
			protected IStatus run(IProgressMonitor monitor) {

				final Collection<EclipseTrackingModul> builder = new LinkedList<EclipseTrackingModul>();

				final IExtensionRegistry registry = Platform.getExtensionRegistry();
				final IExtensionPoint point = registry.getExtensionPoint(EXTENSION_POINT_TRACKINGMANAGER);

				for (final IExtension extension : point.getExtensions()) {
					for (final IConfigurationElement modul : extension.getConfigurationElements()) {
						try {
							final Object o = modul.createExecutableExtension("class");
							ContextInjectionFactory.inject(o, context);
							if (o instanceof EclipseTrackingModul) {
								builder.add((EclipseTrackingModul) o);
							} else {
								System.err.println("Object is not a tracker: " + o);
							}
						} catch (InjectionException e) {
							e.printStackTrace();
						} catch (CoreException e) {
							e.printStackTrace();
						}
					}
				}
				intitialiseModel(Collections.unmodifiableCollection(builder));
				
				
				return Status.OK_STATUS;
			}
		}.schedule(100);
		;
	}

	public void intitialiseModel(final Collection<EclipseTrackingModul> trackers) {
		for (final EclipseTrackingModul tracker : trackers) {
			trackingManager.addTrackingModul(tracker);
			tracker.initListener();
			LOGGER.info(tracker.getClass().getName() + " initialised.");
		}
	}
}