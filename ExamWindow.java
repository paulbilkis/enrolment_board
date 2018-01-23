package coursework.bilkis;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.regex.Pattern;

import javax.swing.*;

public class ExamWindow extends JFrame{
	private Exam exam_data;

	private JPanel labelPanel;
	private JPanel fieldPanel;
	private JTextField[] fields;
	private JButton button;

	private static String[] labels = { "Предмет", "Аудитория", "Дата"};
	private static int[] widths = { 15, 10, 15, 10, 5 };
	private int new_id;

	public ExamWindow(String title, Exam a, int id) {	
		exam_data = a;
		this.setSize(400, 300);
		this.setTitle(title);
		createInterface();
		addListeners();
		new_id = id;
		
	}

	private void createInterface() {
		int i;
		this.setLayout(new BorderLayout());
		JPanel labelPanel = new JPanel(new GridLayout(4, 1));
		JPanel fieldPanel = new JPanel(new GridLayout(4, 1));
		add(labelPanel, BorderLayout.WEST);
		add(fieldPanel, BorderLayout.CENTER);
		
		fields = new JTextField[3];

		for (i = 0; i < fields.length; i++) {		
			fields[i] = new JTextField();
			fields[i].setColumns(widths[i]);
			
			JLabel lab = new JLabel(labels[i], JLabel.RIGHT);
			JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
			JPanel l = new JPanel(new FlowLayout(FlowLayout.RIGHT));
			lab.setLabelFor(fields[i]);
			lab.setVerticalAlignment(JLabel.CENTER);
			l.add(lab);
			p.add(fields[i]);
			labelPanel.add(l);
			fieldPanel.add(p);
		}
		
		button = new JButton("Готово");
		
		JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
		p.add(button);
		fieldPanel.add(p);
		
		if (exam_data != null)
			setFields();
	}
	//"Фамилия", "Имя", "Отчество", "Учебная программа", "Сумма баллов за ЕГЭ", "Факультет"
	
	private boolean checkFields(){
		String[] errors_empty = {"Введите предмет!", "Введите номер аудитории!", "Введите дату!"};
		String[] errors_format = {"Некорретное название предмета", "Некорректный номер аудитории!", "Некорректная дата!"};
		for (int i = 0; i < fields.length; i++){
			
			if (fields[i].getText().isEmpty()) {
				JOptionPane.showMessageDialog(null, errors_empty[i]);
				return false;
			}else if (i == 0){
				if (!check_subject(fields[i].getText())){
					JOptionPane.showMessageDialog(null, errors_format[i]);
					return false;
				}
			}else if (i == 1){
				if (!check_classroom(fields[i].getText())){
					JOptionPane.showMessageDialog(null, errors_format[i]);
					return false;
				}
			}else if (i == 2){
				if (!check_date(fields[i].getText())){
					JOptionPane.showMessageDialog(null, errors_format[i]);
					return false;
				}
			}
		}
		
		return true;
	}
	
	private void setFields (){
		System.out.println("Setting fields!");
		fields[0].setText(exam_data.getSubject());
		fields[1].setText(exam_data.getClassroom());
		fields[2].setText(exam_data.getExamDate());
	}

	private void addListeners() {
		button.addActionListener (new ActionListener() 
		{
			public void actionPerformed (ActionEvent event) 
			{
				System.out.println("Button pressed in ExamWindow");
				if(!checkFields())
					return;
				
				if (exam_data != null){
					int id = exam_data.getId();
					exam_data = new Exam(fields[0].getText(), fields[1]
							.getText(), fields[2].getText(), id);
				}else{
					exam_data = new Exam(fields[0].getText(), fields[1]
							.getText(), fields[2].getText(), new_id);
				}
					dispatchEvent(new WindowEvent(ExamWindow.this, WindowEvent.WINDOW_CLOSING));
					ExamWindow.this.setVisible(false);
				
			}
		});
	}
	
	public Exam get_exam(){
		return exam_data;
	}
	
	public static boolean check_subject(String name){
		Pattern name_pattern = Pattern.compile("[а-яА-ЯёЁa-zA-Z]{2,}");
		return name_pattern.matcher(name).find();
	}
	
	public static boolean check_classroom(String classroom){
		Pattern classroom_pattern = Pattern.compile("[0-9]{1,4}");
		return classroom_pattern.matcher(classroom).find();
	}

	
	public static boolean check_date(String date){
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
			Date d =  sdf.parse(date);
			if (!date.equals(sdf.format(d)))
				return false;
			else
				return true;
						
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}
		
			
	}

}
