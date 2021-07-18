package sd.sym.assignment.gophygita.configuration;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.io.*;
import java.util.Properties;

@Slf4j
public class ResourceManagement {

	private final String propertyFile;

	public ResourceManagement(String propertyFile) {
		this.propertyFile = propertyFile;
	}

	public String getPath() {
		String basePath = new File("").getAbsolutePath();
		log.debug("JAR basePath : {}", basePath);
		return basePath;
	}

	public String getRootPath() {
		String rootPath = getPath() + File.separator + "target" + File.separator + "classes";
		log.debug("rootPath : {}", rootPath);
		return rootPath;
	}

	public String getPropertyFilePath() {
		String propertyFilePath = getRootPath() + File.separator + propertyFile;
		log.debug("{} PropertyFilePath : {}", propertyFile, propertyFilePath);
		return propertyFilePath;
	}

	public Properties readPropertyFile() {
		try(InputStream in = new FileInputStream(getPropertyFilePath())) {
			Properties prop = new Properties();
			prop.load(in);
			log.debug("Property prop - {}", prop);
			return prop;
		} catch (Exception e) {
			log.error("readPropertyFile Error :: {}", ExceptionUtils.getStackTrace(e));
			return null;
		}
	}

	public boolean removePropertyFileKey(String key) {
		try(OutputStream out = new FileOutputStream(getPropertyFilePath())) {
			Properties properties = readPropertyFile();
			if (properties.containsKey(key)) {
				log.debug("Key {} contain", key);
				properties.remove(key);
				properties.store(out, "Deleted key " + key);
				log.debug("Property prop - {}", properties);
				return true;
			} else {
				log.debug("Key {} not contain", key);
			}
		} catch (Exception e) {
			log.error("removePropertyFileKey Error :: {}", ExceptionUtils.getStackTrace(e));
		}
		return false;
	}

	public boolean writePropertyFileKeyValue(String key, String value) {
		try(OutputStream out = new FileOutputStream(getPropertyFilePath())) {
			Properties properties = readPropertyFile();
			properties.put(key, value);
			properties.store(out, "Key-" + key + ", Value-" + value + " Written");
			log.debug("Key-{}, Value-{} Written", key, value);
			return true;
		} catch (Exception e) {
			log.error("writePropertyFileKeyValue Error :: {}", ExceptionUtils.getStackTrace(e));
			return false;
		}
	}

	public boolean writePropertyFileProp(Properties prop) {
		try(OutputStream out = new FileOutputStream(getPropertyFilePath())) {
			Properties properties = readPropertyFile();
			properties.putAll(prop);
			properties.store(out, "Properties added" + prop);
			log.debug("Properties added" + prop);
			return true;
		} catch (Exception e) {
			log.error("writePropertyFileProp Error :: {}", ExceptionUtils.getStackTrace(e));
			return false;
		}
	}
}