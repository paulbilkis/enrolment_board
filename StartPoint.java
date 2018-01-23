package coursework.bilkis;

import javax.swing.JFrame;

/**
 * Класс-точка старта всей программы. Создаёт основное окно и передаёт управление ему.
 * @author bilkis
 *
 */

public class StartPoint {
	
	public static void main(String[] args) {
		MainForm app = new MainForm();
		app.setExtendedState(app.getExtendedState() | JFrame.MAXIMIZED_BOTH);
		app.setVisible(true);
	}

}
