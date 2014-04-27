
import java.io.IOException;
import java.util.List;


import net.sf.mpxj.MPXJException;
import net.sf.mpxj.ProjectFile;
import net.sf.mpxj.Relation;
import net.sf.mpxj.ResourceAssignment;
import net.sf.mpxj.Task;
import net.sf.mpxj.mpp.MPPReader;
import net.sf.mpxj.mspdi.MSPDIWriter;
import net.sf.mpxj.mspdi.schema.Project.Resources.Resource;


public class PrepareData {
	
	public static ProjectFile project;
	public static ProjectFile resultProject;
	public static ProjectFile resultProjectDO;
	public static ProjectFile BNBproject;

	static String s="C:/Users/El¿bieta/Desktop/Sztuczna/D0/100_20_23_9_D1.mpp";


	
	public void readFromFile() throws MPXJException {
		project= new MPPReader().read(s);
		resultProject=new MPPReader().read(s);
		resultProjectDO=new MPPReader().read(s);
		BNBproject=new MPPReader().read(s);

	}
	
	public void printAllTasks() {
		for (Task task : project.getAllTasks())
		{
		System.out.println(task.toString()+"   "+task.getCost()+"  "+task.getResourceAssignments().toString());
		}
	}
	
	public void writeToFile() throws IOException {
		new MSPDIWriter().write(resultProject, s+"Wynik.xml");
		new MSPDIWriter().write(resultProjectDO, s+"WynikDO.xml");

	}


	// metoda skopiowana z przyk³adów, ale coœ jest w niej Ÿle
//	public void iterateThroughtRelations() {		
//	
//		for (Task task : project.getAllTasks())
//		{
//		List<Relation> successors = task.getSuccessors();
//		if (successors!= null && successors.isEmpty() == false)
//		{
//		System.out.println(task.getName() + " nastepniki:");
//		for (Relation relation : successors)
//		{
//		System.out.println(" Zadanie: " +
//		project.getTaskByUniqueID(relation.getTaskUniqueID()).getName());
//		System.out.println(" Typ: " + relation.getType());
//		System.out.println(" Opoznienie: " + relation.getDuration());
//		}
//		}
//	}}
	
	public void iterateThroughtP(ProjectFile project) {
		for (ResourceAssignment assignment : project.getAllResourceAssignments())
		{
		Task task = assignment.getTask();
		
		String taskName;
		if (task == null) taskName = "(puste zadanie)";
		else taskName = task.getName();
		net.sf.mpxj.Resource resource = assignment.getResource();
		String resourceName;
		if (resource == null) resourceName = "(pusty zasob)";
		else resourceName = resource.getName();
		System.out.println("Przydzial: Zadanie=" + taskName + " Zasob=" + resourceName);
	}
	}
	
	public static void iterateThroughtTasks(ProjectFile project) {
		for (Task task : project.getAllTasks())
		{
		System.out.println("Przydzialy do zadania " + task.getName() + ":");
		for (ResourceAssignment assignment : task.getResourceAssignments())
		{
		net.sf.mpxj.Resource resource = assignment.getResource();
		String resourceName;
		if (resource == null) resourceName = "(pusty zasob)";
		else resourceName = resource.getName();
		System.out.println(" " + resourceName);
		}
		}
	}
	
	
}
