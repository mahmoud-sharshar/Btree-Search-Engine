package eg.edu.alexu.csd.filestructure.searchEngine;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.xml.sax.*;

import eg.edu.alexu.csd.filestructure.btree.IBTree;
import eg.edu.alexu.csd.filestructure.btree.Tree;

import org.w3c.dom.*;
import javax.xml.parsers.*;

public class SearchEngine implements ISearchEngine {

	// main Btree to store words in documents
	private IBTree<String, ArrayList<SearchResult>> docBtree;
	// hold words with id and rank in each document
	private Map<String, SearchResult> documentContent;

	public SearchEngine() {
		documentContent = new HashMap<>();
		docBtree = new Tree<>(5);
	}

	@Override
	public void indexWebPage(String filePath) {
		Document xmlDoc = getDocument(filePath);
		// all documents in the file
		NodeList docList = xmlDoc.getElementsByTagName("doc");
		// System.out.println(docList.item(0).getTextContent());
		for (int i = 0; i < docList.getLength(); i++) {
			documentContent = getDocumentMap(docList, i);
			// iterate over map and insert document map in btree
			for (Map.Entry<String, SearchResult> entry : documentContent.entrySet()) {
				ArrayList<SearchResult> SResult = docBtree.search(entry.getKey());
				if (SResult == null) {
					ArrayList<SearchResult> results = new ArrayList<>();
					results.add(entry.getValue());
					docBtree.insert(entry.getKey(), results);
				} else {
					SResult.add(entry.getValue());
				}
			}
		}

	}

	// Reads an XML file into a DOM document
	private Document getDocument(String docString) {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setIgnoringComments(true);
			factory.setIgnoringElementContentWhitespace(true);
			factory.setValidating(false);
			DocumentBuilder builder = factory.newDocumentBuilder();

			return builder.parse(new InputSource(docString));
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			return null;
		}
	}

	// return map of words with id and rank in the document
	private Map<String, SearchResult> getDocumentMap(NodeList docList, int i) {
		Map<String, SearchResult> docWords = new HashMap<>();
		Node document = docList.item(i);
		String doc_id = document.getAttributes().getNamedItem("id").getTextContent();
		String[] docContent = document.getTextContent().split("\\s+");
		// set id and rank for every word in document
		for (int j = 0; j < docContent.length; j++) {
//			System.out.println(docList.item(0).getTextContent().split("\\s+")[i]);
			String word = docContent[j].toLowerCase();
			if (docWords.containsKey(word)) {
				docWords.get(word).setRank(docWords.get(word).getRank() + 1);
			} else {
				SearchResult result = new SearchResult();
				result.setId(doc_id);
				result.setRank(1);
				docWords.put(word, result);
			}
		}
		return docWords;
	}

	@Override
	public void indexDirectory(String directoryPath) {
		File folder = new File(directoryPath);
		File[] listOfFiles = folder.listFiles();
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				this.indexWebPage(listOfFiles[i].getPath());
				System.out.println("Directory " + listOfFiles[i].getPath());
			}

		}
	}

	@Override
	public void deleteWebPage(String filePath) {
		Document xmlDoc = getDocument(filePath);
		// all documents in the file
		NodeList docList = xmlDoc.getElementsByTagName("doc");
		// System.out.println(docList.item(0).getTextContent());
		for (int i = 0; i < docList.getLength(); i++) {
			documentContent = getDocumentMap(docList, i);
			for (Map.Entry<String, SearchResult> entry : documentContent.entrySet()) {
				ArrayList<SearchResult> SResult = docBtree.search(entry.getKey());
				if (SResult != null) {
					for (int j = 0; j < SResult.size(); j++) {
						if (SResult.get(j).getId().equals(entry.getValue().getId())) {
							SResult.remove(j);
//							System.out.println("deleted");
						}
							
					}
				}
			}
		}

	}

	@Override
	public List<ISearchResult> searchByWordWithRanking(String word) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ISearchResult> searchByMultipleWordWithRanking(String sentence) {
		// TODO Auto-generated method stub
		return null;
	}

}
