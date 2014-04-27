import java.util.ArrayList;
import java.util.List;

import core.SkillsUtilities;

import net.sf.mpxj.ProjectFile;
import net.sf.mpxj.Resource;
import net.sf.mpxj.ResourceAssignment;
import net.sf.mpxj.Task;


public class MyTree {
	
	MyNode root;

	
	public MyTree(ProjectFile project, List<Integer> unsch) {
		root=new MyNode();
		root.children=new ArrayList();
		root.unschelduledID=unsch;
		root.project=project;
		root.poziom=0;
	}
	
	public void addChild(MyNode child) {
		root.children.add(child);
	}
	
//	public void addChildren() {
//		for(Task t: root.unschelduled) {
//			System.out.println(t.toString());
//			List<Resource> resources=SkillsUtilities.resourcesCapablePerformingTask(t);
//			for(Resource r: resources) {
//				MyNode child=new MyNode(root);
//				int id=t.getID();
//				addAssigment(child.project.getTaskByID(id),r);
//			//	child.unschelduled.remove(t);
//			}
//
//		}
//	}
	

	public ResourceAssignment addAssigment(Task t, Resource local) {
		ResourceAssignment ra;
		ra = t.addResourceAssignment(local);
		ra.setStart(t.getStart());
		ra.setWork(t.getDuration());
		ra.setRemainingWork(ra.getWork());
		ra.setCost(ra.getWork().getDuration()*local.getStandardRate().getAmount());	
		return ra;
	}

}
