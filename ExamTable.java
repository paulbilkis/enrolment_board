package coursework.bilkis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.table.AbstractTableModel;

public class ExamTable extends AbstractTableModel {
	protected ArrayList<Exam> exams;
	protected static String[] columns = { "Предмет", "Аудитория", "Дата", "Число участников"};
	protected boolean changed;
	
	ExamTable(){
		exams = new ArrayList<Exam>();
		changed = false;
	}
	
	public Exam getExamById(int id){
		for (int i = 0; i<exams.size(); i++){
			if (id == exams.get(i).getId())
				return exams.get(i);
		}
		return null;
	} 
	
	public ArrayList<Exam> getExams() {
		return exams;
	}

	@Override
	public String getColumnName(int c) {
		return columns[c];
	}

	@Override
	public Class<?> getColumnClass(int c) {
		switch (c) {
		case 0:
			return String.class;
		case 1:
			return String.class;
		case 2:
			return String.class;
		case 3:
			return Integer.class;
		default:
			return String.class;
		}
	}

	@Override
	public boolean isCellEditable(int r, int c) {
		return false;
	}

	public String[] getColumns() {
		return this.columns;
	}
	
	public void clearTable() {
		exams.clear();
	 	fireTableDataChanged();
	}

	@Override
	public int getColumnCount() {
		return columns.length;
	}

	@Override
	public int getRowCount() {
		return exams.size();
	}

	@Override
	public Object getValueAt(int r, int c) {
		Exam e = exams.get(r);
		if (e == null)
			return "";
		switch (c) {
		case 0:
			return e.getSubject();
		case 1:
			return e.getClassroom();
		case 2:
			return e.getExamDate();
		case 3:
			ArrayList<Abiturent> a = MainForm.abiturents_model.getAbits();
			int count = 0;
			for (int i = 0; i < a.size(); i++)
				if (a.get(i).isExamAssign(e.getId()))
					count++;
			return count;
		default:
			return "";
		}
	}

	public void deleteRow(int r) {
		if (r < 0 || r > (getRowCount()-1))
			return;
		try{
			exams.remove(r);

		}catch (Exception e){
			System.out.println(e.getMessage());
		}
		changed = true;
		refreshTable();
	}
	
	public void changeRow(int r, Exam a){
		if (r < 0 || r > (getRowCount()-1) || a == null)
			return;
		deleteRow(r);
		exams.add(r,a);
		refreshTable();
	}
	
	public void addRow(Exam a){
		if (a == null) return;
		exams.add(a);
		changed=true;
		refreshTable();
	}
	
	public String[] getExamsStrings(){
		String[] list = new String[exams.size()];
		for (int i = 0; i<exams.size(); i++){
			list[i] = (exams.get(i).toString());
		}
		return list;
	}
	
	public int getNewId(){
		int id = 0;
		for (int i = 0; i<exams.size(); i++){
			if(exams.get(i).getId() > id)
				id = exams.get(i).getId();
		}
		return (id+1);
	}
	
	 public void refreshTable() {	 
		 fireTableDataChanged();	  
	 }
	 
	 public boolean isChanged(){
		 return (changed)? true:false;
	 }
	 
	 public void dataWasSaved(){
		 changed = false;
	 }
}
