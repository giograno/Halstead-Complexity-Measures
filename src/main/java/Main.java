/**
 * 
 */

/**
 * @author Ahmed Metwally
 *
 */

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;



public class Main {
	 
	// construct AST of the .java files	
	public static ASTVisitorMod parse(char[] str, List<Object> line, String className) {
		ASTParser parser = ASTParser.newParser(AST.JLS3); 
		parser.setSource(str);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setResolveBindings(true);
		final CompilationUnit cu = (CompilationUnit) parser.createAST(null);
		line.add(cu.getPackage().getName().getFullyQualifiedName()+"."+className);

			
		// Check for compilationUnits problems in the provided file
		IProblem[] problems = cu.getProblems();
		for(IProblem problem : problems) {
			// Ignore some error because of the different versions.
            if (problem.getID() == 1610613332) 		 // 1610613332 = Syntax error, annotations are only available if source level is 5.0
                continue;
            else if (problem.getID() == 1610613329) // 1610613329 = Syntax error, parameterized types are only available if source level is 5.0
                continue;
            else if (problem.getID() == 1610613328) // 1610613328 = Syntax error, 'for each' statements are only available if source level is 5.0
                continue;
            else
            {
            	// quit compilation if
//    	        System.out.println("CompilationUnit problem Message " + problem.getMessage() + " \t At line= "+problem.getSourceLineNumber() + "\t Problem ID="+ problem.getID());
            }
	    }
				
		// visit nodes of the constructed AST
		ASTVisitorMod visitor= new ASTVisitorMod();
		cu.accept(visitor);
	  
	    return visitor;
	}
	
		
	
	// parse file in char array
	public static char[] ReadFileToCharArray(String filePath) throws IOException {
		StringBuilder fileData = new StringBuilder(1000);
		BufferedReader reader = new BufferedReader(new FileReader(filePath));		
		char[] buf = new char[10];
		int numRead = 0;
		while ((numRead = reader.read(buf)) != -1) {
			String readData = String.valueOf(buf, 0, numRead);
			fileData.append(readData);
			buf = new char[1024];
		}
		reader.close();
 
		return  fileData.toString().toCharArray();	
	}

	
	
	// parse files in a directory to list of char array
	public static List<char[]> ParseFilesInDir(List<String> JavaFiles) throws IOException{
		if(JavaFiles.isEmpty()) 
		{
			System.out.println("There is no java source code in the provided directory");
			System.exit(0);
		}
		
		List<char[]> FilesRead= new ArrayList<char []>();		
		
		for(int i=0; i<JavaFiles.size(); i++)
		{
			System.out.println("Now, reading: "+ JavaFiles.get(i));
			FilesRead.add(ReadFileToCharArray(JavaFiles.get(i)));
		}
		
		return FilesRead;
	}
	
	
	
	// retrieve all .java files in the directory and subdirectories.
	public static List<String> retrieveFiles(String directory) {
		List<String> Files = new ArrayList<String>();
		File dir = new File(directory);
		
		if (!dir.isDirectory())
		{			
			 System.out.println("The provided path is not a valid directory");
			 System.exit(1);
		}
				
		for (File file : dir.listFiles()) {
			if(file.isDirectory())
			{
				Files.addAll(retrieveFiles(file.getAbsolutePath()));
			}
			if (file.getName().endsWith((".java"))) 
			{
				Files.add(file.getAbsolutePath());
			}
		}
		
		return Files;
	}
	
	
	
	public static void main(String[] args) throws IOException {
		// dirs passes as input
		String DirName = args[0];
		String dirOutput = args[1];
		
		// retrieve all .java files in the directory and subdirectories. 
		List<String> JavaFiles=retrieveFiles(DirName);
		
		// parse files in a directory to list of char array
		List<char[]> FilesRead=ParseFilesInDir(JavaFiles);	
				
		ASTVisitorMod ASTVisitorFile;		
		int DistinctOperators;
		int DistinctOperands;
		int OperatorCount;
		int OperandCount;
		 		
		// Construct the AST of each java file. visit different nodes to get the number of operors and operands
		// Retrieve the number of distinct operators, distinct operands, 
		// total operators, and total operands for each .java file in the directory. 
		// Sum each parameter from different files together to be used in Halstead Complexity metrics.

		FileWriter writer = null;
		Path path = Paths.get(dirOutput, "halstead.csv");

		if (FilesRead.size() > 0) {
			writer = new FileWriter(path.toFile());
		}

		CSVUtils.writeLine(writer, Arrays.asList("project",
				"class",
				"vocabulary",
				"length",
				"calculated_length",
				"volume",
				"difficulty",
				"effort",
				"time",
				"bugs"));

		for(int i=0; i<FilesRead.size(); i++) {
			List<Object> line = new ArrayList<>();
			File dirFile = new File(DirName);
			line.add(dirFile.getName());
			File auxJava = new File(JavaFiles.get(i));
			String className = auxJava.getName().replace(".java","");

			System.out.println("Now, AST parsing for : "+ JavaFiles.get(i));
			ASTVisitorFile=parse(FilesRead.get(i), line, className);
			DistinctOperators=ASTVisitorFile.oprt.size();
			DistinctOperands=ASTVisitorFile.names.size();
			
			OperatorCount=0;
			for (int f : ASTVisitorFile.oprt.values())
				OperatorCount += f;

			OperandCount=0;
			for (int f : ASTVisitorFile.names.values())
				OperandCount += f;

			HalsteadMetrics halsteadMetrics = new HalsteadMetrics();
			halsteadMetrics.setParameters(DistinctOperators, DistinctOperands, OperatorCount, OperandCount);

			line.add(halsteadMetrics.getVocabulary());
			line.add(halsteadMetrics.getProglen());
			line.add(halsteadMetrics.getCalcProgLen());
			line.add(halsteadMetrics.getVolume());
			line.add(halsteadMetrics.getDifficulty());
			line.add(halsteadMetrics.getEffort());
			line.add(halsteadMetrics.getTimeReqProg());
			line.add(halsteadMetrics.getTimeDelBugs());
			CSVUtils.writeLine(writer, line);

		}
		writer.flush();
		writer.close();
	}
}