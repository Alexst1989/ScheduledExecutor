package ru.alex.st.pixonic;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/*
 * 
 * На вход поступают пары (LocalDateTime, Callable). Нужно реализовать систему, которая будет выполнять 
 * Callable для каждого пришедшего события в указанный LocalDateTime, либо как можно скорее в случае если 
 * система перегружена и не успевает все выполнять (имеет беклог). Задачи должны выполняться в порядке согласно 
 * значению LocalDateTime либо в порядке прихода события для равных LocalDateTime. События могут приходить 
 * в произвольном порядке и добавление новых пар (LocalDateTime, Callable) может вызываться из разных потоков.
 *
 */

public class Programm {
	
	private static final int N = 10;
	
	public static void main(String args[]) {
		Executor executor = Executors.newFixedThreadPool(N);
		
		//Convinenient start for ShcheduledTaskExecutor
		
		
		
		
	}
	
}
