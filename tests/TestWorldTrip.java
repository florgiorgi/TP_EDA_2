import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

/**
 * Since most of the methods are either private or public void that directly print on
 * the console, testing using asserts was not possible.
 * JUnit Testing was focused on time efficiency.
 *
 */
public class TestWorldTrip {

	private AirportNetwork an;
	private List<String> daysAM ;

	@Before
	public void setUp() throws Exception {
		an = new AirportNetwork();
		daysAM = new ArrayList<String>();
		daysAM.add("Lu");
		an.addAirport("EZE", -34.8236, -58.5289);
		an.addAirport("COR", -31.31, -64.208333);
		an.addAirport("USH", -54.843333, -68.295833);
		an.addAirport("CRD", -45.785278, -67.465556);
		an.addFlight("AA", 5567, daysAM, "EZE", "COR", "10:00", "1h30m" , 4800.0);
		an.addFlight("AA", 2554, daysAM, "COR", "CRD", "12:00", "3h00m" , 3524.0);
		an.addFlight("LAD", 101, daysAM, "CRD", "USH", "15:30", "1h20m" , 1000.0);
		an.addFlight("AA", 1881, daysAM, "USH", "EZE", "20:00", "3h25m" , 3800.0);

	}
	

	@Test
	public void testWorldTrip() {
		an.worldTrip("EZE", "tt", daysAM);
	}

}

