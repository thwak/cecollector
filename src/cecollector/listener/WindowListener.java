package cecollector.listener;

import org.eclipse.ui.IPartService;
import org.eclipse.ui.IWindowListener;
import org.eclipse.ui.IWorkbenchWindow;

public class WindowListener implements IWindowListener {
	
	public PartListener partListener;
	
	public WindowListener(PartListener listener){
		this.partListener = listener;
	}

	@Override
	public void windowActivated(IWorkbenchWindow window) {
		IPartService partService = window.getPartService();		
		if(partService != null){
			partService.addPartListener(partListener);
		}
		if(window.getActivePage() != null)
			window.getActivePage().addPartListener(partListener);
	}

	@Override
	public void windowDeactivated(IWorkbenchWindow window) {
	}

	@Override
	public void windowClosed(IWorkbenchWindow window) {
	}

	@Override
	public void windowOpened(IWorkbenchWindow window) {
		IPartService partService = window.getPartService();		
		if(partService != null){
			partService.addPartListener(partListener);
		}
		if(window.getActivePage() != null)
			window.getActivePage().addPartListener(partListener);
	}

}
