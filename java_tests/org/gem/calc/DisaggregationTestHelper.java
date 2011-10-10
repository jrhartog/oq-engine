package org.gem.calc;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.opensha.commons.data.Site;
import org.opensha.commons.data.function.ArbitrarilyDiscretizedFunc;
import org.opensha.commons.data.function.DiscretizedFuncAPI;
import org.opensha.commons.geo.BorderType;
import org.opensha.commons.geo.Location;
import org.opensha.commons.geo.LocationList;
import org.opensha.commons.geo.Region;
import org.opensha.commons.param.DoubleParameter;
import org.opensha.sha.calc.HazardCurveCalculator;
import org.opensha.sha.earthquake.FocalMechanism;
import org.opensha.sha.earthquake.griddedForecast.MagFreqDistsForFocalMechs;
import org.opensha.sha.earthquake.rupForecastImpl.GEM1.GEM1ERF;
import org.opensha.sha.earthquake.rupForecastImpl.GEM1.SourceData.GEMAreaSourceData;
import org.opensha.sha.earthquake.rupForecastImpl.GEM1.SourceData.GEMSourceData;
import org.opensha.sha.imr.ScalarIntensityMeasureRelationshipAPI;
import org.opensha.sha.imr.attenRelImpl.BA_2008_AttenRel;
import org.opensha.sha.imr.attenRelImpl.CB_2008_AttenRel;
import org.opensha.sha.imr.param.IntensityMeasureParams.PGA_Param;
import org.opensha.sha.imr.param.OtherParams.ComponentParam;
import org.opensha.sha.imr.param.OtherParams.SigmaTruncLevelParam;
import org.opensha.sha.imr.param.OtherParams.SigmaTruncTypeParam;
import org.opensha.sha.imr.param.OtherParams.StdDevTypeParam;
import org.opensha.sha.imr.param.SiteParams.DepthTo2pt5kmPerSecParam;
import org.opensha.sha.imr.param.SiteParams.Vs30_Param;
import org.opensha.sha.magdist.GutenbergRichterMagFreqDist;
import org.opensha.sha.util.TectonicRegionType;

public class DisaggregationTestHelper
{
	public static final Double[] LAT_BIN_LIMS = {-0.6, -0.3, -0.1, 0.1, 0.3, 0.6};
	public static final Double[] LON_BIN_LIMS = {-0.6, -0.3, -0.1, 0.1, 0.3, 0.6};
	public static final Double[] MAG_BIN_LIMS = {5.0, 6.0, 7.0, 8.0, 9.0};
	public static final Double[] EPS_BIN_LIMS = {-3.5, -2.5, -1.5, -0.5, 0.5, 1.5, 2.5, 3.5};
	public static final Double[] DIST_BIN_LIMS = {0.0, 20.0, 40.0, 60.0};
	public static final double POE = 0.1;
	public static final List<Double> IMLS = 
			Arrays.asList(mapLog(new Double[] {
				0.005, 0.007, 0.0098, 0.0137, 0.0192, 0.0269,
				0.0376, 0.0527, 0.0738, 0.103, 0.145, 0.203,
				0.284, 0.397, 0.556, 0.778, 1.09, 1.52, 2.13}));

	public static final double[][][][][] EXPECTED =
		{{{{{0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0}},
		   {{0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0}},
		   {{0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0}},
		   {{0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0}}},
		  {{{0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0}},
		   {{0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0}},
		   {{0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0}},
		   {{0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0036101083032490976, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0}}},
		  {{{0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0}},
		   {{0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0}},
		   {{0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.007220216606498195, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0}},
		   {{0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0}}},
		  {{{0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0}},
		   {{0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.007220216606498195, 0.0, 0.0, 0.0, 0.0},
		    {0.0036101083032490976, 0.0, 0.0, 0.0, 0.0}},
		   {{0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0036101083032490976, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0}},
		   {{0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0036101083032490976, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0}}},
		  {{{0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0}},
		   {{0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0}},
		   {{0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0}},
		   {{0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0}}}},
		 {{{{0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0}},
		   {{0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0036101083032490976, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0}},
		   {{0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0}},
		   {{0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0}}},
		  {{{0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0}},
		   {{0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0}},
		   {{0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.007220216606498195, 0.0, 0.0, 0.0, 0.0},
		    {0.0036101083032490976, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0}},
		   {{0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0}}},
		  {{{0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.032490974729241874, 0.0, 0.0, 0.0, 0.0},
		    {0.0036101083032490976, 0.0, 0.0, 0.0, 0.0}},
		   {{0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0036101083032490976, 0.0, 0.0, 0.0, 0.0},
		    {0.021660649819494584, 0.0, 0.0, 0.0, 0.0},
		    {0.007220216606498195, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0}},
		   {{0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.010830324909747292, 0.0, 0.0, 0.0, 0.0},
		    {0.007220216606498195, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0}},
		   {{0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0036101083032490976, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0}}},
		  {{{0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.010830324909747292, 0.0, 0.0, 0.0, 0.0},
		    {0.010830324909747292, 0.0, 0.0, 0.0, 0.0}},
		   {{0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.010830324909747292, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0}},
		   {{0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.010830324909747292, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0}},
		   {{0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0}}},
		  {{{0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0}},
		   {{0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0036101083032490976, 0.0, 0.0, 0.0, 0.0}},
		   {{0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.007220216606498195, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0}},
		   {{0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0}}}},
		 {{{{0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0}},
		   {{0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0}},
		   {{0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0}},
		   {{0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0}}},
		  {{{0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0036101083032490976, 0.0, 0.0, 0.0, 0.0}},
		   {{0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0036101083032490976, 0.0, 0.0, 0.0, 0.0},
		    {0.007220216606498195, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0}},
		   {{0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0036101083032490976, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0}},
		   {{0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0}}},
		  {{{0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.09025270758122744, 0.0, 0.0, 0.0, 0.0},
		    {0.3574007220216607, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.01444043321299639, 0.0, 0.0, 0.0, 0.0},
		    {0.007220216606498195, 0.0, 0.0, 0.0, 0.0}},
		   {{0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.06859205776173286, 0.0, 0.0, 0.0, 0.0},
		    {0.007220216606498195, 0.0, 0.0, 0.0, 0.0},
		    {0.05054151624548736, 0.0, 0.0, 0.0, 0.0},
		    {0.007220216606498195, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0}},
		   {{0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0036101083032490976, 0.0, 0.0, 0.0, 0.0},
		    {0.010830324909747292, 0.0, 0.0, 0.0, 0.0},
		    {0.0036101083032490976, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0}},
		   {{0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0036101083032490976, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0}}},
		  {{{0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.02527075812274368, 0.0, 0.0, 0.0, 0.0},
		    {0.0036101083032490976, 0.0, 0.0, 0.0, 0.0}},
		   {{0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.010830324909747292, 0.0, 0.0, 0.0, 0.0},
		    {0.010830324909747292, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0}},
		   {{0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0036101083032490976, 0.0, 0.0, 0.0, 0.0},
		    {0.0036101083032490976, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0}},
		   {{0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0036101083032490976, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0}}},
		  {{{0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0}},
		   {{0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0036101083032490976, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0}},
		   {{0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0036101083032490976, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0}},
		   {{0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0}}}},
		 {{{{0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0}},
		   {{0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0036101083032490976, 0.0, 0.0, 0.0, 0.0}},
		   {{0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0}},
		   {{0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0}}},
		  {{{0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0}},
		   {{0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.01444043321299639, 0.0, 0.0, 0.0, 0.0},
		    {0.0036101083032490976, 0.0, 0.0, 0.0, 0.0}},
		   {{0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.007220216606498195, 0.0, 0.0, 0.0, 0.0},
		    {0.0036101083032490976, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0}},
		   {{0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0}}},
		  {{{0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0}},
		   {{0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0036101083032490976, 0.0, 0.0, 0.0, 0.0},
		    {0.0036101083032490976, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0}},
		   {{0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0036101083032490976, 0.0, 0.0, 0.0, 0.0},
		    {0.0036101083032490976, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0}},
		   {{0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0036101083032490976, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0}}},
		  {{{0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.007220216606498195, 0.0, 0.0, 0.0, 0.0}},
		   {{0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.007220216606498195, 0.0, 0.0, 0.0, 0.0},
		    {0.0036101083032490976, 0.0, 0.0, 0.0, 0.0}},
		   {{0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.010830324909747292, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0}},
		   {{0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0}}},
		  {{{0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0}},
		   {{0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0}},
		   {{0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0}},
		   {{0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0036101083032490976, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0}}}},
		 {{{{0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0}},
		   {{0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0}},
		   {{0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0036101083032490976, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0}},
		   {{0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0}}},
		  {{{0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0}},
		   {{0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0}},
		   {{0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0}},
		   {{0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0}}},
		  {{{0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0}},
		   {{0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0}},
		   {{0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.010830324909747292, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0}},
		   {{0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0}}},
		  {{{0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0}},
		   {{0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0036101083032490976, 0.0, 0.0, 0.0, 0.0}},
		   {{0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0}},
		   {{0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0}}},
		  {{{0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0}},
		   {{0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0}},
		   {{0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0}},
		   {{0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0},
		    {0.0, 0.0, 0.0, 0.0, 0.0}}}}};

	public static final Site SITE = makeTestSite();
	public static final Map<
			TectonicRegionType,
			ScalarIntensityMeasureRelationshipAPI> IMR_MAP = makeTestImrMap();

	public static final GEM1ERF ERF = makeTestERF();

	public static final DiscretizedFuncAPI HAZARD_CURVE = makeHazardCurve();

	/**
	 * Map the Math.log() method to each element in the input list
	 * to create a new list.
	 */
	private static Double[] mapLog(Double[] list)
	{
		Double[] result = new Double[list.length];
		for (int i = 0; i < list.length; i++)
		{
			result[i] = Math.log(list[i]);
		}
		return result;
	}
	
	private static Site makeTestSite()
	{
		Site site = new Site(new Location(0.0, 0.0));
		site.addParameter(new DoubleParameter(Vs30_Param.NAME, 760.0));
		site.addParameter(new DoubleParameter(DepthTo2pt5kmPerSecParam.NAME, 1.0));
		return site;
	}

	private static Map<TectonicRegionType, ScalarIntensityMeasureRelationshipAPI> makeTestImrMap()
	{
		Map<TectonicRegionType, ScalarIntensityMeasureRelationshipAPI> imrMap = new HashMap<TectonicRegionType, ScalarIntensityMeasureRelationshipAPI>();
		imrMap.put(TectonicRegionType.ACTIVE_SHALLOW, getTestIMRActiveShallow());
		imrMap.put(TectonicRegionType.STABLE_SHALLOW, getTestIMRStableContinental());
		return imrMap;
	}

	private static ScalarIntensityMeasureRelationshipAPI getTestIMRActiveShallow()
	{
		ScalarIntensityMeasureRelationshipAPI imr = new BA_2008_AttenRel(null);
		imr.setIntensityMeasure(PGA_Param.NAME);
		imr.getParameter(StdDevTypeParam.NAME).setValue(
				StdDevTypeParam.STD_DEV_TYPE_TOTAL);
		imr.getParameter(SigmaTruncTypeParam.NAME).setValue(
				SigmaTruncTypeParam.SIGMA_TRUNC_TYPE_2SIDED);
		imr.getParameter(SigmaTruncLevelParam.NAME).setValue(3.0);
		imr.getParameter(ComponentParam.NAME).setValue(
				ComponentParam.COMPONENT_GMRotI50);
		return imr;
	}

	private static ScalarIntensityMeasureRelationshipAPI getTestIMRStableContinental()
	{
		ScalarIntensityMeasureRelationshipAPI imr = new CB_2008_AttenRel(null);
		imr.setIntensityMeasure(PGA_Param.NAME);
		imr.getParameter(StdDevTypeParam.NAME).setValue(
				StdDevTypeParam.STD_DEV_TYPE_TOTAL);
		imr.getParameter(SigmaTruncTypeParam.NAME).setValue(
				SigmaTruncTypeParam.SIGMA_TRUNC_TYPE_2SIDED);
		imr.getParameter(SigmaTruncLevelParam.NAME).setValue(3.0);
		imr.getParameter(ComponentParam.NAME).setValue(
				ComponentParam.COMPONENT_GMRotI50);
		return imr;
	}

	private static GEM1ERF makeTestERF() 
	{
		ArrayList<GEMSourceData> srcList = new ArrayList<GEMSourceData>();
		srcList.add(makeTestSourceDataActiveShallow());
		srcList.add(makeTestSourceDataStableCrust());
		double timeSpan = 50.0;
		return GEM1ERF.getGEM1ERF(srcList, timeSpan);
	}

	private static GEMSourceData makeTestSourceDataActiveShallow()
	{
		String id = "src1";
		String name = "testSource";
		TectonicRegionType tectReg = TectonicRegionType.ACTIVE_SHALLOW;
		LocationList border = new LocationList();
		border.add(new Location(-0.5, -0.5));
		border.add(new Location(0.5, -0.5));
		border.add(new Location(0.5, 0.5));
		border.add(new Location(-0.5, 0.5));
		Region reg = new Region(border, BorderType.GREAT_CIRCLE);
		double bValue = 1.0;
		double totCumRate = 0.2;
		double min = 5.05;
		double max = 8.95;
		int num = 41;
		GutenbergRichterMagFreqDist magDist = new GutenbergRichterMagFreqDist(
				bValue, totCumRate, min, max, num);
		double strike = 0.0;
		double dip = 90.0;
		double rake = 0.0;
		FocalMechanism focalMechanism = new FocalMechanism(strike, dip, rake);
		MagFreqDistsForFocalMechs magfreqDistFocMech = new MagFreqDistsForFocalMechs(
				magDist, focalMechanism);
		ArbitrarilyDiscretizedFunc aveRupTopVsMag = new ArbitrarilyDiscretizedFunc();
		double magThreshold = 6.5;
		double topOfRuptureDepth = 0.0;
		aveRupTopVsMag.set(magThreshold, topOfRuptureDepth);
		double aveHypoDepth = 5.0;
		GEMSourceData srcData = new GEMAreaSourceData(id, name, tectReg, reg,
				magfreqDistFocMech, aveRupTopVsMag, aveHypoDepth);
		return srcData;
	}

	private static GEMSourceData makeTestSourceDataStableCrust()
	{
		String id = "src1";
		String name = "testSource";
		TectonicRegionType tectReg = TectonicRegionType.STABLE_SHALLOW;
		LocationList border = new LocationList();
		border.add(new Location(-0.5, 0.5));
		border.add(new Location(0.5, 0.5));
		border.add(new Location(0.5, 1.5));
		border.add(new Location(-0.5, 1.5));
		Region reg = new Region(border, BorderType.GREAT_CIRCLE);
		double bValue = 1.0;
		double totCumRate = 0.2;
		double min = 5.05;
		double max = 8.95;
		int num = 41;
		GutenbergRichterMagFreqDist magDist = new GutenbergRichterMagFreqDist(
				bValue, totCumRate, min, max, num);
		double strike = 0.0;
		double dip = 90.0;
		double rake = 0.0;
		FocalMechanism focalMechanism = new FocalMechanism(strike, dip, rake);
		MagFreqDistsForFocalMechs magfreqDistFocMech = new MagFreqDistsForFocalMechs(
				magDist, focalMechanism);
		ArbitrarilyDiscretizedFunc aveRupTopVsMag = new ArbitrarilyDiscretizedFunc();
		double magThreshold = 6.5;
		double topOfRuptureDepth = 0.0;
		aveRupTopVsMag.set(magThreshold, topOfRuptureDepth);
		double aveHypoDepth = 5.0;
		GEMSourceData srcData = new GEMAreaSourceData(id, name, tectReg, reg,
				magfreqDistFocMech, aveRupTopVsMag, aveHypoDepth);
		return srcData;
	}

	private static DiscretizedFuncAPI makeHazardCurve()
	{
		DiscretizedFuncAPI hazardCurve = new ArbitrarilyDiscretizedFunc();
		// initialize the curve our defined list of IMLs,
		// set the corrsponding PoEs to 0.0
		for (Double d : IMLS)
		{
			hazardCurve.set(d, 0.0);
		}
		try {
			HazardCurveCalculator hcc = new HazardCurveCalculator();
			hcc.getHazardCurve(hazardCurve, SITE, IMR_MAP, ERF);
		} catch (RemoteException e) {
			throw new RuntimeException(e);
		}
		return hazardCurve;
	}

}
