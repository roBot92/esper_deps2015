package hu.bme.mit.test.esper;

import java.util.Calendar;
import java.util.List;
import org.junit.Before;

import com.espertech.esper.client.EPOnDemandPreparedQueryParameterized;
import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.time.CurrentTimeEvent;

import hu.bme.mit.entities.TaxiLog;
import hu.bme.mit.esper.main.App;
import hu.bme.mit.test.AbstractTask2Test;

public class EsperTask2Test extends AbstractTask2Test{

	private EPRuntime runtime;
	private EPOnDemandPreparedQueryParameterized updateNamedWindowQuery;



	@Before
	public void setUp() throws Exception {
		super.setUp();
		runtime = App.initializeEngineForTask2(toplist);
		updateNamedWindowQuery = runtime
				.prepareQueryWithParameters(App.getEplQuery(App.ONDEMAND_UPDATE_NAMED_WINDOW_QUERY));
		rollPseudoClock(0);
	}





	@Override
	protected void insertTaxiLogs(List<TaxiLog> taxiLogs) {
		for (TaxiLog tlog : taxiLogs) {
			updateNamedWindowQuery.setObject(1, tlog.getHack_license());
			runtime.executeQuery(updateNamedWindowQuery);
			runtime.sendEvent(tlog);
		}
		
	}


	@Override
	protected void rollPseudoClock(long time) {
		calendar.add(Calendar.MILLISECOND, (int)time);
		runtime.sendEvent(new CurrentTimeEvent(calendar.getTimeInMillis()));		
	}


	@Override
	protected void fireRules() {
		// NOOP		
	}

}
