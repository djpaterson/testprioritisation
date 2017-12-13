package org.kanonizo.test;

import com.scythe.instrumenter.PropertySource;
import org.apache.commons.cli.CommandLine;
import org.junit.Before;
import org.junit.Test;
import org.kanonizo.Properties;
import org.kanonizo.Properties.CoverageApproach;
import org.kanonizo.TestSuitePrioritisation;
import org.mockito.Mock;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

public class TestSuitePrioritisationTest extends MockitoTest {
  @Mock private CommandLine line;
  private java.util.Properties props = new java.util.Properties();
  private PropertySource source = Properties.instance();
  private List<PropertySource> sources = Collections.singletonList(source);

  @Before
  public void setup() {
    props.clear();
    when(line.getOptionProperties("D")).thenReturn(props);
  }

  @Test
  public void testSetCoverageApproach() {
    props.put("coverage_approach", "branch");
    assertEquals(CoverageApproach.LINE, Properties.COVERAGE_APPROACH);
    TestSuitePrioritisation.handleProperties(line, sources);
    assertEquals(CoverageApproach.BRANCH, Properties.COVERAGE_APPROACH);
  }

  @Test
  public void testSetPropertyFromSource() throws IllegalAccessException {
    assertEquals(50, Properties.POPULATION_SIZE);
    source.setParameter("population_size", "100");
    assertEquals(100, Properties.POPULATION_SIZE);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSetIllegalType() throws IllegalAccessException {
    source.setParameter("population_size", "INVALID_TYPE");
    fail("Population size should not be settable to a string");
  }

  @Test
  public void testSingleton() {
    assertEquals(source, Properties.instance());
  }
}