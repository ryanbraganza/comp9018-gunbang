package gb;

import com.jmex.model.converters.X3dToJme;

/**
 * run this class whenever the models are updated
 * 
 * @author ryan
 *
 */
public class Convertor {
	public static void main (String[] args) throws InstantiationException {
		X3dToJme importer = new X3dToJme();
		String from = Constants.Filenames.tankx3d;
		String to = Constants.Filenames.tankjme;
		importer.attemptFileConvert(new String[]{from,to});
	}
}
