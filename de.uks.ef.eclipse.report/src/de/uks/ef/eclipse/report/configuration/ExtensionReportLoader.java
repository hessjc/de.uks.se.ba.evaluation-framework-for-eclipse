package de.uks.ef.eclipse.report.configuration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.ISafeRunnable;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.e4.core.di.annotations.Creatable;

import de.uks.ef.eclipse.report.model.EclipseChartReport;
import de.uks.ef.eclipse.report.model.EclipseReportModul;

@Creatable
public class ExtensionReportLoader {
	public Collection<EclipseReportModul> initialiseReportModul(String EXTENSION_POINT_ID) throws CoreException {
		final Collection<EclipseReportModul> builder = new ArrayList<EclipseReportModul>();

		final IExtensionRegistry registry = Platform.getExtensionRegistry();
		final IExtensionPoint point = registry.getExtensionPoint(EXTENSION_POINT_ID);

		for (final IExtension extension : point.getExtensions()) {
			for (final IConfigurationElement modul : extension.getConfigurationElements()) {
				SafeRunner.run(new ISafeRunnable() {
					@Override
					public void handleException(Throwable e) {
						e.printStackTrace();
					}

					@Override
					public void run() throws Exception {
						Object o = modul.createExecutableExtension("class");
						if (o instanceof EclipseReportModul) {
							builder.add((EclipseReportModul) o);
						} else {
							System.err.println("Object is not a tracker: " + o);
						}
					}
				});
			}
		}
		return Collections.unmodifiableCollection(builder);
	}

	public Map<String, Collection<EclipseChartReport>> initaliseReporting(String EXTENSION_POINT_ID) {
		final IExtensionRegistry registry = Platform.getExtensionRegistry();
		final IExtensionPoint point = registry.getExtensionPoint(EXTENSION_POINT_ID);

		final Map<String, Collection<EclipseChartReport>> resultMap = new HashMap<String, Collection<EclipseChartReport>>();

		for (final IExtension extension : point.getExtensions()) {
			for (final IConfigurationElement modul : extension.getConfigurationElements()) {
				SafeRunner.run(new ISafeRunnable() {
					@Override
					public void handleException(Throwable e) {
						e.printStackTrace();
					}

					@Override
					public void run() throws Exception {
						Object o = modul.createExecutableExtension("class");
						if (o instanceof EclipseReportModul) {
							Collection<EclipseChartReport> builder = new ArrayList<EclipseChartReport>();

							for (IConfigurationElement report : modul.getChildren()) {
								Object r = report.createExecutableExtension("class");
								if (r instanceof EclipseChartReport) {
									builder.add((EclipseChartReport) r);
								}
							}
							resultMap.put(o.getClass().getSimpleName(), Collections.unmodifiableCollection(builder));
						} else {
							System.err.println("Object is not a tracker: " + o);
						}
					}
				});
			}
		}
		return resultMap;
	}
}