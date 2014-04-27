import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.text.html.MinimalHTMLWriter;

import core.ProjectCloner;
import core.SkillsUtilities;
import core.conflicts.ConflictFixer;
import core.eval.Eval;

import net.sf.mpxj.ProjectFile;
import net.sf.mpxj.Resource;
import net.sf.mpxj.ResourceAssignment;
import net.sf.mpxj.Task;
import net.sf.mpxj.mspdi.MSPDIWriter;


public class MyNode{

	MyNode parent;
	List<MyNode> children;
	ProjectFile project;
	List<Integer> unschelduledID;
	int poziom;
	boolean liczDalej=true;
	
	
	public MyNode(MyNode parent) {
		this.parent=parent;
		children=new ArrayList();
		project=getParentProject();
		unschelduledID=unschelduledParent();
		poziom=parent.poziom+1;
		this.parent=null;
	}
	
	public MyNode() {

	}
	
	public void addChild(MyNode child) {
		children.add(child);
	}
	
	public ProjectFile getParentProject() {
		return ProjectCloner.createBaseProject(parent.project, true);
	}
	
	public List<Integer> unschelduledParent(){
		unschelduledID=new ArrayList();
		for(Integer tp: parent.unschelduledID) {
			unschelduledID.add(tp);
		}
		return unschelduledID;
	}
	
	public void addAssigment(){
		
	}
	
		public ResourceAssignment addAssigment(Task t, Resource local) {
			ResourceAssignment ra;
			ra = t.addResourceAssignment(local);
			ra.setStart(t.getStart());
			ra.setWork(t.getDuration());
			ra.setRemainingWork(ra.getWork());
			ra.setCost(ra.getWork().getDuration()*local.getStandardRate().getAmount());	
			return ra;
		}
		
		
		public void addChildren() {
			List<Task> unsch=new ArrayList();
			for(int id:unschelduledID) {
				unsch.add(project.getTaskByID(id));
			}
			System.out.println("xx"+unsch.size() +"poziom: "+poziom);

			for(Task t: unsch) {
				List<Resource> resources=SkillsUtilities.resourcesCapablePerformingTask(t);
//				System.out.println("yy"+resources.toString());
				for(Resource r: resources) {
					MyNode child=new MyNode(this);
					child.unschelduledID.remove(t.getID());
					int id=t.getID();
					ResourceAssignment ra=addAssigment(child.project.getTaskByID(id),r);
					this.children.add(child);
					double koszt=Eval.getProjectCost(child.project);
					if(unsch.size()==1) {
						if(koszt<BNB.globalMinKoszt) {
							System.out.println("Koszt realizacji projektu: "+koszt);
							BNB.globalMinKoszt=koszt;
							System.out.println("                                                              Uwaga!: "+BNB.globalMinKoszt);
							PrepareData.iterateThroughtTasks(child.project);						
							BNB.bestProject=child.project;
						}
					}
					
					if(poziom>1) {
						if(koszt<BNB.minimaNaPoziomach[poziom+1]) {
							BNB.minimaNaPoziomach[poziom+1]=koszt;
						}
					}
			//		System.out.println(ra.toString());
				}
			}
		}
		
		public void addChildrenDO() {
			List<Task> unsch=new ArrayList();
			for(int id:unschelduledID) {
				unsch.add(project.getTaskByID(id));
			}

			System.out.println("xx"+unsch.size() +"poziom: "+poziom);
			for(Task t: unsch) {
				List<Resource> resources=SkillsUtilities.resourcesCapablePerformingTask(t);
//				System.out.println("yy"+resources.toString());
				for(Resource r: resources) {
					MyNode child=new MyNode(this);
					child.unschelduledID.remove(t.getID());
					int id=t.getID();
					ResourceAssignment ra=addAssigment(child.project.getTaskByID(id),r);
					this.children.add(child);
	//				child.project=ProjectCloner.createBaseProject(child.project, true);
					ConflictFixer.pack(child.project);
					ConflictFixer.fixConflicts(child.project); 
					double czas=Eval.getProjectDuration(child.project);
					double koszt=Eval.getProjectCost(child.project);
					child.liczDalej=child.czyLiczycDalejDO();
					if(unsch.size()==1) {
						if(czas<BNB.globalMinTime) {
							System.out.println("Czas realizacji projektu: "+czas+"    koszt"+koszt);
							BNB.globalMinTime=czas;
							System.out.println("                                                              Uwaga!: "+BNB.globalMinTime);
							PrepareData.iterateThroughtTasks(child.project);						
							BNB.bestProjectDO=child.project;
							StaticGreedy.save(child.project);
						}
					}
//					
					if(poziom>1) {
						if(czas<BNB.minimaNaPoziomachDO[poziom+1]) {
							BNB.minimaNaPoziomachDO[poziom+1]=czas;
						}
					}
			//		System.out.println(ra.toString());
				}
			}
		}
		
		
		public void addChildren2() {
			List<Task> unsch=new ArrayList();
			for(int id:unschelduledID) {
				unsch.add(project.getTaskByID(id));
			}
	//		System.out.println("xx"+unsch.size() +"poziom: "+poziom);

			Task t=unsch.get(0);
				List<Resource> resources=SkillsUtilities.resourcesCapablePerformingTask(t);
//				System.out.println("yy"+resources.toString());
				for(Resource r: resources) {
					MyNode child=new MyNode(this);
					child.unschelduledID.remove(t.getID());
					int id=t.getID();
					ResourceAssignment ra=addAssigment(child.project.getTaskByID(id),r);
					this.children.add(child);
//					ConflictFixer.pack(child.project);
//					ConflictFixer.fixConflicts(child.project);
					double koszt=Eval.getProjectCost(child.project);
//					double time=Eval.getProjectDuration(child.project);

					if(unsch.size()==1) {
						if(koszt<BNB.globalMinKoszt //|| (koszt==BNB.globalMinKoszt && time< BNB.minCzasDlaCO) 
								) {
							System.out.println("Koszt realizacji projektu: "+koszt);
							BNB.globalMinKoszt=koszt;
							child.project=ProjectCloner.createBaseProject(child.project, true);
							ConflictFixer.pack(child.project);
							ConflictFixer.fixConflicts(child.project);
							double time=Eval.getProjectDuration(child.project);
					//		BNB.minCzasDlaCO=time;
							System.out.println("                           koszt                                   Uwaga!: "+BNB.globalMinKoszt);
							System.out.println("                           czas                                   Uwaga!: "+time);
							
			//				PrepareData.iterateThroughtTasks(child.project);
							BNB.bestProject=child.project;
						}
						
					}
					
					if(poziom>1) {
						if(koszt<BNB.minimaNaPoziomach[poziom+1]) {
							BNB.minimaNaPoziomach[poziom+1]=koszt;
						}
					}
			//		System.out.println(ra.toString());
				}
			
		}
		
//		public void addChildrenDO2() {
//			List<Task> unsch=new ArrayList();
//			for(int id:unschelduledID) {
//				unsch.add(project.getTaskByID(id));
//			}
////			System.out.println("xx"+unsch.size() +"poziom: "+poziom);
//			Task t=unsch.get(0);
//				List<Resource> resources=SkillsUtilities.resourcesCapablePerformingTask(t);
////				System.out.println("yy"+resources.toString());
//				for(Resource r: resources) {
//					MyNode child=new MyNode(this);
//					child.unschelduledID.remove(t.getID());
//					int id=t.getID();
//					ResourceAssignment ra=addAssigment(child.project.getTaskByID(id),r);
//					this.children.add(child);
//					double czas=Eval.getProjectDuration(child.project);
//					child.project=ProjectCloner.createBaseProject(child.project, true);
//					ConflictFixer.pack(child.project);
//					ConflictFixer.fixConflicts(child.project);
//					czas=Eval.getProjectDuration(child.project);
//					double time=Eval.getProjectCost(child.project);
//					if(unsch.size()==1) {
//						if(czas<BNB.globalMinTime) {
//							System.out.println("Czas realizacji projektu: "+czas+"   koszt:"+time);
//							BNB.globalMinTime=czas;
//							System.out.println("                                                              Uwaga!: "+BNB.globalMinTime);
//						//	PrepareData.iterateThroughtTasks(child.project);						
//							BNB.bestProjectDO=child.project;
//						}
//					}
//					
//					if(poziom>1) {
//						if(czas<BNB.minimaNaPoziomachDO[poziom+1]) {
//							BNB.minimaNaPoziomachDO[poziom+1]=czas;
//						}
//					}
//			//		System.out.println(ra.toString());
//				}
//			
//		}
//		
		public double getKoszt() {
			return Eval.getProjectCost(project);
		}
		
		public double getCzas() {
//			ConflictFixer.pack(project);
//			ConflictFixer.fixConflicts(project);
			return Eval.getProjectDuration(project);
		}
		
		public boolean czyLiczycDalej() {
			int margin=1;
			System.out.println("poziom"+poziom);
			System.out.println("k"+getKoszt());
			System.out.println(BNB.minimaNaPoziomach[poziom]);
			if(getKoszt()<BNB.minimaNaPoziomach[poziom]+margin) {
				return true;
			}
			else {
				return false;
			}
		}
		
		public boolean czyLiczycDalej2() {

			System.out.println(poziom);
			System.out.println(getKoszt());
			System.out.println(StaticGreedy.basicGreedyWithCo(project, unschelduledID));
			System.out.println();
			if(getKoszt()<=StaticGreedy.basicGreedyWithCo(project, unschelduledID)) {
				return true;
			}
			else {
				return false;
			}
		}
		
		public boolean czyLiczycDalejDO() {
			double margin=0;
			double greedy=0;
//			double greedy=StaticGreedy.basicGreedyWithDo(project, unschelduledID);
			double czas=getCzas();
//			System.out.println("bnbglobanmintime"+BNB.globalMinTime);
//			System.out.println("gre"+greedy);
//			PrepareData.iterateThroughtTasks(project);
			System.out.println("poziom"+poziom);

			if (czas<BNB.globalMinTime && czas<=BNB.minimaNaPoziomachDO[poziom]+margin 
				//	&& czas<=(greedy=StaticGreedy.basicGreedyWithDo(project, unschelduledID))
					) {
				System.out.println("bnbglobanmintime"+BNB.globalMinTime);
				System.out.println("cz"+getCzas());
			//	System.out.println("gre"+greedy);
				return true;
			}
//			else 
//				if(
////					czas<=greedy && 
//					czas<=BNB.globalMinTime && czas<=BNB.minimaNaPoziomachDO[poziom]+margin ) {

//				return true;
//			}
			else {
			//	deleteParent(this);
				return false;
			}
		}
	
		public void deleteParent(MyNode n) {
			while(n.poziom>0) {
			deleteParent(n.parent);
			n.parent=null;
			}
		}
	
}
