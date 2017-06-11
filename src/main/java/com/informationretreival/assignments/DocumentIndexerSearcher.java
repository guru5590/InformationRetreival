package com.informationretreival.assignments;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.misc.HighFreqTerms;
import org.apache.lucene.misc.HighFreqTerms.TotalTermFreqComparator;

public class DocumentIndexerSearcher {

	// Using SimpleAnalyser to index the documents
	private static Analyzer analyzer = new SimpleAnalyzer();
	private static Directory indexDirectory = new RAMDirectory();
	private static String[] indexfields = { ProgramConstants.LINE_NUMBER, ProgramConstants.PLAY_NAME,
			ProgramConstants.SPEECH_NUMBER, ProgramConstants.TEXT_ENTRY, ProgramConstants.SPEAKER };

	private Document createDocument(DocumentStructure doc) {

		Document document = new Document();
		document.add(new LongPoint(ProgramConstants.LINE_ID, doc.getLineId()));
		document.add(new StringField(ProgramConstants.PLAY_NAME, doc.getPlayName(), Field.Store.YES));
		document.add(new StringField(ProgramConstants.SPEECH_NUMBER, doc.getSpeechNumber(), Field.Store.YES));
		document.add(new StringField(ProgramConstants.LINE_NUMBER, doc.getLineNumber(), Field.Store.YES));
		document.add(new StringField(ProgramConstants.SPEAKER, doc.getSpeaker(), Field.Store.YES));
		document.add(new TextField(ProgramConstants.TEXT_ENTRY, doc.getTextEntry(), Field.Store.YES));
		return document;
	}

	public void indexDocument(List<DocumentStructure> inputDocumentList) throws Exception {

		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		IndexWriter writer = new IndexWriter(indexDirectory, config);
		Document doc = null;

		for (DocumentStructure inputDoc : inputDocumentList) {
			doc = createDocument(inputDoc);
			writer.addDocument(doc);
		}

		writer.close();

		System.out.println("Indexing Complete");
	}

	public HashMap<String, ArrayList<String>> computeIndexStatsAndFrequentTerms() throws IOException, Exception {

		HashMap<String, ArrayList<String>> termsdictionary = new HashMap<>();
		
		IndexReader ireader = DirectoryReader.open(indexDirectory);
		System.out.println("Total number of Documents in Index = " + ireader.maxDoc());

		for (String field : indexfields) {
			ArrayList<String> indexedTermsList = new ArrayList<String>();
			org.apache.lucene.misc.TermStats[] terms = HighFreqTerms.getHighFreqTerms(ireader,
					ProgramConstants.numTopFields, field, new TotalTermFreqComparator());
			System.out.println("Top Terms For the indexed field " + field.toUpperCase());
			
			for (int i = 0; i < terms.length; i++) {
				System.out.println(terms[i].toString());
				indexedTermsList.add(terms[i].termtext.utf8ToString());

			}
			termsdictionary.put(field, indexedTermsList);
			System.out.println();
		}
		
		return termsdictionary;
	}

	private BooleanQuery createBooleanQuery(HashMap<String, ArrayList<String>> dictTerms, String clause) {
		Random randomGenerator = new Random();
		int randomIntBound = dictTerms.size();
		
		List<String> keys      = new ArrayList<String>(dictTerms.keySet());
		String       randomKey = keys.get( randomGenerator.nextInt(keys.size()) );
		ArrayList<String>  valuelist     = dictTerms.get(randomKey);
		
		BooleanQuery booleanQuery = new BooleanQuery.Builder().build();

		// create a term to to be used in search
		Term term1 = new Term(randomKey, valuelist.get(randomGenerator.nextInt(randomIntBound)));

		// create the term query object
		Query query1 = new TermQuery(term1);
		
		List<String> keys2      = new ArrayList<String>(dictTerms.keySet());
		String       randomKey2 = keys2.get( randomGenerator.nextInt(keys.size()) );
		ArrayList<String>  valuelist2     = dictTerms.get(randomKey2);
		
		Term term2 = new Term(randomKey2, valuelist2.get(randomGenerator.nextInt(randomIntBound)));
		// create the term query object
		Query query2 = new TermQuery(term2);

		if (clause.equalsIgnoreCase("and")) {

			BooleanQuery booleanQuery1 = new BooleanQuery.Builder().add(query1, BooleanClause.Occur.MUST)
					.add(query2, BooleanClause.Occur.MUST).build();
			return booleanQuery1;
			
		} else if (clause.equalsIgnoreCase("or")) {

			BooleanQuery booleanQuery1 = new BooleanQuery.Builder().add(query1, BooleanClause.Occur.SHOULD)
					.add(query2, BooleanClause.Occur.SHOULD).build();
			return booleanQuery1;
		}
		return booleanQuery;
	}

	public void searchUsingBooleanQuery(HashMap<String, ArrayList<String>> dictTerms, String booleanOperation)
			throws IOException, ParseException {

		// Now search the index:
		DirectoryReader ireader = DirectoryReader.open(indexDirectory);

		IndexSearcher isearcher = new IndexSearcher(ireader);
		List<Integer> numberSearchResults = new ArrayList<Integer>();
		for (int index = 0; index < ProgramConstants.numberofSearchResults.length; index++) {
			numberSearchResults.add(ProgramConstants.numberofSearchResults[index]);
		}

		System.out.println("Results for Boolean " + booleanOperation.toUpperCase());
		for (Integer loop : numberSearchResults) {
			long totalTime = 0;
			long totalHits = 0;
			for (int i = 1; i <= ProgramConstants.numBooleanQueries; ++i) {
				BooleanQuery booleanQuery = createBooleanQuery(dictTerms, booleanOperation);
				long startTime = System.nanoTime();
				ScoreDoc[] hits = isearcher.search(booleanQuery, loop).scoreDocs;
				long endTime = System.nanoTime();
				totalHits = totalHits + hits.length;
				totalTime = (endTime - startTime);
			}
			System.out.println(
					"Average time to extract top  " + loop + " Documents = " + (totalTime / loop) + " nano seconds");

		}
		System.out.println();
	}
}
