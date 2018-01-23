package coursework.bilkis;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DataFileProcess {
	private String extension;
	private String filename;

	public DataFileProcess(String f) {
		extension = giveExtension(f);
		filename = f;
	}
	
	public static String giveExtension(String f){
		String extension = "";
		int i = f.lastIndexOf('.');
		int p = Math.max(f.lastIndexOf('/'), f.lastIndexOf('\\'));

		if (i > p) {
			extension = f.substring(i + 1);
		}
		return extension;
	}
	
	public void loadData (ArrayList<Abiturent> abits){
		
			if (extension.equals("xml")) {
				loadXMLData(abits);
			} else {
				loadTextData(abits);
			}
	}
	
	public void saveData (ArrayList<Abiturent> abits){
			if (extension.equals("xml")) {
				saveXMLData(abits);
			} else {
				saveTextData(abits);
			}
	}
	
	public void loadExamData(ArrayList<Exam> exams){
		loadXMLDataExams(exams);
	}
	
	public void saveExamData(ArrayList<Exam> exams){
		saveXMLDataExams(exams);
	}

	/**
	 * Заполняет массив абитуриентов данными из текстового файла
	 * 
	 * @param abits
	 *            массив для заполнения
	 * @param filename
	 *            имя файла для загрузки данных из
	 * @throws FileNotFoundException
	 */
	private void loadTextData(ArrayList<Abiturent> abits) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filename));
			String line;
			String[] parsed_abit = null;
			abits.clear();
			Map<Integer,String> exams = new HashMap<Integer,String>();

			while ((line = reader.readLine()) != null) {
				if (line.equals("abiturent")) { // next line is abiturent
												// details
					// create object abiturent (7 fields -> 5 strings, int,
					// bool)
					line = reader.readLine();
					parsed_abit = line.split(",");

				} else if (line != null) { // line is "exams num_of_exams"
					String[] parsed = line.split(" ");
					if (parsed.length == 2) {
						if (parsed[0].equals("exams")) {
							exams.clear();

							int num_of_exams = Integer.parseInt(parsed[1]);
							
							for (int i = 0; i < num_of_exams
									&& (line = reader.readLine()) != null; i++) {
								String[] parsed_exam = line.split(",");
								// create object exam in exams (2 fields strings
								// )
								if (parsed_exam.length == 2)
									exams.put(Integer.parseInt(parsed_exam[0]),parsed_exam[1]);
							}
							if (parsed_abit != null && parsed_abit.length == 7) {
								abits.add(new Abiturent(
										parsed_abit[0],
										parsed_abit[1],
										parsed_abit[2],
										parsed_abit[3],
										parsed_abit[4],
										Integer.parseInt(parsed_abit[5]),
										new Boolean(parsed_abit[6].equals("1")),
										new HashMap<Integer,String>(exams)));
								parsed_abit = null;
							}
						}
					}
				}
			}
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	private void saveTextData(ArrayList<Abiturent> abits) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
			int i;
			Map<Integer,String> exams = null;

			for (i = 0; i < abits.size(); i++) {
				writer.write("abiturent");
				writer.write("\n");
				writer.write(abits.get(i).get_csv_line());
				writer.write("\n");
				exams = abits.get(i).getExams();
				if (exams == null) {
					writer.write("exams 0");
					writer.write("\n");
					continue;
				}
				writer.write("exams " + Integer.toString(exams.size()));
				writer.write("\n");
				for (Map.Entry<Integer, String> e : exams.entrySet()) {
					if (e.getValue().isEmpty())
						writer.write(e.getKey()+", ");
					else
						writer.write(e.getKey()+","+e.getValue());
					writer.write("\n");
				}
			}
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Заполняет массив абитуриентов данными из XML-файла
	 * 
	 * @param abits
	 *            массив для заполнения
	 * @param filename
	 *            имя файла для загрузки данных из
	 */
	private void loadXMLData(ArrayList<Abiturent> abits) {
		try {
			DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();
			Document doc = dBuilder.parse(new File(filename));
			NodeList exams_nlist, nlist;
			Node tmp_node;
			Element tmp_element, exams_node;
			String firstName, lastName, patronymic, faculty, studyProgram, egeSum, original;
			Map<Integer,String> exams = new HashMap<Integer,String>();

			if (doc.hasChildNodes()) {
				tmp_element = (Element) doc.getElementsByTagName("abiturents")
						.item(0);
				nlist = tmp_element.getElementsByTagName("abiturent");
				abits.clear();
				for (int i = 0; i < nlist.getLength(); i++) {
					tmp_node = nlist.item(i);

					if (tmp_node.getNodeType() == Node.ELEMENT_NODE) {
						System.out.println("Read abiturent from XML.");
						tmp_element = (Element) tmp_node;
						firstName = getSingleNodeContent(tmp_node, "firstName");
						lastName = getSingleNodeContent(tmp_node, "lastName");
						patronymic = getSingleNodeContent(tmp_node,
								"patronymic");
						faculty = getSingleNodeContent(tmp_node, "faculty");
						studyProgram = getSingleNodeContent(tmp_node,
								"studyProgram");
						egeSum = getSingleNodeContent(tmp_node, "egeSum");
						original = getSingleNodeContent(tmp_node, "original");
						if (tmp_element.getElementsByTagName("exams")
								.getLength() > 0) {
							exams_node = (Element) tmp_element
									.getElementsByTagName("exams").item(0);

							if (exams_node.getNodeType() == Node.ELEMENT_NODE
									&& (exams_nlist = exams_node
											.getElementsByTagName("exam")) != null) {
								exams.clear();

								for (int j = 0; j < exams_nlist.getLength(); j++) {
									
									NamedNodeMap attr = exams_nlist.item(j).getAttributes();
									exams.put(Integer.parseInt(attr.getNamedItem("exam_id").getNodeValue()), 
											attr.getNamedItem("mark").getNodeValue());
								}
							}

							System.out.println("reading exams, num = "
									+ exams.size());
						}
						abits.add(new Abiturent(lastName, firstName,
								patronymic, faculty, studyProgram, Integer
										.parseInt(egeSum), new Boolean(original
										.equals("1")), new HashMap<Integer,String>(
										exams)));
					}
				}

			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	/*
	  throws ParserConfigurationException,
			TransformerFactoryConfigurationError, IOException,
			TransformerException 
	 */

	private void saveXMLData(ArrayList<Abiturent> abits) {
		try {
			DocumentBuilder builder = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();
			Document doc = builder.newDocument();
			Element examElement, examsElement, abitElement, rootElement = doc
					.createElement("abiturents");
			doc.appendChild(rootElement);

			for (int i = 0; i < abits.size(); i++) {
				abitElement = doc.createElement("abiturent");
				rootElement.appendChild(abitElement);

				createAndSetNode(doc, abitElement, "firstName", abits.get(i)
						.getFirstName());
				createAndSetNode(doc, abitElement, "lastName", abits.get(i)
						.getLastName());
				createAndSetNode(doc, abitElement, "patronymic", abits.get(i)
						.getPatronymic());
				createAndSetNode(doc, abitElement, "faculty", abits.get(i)
						.getFaculty());
				createAndSetNode(doc, abitElement, "egeSum",
						Integer.toString(abits.get(i).getEgeSum()));
				createAndSetNode(doc, abitElement, "studyProgram", abits.get(i)
						.getStudyProgram());
				createAndSetNode(doc, abitElement, "original",
						Integer.toString(abits.get(i).getOriginalInt()));

				if (abits.get(i).getExams() == null)
					continue;
				if (abits.get(i).getExams().size() > 0) {
					examsElement = doc.createElement("exams");
					abitElement.appendChild(examsElement);
				} else {
					continue;
				}
				for (Map.Entry<Integer, String> e : abits.get(i)
						.getExams().entrySet()) {
					examElement = doc.createElement("exam");
					examsElement.appendChild(examElement);
					setAttribute(examElement, "exam_id", Integer.toString(e.getKey()));
					setAttribute(examElement, "mark", e.getValue());
					/*createAndSetNode(doc, examElement, "subject", abits.get(i)
							.getExams().get(j).getSubject());
					createAndSetNode(doc, examElement, "classroom", abits
							.get(i).getExams().get(j).getClassroom());
					createAndSetNode(doc, examElement, "examDate", abits.get(i)
							.getExams().get(j).getExamDate());
					createAndSetNode(doc, examElement, "mark", abits.get(i)
							.getExams().get(j).getMark());*/
				}
			}

			// Создание преобразователя документа
			Transformer trans = TransformerFactory.newInstance()
					.newTransformer();
			// Создание файла с именем books.xml для записи документа
			java.io.FileWriter fw = new FileWriter(filename);
			// Запись документа в файл
			trans.transform(new DOMSource(doc), new StreamResult(fw));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void loadXMLDataExams(ArrayList<Exam> abits) {
		try {
			DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();
			Document doc = dBuilder.parse(new File(filename));
			NodeList nlist;
			Node tmp_node;
			Element tmp_element;
			int id;
			String subject, classroom, examDate;

			if (doc.hasChildNodes()) {
				tmp_element = (Element) doc.getElementsByTagName("exams").item(
						0);
				nlist = tmp_element.getElementsByTagName("exam");
				abits.clear();
				for (int i = 0; i < nlist.getLength(); i++) {
					tmp_node = nlist.item(i);

					if (tmp_node.getNodeType() == Node.ELEMENT_NODE) {
						System.out.println("Read exam from XML.");
						tmp_element = (Element) tmp_node;
						subject = getSingleNodeContent(tmp_node, "subject");
						classroom = getSingleNodeContent(tmp_node, "classroom");
						examDate = getSingleNodeContent(tmp_node, "examDate");
						id = Integer.parseInt(getSingleNodeContent(tmp_node, "id"));
						abits.add(new Exam(subject, classroom, examDate, id));
					}
				}
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	/*
	  throws ParserConfigurationException,
			TransformerFactoryConfigurationError, IOException,
			TransformerException 
	 */

	/**
	 * Сохраняет базу данных экзаменов
	 * @param abits
	 */
	private void saveXMLDataExams(ArrayList<Exam> abits) {
		try {
			DocumentBuilder builder = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();
			Document doc = builder.newDocument();
			Element abitElement, rootElement = doc
					.createElement("exams");
			doc.appendChild(rootElement);
			System.out.println("Saving exams into "+filename+", size of array: "+abits.size());
			for (int i = 0; i < abits.size(); i++) {
				System.out.println("Saving exam: "+abits.get(i).getSubject());
				abitElement = doc.createElement("exam");
				rootElement.appendChild(abitElement);

				createAndSetNode(doc, abitElement, "subject", abits.get(i)
						.getSubject());
				createAndSetNode(doc, abitElement, "classroom", abits.get(i)
						.getClassroom());
				createAndSetNode(doc, abitElement, "examDate", abits.get(i)
						.getExamDate());
				createAndSetNode(doc, abitElement, "id", Integer.toString(abits.get(i)
						.getId()));
			}

			// Создание преобразователя документа
			Transformer trans = TransformerFactory.newInstance()
					.newTransformer();
			// Создание файла с именем books.xml для записи документа
			java.io.FileWriter fw = new FileWriter(filename);
			// Запись документа в файл
			trans.transform(new DOMSource(doc), new StreamResult(fw));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Создаёт дочерний узёл в xml-файле, и заполняет его текстом
	 * @param parent
	 * @param nodename
	 * @param value
	 */
	private void createAndSetNode(Document doc, Node parent, String nodename,
			String value) {
		Element nickname = doc.createElement(nodename);
		nickname.appendChild(doc.createTextNode(value));
		parent.appendChild(nickname);
	}

	/**
	 * Получает данные из конкретного дочернего одиночного тэга
	 * @param parent родитель
	 * @param name имя тэга
	 * @return
	 */
	private String getSingleNodeContent(Node parent, String name) {
		if (parent.getNodeType() == Node.ELEMENT_NODE) {
			Element el = (Element) parent;
			NodeList nlist = el.getElementsByTagName(name);
			if (nlist.getLength() > 0)
				return nlist.item(0).getTextContent();
			else
				return "";
		} else {
			return "";
		}
	}
	
	 private static void setAttribute(Node node, String attName, String val) {
		    NamedNodeMap attributes = node.getAttributes();
		    Node attNode = node.getOwnerDocument().createAttribute(attName);
		    attNode.setNodeValue(val);
		    attributes.setNamedItem(attNode);
		}

}
