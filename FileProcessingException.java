package coursework.bilkis;

/**
 * Класс для исключений, связанных с обработкой файлов (чтение, запись, etc)
 * @author bilkis
 *
 */
public class FileProcessingException extends Exception {
	public FileProcessingException() {
		super("Some file issues");
	}
}
