package bioseqtool;

import java.util.*;
import java.io.*;
import utils.Decimal;
import utils.InputHandler;

public class DNAAnalyzer
{
	protected String seq; // store sequence
	protected String filename; // store file name
	
	public DNAAnalyzer(String seq)
	{
		this.seq = InputHandler.conjoinEntry(seq.toUpperCase());
    }
	
	public DNAAnalyzer(String filename, boolean isFile)
	{
		this.filename = filename;
	}
	
	public void readSeqFromFile()
	{		
		try
		{
			BufferedReader br = new BufferedReader(new FileReader("seq.fasta"));
			StringBuilder sb = new StringBuilder();
			String line;
			
			while((line = br.readLine()) != null)
			{
				if (!line.startsWith(">"))
				{
					sb.append(line.trim());
				}
			}
			
			seq = sb.toString();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
			
		System.out.println("Sequence read successfully!");
	}
	
	public void stats()
	{
		System.out.println("Showing sequence statistics...");
		
		try
		{
			Thread.sleep(500);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		
		System.out.println("Length: " + seq.length());
		System.out.println("\tA : " + countBasePerCent('A') + " %");
		System.out.println("\tT : " + countBasePerCent('T') + " %");
		System.out.println("\tG : " + countBasePerCent('G') + " %");
		System.out.println("\tC : " + countBasePerCent('C') + " %");
		System.out.println("Molecular Weight: " + getMolecularWeight());
		System.out.println("GC Content (%): " + getGCContent());
		 
		try
		{
			FileWriter fw = new FileWriter("base_composition.txt", false);
			
			fw.write("A\t" + String.valueOf(countBasePerCent('A')) + "\n");
			fw.write("T\t" + String.valueOf(countBasePerCent('T')) + "\n");
			fw.write("G\t" + String.valueOf(countBasePerCent('G')) + "\n");
			fw.write("C\t" + String.valueOf(countBasePerCent('C')) + "\n");
			
			fw.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		runRScript("plot_base_comp.R");
	}
	
	protected void runRScript(String rScriptPath) 
	{
		try 
		{
			// Build the command to run Rscript
		    String command = "Rscript " + rScriptPath;
		    
		    Process process = Runtime.getRuntime().exec(command);

		    // Capture R output (optional)
		    BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
		    String line;
		    while ((line = reader.readLine()) != null) 
		    {
		        System.out.println("[R] " + line);
		    }

		    // Capture R errors (if any)
		    BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
		    while ((line = errorReader.readLine()) != null) 
		    {
		        System.err.println("[R Error] " + line);
		    }

		    // Wait for R to finish
		    int exitCode = process.waitFor();
		    if (exitCode == 0) 
		    {
		        System.out.println("R script executed successfully!");
		        System.out.println("Bar plot saved as base_composition.png");
		    } 
		    else 
		    {
		        System.out.println("R script execution failed. Exit code: " + exitCode);
		    }
		} 
		catch (Exception e) 
		{
		    e.printStackTrace();
		}
	}

	protected int countBase(char base)
	{
		base = Character.toUpperCase(base);
		int count = 0;

		for (int i = 0; i < seq.length(); i++)
		{
			if (seq.charAt(i) == base)
			{
				count++;
			}
		}

		return count;
	}

	protected double countBasePerCent(char base)
	{
		int count = countBase(base);
		double perCentCount = ((double) count / seq.length()) * 100; // calculating % count

		Decimal obj = new Decimal(perCentCount);
		perCentCount = obj.roundToNPlace(2);
		
		return perCentCount;
	}
	
	public double getGCContent()
	{
		double gcContent = countBasePerCent('G') + countBasePerCent('C');

		Decimal obj = new Decimal(gcContent);
		gcContent = obj.roundToNPlace(2);

		return gcContent;
	}
	
	public double getMolecularWeight()
	{
		double weight = 0;
		
		for (int i = 0; i < seq.length(); i++)
		{
			char base = seq.charAt(i);
			switch (base)
			{
				case 'A':
					weight += 313.21;
					break;
				case 'T':
					weight += 304.2;
					break;
				case 'G':
					weight += 329.21;
					break;
				case 'C':
					weight += 289.18;
					break;
				default:
					weight += 0;
					System.out.println("Ignoring unidentified character " + base + " at position " + (i + 1));
			}
		}
		
		Decimal obj = new Decimal(weight);
		weight = obj.roundToNPlace(2);
		
		return weight;
	}
	
	/*public Vector<Integer> getORFFrame(String dnaSequence, int startPos)
	{
		Vector<Integer> ORF = new Vector<Integer>(2);
		int startCodonPos = -1;
		int stopCodonPos = -1;
		
		for (int i = startPos; i < dnaSequence.length() - 3; i += 3)
		{
			String codon = dnaSequence.substring(i, i + 3);
			
			if ("ATG".equals(codon))
			{
				startCodonPos = i + 1;
			}
			
			if ("TAG".equals(codon))
			{
				stopCodonPos = i + 1;
			}
		}
	}*/
	
	public ArrayList<ArrayList<Integer>> getPalindrome(int minLength, int maxLength)
	{
		ArrayList<ArrayList<Integer>> palindromePosLength = new ArrayList<ArrayList<Integer>>();
		
		for (int j = minLength; j <= maxLength; j++)
		{
			for (int i = 0; i < seq.length() - (j - 1); i++)
			{
				String dnaString = seq.substring(i, i + j);
				
				SeqManip obj1 = new SeqManip(dnaString);
				String revComplmntDnaString = obj1.reverseComplement();
				
				if (dnaString.equals(revComplmntDnaString))
				{
					ArrayList<Integer> posLength = new ArrayList<Integer>(2);
					
					posLength.add(i + 1);
					posLength.add(j);
					
					palindromePosLength.add(posLength);
				}
			}
		}
		
		return palindromePosLength;
	}
	
	public ArrayList<Integer> getMotifPos(String pattern)
	{
		ArrayList<Integer> motifPos = new ArrayList<Integer>();
		int patternLen = pattern.length();
		
		for (int i = 0; i < seq.length() - patternLen - 1; i++)
		{
			String dnaString = seq.substring(i, i + patternLen);
			
			if (dnaString.equals(pattern))
			{
				motifPos.add(i + 1);
			}
		}
		
		return motifPos;
	}
	
	public int getHammingDistance(String seq)
	{
		int hammDist = 0;
		
		for (int i = 0; i < seq.length(); i++)
		{
			if (this.seq.charAt(i) != seq.charAt(i))
			{
				hammDist++;
			}
		}
		
		return hammDist;
	}
	
	public double getTransitTransverseRatio(String seq)
	{
		int transitions = 0;
		int transversions = 0;
		
		ArrayList<String> transitionSet = new ArrayList<String>(4);
		transitionSet.add("AG");
		transitionSet.add("GA");
		transitionSet.add("CT");
		transitionSet.add("TC");
		
		ArrayList<String> noMutationSet = new ArrayList<String>(4);
		noMutationSet.add("AA");
		noMutationSet.add("TT");
		noMutationSet.add("GG");
		noMutationSet.add("CC");
		
		for (int i = 0; i < this.seq.length(); i++)
		{
			char base1 = this.seq.charAt(i);
			char base2 = seq.charAt(i);
			
			if (transitionSet.contains(base1 + "" + base2))
			{
				transitions++;
			}
			else if (!noMutationSet.contains(base1 + "" + base2))
			{
				transversions++;
			}
		}
		
		if (transversions == 0)
		{
			return Double.POSITIVE_INFINITY;
		}
		
		double ratio = (double) transitions / transversions;
		ratio = (double) Math.round(ratio * 1000) / 1000;
		
		return ratio;
	}
}
