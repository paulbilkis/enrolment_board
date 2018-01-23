package coursework.bilkis;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

/**
 * Табличная модель, капелюшку заточенная под мои нужны.
 * 
 * @author bilkis
 * 
 */
public class AbitTable extends AbstractTableModel {
	/**
	 * Массив названий колонок.
	 */
	/*
	 * private static String[] columns = { "ФИО", "Факультет", "Предмет",
	 * "Номер аудитории", "Дата", "Оценка" };
	 */
	protected static String[] columns = { "ФИО", "Факультет",
			"Вступительные экзамены", "Направление", "Сумма баллов за ЕГЭ",
			"Оригинал аттестата" };
	protected boolean changed;

	protected ArrayList<Abiturent> abits;

	public AbitTable() {
		// super(null, columns);
		abits = new ArrayList<Abiturent>();
		changed = false;
	}
	
	public ArrayList<Abiturent> getAbits() {
		return abits;
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
			return String.class;
		case 4:
			return int.class;
		case 5:
			return String.class;
		default:
			return null;
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
		abits.clear();
	 	fireTableDataChanged();
	}

	@Override
	public int getColumnCount() {
		return columns.length;
	}

	@Override
	public int getRowCount() {
		return abits.size();
	}

	@Override
	public Object getValueAt(int r, int c) {
		switch (c) {
		case 0:
			return abits.get(r).toString();
		case 1:
			return abits.get(r).getFaculty();
		case 2:
			return ((abits.get(r).needExams()) ? "да" : "нет");
		case 3:
			return abits.get(r).getStudyProgram();
		case 4:
			return abits.get(r).getEgeSum();
		case 5:
			return ((abits.get(r).getOriginal()) ? "да" : "нет");
		default:
			return "";
		}
	}

	public void deleteRow(int r) {
		if (r < 0 || r > (getRowCount()-1))
			return;
		try{
			abits.remove(r);
		}catch (Exception e){
			System.out.println(e.getMessage());
		}
		changed = true;
		refreshTable();
	}
	
	public void deleteRow (int[] r)
	{
		for (int i = 0; i < r.length; i++)
			deleteRow(r[i]);
	}
	
	public void changeRow(int r, Abiturent a){
		if (r < 0 || r > (getRowCount()-1) || a == null)
			return;
		deleteRow(r);
		abits.add(r,a);
		refreshTable();
	}
	
	public void addRow(Abiturent a){
		if (a == null) return;
		abits.add(a);
		changed=true;
		refreshTable();
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
