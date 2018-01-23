package coursework.bilkis;

import java.util.ArrayList;
import java.util.Map;

public class ExamTableAbit extends ExamTable {
	private Map<Integer,String> marks;
	static{
		columns = new String[]{ "Предмет", "Аудитория", "Дата", "Отметка"};
	}
	ExamTableAbit(Map<Integer,String> m, ArrayList<Exam> e){
		exams = e;
		marks = m;
		changed = false;
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
			return marks.get(e.getId());
		default:
			return "";
		}
	}
	
	@Override
	public boolean isCellEditable(int r, int c)
	{
	    	return false;
	}
	  
}
