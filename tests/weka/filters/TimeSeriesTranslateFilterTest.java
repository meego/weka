/*
 * Copyright 2000 Webmind Inc. 
 */

package weka.filters;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import weka.core.Instances;
import weka.core.Instance;

/**
 * Tests TimeSeriesTranslateFilter. Run from the command line with:<p>
 * java weka.filters.TimeSeriesTranslateFilterTest
 *
 * @author <a href="mailto:len@webmind.com">Len Trigg</a>
 * @version $Revision: 1.1 $
 */
public class TimeSeriesTranslateFilterTest 
  extends AbstractTimeSeriesFilterTest {

  public TimeSeriesTranslateFilterTest(String name) { super(name);  }

  /** Creates a default TimeSeriesTranslateFilter */
  public Filter getFilter() {
    return getFilter("2-3");
  }

  /** Creates a specialized TimeSeriesTranslateFilter */
  public Filter getFilter(String rangelist) {
    
    TimeSeriesTranslateFilter af = new TimeSeriesTranslateFilter();
    af.setAttributeIndices(rangelist);
    return af;
  }

  public void testInverted() {
    m_Filter = getFilter("1,4,2-3");
    ((TimeSeriesTranslateFilter)m_Filter).setInvertSelection(true);
    Instances result = useFilter();
    // Number of attributes shouldn't change
    assertEquals(m_Instances.numAttributes(), result.numAttributes());
    assertEquals(m_Instances.numInstances() - 1, result.numInstances());
    // Check conversion looks OK
    for (int i = 0; i < result.numInstances(); i++) {
      Instance in = m_Instances.instance(i + 1);
      Instance out = result.instance(i);
      for (int j = 0; j < result.numAttributes(); j++) {
        if ((j != 4) && (j != 5)) {
          if (in.isMissing(j)) {
            assert("Nonselected missing values should pass through",
                   out.isMissing(j));
          } else if (result.attribute(j).isString()) {
            assertEquals("Nonselected attributes shouldn't change. "
                         + in + " --> " + out,
                         m_Instances.attribute(j).value((int)in.value(j)),
                         result.attribute(j).value((int)out.value(j)));
          } else {
            assertEquals("Nonselected attributes shouldn't change. "
                         + in + " --> " + out,
                         in.value(j),
                         out.value(j), TOLERANCE);
          }
        }
      }
    }    
  }


  public static Test suite() {
    return new TestSuite(TimeSeriesTranslateFilterTest.class);
  }

  public static void main(String[] args){
    junit.textui.TestRunner.run(suite());
  }

}
