package cn.edu.ruc.data;

import cn.edu.ruc.domain.Description;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class DescriptionManager {
	private Map<Integer, Description> entityId2descriptionMap = new HashMap<>();

	public DescriptionManager(String inputPath){
		loadDescription(inputPath);
	}

	public void loadDescription(String inputPath){
		try {
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(inputPath), "UTF-8"));
			String tmpString;
			while((tmpString = bufferedReader.readLine()) != null) {
				String[] tokens = tmpString.split("\t");
				int entityId = Integer.parseInt(tokens[0]);
				String content= tokens[1];
				String image = tokens[2];

				if(!content.equals("null") && !image.equals("null")) {
					entityId2descriptionMap.put(entityId, new Description("", ""));
					//entityId2descriptionMap.put(entityId, new Description(content, image));
				}
			}

			bufferedReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Description getDescription(int entityId) {
		return entityId2descriptionMap.get(entityId);
	}

	public boolean hasDescription(int entityId) {
		if(entityId2descriptionMap.containsKey(entityId))
			return true;
		else
			return false;
	}
}
