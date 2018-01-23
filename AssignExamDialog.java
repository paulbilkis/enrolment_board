package coursework.bilkis;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.SwingConstants;

public class AssignExamDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private Integer[] map_select_exam_id;
	private JComboBox comboBox;
	/**
	 * Create the dialog.
	 */
	public AssignExamDialog(final ArrayList<Abiturent> a, final int[] selected_abits, ArrayList<Exam> e) {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setTitle("Назначить экзамен");
		
		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.X_AXIS));
		getContentPane().add(contentPanel);
	
		map_select_exam_id = new Integer[e.size()];
		String[] exams_string = new String[e.size()];
		
		for (int i = 0; i < e.size(); i++){
			map_select_exam_id[i] = e.get(i).getId();
			exams_string[i] = e.get(i).getSubject() + " " + e.get(i).getExamDate();
		}
			
		
		contentPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		{
			JLabel lblNewLabel_1 = new JLabel("Экзамен: ");
			contentPanel.add(lblNewLabel_1);
		}
		{
			JButton button = new JButton("Назначить");
			button.setHorizontalAlignment(SwingConstants.RIGHT);
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					int selected_exam = map_select_exam_id[comboBox.getSelectedIndex()];
					System.out.printf("exam chooses: %d", selected_exam);
					for (int i = 0; i < selected_abits.length; i++){
						if (!a.get(selected_abits[i]).isExamAssign(selected_exam)){
							a.get(selected_abits[i]).addExam(selected_exam, "");
							System.out.println("Assign exam_id: "+selected_exam+", to abit: "+
								a.get(selected_abits[i]).toString());
						}else{
							JOptionPane.showMessageDialog(null, 
									"Абитуриент "+a.get(selected_abits[i]).toString()+" уже записан на этот экзамен.");
						}
					}
					dispatchEvent(new WindowEvent(AssignExamDialog.this, WindowEvent.WINDOW_CLOSING));
					AssignExamDialog.this.setVisible(false);
				}
			});
			{
				comboBox = new JComboBox<String>(exams_string);
				contentPanel.add(comboBox);
			}
			contentPanel.add(button);
		}
		pack();
	}

}
