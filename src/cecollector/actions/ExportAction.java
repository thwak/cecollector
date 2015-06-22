package cecollector.actions;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;

public class ExportAction implements IWorkbenchWindowActionDelegate {
	String dataPath;
	int BUF_SIZE = 200;
	
	@Override
	public void run(IAction action) {
		IWorkbench workbench = PlatformUI.getWorkbench();
		FileDialog dialog = new FileDialog(workbench.getActiveWorkbenchWindow().getShell(), SWT.SAVE);
		dialog.setOverwrite(true);
		dialog.setFileName("cedata.zip");
		dialog.setText("Export to");			
		String zipFileName = dialog.open();
		if (zipFileName != null) {
			File zipFile = new File(zipFileName);
			ZipOutputStream zos = null;
			ZipEntry zipEntry = null;
			FileOutputStream fos = null;
			BufferedInputStream bis = null;
			FileInputStream fis = null;
			byte[] buf = new byte[BUF_SIZE];
			int len = 0;
			String fileName = null;
			int startIndex = dataPath.length()-1;
			try {
				fos = new FileOutputStream(zipFile);
				zos = new ZipOutputStream(fos);
				zos.setLevel(ZipOutputStream.DEFLATED);
				List<File> fileList = getFileList(new File(dataPath));
				for (File f : fileList) {
					fileName = f.getAbsolutePath().substring(startIndex);
					zipEntry = new ZipEntry(fileName);
					zipEntry.setTime(System.currentTimeMillis());
					zos.putNextEntry(zipEntry);
					fis = new FileInputStream(f);
					bis = new BufferedInputStream(fis, BUF_SIZE);
					while ((len = bis.read(buf)) > 0) {
						zos.write(buf, 0, len);
					}
					fis.close();
					bis.close();
					zos.flush();
					zos.closeEntry();
				}
				zos.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
	private List<File> getFileList(File dir){
		List<File> fileList = new ArrayList<File>();
		for(File f : dir.listFiles()){
			if(f.isDirectory()){
				fileList.addAll(getFileList(f));
			}else{
				fileList.add(f);
			}
		}
		
		return fileList;
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
	}

	@Override
	public void dispose() {
	}

	@Override
	public void init(IWorkbenchWindow window) {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot root = workspace.getRoot();
		URI uriRoot = root.getLocationURI();
		String path = uriRoot.getPath() + File.separator + ".metadata"
				+ File.separator + ".cecollector" + File.separator;
		dataPath = path;
	}

}
