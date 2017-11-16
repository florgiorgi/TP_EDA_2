import static org.junit.Assert.*;

import java.io.IOException;
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
public class TestFindRoute {

	private AirportNetwork an;
	private List<String> daysAM ;

	@Before
	public void setUp() throws Exception {
		an = new AirportNetwork();
		daysAM = new ArrayList<String>();
		daysAM.add("Lu");
		daysAM.add("Mi");
		daysAM.add("Vi");
		an.addAirport("EZE", -34.8236, -58.5289);
		an.addAirport("MIA", 25.793333, -80.290556);
		an.addAirport("GRU", -23.435556, -46.473056);
		an.addAirport("COR", -31.31, -64.208333);
		an.addFlight("AA", 5567, daysAM, "EZE", "MIA", "10:00", "13h00m" , 1200.00);
		an.addFlight("GOL", 700, daysAM, "EZE", "GRU", "10:00", "2h00m" , 300.00);
		an.addFlight("GOL", 856, daysAM, "GRU", "MIA", "16:00", "11h00m" , 800.00);
		an.addFlight("AAS", 452, daysAM, "EZE", "COR", "10:00", "1h20m" , 100.00);
		an.addFlight("AAS", 460, daysAM, "COR", "MIA", "13:00", "12h30m" , 800.00);

		
	}

	@Test (timeout = 1000)
	public void testFindRoute() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, IOException {
		an.findRoute("EZE", "MIA", "tt", daysAM, "text", "stdout");
		an.findRoute("EZE", "MIA", "ft", daysAM, "text", "stdout");
		an.findRoute("EZE", "MIA", "pr", daysAM, "text", "stdout");


	}
}

