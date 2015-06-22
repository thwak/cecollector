package cecollector.listener;

import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.texteditor.ITextEditor;

public class PartListener implements IPartListener2 {

	DocumentChangeListener listener = null;
	
	@Override
	public void partActivated(IWorkbenchPartReference partRef) {
		IWorkbenchPart part = partRef.getPart(false);
		if(part instanceof ITextEditor){
			ITextEditor editor = (ITextEditor)part;
			System.out.println("Editor["+editor.getTitle()+"] is activated");								
			IDocument document = editor.getDocumentProvider().getDocument(editor.getEditorInput());
			if(document != null){									
				if(listener != null){
					document.addDocumentListener(listener);
				}else{
					listener = new DocumentChangeListener(editor);
					System.out.println("New listener added.");
				}
			}else{
				System.out.println("No doc.");
			}
		}
	}

	@Override
	public void partBroughtToTop(IWorkbenchPartReference partRef) {
	}

	@Override
	public void partClosed(IWorkbenchPartReference partRef) {
	}

	@Override
	public void partDeactivated(IWorkbenchPartReference partRef) {
	}

	@Override
	public void partOpened(IWorkbenchPartReference partRef) {
		IWorkbenchPart part = partRef.getPart(false);
		if(part instanceof ITextEditor){
			ITextEditor editor = (ITextEditor)part;
			System.out.println("Editor["+editor.getTitle()+"] is opened");
			IDocument document = editor.getDocumentProvider().getDocument(editor.getEditorInput());
			listener = new DocumentChangeListener(editor);
			if(document != null){
				document.addDocumentListener(listener);
			}
		}
	}

	@Override
	public void partHidden(IWorkbenchPartReference partRef) {
	}

	@Override
	public void partVisible(IWorkbenchPartReference partRef) {
	}

	@Override
	public void partInputChanged(IWorkbenchPartReference partRef) {
	}

}
