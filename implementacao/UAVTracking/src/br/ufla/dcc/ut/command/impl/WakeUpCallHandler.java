package br.ufla.dcc.ut.command.impl;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import br.ufla.dcc.grubix.simulator.event.WakeUpCall;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface WakeUpCallHandler {
	Class<? extends WakeUpCall> value();
}
