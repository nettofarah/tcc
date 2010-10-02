package br.ufla.dcc.ut.utils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import br.ufla.dcc.grubix.simulator.event.Event;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface EventHandler {	
	Class<? extends Event> event();

	String args() default "";
}
