package lse;

import java.io.*;
import java.util.*;

/**
 * This class builds an index of keywords. Each keyword maps to a set of pages in
 * which it occurs, with frequency of occurrence in each page.
 *
 */
public class LittleSearchEngine {
	
	/**
	 * This is a hash table of all keywords. The key is the actual keyword, and the associated value is
	 * an array list of all occurrences of the keyword in documents. The array list is maintained in 
	 * DESCENDING order of frequencies.
	 */
	HashMap<String,ArrayList<Occurrence>> keywordsIndex;
	
	/**
	 * The hash set of all noise words.
	 */
	HashSet<String> noiseWords;
	
	/**
	 * Creates the keyWordsIndex and noiseWords hash tables.
	 */
	public LittleSearchEngine() {
		keywordsIndex = new HashMap<String,ArrayList<Occurrence>>(1000,2.0f);
		noiseWords = new HashSet<String>(100,2.0f);
	}
	
	/**
	 * Scans a document, and loads all keywords found into a hash table of keyword occurrences
	 * in the document. Uses the getKeyWord method to separate keywords from other words.
	 * 
	 * @param docFile Name of the document file to be scanned and loaded
	 * @return Hash table of keywords in the given document, each associated with an Occurrence object
	 * @throws FileNotFoundException If the document file is not found on disk
	 */
	public HashMap<String,Occurrence> loadKeywordsFromDocument(String docFile) 
	throws FileNotFoundException {
//		
		HashMap<String,Occurrence> list = new HashMap<String,Occurrence>(1000,2.0f);
		Scanner scan = new Scanner(new File(docFile));
		while(scan.hasNext()) {
			String preCheck = scan.next();
			String bit = getKeyword(preCheck);
				if(bit!= null) {//valid keyword
					if(list.containsKey(bit)) {
						Occurrence key = list.get(bit);
						key.frequency = key.frequency + 1;
						list.put(bit, key);	
					}else {
						Occurrence key = new Occurrence(docFile,1);
						list.put(bit,key);
					}
					
				}
			
		}
		
		return list;
	}
	
	/**
	 * Merges the keywords for a single document into the master keywordsIndex
	 * hash table. For each keyword, its Occurrence in the current document
	 * must be inserted in the correct place (according to descending order of
	 * frequency) in the same keyword's Occurrence list in the master hash table. 
	 * This is done by calling the insertLastOccurrence method.
	 * 
	 * @param kws Keywords hash table for a document
	 */
	public void mergeKeywords(HashMap<String,Occurrence> kws) {
		Object[] array = kws.keySet().toArray();
		for(int i =0;i<array.length;i++) {
			if(keywordsIndex.containsKey(array[i].toString())) {
				ArrayList<Occurrence> list = keywordsIndex.get(array[i].toString());//grabs master
				Occurrence item = kws.get(array[i]);//local
				list.add(item);
				insertLastOccurrence(list);
				keywordsIndex.put(array[i].toString(), list);
			}else {
				ArrayList<Occurrence> list = new ArrayList();
				Occurrence item = kws.get(array[i].toString());
				list.add(item);
				insertLastOccurrence(list);
				keywordsIndex.put(array[i].toString(), list);
			}
		}
	}
	
	/**
	 * Given a word, returns it as a keyword if it passes the keyword test,
	 * otherwise returns null. A keyword is any word that, after being stripped of any
	 * trailing punctuation, consists only of alphabetic letters, and is not
	 * a noise word. All words are treated in a case-INsensitive manner.
	 * 
	 * Punctuation characters are the following: '.', ',', '?', ':', ';' and '!'
	 * 
	 * @param word Candidate word
	 * @return Keyword (word without trailing punctuation, LOWER CASE)
	 */
	public String getKeyword(String word) {
		if((word ==null)||word.equals(null)){
			return null;
		}
		word = word.toLowerCase();
		String temp = word;
		for(int i=word.length()-1;i>0;i--) {//removes punc at end
			if(Character.isLetter(word.charAt(i))){
				break;
			}
			if(word.charAt(i)=='.'||word.charAt(i)==','||word.charAt(i)=='?'||word.charAt(i)==';'||word.charAt(i)==':'||word.charAt(i)=='!') {
				temp = temp.substring(0, temp.length()-1);
			}
		}
		for(int i=0;i<temp.length();i++) {//checks if it contains punc in word 'what-ever'
			char c = temp.charAt(i);
			if(!Character.isLetter(c)) {
				return null;
			}
		}
		if(noiseWords.contains(temp)) {//checks if it is noiseword
			return null;
		}
		return temp;
	}
	
	/**
	 * Inserts the last occurrence in the parameter list in the correct position in the
	 * list, based on ordering occurrences on descending frequencies. The elements
	 * 0..n-2 in the list are already in the correct order. Insertion is done by
	 * first finding the correct spot using binary search, then inserting at that spot.
	 * 
	 * @param occs List of Occurrences
	 * @return Sequence of mid point indexes in the input list checked by the binary search process,
	 *         null if the size of the input list is 1. This returned array list is only used to test
	 *         your code - it is not used elsewhere in the program.
	 */
	public ArrayList<Integer> insertLastOccurrence(ArrayList<Occurrence> occs) {
		ArrayList<Integer> answer = new ArrayList();// add mids to this //return
		if(occs.size()<=1) {
			return null;
		}
			int low = 0;
			int high = occs.size()-2;
			int mid = (low + high)/2;
			int value = occs.get(occs.size()-1).frequency;
			
			while(high>=low) {
				mid = (high+low)/2;
				int term = occs.get(mid).frequency;
				answer.add(mid);
				if(term == value) {
					break;
				}else if(term<value) {
					high = mid-1;
				}else if(term>value) {
					low = mid+1;
					if(high<=mid) {
						mid++;
					}
					
				}
				
			}
		answer.add(mid);
		Occurrence insert = occs.get(occs.size()-1);
		occs.remove(occs.size()-1);
		int index = answer.get(answer.size()-1);
		occs.add(index, insert);
		answer.remove(answer.size()-1);//removes extra
		return answer;
		
		
	}
	
	/**
	 * This method indexes all keywords found in all the input documents. When this
	 * method is done, the keywordsIndex hash table will be filled with all keywords,
	 * each of which is associated with an array list of Occurrence objects, arranged
	 * in decreasing frequencies of occurrence.
	 * 
	 * @param docsFile Name of file that has a list of all the document file names, one name per line
	 * @param noiseWordsFile Name of file that has a list of noise words, one noise word per line
	 * @throws FileNotFoundException If there is a problem locating any of the input files on disk
	 */
	public void makeIndex(String docsFile, String noiseWordsFile) 
	throws FileNotFoundException {
		// load noise words to hash table
		Scanner sc = new Scanner(new File(noiseWordsFile));
		while (sc.hasNext()) {
			String word = sc.next();
			noiseWords.add(word);
		}
		
		// index all keywords
		sc = new Scanner(new File(docsFile));
		while (sc.hasNext()) {
			String docFile = sc.next();
			HashMap<String,Occurrence> kws = loadKeywordsFromDocument(docFile);
			mergeKeywords(kws);
		}
		sc.close();
	}
	
	/**
	 * Search result for "kw1 or kw2". A document is in the result set if kw1 or kw2 occurs in that
	 * document. Result set is arranged in descending order of document frequencies. (Note that a
	 * matching document will only appear once in the result.) Ties in frequency values are broken
	 * in favor of the first keyword. (That is, if kw1 is in doc1 with frequency f1, and kw2 is in doc2
	 * also with the same frequency f1, then doc1 will take precedence over doc2 in the result. 
	 * The result set is limited to 5 entries. If there are no matches at all, result is null.
	 * 
	 * @param kw1 First keyword
	 * @param kw1 Second keyword
	 * @return List of documents in which either kw1 or kw2 occurs, arranged in descending order of
	 *         frequencies. The result size is limited to 5 documents. If there are no matches, returns null.
	 */
	public ArrayList<String> top5search(String kw1, String kw2) {
		/** COMPLETE THIS METHOD **/
		
		ArrayList<Occurrence> word1 = new ArrayList();
		ArrayList<Occurrence> word2 = new ArrayList();
		ArrayList<Occurrence> total = new ArrayList();
		if(keywordsIndex.containsKey(kw1)) {
			word1=keywordsIndex.get(kw1);
		}
		if(keywordsIndex.containsKey(kw2)) {
			word2 = keywordsIndex.get(kw2);
		}
		total.addAll(word1);
		total.addAll(word2);
		
		if(total.size()==0) {
			return null;
		}
		for(int i=0;i<word1.size();i++) {
			for(int k =word1.size();k<total.size();k++) {
				if((total.get(i).frequency==total.get(k).frequency)&&(total.get(i).document==total.get(k).document)) {
					total.remove(k);
					break;
				}
				if(total.get(i).frequency<total.get(k).frequency) {//perform swap
					
					Occurrence swap1 = total.get(i);
					Occurrence swap2 = total.get(k);
					total.set(i,swap2);
					total.set(k, swap1);
				}
			}
		}
		
		
		// Remove Duplicates
		for(int i=0;i<total.size()-1;i++) {
			for(int k=i+1;k<total.size();k++) {
				if(total.get(i).document==total.get(k).document) {
					total.remove(k);
				}
			}
			
		}
		
		//TRIM
		for(int i=total.size();i>5;i--) {
			total.remove(total.size()-1);
		}
		//load in answer
		ArrayList<String> answer = new ArrayList();
		for(int i=0;i<total.size();i++) {
			answer.add(total.get(i).document);
		}
		
		return answer;
	
	}
}
