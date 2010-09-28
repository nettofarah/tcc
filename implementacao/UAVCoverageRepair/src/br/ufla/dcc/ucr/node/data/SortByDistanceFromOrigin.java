package br.ufla.dcc.ucr.node.data;

public class SortByDistanceFromOrigin implements CoverageInfoComparator {

	@Override
	public int compare(CoverageInformation me, CoverageInformation other) {
		if (me.distanceFromOrigin() == other.distanceFromOrigin())
			return 0;
		
		if(me.distanceFromOrigin() > other.distanceFromOrigin()){
			return 1;
		}else{
			return -1;
		}
	}

}
