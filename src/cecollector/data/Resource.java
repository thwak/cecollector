package cecollector.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Resource implements Serializable {
	private static final long serialVersionUID = 7840078932186884232L;
	public String name;
	public TreeMap<Long, String> history;
	public Map<Long, List<Problem>> problems;
	
	public Resource(String name){
		this.name = name;
		history = new TreeMap<Long, String>();
		problems = new HashMap<Long, List<Problem>>();
	}
	
	public void addContent(long stamp, String content){
		history.put(stamp, content);
	}
	
	public void addProblem(long stamp, Problem problem){
		if(problems.containsKey(stamp)){
			problems.get(stamp).add(problem);
		}else{
			List<Problem> list = new ArrayList<Problem>();
			list.add(problem);
			problems.put(stamp, list);
		}
	}
}
