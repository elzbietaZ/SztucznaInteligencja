import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import core.ProjectCloner;
import core.SkillsUtilities;
import core.conflicts.ConflictFixer;
import core.eval.Eval;

import net.sf.mpxj.ProjectFile;
import net.sf.mpxj.Relation;
import net.sf.mpxj.Resource;
import net.sf.mpxj.ResourceAssignment;
import net.sf.mpxj.Task;
import net.sf.mpxj.mspdi.MSPDIWriter;


public class BNB {
	
//	ProjectFile project=PrepareData.project;
	ProjectFile BNBproject=PrepareData.BNBproject;
	ProjectFile BNBprojectSorted;
	List<Integer> unschelduledTasksID;
	List<List<ProjectFile>> bruteforce;
	static double globalMinKoszt=555555555;
	static double globalMinTime=55555555;

	static double [] minimaNaPoziomach;
	static double [] minimaNaPoziomachDO;

	static ProjectFile bestProject;
	static ProjectFile bestProjectDO;
	
	static double minCzasDlaCO=Double.MAX_VALUE;

	
	
	public void prepareResultProject() {
		
		unschelduledTasksID = new ArrayList<Integer>();
		bruteforce=new ArrayList<List<ProjectFile>>();
		BNBproject.getAllTasks().remove(0);

		
		int size=BNBproject.getAllResourceAssignments().size();
		int taskSize=BNBproject.getAllTasks().size();
		for(int i=0; i<size; i++) {
			BNBproject.getAllResourceAssignments().get(0).remove();
		}
	//	BNBprojectSorted=ProjectCloner.createBaseProject(BNBproject, false);

		for(int i=0; i<taskSize; i++) {
			List<Resource> resources=SkillsUtilities.resourcesCapablePerformingTask(BNBproject.getAllTasks().get(i));	
			if(resources.size()!=0) {
			int x=BNBproject.getAllTasks().get(i).getID();
			unschelduledTasksID.add(x);
			}
		}
		minimaNaPoziomach=new double[size+1];
		for(int i=0; i<minimaNaPoziomach.length; i++) {
			minimaNaPoziomach[i]=Double.MAX_VALUE;
		}
		
		minimaNaPoziomachDO=new double[size+1];
		for(int i=0; i<minimaNaPoziomachDO.length; i++) {
			minimaNaPoziomachDO[i]=Double.MAX_VALUE;
		}
		

		
		
	}
	
	public BNB() {
		prepareResultProject();
//		sortTasks();
	}
	
	public void sortTasks() {
	
		int j=0;
		List<Relation> predecessors=new ArrayList<Relation>();
		List<Integer> hasPrede=new ArrayList<Integer>();
		
		int size=BNBproject.getAllTasks().size();
		while(j<size) {
			predecessors=BNBproject.getAllTasks().get(j).getPredecessors();
			if(predecessors!=null) {
				Task t=BNBproject.getAllTasks().get(j);
				System.out.println("mmm"+t.getID());
				hasPrede.add(t.getID());			
			//	unschelduledTasksID.add(t.getID());
			}
			j++;
		}
		unschelduledTasksID.removeAll(hasPrede);
		unschelduledTasksID.addAll(hasPrede);
		System.out.println(unschelduledTasksID.toString());
	}
	
	public void printInfo(ProjectFile projectt) {
		
		ConflictFixer.pack(projectt);
		ConflictFixer.fixConflicts(projectt);
		System.out.println("Czas trwania projektu: "+Eval.getProjectDuration(projectt));
		System.out.println("Koszt realizacji projektu: "+Eval.getProjectCost(projectt));
//		double time_component_weight = 0.8;
//		double eval = Eval.evalScheduling(projectt, time_component_weight);
//		System.out.println("Wartosc funkcji oceny dla projektu: "+eval);
	}
	
	public void makeMyTree() {
		System.out.println(BNBproject.getAllTasks().toString());
		MyTree myTree=new MyTree(BNBproject,unschelduledTasksID);
		buildTree(myTree.root);	
		bestProject=ProjectCloner.createBaseProject(bestProject, true);
		printInfo(bestProject);		
		PrepareData.iterateThroughtTasks(bestProject);
		System.out.println("czy nie ma konfliktów: "+ConflictFixer.isConflictsFree(bestProject));
		try {
			new MSPDIWriter().write(bestProject, PrepareData.s+"BNBNoweObliczenia.xml");
		} catch (IOException e) {
			e.printStackTrace();
		}
	
	}
	
	public void makeMyTreeDO() {
		
		System.out.println(BNBproject.getAllTasks().toString());
		MyTree myTree=new MyTree(BNBproject,unschelduledTasksID);
		buildTreeDO(myTree.root);
	    bestProjectDO=ProjectCloner.createBaseProject(bestProjectDO, true);
		printInfo(bestProjectDO);
		PrepareData.iterateThroughtTasks(bestProjectDO);
		System.out.println("czy nie ma konfliktów: "+ConflictFixer.isConflictsFree(bestProjectDO));
		try {
			new MSPDIWriter().write(bestProjectDO, PrepareData.s+"BNBWynikDO.xml");
		} catch (IOException e) {
			e.printStackTrace();
		}


	
	}
	
	public void buildTree(MyNode node) {
			node.addChildren2();
			for(MyNode child: node.children) {
				if(child.unschelduledID.size()>0 && child.czyLiczycDalej2()) {
				buildTree(child);
				}
							
			}
	}
	
	public void buildTreeDO(MyNode node) {
		node.addChildrenDO();
		for(MyNode child: node.children) {
			if(child.unschelduledID.size()>0 && child.liczDalej) {
			buildTreeDO(child);
			}
						
		}
}
	
	public void algotytmBruteForce() {
		System.out.println("BNB - optymalizacja po koszcie: ");
		BNBproject.getAllTasks().remove(0);
		int taskSize=BNBproject.getAllTasks().size();
		int i=0;
		Task t;
		List<Task> startList=BNBproject.getAllTasks();	
		List unschelduledWlasciwa=new ArrayList();
		unschelduledWlasciwa.addAll(unschelduledTasksID);
		List<Task> availableTasks=startList;
		sortTasks(); 
		while(i<taskSize) {
			bruteforce.add(new ArrayList<ProjectFile>());
			for(Task tt: availableTasks) {
				List<Resource> resources=SkillsUtilities.resourcesCapablePerformingTask(tt);
				for(Resource r: resources) {
					List<ProjectFile> projects=new ArrayList<ProjectFile>();
					int id=tt.getID();
					ProjectFile p=ProjectCloner.createBaseProject(tt.getParentFile(), true);
					ResourceAssignment ra=addAssigment(p.getTaskByID(id), r, p);
	//				System.out.println(p.getAllResourceAssignments().toString());
					projects.add(p);
					for(ProjectFile pp: projects) {
						ProjectFile p2=ProjectCloner.createBaseProject(pp, true);
						
					}
					bruteforce.get(i).add(p);
				}
			}
			i++;
		}
		traverse();
	}
	
	public void traverse() {
		for(List<ProjectFile> list: bruteforce) {
			for(ProjectFile p:list) {
				ConflictFixer.fixConflicts(p);
				PrepareData.iterateThroughtTasks(p);
				printInfo(p);
			}
		}
	}
	
	public List<Task> getAvailableTasks(List<Task> lista){
		List<Task> availableTasks=new ArrayList<>();
		for(Task t:lista) {
			if(t.getPredecessors()==null) {
				availableTasks.add(t);
			}
			else {
				List<Relation> relacje=t.getPredecessors();
				for(Relation r:relacje) {
					int id=r.getTargetTask().getID();
					if(!unschelduledTasksID.contains(id)) {
						availableTasks.add(t);
					}
				}
			}
		}
		System.out.println(availableTasks.toString());		
		return availableTasks;
	}
	
	public void think() {
		ProjectFile p=ProjectCloner.createBaseProject(BNBproject, false);
		PrepareData.iterateThroughtTasks(p);
		System.out.println(p.getAllResources().size());
		System.out.println(p.getAllResources().get(0));
		

		
	}
	
	public ResourceAssignment addAssigment(Task t, Resource local, ProjectFile p) {
		ResourceAssignment ra=new ResourceAssignment(p);
		ra = t.addResourceAssignment(local);
		ra.setStart(t.getStart());
		ra.setWork(t.getDuration());
		ra.setRemainingWork(ra.getWork());
		ra.setCost(ra.getWork().getDuration()*local.getStandardRate().getAmount());	
		return ra;

	}
	
	
	

}
