/*******************************************************************************
 * Copyright (c) 2015 Karim Ali Ondřej Lhoták.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Karim Ali - initial API and implementation and/or initial documentation
 *******************************************************************************/
package averroes.tamiflex;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Set;

import averroes.options.AverroesOptions;

/**
 * A database for all the TamiFlex facts Averroes extracts from the given
 * TamiFlex facts file.
 * 
 * @author karim
 * 
 */
public class TamiFlexFactsDatabase {

	private static Set<String> arrayNewInstance = new HashSet<String>();
	private static Set<String> classForName = new HashSet<String>();
	private static Set<String> classNewInstance = new HashSet<String>();
	private static Set<String> constructorNewInstance = new HashSet<String>();
	private static Set<String> methodInvoke = new HashSet<String>();

	// Load the TamiFlex facts file on the first call to any static method
	static {
		try {
			loadFacts();
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Load the facts into the database.
	 * 
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	private static void loadFacts() throws IOException, URISyntaxException {

		if (AverroesOptions.isTamiflexEnabled()) {
			String fileToLoad = AverroesOptions.getTamiflexFactsFile();

			// BufferedReader in = new BufferedReader(new
			// FileReader(FileUtils.getResource(fileToLoad)));
			BufferedReader in = new BufferedReader(new FileReader(fileToLoad));
			String line;

			while ((line = in.readLine()) != null) {
				// Ignore empty lines
				if (line.length() == 0) {
					continue;
				}

				// Those are the the columns in the reflection log file
				// generated by
				// TamiFlex (got this from Soot)
				String[] portions = line.split(";", -1);
				ReflectiveCallType type = ReflectiveCallType.stringToType(portions[0]);
				String target = portions[1];

				// If it's a type that we handle, i.e. not null then add the
				// target
				// to the corresponding set
				if (type != null) {
					if (type.equals(ReflectiveCallType.ARRAY_NEW_INSTANCE)) {
						arrayNewInstance.add(target);
					} else if (type.equals(ReflectiveCallType.CLASS_FOR_NAME)) {
						classForName.add(target);
					} else if (type.equals(ReflectiveCallType.CLASS_NEWINSTANCE)) {
						classNewInstance.add(target);
					} else if (type.equals(ReflectiveCallType.CONSTRUCTOR_NEWINSTANCE)) {
						constructorNewInstance.add(target);
					} else if (type.equals(ReflectiveCallType.METHOD_INVOKE)) {
						methodInvoke.add(target);
					}
				}
			}

			in.close();
		}
	}

	/**
	 * Get the {@value ReflectiveCallType#ARRAY_NEW_INSTANCE} facts.
	 * 
	 * @return
	 */
	public static Set<String> getArrayNewInstance() {
		return arrayNewInstance;
	}

	/**
	 * Get the {@value ReflectiveCallType#CLASS_FOR_NAME} facts.
	 * 
	 * @return
	 */
	public static Set<String> getClassForName() {
		return classForName;
	}

	/**
	 * Get the {@value ReflectiveCallType#CLASS_NEWINSTANCE} facts.
	 * 
	 * @return
	 */
	public static Set<String> getClassNewInstance() {
		return classNewInstance;
	}

	/**
	 * Get the {@value ReflectiveCallType#CONSTRUCTOR_NEWINSTANCE} facts.
	 * 
	 * @return
	 */
	public static Set<String> getConstructorNewInstance() {
		return constructorNewInstance;
	}

	/**
	 * Get the {@value ReflectiveCallType#METHOD_INVOKE} facts.
	 * 
	 * @return
	 */
	public static Set<String> getMethodInvoke() {
		return methodInvoke;
	}
}
