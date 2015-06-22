package cecollector.listener;

import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.ui.texteditor.ITextEditor;

public class DocumentChangeListener implements IDocumentListener {
	
	public long timestamp;
	public ITextEditor editor;
	public IDocument document;
	
	public DocumentChangeListener(ITextEditor editor){
		timestamp = System.currentTimeMillis();
		this.editor = editor;
		document = editor.getDocumentProvider().getDocument(editor.getEditorInput());
	}

	@Override
	public void documentAboutToBeChanged(DocumentEvent event) {
		//interval 1 min.
		if(System.currentTimeMillis() > timestamp + 60000){
			editor.doSave(null);			
		}
		timestamp = System.currentTimeMillis();
	}

	@Override
	public void documentChanged(DocumentEvent event) {
		//Do nothing.
	}

}
