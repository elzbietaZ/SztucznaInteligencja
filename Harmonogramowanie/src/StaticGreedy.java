import java.io.IOException;
import java.util.List;

import net.sf.mpxj.ProjectFile;
import net.sf.mpxj.Resource;
import net.sf.mpxj.ResourceAssignment;
import net.sf.mpxj.Task;
import net.sf.mpxj.mspdi.MSPDIWriter;
import core.ProjectCloner;
import core.SkillsUtilities;
import core.conflicts.ConflictFixer;
import core.eval.Eval;


public class StaticGreedy {
	
	
	public static double basicGreedyWithDo(ProjectFile project, List<Integer> unschelduled) {
	
//		System.out.println("un"+unschelduled.size());
		ProjectFile resultProjectDO=ProjectCloner.createBaseProject(project, false);
		int taskSizeInitial=resultProjectDO.getAllTasks().size();
		
		
		for(int i=0; i<unschelduled.size();i++) {
			int index=unschelduled.get(i);
			try {
			resultProjectDO.getTaskByID(index).remove();
			}
			catch(NullPointerException e){
			}
			
			}
		

		int taskSize=resultProjectDO.getAllTasks().size();
//		System.out.println("ts"+taskSize);
		int i=0;
		Task t;
		
		while(i<taskSize) {
			t=resultProjectDO.getAllTasks().get(i);
			List<Resource> resources=SkillsUtilities.resourcesCapablePerformingTask(t);		
			if(resources.size()!=0) {
			Resource local=resources.get(0); // tutaj siê wysypuje jak usuniemy powi¹zania
			Resource temp;
			ResourceAssignment localAssigment;
			localAssigment=addAssigment(t, local);
			fix(resultProjectDO);
			double localDuration=Eval.getProjectDuration(resultProjectDO);
			ResourceAssignment tempAssigment=new ResourceAssignment(resultProjectDO);;
			double tempDuration;

			for(Resource r:resources) {
				temp=r;
				localAssigment.remove();
				tempAssigment=addAssigment(t, r);
				fix(resultProjectDO);
				tempDuration=Eval.getProjectDuration(resultProjectDO);
				if(tempDuration<localDuration) {
					localAssigment=addAssigment(t,r);
					fix(resultProjectDO);
					localDuration=Eval.getProjectDuration(resultProjectDO);
					local=temp;
				}				
					tempAssigment.remove();				
			}

			addAssigment(t, local);
			}
			i++;
		}
		fix(resultProjectDO);
	//	System.out.println("start");
	//	PrepareData.iterateThroughtTasks(resultProjectDO);
	//	System.out.println("stop");
		return Eval.getProjectDuration(resultProjectDO);
		
	}
	
	public static double basicGreedyWithCo(ProjectFile project, List<Integer> unschelduled) {
		ProjectFile resultProject=ProjectCloner.createBaseProject(project, false);
	//	int taskSizeInitial=resultProjectDO.getAllTasks().size();
		
		
		for(int i=0; i<unschelduled.size();i++) {
			int index=unschelduled.get(i);
			try {
			resultProject.getTaskByID(index).remove();
			}
			catch(NullPointerException e){
			}
			
			}
		int taskSize=resultProject.getAllTasks().size();
		int i=0;
		Task t;
		
		while(i<taskSize) {
			t=resultProject.getAllTasks().get(i);
			List<Resource> resources=SkillsUtilities.resourcesCapablePerformingTask(t);		
			if(resources.size()!=0) {
			Resource local=resources.get(0); // tutaj siê wysypuje jak usuniemy powi¹zania
			Resource temp;
			ResourceAssignment localAssigment;
			localAssigment=addAssigment(t, local);
			double localCost=Eval.getProjectCost(resultProject);
			ResourceAssignment tempAssigment=new ResourceAssignment(resultProject);;
			double tempCost;

			for(Resource r:resources) {
				temp=r;
				localAssigment.remove();
				tempAssigment=addAssigment(t, r);
				tempCost=Eval.getProjectCost(resultProject);
				if(tempCost<localCost) {
					localAssigment=addAssigment(t,r);
					localCost=Eval.getProjectCost(resultProject);
					local=temp;
				}				
					tempAssigment.remove();				
			}

			addAssigment(t, local);
			}
			i++;
		}
		return Eval.getProjectCost(resultProject);

		
	}
	
	public static ResourceAssignment addAssigment(Task t, Resource local) {
		ResourceAssignment ra;
		ra = t.addResourceAssignment(local);
		ra.setStart(t.getStart());
		ra.setWork(t.getDuration());
		ra.setRemainingWork(ra.getWork());
		ra.setCost(ra.getWork().getDuration()*local.getStandardRate().getAmount());	
		return ra;

	}
	

	private static void fix(ProjectFile p) {
		ConflictFixer.pack(p);
		ConflictFixer.fixConflicts(p);
	}

	
	public static void save(ProjectFile bestProject) {
	
		bestProject=ProjectCloner.createBaseProject(bestProject, true);
		ConflictFixer.pack(bestProject);
		ConflictFixer.fixConflicts(bestProject);
		System.out.println("Czas trwania projektu: "+Eval.getProjectDuration(bestProject));
		System.out.println("Koszt realizacji projektu: "+Eval.getProjectCost(bestProject));	
		PrepareData.iterateThroughtTasks(bestProject);
		System.out.println("czy nie ma konfliktów: "+ConflictFixer.isConflictsFree(bestProject));
		try {
			new MSPDIWriter().write(bestProject, PrepareData.s+"BNBNoweObliczenia19.xml");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
