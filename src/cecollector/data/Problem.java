package cecollector.data;

import java.io.Serializable;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IMarkerDelta;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.runtime.CoreException;

public class Problem implements Serializable {
	private static final long serialVersionUID = -3886756087569803856L;
	public long stamp;
	public int kind;
	public String resourceName;
	public long id;
	public String type;
	public int severity;
	public String message;
	public String arguments;
	public int charStart;
	public int charEnd;
	public int categoryId;
	public int lineNumber;
	public String sourceId;
	
	public Problem(IMarker marker){
		this.kind = IResourceDelta.ADDED;
		this.stamp = marker.getResource().getModificationStamp();
		this.resourceName = marker.getResource().getFullPath().toString();
		this.id = marker.getId();		
		this.severity = marker.getAttribute("severity", 1);
		this.message = marker.getAttribute("message", "");
		this.arguments = marker.getAttribute("arguments", "");
		this.charStart = marker.getAttribute("charStart", -1);
		this.charEnd = marker.getAttribute("charEnd", -1);
		this.categoryId = marker.getAttribute("categoryId", 0);
		this.lineNumber = marker.getAttribute("lineNumber", -1);
		this.sourceId = marker.getAttribute("sourceId", "");
		try {
			this.type = marker.getType();
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}
	
	public Problem(IMarkerDelta delta){
		this.kind = delta.getKind();
		if(this.kind == IResourceDelta.REMOVED){			
			this.stamp = delta.getResource().getModificationStamp();			
			this.resourceName = delta.getResource().getFullPath().toString();
			this.id = delta.getId();			
			this.severity = delta.getAttribute("severity", 1);
			this.message = delta.getAttribute("message", "");
			this.arguments = delta.getAttribute("arguments", "");
			this.charStart = delta.getAttribute("charStart", -1);
			this.charEnd = delta.getAttribute("charEnd", -1);
			this.categoryId = delta.getAttribute("categoryId", 0);
			this.lineNumber = delta.getAttribute("lineNumber", -1);
			this.sourceId = delta.getAttribute("sourceId", "");
			this.type = delta.getType();			
		}else{
			IMarker marker = delta.getMarker();
			this.stamp = marker.getResource().getModificationStamp();
			this.resourceName = marker.getResource().getFullPath().toString();
			this.id = marker.getId();		
			this.severity = marker.getAttribute("severity", 1);
			this.message = marker.getAttribute("message", "");
			this.arguments = marker.getAttribute("arguments", "");
			this.charStart = marker.getAttribute("charStart", -1);
			this.charEnd = marker.getAttribute("charEnd", -1);
			this.categoryId = marker.getAttribute("categoryId", 0);
			this.lineNumber = marker.getAttribute("lineNumber", -1);
			this.sourceId = marker.getAttribute("sourceId", "");
			try {
				this.type = marker.getType();
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}		
	}
}
