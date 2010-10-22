package br.ufla.dcc.ut.command.impl;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.scannotation.AnnotationDB;
import org.scannotation.ClasspathUrlFinder;

import br.ufla.dcc.grubix.simulator.event.Packet;
import br.ufla.dcc.grubix.simulator.event.WakeUpCall;
import br.ufla.dcc.grubix.simulator.node.ApplicationLayer;
import br.ufla.dcc.ut.command.Command;
import br.ufla.dcc.ut.command.CommandFactory;
import br.ufla.dcc.ut.command.UnbuiltCommandFactoryException;
import br.ufla.dcc.ut.command.packet.PacketCommand;
import br.ufla.dcc.ut.command.wuc.WakeUpCallCommand;

public class ReflectionCommandFactory implements CommandFactory {

	private ApplicationLayer applicationLayer;
	private static Map<Class<? extends WakeUpCall>,Class<? extends WakeUpCallCommand>> wakeUpCallHandlers;
	private static Map<Class<? extends Packet>, Class<? extends PacketCommand>> packetHandlers;
	
	public static String WAKE_UP_CALL_HANDLER_PATH = "br.ufla.dcc.ut.command.impl.WakeUpCallHandler";
	public static String PACKET_HANDLER_PATH = "br.ufla.dcc.ut.command.impl.PacketHandler";
	
	static{
		try {
			ReflectionCommandFactory.processWakeUpCallHandlers();
			ReflectionCommandFactory.processPacketHandlers();
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	@Override
	public void buildFactory(ApplicationLayer applicationLayer) {
		this.applicationLayer = applicationLayer;
	}

	private boolean isFactoryReady(){
		return this.applicationLayer != null;
	}

	@Override
	public Command getPacketCommand(Packet packet) throws Exception {
		
		if(this.isFactoryReady()){
			Class<? extends PacketCommand> commandClass = packetHandlers.get(packet.getClass());
			PacketCommand command = commandClass.newInstance();
			
			command.setApplicationLayer(this.applicationLayer);
			command.setPacket(packet);
			
			return command;
		}else{
			throw new UnbuiltCommandFactoryException();
		}
	}

	@Override
	public Command getWakeUpCallCommand(WakeUpCall wakeUpCall) throws Exception {
		boolean factoryReady = this.isFactoryReady();
		if(this.isFactoryReady()){
			
			Class<? extends WakeUpCallCommand> commandClass = wakeUpCallHandlers.get(wakeUpCall.getClass());
			WakeUpCallCommand command = commandClass.newInstance();
			
			command.setApplicationLayer(this.applicationLayer);
			command.setWakeUpCall(wakeUpCall);
			
			return command;
		}else{
			throw new UnbuiltCommandFactoryException();
		}
	}
	
	
	private static void processPacketHandlers() throws IOException, ClassNotFoundException{
		packetHandlers = new HashMap<Class<? extends Packet>, Class<? extends PacketCommand>>();
		
		Set<String> annotationIndex = getAnnotationIndex(PACKET_HANDLER_PATH);
		for (String sClazz : annotationIndex) {
			Class<?> clazz = Class.forName(sClazz);
		
			Class<? extends PacketCommand> handler = (Class<? extends PacketCommand>) clazz;
			PacketHandler annotation = handler.getAnnotation(PacketHandler.class);
			
			Class<? extends Packet> value = annotation.value();
			
			packetHandlers.put(value, handler);
		}
	}
	
	
	@SuppressWarnings("unchecked")
	private static void processWakeUpCallHandlers() throws IOException, ClassNotFoundException{
		
		wakeUpCallHandlers = new HashMap<Class<? extends WakeUpCall>, Class<? extends WakeUpCallCommand>>();
		
		Set<String> annotationIndex = getAnnotationIndex(WAKE_UP_CALL_HANDLER_PATH);
		for (String sClazz : annotationIndex) {
			Class<?> clazz = Class.forName(sClazz);
		
			Class<? extends WakeUpCallCommand> handler = (Class<? extends WakeUpCallCommand>) clazz;
			WakeUpCallHandler annotation = handler.getAnnotation(WakeUpCallHandler.class);
			
			Class<? extends WakeUpCall> value = annotation.value();
			
			wakeUpCallHandlers.put(value, handler);			
		}
	}

	
	private static Set<String> getAnnotationIndex(String path) throws IOException{
		URL[] findClassPaths = ClasspathUrlFinder.findClassPaths("bin");
		
		AnnotationDB db = new AnnotationDB();
		db.scanArchives(findClassPaths);

		Map<String, Set<String>> annotationIndex = db.getAnnotationIndex();
		Set<String> set = annotationIndex.get(path);
		
		return set;
	}
}
