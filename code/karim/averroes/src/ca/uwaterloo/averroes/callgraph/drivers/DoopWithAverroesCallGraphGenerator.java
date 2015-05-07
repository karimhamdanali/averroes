package ca.uwaterloo.averroes.callgraph.drivers;

import ca.uwaterloo.averroes.callgraph.CallGraph;
import ca.uwaterloo.averroes.callgraph.CallGraphFactory;
import ca.uwaterloo.averroes.callgraph.gxl.GXLWriter;
import ca.uwaterloo.averroes.exceptions.AverroesException;
import ca.uwaterloo.averroes.properties.AverroesProperties;
import ca.uwaterloo.averroes.util.TimeUtils;
import ca.uwaterloo.averroes.util.io.FileUtils;

/**
 * A driver class that generates call graph for DoopAverroes, that is Doop using the Averroes placeholder library.
 * 
 * @author karim
 * 
 */
public class DoopWithAverroesCallGraphGenerator {

	public static void main(String[] args) {
		try {
			TimeUtils.reset();
			if (args.length != 3) {
				usage();
				throw new AverroesException("DoopAverroes expects exactly 3 arguments.");
			}

			// Process the arguments
			String doopHome = args[0];
			String base = args[1];
			String benchmark = args[2];

			FileUtils.createDirectory(AverroesProperties.getOutputDir());
			CallGraph doop = CallGraphFactory.generateDoopWithAverroesCallGraph(doopHome, base, benchmark);
			System.out.println("Total time to finish: " + TimeUtils.elapsedTime());
			new GXLWriter().write(doop, FileUtils.doopAverroesCallGraphFile());

			// Print some statistics
			System.out.println("=================================================");
			System.out.println("# edges = " + doop.size());
			System.out.println("=================================================");
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	private static void usage() {
		System.out.println("");
		System.out.println("Usage: java -jar doopaverroes.jar <absolute_path_to_doop_home> <benchmark_name>");
		System.out.println("");
	}
}