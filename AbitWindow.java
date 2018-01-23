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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import javax.swing.*;

public class AbitWindow extends JFrame {
	private Abiturent abit_data;

	private JPanel labelPanel;
	private JPanel fieldPanel;
	private JTextField[] fields;
	private JButton button;
	private JComboBox<String> comboBoxFaculty;
	private JCheckBox checkBoxOriginal;

	private static String[] faculties = { "ФКТИ", "ФРТ", "ФЭА", "ФЭЛ", "ФЭМ",
			"ГФ" };
	private static String[] labels = { "Фамилия", "Имя", "Отчество",
			"Учебная программа", "Сумма баллов за ЕГЭ", "Факультет" };
	private static int[] widths = { 15, 10, 15, 10, 5 };


	public AbitWindow(String title, Abiturent a) {
		
		abit_data = a;

		this.setSize(600, 400);
		this.setTitle(title);
		createInterface();
		addListeners();
		pack();
	}

	private void createInterface() {
		int i;
		this.setLayout(new BorderLayout());
		JPanel labelPanel = new JPanel(new GridLayout(8, 1));
		JPanel fieldPanel = new JPanel(new GridLayout(8, 1));
		add(labelPanel, BorderLayout.WEST);
		add(fieldPanel, BorderLayout.CENTER);
		
		fields = new JTextField[5];

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
		comboBoxFaculty = new JComboBox(faculties);
		checkBoxOriginal = new JCheckBox("Оригинал");
		
		JLabel lab = new JLabel(labels[i], JLabel.RIGHT);
		JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel l = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		lab.setLabelFor(comboBoxFaculty);
		l.add(lab);
		p.add(comboBoxFaculty);
		labelPanel.add(l);
		fieldPanel.add(p);
		
		fieldPanel.add(checkBoxOriginal);
		p = new JPanel(new FlowLayout(FlowLayout.LEFT));
		p.add(button);
		fieldPanel.add(p);
		
		if (abit_data != null)
			setFields();
	}
	//"Фамилия", "Имя", "Отчество", "Учебная программа", "Сумма баллов за ЕГЭ", "Факультет"
	
	private boolean checkFields(){
		String[] errors_empty = {"Введите фамилию!", "Введите имя!", "", "Введите учёбную программу!",
				"Введите баллы за ЕГЭ (0 если отсутствуют)!"};
		String[] errors_format = {"Неправильная фамилия!", "Неправильное имя!", "", "", "Неверная сумма баллов ЕГЭ!"};
		for (int i = 0; i < fields.length; i++){
			if (i == 2)
				continue;
			
			if (fields[i].getText().isEmpty()) {
				JOptionPane.showMessageDialog(null, errors_empty[i]);
				return false;
			}else if (i < 2){
				if (!check_name(fields[i].getText())){
					JOptionPane.showMessageDialog(null, errors_format[i]);
					return false;
				}
			}else if (i == 4){
				if (!check_ege(fields[i].getText())){
					JOptionPane.showMessageDialog(null, errors_format[i]);
					return false;
				}
			}
		}
		
		return true;
	}
	
	private void setFields (){
		System.out.println("Setting fields!");
		fields[0].setText(abit_data.getLastName());
		fields[1].setText(abit_data.getFirstName());
		fields[2].setText(abit_data.getPatronymic());
		fields[3].setText(abit_data.getStudyProgram());
		fields[4].setText(Integer.toString(abit_data.getEgeSum()));
		comboBoxFaculty.setSelectedItem(abit_data.getFaculty());
		checkBoxOriginal.setSelected(abit_data.getOriginal());
	}

	private void addListeners() {
		button.addActionListener (new ActionListener() 
		{
			public void actionPerformed (ActionEvent event) 
			{
				System.out.println("Button pressed in AbitWindow");
				if(!checkFields())
					return;
				Map<Integer,String> exams = null;
				if (abit_data != null)
					exams = abit_data.getExams();
				if (exams != null)
					abit_data = new Abiturent(fields[0].getText(), fields[1]
							.getText(), fields[2].getText(),
							faculties[comboBoxFaculty.getSelectedIndex()],
							fields[3].getText(), Integer.parseInt(fields[4]
									.getText()), checkBoxOriginal.isSelected(),
							new HashMap<Integer,String>(exams));
				else
					abit_data = new Abiturent(fields[0].getText(), fields[1]
							.getText(), fields[2].getText(),
							faculties[comboBoxFaculty.getSelectedIndex()],
							fields[3].getText(), Integer.parseInt(fields[4]
									.getText()), checkBoxOriginal.isSelected(),
									new HashMap<Integer,String>());
				dispatchEvent(new WindowEvent(AbitWindow.this, WindowEvent.WINDOW_CLOSING));
				AbitWindow.this.setVisible(false);
				
			}
		});
	}
	
	public Abiturent get_abit(){
		return abit_data;
	}
	
	public static boolean check_name(String name){
		Pattern name_pattern = Pattern.compile("^[а-яА-ЯёЁa-zA-Z]{2,}$");
		return name_pattern.matcher(name).find();
	}
	
	public static boolean check_ege(String ege){
		Pattern ege_pattern = Pattern.compile("[0-9]{1,3}");
		return ege_pattern.matcher(ege).find();
	}

}
