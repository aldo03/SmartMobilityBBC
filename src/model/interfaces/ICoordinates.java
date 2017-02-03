package model.interfaces;

/**
 * Interface that models GPS coordinates  
 * @author BBC
 *
 */
public interface ICoordinates {
	/**
	 * gets the latitude
	 * @return latitude
	 */
	double getLatitude();
	
	/**
	 * gets the longitude
	 * @return longitude
	 */
	double getLongitude();
	
	
	/**
	 * tells if the coordinates are close enough
	 * @param coordinates
	 * @return true if the coordinates are close enough, false otherwise
	 */
	boolean isCloseEnough(ICoordinates coordinates);
}
