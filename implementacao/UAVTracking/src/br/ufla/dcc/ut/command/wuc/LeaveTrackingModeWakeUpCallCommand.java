package br.ufla.dcc.ut.command.wuc;

import br.ufla.dcc.ut.command.impl.WakeUpCallHandler;
import br.ufla.dcc.ut.node.RegularNode;
import br.ufla.dcc.ut.node.wuc.LeaveTrackingModeWakeUpCall;

@WakeUpCallHandler(LeaveTrackingModeWakeUpCall.class)
public class LeaveTrackingModeWakeUpCallCommand extends WakeUpCallCommand{

	@Override
	public void execute() {
		RegularNode regNode = (RegularNode) this.applicationLayer;
		
		regNode.leaveTrackingMode();
	}

}
