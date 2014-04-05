import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import core.ProjectCloner;
import core.SkillsUtilities;
import core.conflicts.Conflict;
import core.conflicts.ConflictFixer;
import core.eval.Eval;
import core.skills.Qualification;

import net.sf.mpxj.ProjectFile;
import net.sf.mpxj.Resource;
import net.sf.mpxj.ResourceAssignment;
import net.sf.mpxj.Task;


public class Greedy {
	
	ProjectFile project=PrepareData.project;
	ProjectFile resultProject=PrepareData.resultProject;
	ProjectFile resultProjectDO=PrepareData.resultProjectDO;
	ProjectFile resultProjectPartialFile=ProjectCloner.createBaseProject(project, false);


	
	Greedy(){
		prepareResultProject();
	}
	
	public void prepareResultProject() {
		System.out.println("prepare "+resultProject.getAllResourceAssignments().size());
		int size=resultProject.getAllResourceAssignments().size();
		for(int i=0; i<size; i++) {
			resultProject.getAllResourceAssignments().get(0).remove();
			resultProjectDO.getAllResourceAssignments().get(0).remove();
		}

	}
	
	public void printInfo(ProjectFile projectt) {
		
		System.out.println("Czas trwania projektu: "+Eval.getProjectDuration(projectt));
		System.out.println("Koszt realizacji projektu: "+Eval.getProjectCost(projectt));
		double time_component_weight = 0.8;
		double eval = Eval.evalScheduling(projectt, time_component_weight);
		System.out.println("Wartosc funkcji oceny dla projektu: "+eval);
	}
	
	//dzia³a
	public void basicGreedyWithCo() {
		printInfo(project);

		System.out.println("Optymalizacja po koszcie: ");
		System.out.println("size"+resultProject.getAllTasks().size());
		resultProject.getAllTasks().remove(0);
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
		ConflictFixer.pack(resultProject);
		ConflictFixer.fixConflicts(resultProject);
		printInfo(resultProject);
//		for (Task tt:resultProject.getAllTasks()) {
//			System.out.println(tt.getResourceAssignments().toString());
//		}
		System.out.println("czy nie ma konfliktów "+ConflictFixer.isConflictsFree(resultProject));

		
	}
	public void basicGreedyWithDo() {
		System.out.println("Optymalizacja po czasie: ");
		System.out.println("size"+resultProjectDO.getAllTasks().size());
		resultProjectDO.getAllTasks().remove(0);
		int taskSize=resultProjectDO.getAllTasks().size();
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
		printInfo(resultProjectDO);
		System.out.println("czy nie ma konfliktów "+ConflictFixer.isConflictsFree(resultProjectDO));

		
	}
	
	private void fix(ProjectFile p) {
		ConflictFixer.pack(p);
		ConflictFixer.fixConflicts(p);
	}
	
//	public void basicGreedyWithDO() {
//		System.out.println("Optymalizacja po czasie: ");
//		resultProjectDO.getAllTasks().remove(0);
//		int taskSize=resultProjectDO.getAllTasks().size();
//		int i=0;
//		Task t;
//		ResourceAssignment ra;
//		while(i<taskSize) {
//			t=resultProjectDO.getAllTasks().get(i);
//			List<Resource> resources=SkillsUtilities.resourcesCapablePerformingTask(t);
//			if(resources.size()!=0) {
//			Resource local=resources.get(0);
//			ProjectFile tempProject;
//			ra=addAssigment(t, local);
//			Task t2;
//			for(Resource r:resources) {
//				tempProject=ProjectCloner.createBaseProject(resultProjectDO, true);
//				t2=tempProject.getTaskByID(t.getID());
//				addAssigment(t2,r);
//				ConflictFixer.fixConflicts(resultProjectDO);			
//				if(Eval.getProjectDuration(tempProject)<Eval.getProjectDuration(resultProjectDO)) {
//					t.getResourceAssignments().clear();
//					addAssigment(t,r);
//				}
//			}
//			}
//			i++;
//		}
//		System.out.println(resultProjectDO.getAllResourceAssignments().size());
//
//		System.out.println(resultProjectDO.getAllResourceAssignments().size());
//		System.out.println(ConflictFixer.isConflictsFree(resultProjectDO));
//		ConflictFixer.fixConflicts(resultProjectDO);
//		System.out.println("wolny od konfliktów: "+ConflictFixer.isConflictsFree(resultProjectDO));
//		printInfo(resultProjectDO);
//		for (Task tt:resultProjectDO.getAllTasks()) {
//			System.out.println(tt.getResourceAssignments().toString());
//		}
//		
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
