package lse;

import lse.LittleSearchEngine;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

import java.io.*;
import java.util.*;

public class Driver 
{
	
	
	public static void main (String args[]) throws IOException {
		LittleSearchEngine engine=new LittleSearchEngine();
//		Scanner scan  = new Scanner(new File("AliceCh1.txt"));
//		Scanner noise  = new Scanner(new File("noisewords.txt"));
//		while(noise.hasNextLine()) {
//			String bit = noise.nextLine();
//			engine.noiseWords.add(bit);
//		}
//		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//		System.out.println("Enter Doc Name-->");
		
		String doc = "docs.txt";//br.readLine();
//		System.out.println("Enter Noise words Name-->");
		String noise = "noisewords.txt";//br.readLine();
		engine.makeIndex(doc,noise);
		Object[] array = engine.keywordsIndex.keySet().toArray();
		//System.out.println(engine.keywordsIndex.containsKey("Rabbit"));
		for(String Key: engine.keywordsIndex.keySet()) {
			System.out.println(Key);
			System.out.println(engine.keywordsIndex.get(Key));
		}
		
		//engine.noiseWords
		
		//HashMap<String,Occurrence> map = new HashMap();
		Occurrence one = new Occurrence("hi",12);
		Occurrence two = new Occurrence("hi",8);
		Occurrence three = new Occurrence("hi",7);
		Occurrence four = new Occurrence("hi",5);
		Occurrence five = new Occurrence("hi",3);
		Occurrence six = new Occurrence("hi",2);
		Occurrence seven = new Occurrence("hello",6);
		//Occurrence eight = new Occurrence("hi",13);
		//map.put("hello", one);
		ArrayList<Occurrence> list = new ArrayList();
		list.add(one);
		list.add(two);
		list.add(three);
		list.add(four);
		list.add(five);
		list.add(six);
		list.add(seven);
		//System.out.println("ILO: "+ engine.insertLastOccurrence(list));
		System.out.print("ANSWER: " + engine.top5search("own","sound"));
		//System.out.println(engine.getKeyword("World"));
		//engine.keywordsIndex.put("hello", list);
		//engine.mergeKeywords(map);
		//System.out.println(engine.getKeyword("hellos"));
		
		
		
//	list.add(five);
//	list.add(six);
//	list.add(seven);
//	list.add(eight);
//		
//		ArrayList answer = engine.insertLastOccurrence(list);
//		
//		for(int i =0;i<answer.size();i++) {
//			System.out.print(answer.get(i)+",");
//		}
//		System.out.println();
//		for(int i =0;i<list.size();i++) {
//			System.out.print(list.get(i)+",");
//		}
	}
	
}
