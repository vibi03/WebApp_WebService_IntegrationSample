package utilities;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;

public class PropertyReader {

	private static CompositeConfiguration cc = null;

	private static String[] CONFIG_FILES = new String[] {
			"configuration.properties"/*, "response.properties"*/};
// "request.properties"
	static {
		cc = new CompositeConfiguration();
		PropertiesConfiguration conf = new PropertiesConfiguration();
		try {
		for (String propertyFileName : CONFIG_FILES) {			
				
				conf.clear();
				conf.setDelimiterParsingDisabled(true);
				conf.load(propertyFileName);
				conf.setReloadingStrategy(new FileChangedReloadingStrategy());
				cc.addConfiguration(conf);
			} 
		}
		catch (Exception exception) {
				System.err.println("!!! Exception reading property files !!!");
				exception.printStackTrace();
			}
		
	}

	public static String getProperty(String propertyKey) {

		return (String) cc.getProperty(propertyKey);
	}

	// property reader - with null check
	public static String getPropertyNotNull(String propertyKey)
			throws Throwable {

		String property = PropertyReader.getProperty(propertyKey);
		if (property == null) {
			throw new Exception("!!! Error - getProperty for '" + propertyKey
					+ "' returned null");
		}

		return property;
	}
}
