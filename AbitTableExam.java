package coursework.bilkis;

import java.util.ArrayList;

public class AbitTableExam extends AbitTable{
	static {
		columns = new String[] { "ФИО", "Факультет", "Оценка" };
	}
	private int exam_id;
	public AbitTableExam(ArrayList<Abiturent> a, int exam_id){
		abits = a;
		this.exam_id = exam_id;
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
		default:
			return null;
		}
	}
	
	@Override
	public Object getValueAt(int r, int c) {
		switch (c) {
		case 0:
			return abits.get(r).toString();
		case 1:
			return abits.get(r).getFaculty();
		case 2:
			return abits.get(r).getMark(exam_id);
		default:
			return "";
		}
	}
}
