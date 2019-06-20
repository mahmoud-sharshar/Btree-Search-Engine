package eg.edu.alexu.csd.filestructure.searchEngine;

public class test {

	public static void main(String[] args) {
		SearchEngine s = new SearchEngine();
		s.indexDirectory("C:\\Users\\mahmo\\Downloads\\BTree\\src\\Wikipedia Data Sample");
		s.deleteWebPage("C:\\Users\\mahmo\\Downloads\\BTree\\src\\Wikipedia Data Sample\\wiki_02.xml");

	}

}
