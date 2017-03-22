package org.latefire.deals;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.latefire.deals.database.AbsModel;
import org.latefire.deals.database.DatabaseManager;
import org.latefire.deals.database.Deal;

import static org.junit.Assert.assertEquals;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class) public class ExampleInstrumentedTest {

  @Test public void useAppContext() throws Exception {
    // Context of the app under test.
    Context appContext = InstrumentationRegistry.getTargetContext();
    assertEquals("org.latefire.deals", appContext.getPackageName());
  }

  @Test public void testOrderByChild1() throws Exception {
    DatabaseManager mgr = DatabaseManager.getInstance();
    mgr.getDealsOrderByChild1("dealPrice", models -> {
      ArrayList<Deal> deals = (ArrayList<Deal>) models;
      print(deals);
    });
    wait(10);
  }

  @Test public void testOrderByChild2() throws Exception {
    DatabaseManager mgr = DatabaseManager.getInstance();
    mgr.getDealsOrderByChild2("dealPrice", new DatabaseManager.MultiSingleQueryCallback() {
          // Called once for each fetched object
          @Override public void yourResult(AbsModel model) {
            Deal deal = (Deal) model;
            print(deal);
          }
          // Called when all data has been fetched
          @Override public void finished() {
            print("Finished loading data");
          }
        });
        wait(10);
  }

  @Test public void testOrderByChild3() throws Exception {
    DatabaseManager mgr = DatabaseManager.getInstance();
    mgr.getDealsOrderByChild3("dealPrice", models -> {
      ArrayList<Deal> deals = (ArrayList<Deal>) models;
      print(deals);
    });
    wait(10);
  }

  @Test public void testOrderByKey() throws Exception {
    DatabaseManager mgr = DatabaseManager.getInstance();
    mgr.getDealsOrderByKey(models -> {
      ArrayList<Deal> deals = (ArrayList<Deal>) models;
      print(deals);
    });
    wait(10);
  }

  @Test public void testOrderByValue() throws Exception {
    DatabaseManager mgr = DatabaseManager.getInstance();
    mgr.getDealsOrderByValue(models -> {
      ArrayList<Deal> deals = (ArrayList<Deal>) models;
      print(deals);
    });
    wait(10);
  }

  // Stop execution for n seconds: needed for tests that include a callback
  private void wait(int seconds) throws Exception {
    CountDownLatch latch = new CountDownLatch(1);
    latch.await(seconds, TimeUnit.SECONDS);
  }

  private void print(Object o) {
    System.out.println(o.toString());
  }
}
