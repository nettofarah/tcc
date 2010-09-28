package br.ufla.dcc.ucr.node.data;

public class SortByCoverage implements CoverageInfoComparator {

	@Override
	public int compare(CoverageInformation me, CoverageInformation other) {
		if(me.isEvent())
			return 1;
		if(other.isEvent())
			return -1;
		
		if (me.getCoverage() == other.getCoverage())
			return 0;
		
		if(me.getCoverage() < other.getCoverage()){
			return 1;
		}else{
			return -1;
		}
	}


}
