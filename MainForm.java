package coursework.bilkis;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.*;
import javax.swing.table.TableRowSorter;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRXmlDataSource;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleHtmlExporterConfiguration;
import net.sf.jasperreports.export.SimpleHtmlExporterOutput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;

/**
 * Класс основной формы
 * @author bilkis
 *
 */
public class MainForm extends JFrame {	
	private boolean just_run;
	private Object mutex;
	/**
	 * Таблицы
	 */
	private JTable abiturents;
	public static AbitTable abiturents_model;
	
	private JTable exams_table;
	private ExamTable exam_model;
	
	/**
	 * Содержание выпадающего меню
	 */
	private JMenuBar menuBar;
	
	private JMenu abiturent;
	private JMenu reports;
	private JMenu file;
	private JMenu exam_menu;
	
	private JMenuItem load_data_xml;
	private JMenuItem save_data_xml;
	
	private JMenuItem load_data_txt;
	private JMenuItem save_data_txt;
	
	private JMenuItem abit_show_all;
	private JMenuItem add_abit;
	
	private JMenuItem exam_stats;
	private JMenuItem creat_report_pdf;
	
	/**
	 * Элементы поиска
	 */
	
	private JTextField textSearch;
    private JButton buttonSearch;
    private JComboBox<String> comboBoxSearch;
    private JButton buttonClearSearch;

	private JPanel leftPanel;
	private JPanel rightPanel;
	private JPanel rightPanel_tab2;
	
	/**
	 * 
	 * Элементы popup в таблице
	 */
	
	private JMenuItem abit_show_exams;
	private JMenuItem abit_add_exam;
	private JMenuItem abit_edit;
	private JMenuItem abit_delete;
	
	private JMenuItem exam_add;
	private JMenuItem exam_edit;
	private JMenuItem exam_delete;
	
	

	private int examRowClicked, rowClicked[];
	private final static String pattern_pdf = "report-pdf.jrxml"; 
	private final static String pattern_html = "report-html.jrxml"; 
	private String filename;
	
	private static final String exam_filename = "D:/exams.xml";
	
	private JTabbedPane tabs;
	private JPanel tab1;
	private JPanel tab2;
	
	private JPanel infoPanel;
	private JLabel num_of_rows;
	
	public MainForm () {
	
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}	
    });
		
		mutex = new Object(); 
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setSize(screenSize.width, screenSize.height);
        this.setTitle("Приёмная комиссия");
        this.just_run = true;
        
        tabs = new JTabbedPane();
        
        createMenu();
        
        leftPanel = new JPanel(new BorderLayout());
        rightPanel = new JPanel(new BorderLayout());
        rightPanel_tab2 = new JPanel(new GridLayout());
        infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel info_label = new JLabel("Колличество записей: ");
        num_of_rows = new JLabel();
        infoPanel.add(info_label);
        infoPanel.add(num_of_rows);
        
        tab1 = new JPanel();
        tab2 = new JPanel();
        tab1.setLayout(new BorderLayout());
        tab1.add(infoPanel, BorderLayout.NORTH);
        tab1.add(leftPanel, BorderLayout.WEST);
        tab1.add(rightPanel, BorderLayout.CENTER);
        tab2.setLayout(new BorderLayout());
        tab2.add(rightPanel_tab2, BorderLayout.CENTER);
        
        tabs.addTab("Абитуриенты", tab1);
        tabs.addTab("Экзамены", tab2);
        
        this.setLayout(new BorderLayout());
        this.add(tabs, BorderLayout.CENTER);
        
		createTable();
		createExamTable();
		createSearch();
		
		addListeners();
	}
	
	/**
	 * Создаёт меню
	 */
	private void createMenu() {
        menuBar = new JMenuBar(); 
        
        exam_menu = new JMenu("Экзамен");
        
        file = new JMenu("Файл");
        load_data_xml = new JMenuItem("Загрузить из *.xml");
        save_data_xml = new JMenuItem("Выгрузить в *.xml");
        load_data_txt = new JMenuItem("Загрузить из *.txt");
        save_data_txt = new JMenuItem("Выгрузить в *.txt");
        file.add(load_data_xml);
        file.add(load_data_txt);
        file.add(save_data_xml);
        file.add(save_data_txt);
        
        abiturent = new JMenu("Абитуренты");
        abit_show_all = new JMenuItem("Показать всех");	
    	add_abit = new JMenuItem("Добавить абитурента");
    	
    	abiturent.add(abit_show_all);
    	abiturent.add(add_abit);
       
        reports = new JMenu("Отчётность");	
    	
    	creat_report_pdf = new JMenuItem("Отчёт по абитуриентам в PDF");
    	exam_stats = new JMenuItem("Общий отчёт по экзаменам в PDF");
    	reports.add(creat_report_pdf);
    	reports.add(exam_stats);
    	
    	menuBar.add(file);
    	menuBar.add(abiturent);
    	menuBar.add(reports);
    	
    	//menuBar.add(exam_menu);
    	
    	
    	setJMenuBar(menuBar);
	}
	
	/**
	 * Создаёт всех слушателей (кроме pop-up'вских)
	 */
	private void addListeners() {
		load_data_xml.addActionListener (new ActionListener() 
		{
			public void actionPerformed (ActionEvent event) 
			{
				try {
					loadTable(true);
				} catch (FileProcessingException e) {
					System.out.println(e.getMessage());
				}
				catch (IOException e) {
					System.out.println(e.getMessage());
				}
			}
		});
		
		load_data_txt.addActionListener (new ActionListener() 
		{
			public void actionPerformed (ActionEvent event) 
			{
				try {
					loadTable(false);
				} catch (FileProcessingException e) {
					System.out.println(e.getMessage());
				}
				catch (IOException e) {
					System.out.println(e.getMessage());
				}
			}
		});
		
		add_abit.addActionListener (new ActionListener() 
		{
			public void actionPerformed (ActionEvent event) 
			{
				final AbitWindow add_abit_window = new AbitWindow("Добавить абитуриента", null);
				add_abit_window.setVisible(true);
				add_abit_window.addWindowListener(new WindowAdapter() {
					  public void windowClosing(WindowEvent we) {
						  	abiturents_model.addRow(add_abit_window.get_abit());
							abiturents_model.refreshTable();
							add_abit_window.dispose();
						  }
						});
				abiturents_model.refreshTable();
				System.out.println("Menu item: add new abit");
				num_of_rows.setText(Integer.toString(abiturents_model.getRowCount()));
			}
		});
		
		abit_show_all.addActionListener (new ActionListener() 
		{
			public void actionPerformed (ActionEvent event) 
			{
				System.out.println("Menu item: show all abiturents");
				// показать все
				filterRowsInTable("", -1);
				num_of_rows.setText(Integer.toString(abiturents_model.getRowCount()));
			}
		});
		
		save_data_xml.addActionListener (new ActionListener() 
		{
			public void actionPerformed (ActionEvent event) 
			{
				try {
					saveTable(true);
				} catch (IOException e) {
					System.out.println(e.getMessage());
				}
			}
		});
		
		save_data_txt.addActionListener (new ActionListener() 
		{
			public void actionPerformed (ActionEvent event) 
			{
				try {
					saveTable(false);
				} catch (IOException e) {
					System.out.println(e.getMessage());
				}
			}
		});
		
		
		
			
		exam_stats.addActionListener (new ActionListener() 
		{
			public void actionPerformed (ActionEvent event) 
			{
				try {
					if (abiturents_model.isChanged())
						saveTable(true);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Thread create_report = new Thread(new Runnable(){
					public void run(){
						synchronized(MainForm.this.mutex){
							createReport("pdf");
							MainForm.this.mutex.notify();
						}
					}
				});
				create_report.start();
				
				System.out.println("Menu item: exam stats");
			}
		});
		
		
		creat_report_pdf.addActionListener (new ActionListener() 
		{
			public void actionPerformed (ActionEvent event) 
			{	
				try {
					if (abiturents_model.isChanged())
						saveTable(true);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//Thread create_report = new ReportThread(MainForm.this,"pdf", MainForm.this.mutex);
				Thread create_report = new Thread(new Runnable(){
					public void run(){
						synchronized(MainForm.this.mutex){
							createReport("pdf");
							MainForm.this.mutex.notify();
						}
					}
				});
				create_report.start();
				System.out.println("Menu item: create report");
			}
		});
		
		
		
		buttonSearch.addActionListener (new ActionListener() 
		{
			public void actionPerformed (ActionEvent event) 
			{
				System.out.println("Button pressed: search");
				System.out.printf("item choosen: %d\n", comboBoxSearch.getSelectedIndex());
		       try{
		    	   searchInTable();	
		       } catch (NothingToSearch e){
		    	   JOptionPane.showMessageDialog(null, e.getMessage());
		       }
				
			}
		});
		
		buttonClearSearch.addActionListener (new ActionListener() 
		{
			public void actionPerformed (ActionEvent event) 
			{
				System.out.println("Button pressed: clear search");
				filterRowsInTable("", -1);
			}
		});
		
		textSearch.addActionListener(new ActionListener() 
		{
			public void actionPerformed (ActionEvent event) 
			{	
				System.out.println("Enter in search field pressed");
				System.out.printf("item choosen: %d\n", comboBoxSearch.getSelectedIndex());
		       try{
		    	   searchInTable();	
		       } catch (NothingToSearch e){
		    	   JOptionPane.showMessageDialog(null, e.getMessage());
		       }
				
			}
		});
		
		
	}
	
	/**
	 * Поиск в таблице по одному из столбцов
	 * @throws NothingToSearch
	 */
	private void searchInTable() throws NothingToSearch{
		int column = comboBoxSearch.getSelectedIndex();
		String search_text = textSearch.getText();
		if (search_text.isEmpty())
			throw new NothingToSearch();
		
		filterRowsInTable("(?i)" + search_text, column);
	}
	
	
	/**
	 * Осуществляет фильтрацию (в т.ч. поиск) по таблице
	 * @param regex Регулярное выражение для фильтрации
	 * @param column В каких колонках. Если -1, то фильтрация будет по всем.
	 */
	private void filterRowsInTable(String regex, int column) {
		TableRowSorter<AbitTable> sorter = (TableRowSorter<AbitTable>) abiturents.getRowSorter();      
		if (regex == null)
			regex = "";
		if (column == -1)
			sorter.setRowFilter(RowFilter.regexFilter(regex));
		else
			sorter.setRowFilter(RowFilter.regexFilter(regex, column));
		num_of_rows.setText(Integer.toString(sorter.getViewRowCount()));
	}
	
	/**
	 * Создаёт таблицу абитуриентов
	 */
	private void createTable() {
		abiturents_model = new AbitTable();
        abiturents = new JTable(abiturents_model);
        
        abit_show_exams = new JMenuItem("Показать экзамены");
        abit_show_exams.addActionListener (new ActionListener() 
		{
			public void actionPerformed (ActionEvent event) 
			{
				System.out.printf("popup item: show exams, rowClicked=%d\n", rowClicked[0]);
				if (!abiturents_model.getAbits().get(rowClicked[0]).needExams()){
					JOptionPane.showMessageDialog(null, "У этого абитуриента отсутствуют вступительные экзамены.");
					return;
				}
					
				final ShowExamsWindow exam_window = new ShowExamsWindow(abiturents_model.getAbits().get(rowClicked[0]), exam_model.getExams());
				exam_window.setVisible(true);
				exam_window.addWindowListener(new WindowAdapter() {
					  public void windowClosing(WindowEvent we) {
							abiturents_model.refreshTable();
							exam_window.dispose();
						  }
						});
				
			}
		});
        
        abit_add_exam = new JMenuItem("Назначить вступительный экзамен");
        abit_add_exam.addActionListener (new ActionListener() 
		{
			public void actionPerformed (ActionEvent event) 
			{
				System.out.println("popup item: add exam to abits");
				final AssignExamDialog assign_dialog = new AssignExamDialog(abiturents_model.getAbits(), 
						rowClicked, exam_model.getExams());
				assign_dialog.setVisible(true);
				assign_dialog.addWindowListener(new WindowAdapter() {
					  public void windowClosing(WindowEvent we) {
							abiturents_model.refreshTable();
							assign_dialog.dispose();
						  }
						});
			}
		});
        
        abit_edit = new JMenuItem("Редактировать");
        abit_edit.addActionListener (new ActionListener() 
		{
			public void actionPerformed (ActionEvent event) 
			{
				System.out.printf("popup item: edit an abit, rowClicked=%d\n", rowClicked[0]);
				final AbitWindow edit_window = new AbitWindow("Редактирование", abiturents_model.getAbits().get(rowClicked[0]));
				edit_window.setVisible(true);
				edit_window.addWindowListener(new WindowAdapter() {
					  public void windowClosing(WindowEvent we) {
						  	abiturents_model.changeRow(rowClicked[0], edit_window.get_abit());
							abiturents_model.refreshTable();
							edit_window.dispose();
						  }
						});
				
			}
		});
        
        abit_delete = new JMenuItem("Удалить");
        abit_delete.addActionListener (new ActionListener() 
		{
			public void actionPerformed (ActionEvent event) 
			{
				System.out.println("popup item: delete an abit");
				abiturents_model.deleteRow(rowClicked);
				num_of_rows.setText(Integer.toString(abiturents_model.getRowCount()));
			}
		});
        
        abiturents.setRowSelectionAllowed(true);
        abiturents.setColumnSelectionAllowed(false);
        abiturents.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        
        abiturents.setRowSorter(new TableRowSorter<AbitTable>(abiturents_model));
        abiturents.addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent e) {
                int[] rowindex = abiturents.getSelectedRows();
                
                if (rowindex.length == 0)
                    return;  
                
                if (e.isPopupTrigger() && e.getComponent() instanceof JTable ) {
                	rowClicked = new int[rowindex.length];
                	for (int i = 0; i < rowindex.length; i++){
                		rowClicked[i] = abiturents.convertRowIndexToModel(rowindex[i]);
                	}
                	JPopupMenu popup = new JPopupMenu();
                	popup.add(abit_add_exam);
					popup.add(abit_delete);
                	if (rowindex.length == 1){	
						popup.add(abit_show_exams);
						popup.add(abit_edit);					
                	}
                	popup.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
        
        rightPanel.add(new JScrollPane(abiturents), BorderLayout.CENTER);
        
        try {
			loadTable(true);
		} catch (FileProcessingException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
        catch (IOException e){
        	JOptionPane.showMessageDialog(null, e.getMessage());
        }
	}
	
	
	/**
	 * Создаёт таблицу экзаменов
	 */
	private void createExamTable() {
		exam_model = new ExamTable();
        exams_table = new JTable(exam_model);
        
        final JMenuItem exam_show_abits = new JMenuItem("Показать студентов");
        exam_show_abits.addActionListener (new ActionListener() 
		{
			public void actionPerformed (ActionEvent event) 
			{
					
				final ShowAssignedAbits exam_window = new ShowAssignedAbits(abiturents_model.getAbits(), exam_model.getExams().get(examRowClicked).getId());
				exam_window.setVisible(true);
				exam_window.addWindowListener(new WindowAdapter() {
					  public void windowClosing(WindowEvent we) {
						
							exam_window.dispose();
						  }
						});
				
			}
		});
        
        exam_edit = new JMenuItem("Редактировать");
        exam_edit.addActionListener (new ActionListener() 
		{
			public void actionPerformed (ActionEvent event) 
			{
				System.out.printf("popup item: edit an exam, examRowClicked=%d\n", examRowClicked);
				final ExamWindow edit_window = new ExamWindow("Редактировать экзамен", exam_model.getExams().get(examRowClicked),
						exam_model.getExams().get(examRowClicked).getId());
				edit_window.setVisible(true);
				edit_window.addWindowListener(new WindowAdapter() {
					  public void windowClosing(WindowEvent we) {
						    exam_model.changeRow(examRowClicked, edit_window.get_exam());
							exam_model.refreshTable();
							edit_window.dispose();
							try {
								MainForm.this.saveExamTable();
							} catch (FileProcessingException | IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						  }
						});
				
			}
		});
        
        exam_delete = new JMenuItem("Удалить");
        exam_delete.addActionListener (new ActionListener() 
		{
			public void actionPerformed (ActionEvent event) 
			{
				System.out.printf("popup item: delete an exam, examRowClicked=%d\n", examRowClicked);
				int exam_id = exam_model.getExams().get(examRowClicked).getId();
				for (int i = 0; i < abiturents_model.getAbits().size(); i++){
					if(abiturents_model.getAbits().get(i).isExamAssign(exam_id)){
						abiturents_model.getAbits().get(i).removeExam(exam_id);
					}
				}
				exam_model.deleteRow(examRowClicked);
				abiturents_model.refreshTable();
				try {
					saveExamTable();
				} catch (FileProcessingException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
        
        exam_add = new JMenuItem("Добавить экзамен");
        exam_menu.add(exam_add);
        exam_add.addActionListener (new ActionListener() 
		{
			public void actionPerformed (ActionEvent event) 
			{
				System.out.printf("popup item: add an exam, examRowClicked=%d\n", examRowClicked);
				 
				final ExamWindow edit_window = new ExamWindow("Добавить экзамен", null,
						exam_model.getNewId());
				edit_window.setVisible(true);
				edit_window.addWindowListener(new WindowAdapter() {
					  public void windowClosing(WindowEvent we) {
						    exam_model.addRow(edit_window.get_exam());
							exam_model.refreshTable();
							edit_window.dispose();
							try {
								saveExamTable();
							} catch (FileProcessingException | IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						  }
						});
			}
		});
        
        exams_table.setRowSorter(new TableRowSorter<ExamTable>(exam_model));
        exams_table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                int r = exams_table.rowAtPoint(e.getPoint());
                if (r >= 0 && r < exams_table.getRowCount()) {
                	exams_table.setRowSelectionInterval(r, r);
                } else {
                	exams_table.clearSelection();
                }

                int rowindex = exams_table.getSelectedRow();
                
                if (rowindex < 0)
                    return;  
                
                if (e.isPopupTrigger() && e.getComponent() instanceof JTable ) {
                    JPopupMenu popup = new JPopupMenu();
                    popup.add(exam_show_abits);
                    popup.add(exam_add);
                    popup.add(exam_edit);
                    popup.add(exam_delete);
                    
          	        examRowClicked = exams_table.convertRowIndexToModel(rowindex);
                    popup.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
        
        rightPanel_tab2.add(new JScrollPane(exams_table));
        
        try {
			loadExamTable();
		} catch (FileProcessingException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
        catch (IOException e){
        	JOptionPane.showMessageDialog(null, e.getMessage());
        }
	}
	

	/**
	 * Сохраняет данные из таблицы в файл
	 * @throws FileProcessingException
	 */
	
	private void saveTable(boolean is_xml) throws FileNotFoundException, IOException{
		ArrayList<Abiturent> abits = abiturents_model.getAbits();
		FileDialog save = new FileDialog(this, "Выгрузить данные", FileDialog.SAVE);
		if (is_xml){
			save.setFile("*.xml");
		}else{
			save.setFile("*.txt");
		}
		save.setVisible(true);
		filename = save.getDirectory() + save.getFile();
		
		if(filename.isEmpty() || filename.equals("nullnull")) return; 
		System.out.println("Saving to file "+filename);
		abiturents_model.dataWasSaved();
		// launch it as a different thread
		Thread data_thread = new SaveThread(abits, filename, mutex);
		data_thread.start();
	}
	
	/**
	 * Загружает таблицу данными извне. 
	 * @throws FileProcessingException
	 */
	private void loadTable(boolean is_xml) throws FileProcessingException, IOException {
		if (just_run) {
			just_run = false;
			filename = "D:/data.xml";
		} else {
			FileDialog open = new FileDialog(this, "Загрузить данные", FileDialog.LOAD);
			if (is_xml){
				open.setFile("*.xml");
			}else{
				open.setFile("*.txt");
			}
			open.setVisible(true);
			String filename = open.getDirectory() + open.getFile();
			if (filename.isEmpty() || filename.equals("nullnull"))
				return;
		}
		System.out.println("Loading from file " + filename);
		abiturents_model.clearTable();
		ArrayList<Abiturent> abits = abiturents_model.getAbits();
		Thread data_thread = new LoadThread(abits, filename, mutex);
		data_thread.start();

		try {
			data_thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Refreshing table!");
		abiturents_model.refreshTable();
		num_of_rows.setText(Integer.toString(abiturents_model.getRowCount()));
	}
	
	/**
	 * Загружает таблицу экзаменов
	 * @throws FileProcessingException
	 * @throws IOException
	 */
	private void loadExamTable() throws FileProcessingException,IOException{
		ArrayList<Exam> exams = exam_model.getExams();
		exam_model.clearTable();
		DataFileProcess inst = new DataFileProcess(exam_filename);
		inst.loadExamData(exams);
		exam_model.refreshTable();
	}
	
	/**
	 * Сохраняет таблицу экзаменов
	 * @throws FileProcessingException
	 * @throws IOException
	 */
	private void saveExamTable() throws FileProcessingException,IOException{
		ArrayList<Exam> exams = exam_model.getExams();
		DataFileProcess inst = new DataFileProcess(exam_filename);
		inst.saveExamData(exams);
	}
	
	/**
	 * Создаёт форму поиска
	 */
	private void createSearch() {	
		textSearch = new JTextField();
        buttonSearch = new JButton("Поиск");
        comboBoxSearch = new JComboBox(abiturents_model.getColumns());
        buttonClearSearch = new JButton("Очистить");
        JToolBar searchToolBar = new JToolBar();
        searchToolBar.setFloatable(false);
        searchToolBar.add(textSearch);
        searchToolBar.add(new JToolBar.Separator());
        searchToolBar.add(comboBoxSearch);
        searchToolBar.add(new JToolBar.Separator());
        searchToolBar.add(buttonSearch);
        searchToolBar.add(new JToolBar.Separator());
        searchToolBar.add(buttonClearSearch);
        rightPanel.add(searchToolBar, BorderLayout.SOUTH);
		
	}
	
	/**
	 * Создаёт отчёт в нужном типе
	 * TODO сделать паттерн параметром
	 * @param type
	 */
	void createReport(String type) {
		FileDialog save = new FileDialog(this, "Сохранить отчёт", FileDialog.SAVE);
		String template;
		if (type.equals("pdf")){
			save.setFile(".pdf");
			template = pattern_pdf;
		}else{ 
			save.setFile(".html");
			template = pattern_html;
		}
		save.setVisible(true);
		String result = save.getDirectory() + save.getFile();
		if(result.isEmpty() || result.equals("nullnull")) return; 
		if(filename.isEmpty() || filename.equals("nullnull")) return; 
		System.out.println("Saving report to "+result);
		System.out.println("Datasourse is "+filename);
		try {
			// Указание  источника XML-данных
			JRDataSource ds = new JRXmlDataSource(filename, "/abiturents/abiturent");
			// Создание отчета на базе шаблона
			JasperReport jasperReport = JasperCompileManager.compileReport(template);
			// Заполнение отчета данными
			JasperPrint print = JasperFillManager.fillReport(jasperReport, new HashMap<String,Object>(), ds); 
			
			HtmlExporter exporter_html;
			JRPdfExporter exporter_pdf;

			if(type.equals("pdf")){
				JasperExportManager.exportReportToPdfFile(print,result);
				/*exporter_pdf = new JRPdfExporter();
				exporter_pdf.setExporterOutput(new SimpleOutputStreamExporterOutput(result));
				exporter_pdf.setExporterInput(new SimpleExporterInput(print));
				SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
				exporter_pdf.setConfiguration(configuration);
				exporter_pdf.exportReport();*/
			}else{
				JasperExportManager.exportReportToHtmlFile(print,result);
				/*exporter_html = new HtmlExporter (); // Генерация отчета в формате HTML
				exporter_html.setExporterOutput(new SimpleHtmlExporterOutput(result));
				exporter_html.setExporterInput(new SimpleExporterInput(print));
				SimpleHtmlExporterConfiguration configuration = new SimpleHtmlExporterConfiguration();
				exporter_html.setConfiguration(configuration);
				exporter_html.exportReport();*/
			}
			
			} catch (JRException e) { e.printStackTrace(); }
		
	}
	
}
