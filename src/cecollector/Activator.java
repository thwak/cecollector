package cecollector;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import cecollector.listener.PartListener;
import cecollector.listener.ResourceChangeListener;
import cecollector.listener.WindowListener;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "CECollector"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;
	private static ResourceChangeListener listener = null;
	
	/**
	 * The constructor
	 */
	public Activator() {
		listener = new ResourceChangeListener();
	}

	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		IWorkbench workbench = plugin.getWorkbench();
		PartListener partListener = new PartListener();
		WindowListener windowListener = new WindowListener(partListener);
		workbench.addWindowListener(windowListener);
		IWorkbenchWindow activeWindow = workbench.getActiveWorkbenchWindow();
		if(activeWindow != null){
			IWorkbenchPage activePage = activeWindow.getActivePage();
			if(activePage != null){
				activePage.addPartListener(partListener);
			}
		}
		ResourcesPlugin.getWorkspace().addResourceChangeListener(listener, IResourceChangeEvent.POST_CHANGE);
	}

	public void stop(BundleContext context) throws Exception {
		plugin = null;
		//Store data when shutdown.
		listener.storeResource();
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}
}
