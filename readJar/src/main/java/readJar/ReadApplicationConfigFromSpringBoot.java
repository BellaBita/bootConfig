package readJar;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.yaml.snakeyaml.Yaml;

public class ReadApplicationConfigFromSpringBoot {

	// private static final String[] RES_PATHS = "META-INF/MANIFEST.MF,
	// BOOT-INF/classes/application.properties, BOOT-INF/classes/application.yaml,
	// BOOT-INF/classes/application.yaml ";

	public static void main(String[] args) {
		String fileZip = "C:\\workspace\\demoApp01\\target\\demoApp01-0.0.1-SNAPSHOT.jar";

		Pattern p = Pattern.compile("^(BOOT-INF|WEB-INF)/classes/application\\.(properties|yaml|yml)$");

		Map<String, Map<String, Object>> yamlMap = new HashMap<String, Map<String, Object>>();
		Map<String, Properties> propsMap = new HashMap<String, Properties>();

		Properties manifest = new Properties();
		;

		ZipInputStream zis = null;
		try {
			zis = new ZipInputStream(new FileInputStream(fileZip));
			ZipEntry zipEntry = null;
			while ((zipEntry = zis.getNextEntry()) != null) {

				if (zipEntry.getName().equalsIgnoreCase("META-INF/MANIFEST.MF")) {
					System.out.println(zipEntry.getName());
					manifest.load(zis);
				} else if (p.matcher(zipEntry.getName()).matches()) {
					System.out.println(zipEntry.getName());
					if (zipEntry.getName().toLowerCase().matches(".+\\.(yaml|yml)$")) {
						Yaml yaml = new Yaml();
						Map<String, Object> doc = yaml.load(zis);
						yamlMap.put(zipEntry.getName(), doc);
					} else {
						Properties props = new Properties();
						props.load(zis);
						propsMap.put(zipEntry.getName(), props);
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (zis != null) {
				try {
					zis.closeEntry();
				} catch (IOException e) {
				}
				try {
					zis.close();
				} catch (IOException e) {
				}
			}
		}

		String classPath = manifest.getProperty("Spring-Boot-Classes") == null ? "BOOT-INF/classes/"
				: manifest.getProperty("Spring-Boot-Classes");
		Properties applicationEnv = null;
		
		//application.properties 먼저 검색
		for (String key : propsMap.keySet() ) {
			if (key.startsWith(classPath)) {
				applicationEnv = propsMap.get(key);
			}
		}
		//application.properties가 업으면 application.yaml 검색
		if (applicationEnv == null) {
			for (String key : yamlMap.keySet() ) {
				if (key.startsWith(classPath)) {
					applicationEnv = new Properties();
					flattenYaml("",applicationEnv, yamlMap.get(key));
					break;
				}
			}
		}
		
		System.out.println(applicationEnv);
	}
	
	@SuppressWarnings("unchecked")
	private static void flattenYaml(String parentKey, Properties props, Map<String, Object> doc ) {
		for (String key : doc.keySet()) {
			String keyPath = parentKey.isEmpty() ? key : parentKey + "." + key;
			if (doc.get(key) instanceof Map) {
				flattenYaml( keyPath, props, (Map<String, Object>)doc.get(key));
			} else {
				props.put(keyPath, doc.get(key).toString());
			}
		}
	}

}
