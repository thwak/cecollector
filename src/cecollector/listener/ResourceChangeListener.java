package cecollector.listener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarkerDelta;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;

import cecollector.data.Problem;
import cecollector.data.Resource;

public class ResourceChangeListener implements IResourceChangeListener {

	private SimpleDateFormat dateFormat;
	private SimpleDateFormat timeFormat;
	private Map<String, Resource> resources;
	private long timestamp;
	private long interval;
	
	public ResourceChangeListener() {
		this.dateFormat = new SimpleDateFormat("yyyyMMdd");
		this.timeFormat = new SimpleDateFormat("HHmmss_SSS");
		resources = new HashMap<String, Resource>();
		timestamp = 0;
		interval = 3*60*1000;	//3 min.
	}
	
	@Override
	public void resourceChanged(IResourceChangeEvent event) {		
		IMarkerDelta[] markerDeltas = event.findMarkerDeltas("org.eclipse.jdt.core.problem", true);
		for(IMarkerDelta delta : markerDeltas){
			//Only process Java Problems.
			if(delta.getResource().getType() == IResource.FILE){				
				IFile file = (IFile)delta.getResource();
				String filePath = file.getFullPath().toString();
				Resource resource = null;
				Problem problem = null;
				String content = null;
				long stamp = file.getModificationStamp();
				try {
					if(file.getFileExtension().equals("java")){
						if(resources.containsKey(filePath)){
							resource = resources.get(filePath);
						}else{
							resource = new Resource(filePath);
							resources.put(filePath, resource);
						}
						content = readFile(file);
						resource.addContent(stamp, content);
						problem = new Problem(delta);
						resource.addProblem(stamp, problem);						
					}
				} catch (CoreException | IOException e) {
					e.printStackTrace();
				}
			}
		}
		long currentTime = System.currentTimeMillis();
		//if time interval is passed and there is any change in resources, store it.
		if((currentTime - timestamp) >= interval &&
				resources.size() > 0){
			storeResource();
			timestamp = System.currentTimeMillis();
			resources = new HashMap<String, Resource>(); 
		}
	}

	public void storeResource() {
		ObjectOutputStream os = null;
		FileOutputStream fos = null;
		try {
			IWorkspace workspace = ResourcesPlugin.getWorkspace();
			IWorkspaceRoot root = workspace.getRoot();
			URI uriRoot = root.getLocationURI();
			String path = uriRoot.getPath() + File.separator + ".metadata"
					+ File.separator + ".cecollector" + File.separator;
			Date timestamp = new Date(System.currentTimeMillis());
			String dirName = dateFormat.format(timestamp);
			File dir = new File(path + dirName);
			if(!dir.exists()){
				dir.mkdirs();
			}
			String fileName = timeFormat.format(timestamp); 
			File file = new File(path + dirName + File.separator + fileName);
			fos = new FileOutputStream(file);
			os = new ObjectOutputStream(fos);
			os.writeObject(resources);
			os.flush();			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fos.close();
				os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private String readFile(IFile file) throws CoreException, IOException {
		InputStream is = file.getContents();
		byte[] buf = new byte[200];
		StringBuffer sb = new StringBuffer("");
		while(is.read(buf) > -1){
			sb.append(new String(buf));
			buf = new byte[200];
		}		
		return sb.toString();
	}

}
