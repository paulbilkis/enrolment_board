package coursework.bilkis;

class Exam {
    private String subject;
    private String classroom;
    private String examDate;
    private int id;
    
    
    public String get_csv_line (){
    	return subject+","+classroom+","+examDate+","+id;
    }
    public Exam(String subject, String classroom, String examDate, int id) {
        this.subject = subject;
        this.classroom = classroom;
        this.examDate = examDate;
        this.id = id;
    }
    
    public int getId(){
    	return this.id;
    }

    public Exam(Exam exam) {
        this(exam.getSubject(), exam.getClassroom(), exam.getExamDate(), exam.getId());
    }

    public String getSubject() {
        return subject;
    }

    public String getClassroom() {
        return classroom;
    }

    public String getExamDate() {
        return examDate;
    }
    
    public String toString(){
    	return subject+", ауд. "+classroom+", дата: "+examDate;
    }
}

