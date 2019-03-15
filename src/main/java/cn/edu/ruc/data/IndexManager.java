package cn.edu.ruc.data;


import cn.edu.ruc.domain.DocumentOfEntity;
import cn.edu.ruc.domain.Pair;
import cn.edu.ruc.ultity.URLComponent;
import com.google.gson.Gson;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;

import java.io.*;
import java.net.URLDecoder;
import java.nio.file.Paths;

public class IndexManager {
	private URLComponent urlComponent = new URLComponent("UTF-8");
	private DirectoryReader directoryReader = null;
	private Gson gson = new Gson();
	
	public IndexManager(String inputPath, String outputPath){
		buildIndex(inputPath, outputPath);
	}
	
	public void buildIndex(String inputPath, String outputPath){
		if(!new File(outputPath).isDirectory()) {
			IndexWriterConfig indexWriterConfig = new IndexWriterConfig(new StandardAnalyzer());
			indexWriterConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE);

			try {
				BufferedReader bufferedReader;
				IndexWriter indexWriter = new IndexWriter(FSDirectory.open(Paths.get(outputPath)), indexWriterConfig);

				String tmpString;
				for(String fileName : new File(inputPath).list()) {
					bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(inputPath + fileName)), "UTF-8"));
					while((tmpString = bufferedReader.readLine()) != null){
						DocumentOfEntity documentOfEntity = gson.fromJson(tmpString, DocumentOfEntity.class);
						indexWriter.addDocument(createDocument(documentOfEntity));
					}
					bufferedReader.close();
				}

				indexWriter.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		try {
			directoryReader = DirectoryReader.open(FSDirectory.open(Paths.get(outputPath)));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Document createDocument(DocumentOfEntity documentOfEntity) throws IOException{
		Document document = new Document();

		document.add(new Field("uri", urlComponent.decode(documentOfEntity.getUri()), TextField.TYPE_STORED));
		document.add(new Field("context", urlComponent.decode(documentOfEntity.getContext()).toLowerCase(), TextField.TYPE_STORED));

		String labelString = "";
		for(String label : documentOfEntity.getLabelList())
			labelString += urlComponent.decode(label).toLowerCase() + "\t";
		document.add(new Field("labels", labelString, TextField.TYPE_STORED));

		String similarEntityString = "";
		for(String similarEntity : documentOfEntity.getSimilarEntityList())
			similarEntityString += urlComponent.decode(similarEntity).replaceAll("_", " ").toLowerCase() + "\t";
		document.add(new Field("similarEntities", similarEntityString, TextField.TYPE_STORED));

		String categoryString = "";
		for(String category : documentOfEntity.getCategoryList())
			categoryString += urlComponent.decode(category.replaceAll("Category:", "")).replaceAll("_", " ").toLowerCase() + "\t";
		document.add(new Field("categories", categoryString, TextField.TYPE_STORED));

		String relatedAttributeString = "";
		for(Pair relatedAttribute : documentOfEntity.getRelatedAttributeList())
			relatedAttributeString += relatedAttribute.getRelation() + "\t" + urlComponent.decode(relatedAttribute.getEntity()).toLowerCase() + "\t";
		document.add(new Field("relatedAttributes", relatedAttributeString, TextField.TYPE_STORED));

		String relatedEntityString = "";
		for(Pair relatedEntity : documentOfEntity.getRelatedEntityList())
			relatedEntityString += relatedEntity.getRelation() + "\t" + urlComponent.decode(relatedEntity.getEntity()).replaceAll("_", " ").toLowerCase() + "\t";
		document.add(new Field("relatedEntities", relatedEntityString, TextField.TYPE_STORED));

		return document;
	}
	
	public DirectoryReader getDirectoryReader() {
		return directoryReader;
	}
}
