package coursework.bilkis;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Pattern;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class ShowExamsWindow extends JFrame{
	private JTable table;
	private ExamTableAbit tableModel;
	private JPanel panel;
	private int[] rowClicked;
	ArrayList<Exam> exams;
	
	public ShowExamsWindow(final Abiturent abit, ArrayList<Exam> e){
		exams = new ArrayList<Exam>();
		System.out.println("Rows exams total: "+abit.getExams().size());
		for (Integer key : abit.getExams().keySet()){
			for (int i = 0; i < e.size(); i++){
				if (e.get(i).getId() == key){
					exams.add(e.get(i));
					System.out.printf("\nExam row added. exam_id=%d, key=%d", e.get(i).getId(), key);
				}
			}
		}
		
		// Подготовка выпадающего меню
		final JMenuItem delete_exam = new JMenuItem("Удалить");
		final JMenu set_mark = new JMenu("Выставить оценку");
		JMenuItem high_mark = new JMenuItem("Отлично");
		JMenuItem medium_mark = new JMenuItem("Хорошо");
		JMenuItem low_mark = new JMenuItem("Удовлетворительно");
		JMenuItem bad_mark = new JMenuItem("Неудовлетворительно");
		set_mark.add(high_mark);
		set_mark.add(medium_mark);
		set_mark.add(low_mark);
		set_mark.add(bad_mark);

		//Слушатели для popup
		delete_exam.addActionListener (new ActionListener() 
		{
			public void actionPerformed (ActionEvent event) 
			{
				for (int i = 0; i < rowClicked.length; i++){
					abit.removeExam(tableModel.getExams().get(rowClicked[i]).getId());
					tableModel.getExams().remove(rowClicked[i]);
					tableModel.refreshTable();
				}
			}
		});
		
		high_mark.addActionListener (new ActionListener() 
		{
			public void actionPerformed (ActionEvent event) 
			{
				for (int i = 0; i < rowClicked.length; i++){
					abit.setExamMark(exams.get(rowClicked[i]).getId(), "Отлично");
					tableModel.refreshTable();
				}
			}
		});
		
		medium_mark.addActionListener (new ActionListener() 
		{
			public void actionPerformed (ActionEvent event) 
			{
				for (int i = 0; i < rowClicked.length; i++){
					abit.setExamMark(exams.get(rowClicked[i]).getId(), "Хорошо");
					tableModel.refreshTable();
				}
			}
		});
		
		low_mark.addActionListener (new ActionListener() 
		{
			public void actionPerformed (ActionEvent event) 
			{
				for (int i = 0; i < rowClicked.length; i++){
					abit.setExamMark(exams.get(rowClicked[i]).getId(), "Удовлетворительно");
					tableModel.refreshTable();
				}
			}
		});
		
		bad_mark.addActionListener (new ActionListener() 
		{
			public void actionPerformed (ActionEvent event) 
			{
				for (int i = 0; i < rowClicked.length; i++){
					abit.setExamMark(exams.get(rowClicked[i]).getId(), "Неудовлетворительно");
					tableModel.refreshTable();
				}
			}
		});
		
		
		
		
		// Создание таблицы 
		tableModel = new ExamTableAbit(abit.getExams(), exams);
		table = new JTable(tableModel);
		
		table.setRowSelectionAllowed(true);
        table.setColumnSelectionAllowed(false);
        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION); 
        table.setRowSorter(new TableRowSorter<ExamTableAbit>(tableModel));
        
        // Подключение выпадающего меню
		table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
            
                int[] rowindex = table.getSelectedRows();
                
                if (rowindex.length == 0)
                    return;  
                
                if (e.isPopupTrigger() && e.getComponent() instanceof JTable ) {
                	rowClicked = new int[rowindex.length];
                	for (int i = 0; i < rowindex.length; i++){
                		rowClicked[i] = table.convertRowIndexToModel(rowindex[i]);
                	}
                	JPopupMenu popup = new JPopupMenu();
                	popup.add(delete_exam);
					popup.add(set_mark);
                	
                	popup.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
		
		//this.setSize(400, 300);
		this.setTitle("Вступительные экзамены: "+abit.toString());
		this.setLayout(new FlowLayout());
		panel = new JPanel(new BorderLayout());
		panel.add(new JScrollPane(table), BorderLayout.CENTER);
		this.add(panel);
		this.pack();
	}
}
