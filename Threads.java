package coursework.bilkis;

import java.util.ArrayList;

class SaveThread extends Thread{
	private ArrayList<Abiturent> abits;
	private String filename;
	private Object mutex;
	
	public SaveThread (ArrayList<Abiturent> a, String f, Object m){
		abits = a;
		filename = f;
		mutex = m;
	}
	public void run(){
		System.out.println("Starting save thread ");
		synchronized (mutex){
			System.out.println("In locked region (save thread)");
			DataFileProcess inst = new DataFileProcess(filename);
			inst.saveData(abits);
			mutex.notify();
		}
		System.out.println("Release mutex");
	}
};

class LoadThread extends Thread{
	private ArrayList<Abiturent> abits;
	private String filename;
	private Object mutex;
	LoadThread (ArrayList<Abiturent> a, String f, Object m){
		abits = a;
		filename = f;
		mutex = m;
	}
	public void run(){
		System.out.println("Starting load thread: ");
		synchronized (mutex) {
			System.out.println("In locked region (load)");
			DataFileProcess inst = new DataFileProcess(filename);
			inst.loadData(abits);
			mutex.notify();
		}
		System.out.println("Released mutex!");
	}
};

class ReportThread extends Thread{
	private Object mutex;
	private MainForm obj;
	private String type;
	ReportThread (MainForm a, String t, Object m){
		obj = a;
		type = t;
		mutex = m;
	}
	public void run(){
		synchronized (mutex) {
			obj.createReport(type);
			mutex.notify();
		}
	}
}
