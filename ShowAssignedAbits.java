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

public class ShowAssignedAbits extends JFrame{
	private JTable table;
	private AbitTableExam tableModel;
	private JPanel panel;
	private int[] rowClicked;
	private ArrayList<Abiturent> abits;
	private int exam_id;
	
	public ShowAssignedAbits(ArrayList<Abiturent> a, final int exam_id){	
		abits = new ArrayList<Abiturent>();
		this.exam_id = exam_id;
		
		for (int i = 0; i < a.size(); i++){
			if (a.get(i).isExamAssign(exam_id))
				abits.add(a.get(i));
		}
		
		System.out.println("exam_id:"+ exam_id+"abits length: "+abits.size());
		
		
		
		// Подготовка выпадающего меню
		final JMenu set_mark = new JMenu("Выставить оценку");
		JMenuItem high_mark = new JMenuItem("Отлично");
		JMenuItem medium_mark = new JMenuItem("Хорошо");
		JMenuItem low_mark = new JMenuItem("Удовлетворительно");
		JMenuItem bad_mark = new JMenuItem("Неудовлетворительно");
		set_mark.add(high_mark);
		set_mark.add(medium_mark);
		set_mark.add(low_mark);
		set_mark.add(bad_mark);
		
		high_mark.addActionListener (new ActionListener() 
		{
			public void actionPerformed (ActionEvent event) 
			{
				for (int i = 0; i < rowClicked.length; i++){
					tableModel.getAbits().get(rowClicked[i])
					.setExamMark(exam_id, "Отлично");
					tableModel.refreshTable();
				}
			}
		});
		
		medium_mark.addActionListener (new ActionListener() 
		{
			public void actionPerformed (ActionEvent event) 
			{
				for (int i = 0; i < rowClicked.length; i++){
					tableModel.getAbits().get(rowClicked[i])
					.setExamMark(exam_id, "Хорошо");
					tableModel.refreshTable();
				}
			}
		});
		
		low_mark.addActionListener (new ActionListener() 
		{
			public void actionPerformed (ActionEvent event) 
			{
				for (int i = 0; i < rowClicked.length; i++){
					tableModel.getAbits().get(rowClicked[i])
					.setExamMark(exam_id, "Удовлетворительно");
					tableModel.refreshTable();
				}
			}
		});
		
		bad_mark.addActionListener (new ActionListener() 
		{
			public void actionPerformed (ActionEvent event) 
			{
				for (int i = 0; i < rowClicked.length; i++){
					tableModel.getAbits().get(rowClicked[i])
					.setExamMark(exam_id, "Неудовлетворительно");
					tableModel.refreshTable();
				}
			}
		});
		
		tableModel = new AbitTableExam(abits, exam_id);
		table = new JTable(tableModel);
		
		table.setRowSelectionAllowed(true);
        table.setColumnSelectionAllowed(false);
        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION); 
        table.setRowSorter(new TableRowSorter<AbitTableExam>(tableModel));
        
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
					popup.add(set_mark);
                	
                	popup.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
		
		//this.setSize(400, 300);
		this.setTitle("Вступительный экзамен");
		this.setLayout(new FlowLayout());
		panel = new JPanel(new BorderLayout());
		panel.add(new JScrollPane(table), BorderLayout.CENTER);
		this.add(panel);
		this.pack();
	}
}
