package coursework.bilkis;
import java.util.ArrayList;
import java.util.Map;

public class Abiturent {
	    private String firstName;
	    private String lastName;
	    private String patronymic;
	    private String faculty;
	    private String studyProgram;
	    private int egeSum;
	    private boolean original;
	    private Map<Integer,String> exams; // exam_id,mark

	    public String get_csv_line (){
	    	return lastName+","+firstName+","+patronymic+","+faculty+","+studyProgram+","
	    			+Integer.toString(egeSum)+","+((original) ? "1" : "0");
	    }
	    	
	    Abiturent(String lastName, String firstName, String patronymic, String faculty, String studyProgram, int egeSum, boolean original, Map<Integer,String> exams) {
	        this.firstName = firstName;
	        this.lastName = lastName;
	        this.patronymic = patronymic;
	        this.faculty = faculty;
	        this.studyProgram = studyProgram;
	        this.egeSum = egeSum;
	        this.original = original;
	        this.exams = exams;
	    }
	    
	    public int getEgeSum() {
	    	return egeSum;
	    }
	    
	    public boolean getOriginal() {
	    	return original;
	    }
	    
	    public String getStudyProgram() {
	    	return studyProgram;
	    }

	    public String getFirstName() {
	        return firstName;
	    }

	    public String getLastName() {
	        return lastName;
	    }

	    public String getPatronymic() {
	        return patronymic;
	    }

	    public String getFaculty() {
	        return faculty;
	    }
	    
	    public int getOriginalInt(){
	    	return (original)? 1 : 0;
	    }

	    public Map<Integer,String> getExams() {
	        return exams;
	    }

	    void addExam(int exam_id, String mark) {
	        exams.putIfAbsent(exam_id, mark);
	        //System.out.printf("\nAbit: %s. Add exam. id: %d, mark: %s. Exams total: %d\n", this.toString(), exam_id, mark, exams.size());
	    }
	    
	    public boolean needExams() {
	    	return !(exams.isEmpty());
	    }
	    

	    @Override
	    public String toString() {
	        return  lastName + " " + firstName + " " + patronymic;
	    }
	    
	    public void setExamMark (int exam_id, String mark){
	    	if (exams.containsKey(exam_id)){
	    		exams.put(exam_id, mark);
	    	}
	    }
	    
	    public void removeExam (int exam_id){
	    	if (exams.containsKey(exam_id)){
	    		exams.remove(exam_id);
	    	}
	    }
	    
	    public boolean isExamAssign (int exam_id){
	    	return exams.containsKey(exam_id);
	    }
	    
	    public String getMark (int exam_id){
	    	if (exams.containsKey(exam_id)){
	    		return exams.get(exam_id);
	    	}else{
	    		return "";
	    	}
	    }

}
