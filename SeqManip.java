package bioseqtool;

import java.util.*;

public class SeqManip
{
	protected String seq; // DNA sequence to work on
	
	protected HashMap<String, String> codonTable = new HashMap<>(); // HashMap storing codon table
	{
		// DNA Codon Table (with one-letter amino acid codes)
		codonTable.put("TTT", "F"); codonTable.put("TTC", "F");
		codonTable.put("TTA", "L"); codonTable.put("TTG", "L");
		codonTable.put("CTT", "L"); codonTable.put("CTC", "L");
		codonTable.put("CTA", "L"); codonTable.put("CTG", "L");
		codonTable.put("ATT", "I"); codonTable.put("ATC", "I");
		codonTable.put("ATA", "I"); codonTable.put("ATG", "M"); // Start codon

		codonTable.put("GTT", "V"); codonTable.put("GTC", "V");
		codonTable.put("GTA", "V"); codonTable.put("GTG", "V");

		codonTable.put("TCT", "S"); codonTable.put("TCC", "S");
		codonTable.put("TCA", "S"); codonTable.put("TCG", "S");
		codonTable.put("CCT", "P"); codonTable.put("CCC", "P");
		codonTable.put("CCA", "P"); codonTable.put("CCG", "P");
		codonTable.put("ACT", "T"); codonTable.put("ACC", "T");
		codonTable.put("ACA", "T"); codonTable.put("ACG", "T");
		codonTable.put("GCT", "A"); codonTable.put("GCC", "A");
		codonTable.put("GCA", "A"); codonTable.put("GCG", "A");

		codonTable.put("TAT", "Y"); codonTable.put("TAC", "Y");
		codonTable.put("TAA", "*"); codonTable.put("TAG", "*"); // Stop codons

		codonTable.put("CAT", "H"); codonTable.put("CAC", "H");
		codonTable.put("CAA", "Q"); codonTable.put("CAG", "Q");
		codonTable.put("AAT", "N"); codonTable.put("AAC", "N");
		codonTable.put("AAA", "K"); codonTable.put("AAG", "K");
		codonTable.put("GAT", "D"); codonTable.put("GAC", "D");
		codonTable.put("GAA", "E"); codonTable.put("GAG", "E");

		codonTable.put("TGT", "C"); codonTable.put("TGC", "C");
		codonTable.put("TGA", "*"); codonTable.put("TGG", "W");

		codonTable.put("CGT", "R"); codonTable.put("CGC", "R");
		codonTable.put("CGA", "R"); codonTable.put("CGG", "R");
		codonTable.put("AGT", "S"); codonTable.put("AGC", "S");
		codonTable.put("AGA", "R"); codonTable.put("AGG", "R");

		codonTable.put("GGT", "G"); codonTable.put("GGC", "G");
		codonTable.put("GGA", "G"); codonTable.put("GGG", "G");
	}
	public SeqManip(String str) // Constructor that assigns seq
	{
		seq = str.toUpperCase();
	}
	
	public String transcribe() // Method that returns transcribed seq
	{
		String transcribedSeq = "";
		
		for (int i = 0; i < seq.length(); i++)
		{
			char base = seq.charAt(i);
			
			switch (base)
			{
				case 'A':
					transcribedSeq += 'U';
					break;
				case 'T':
					transcribedSeq += 'A';
					break;
				case 'G':
					transcribedSeq += 'C';
					break;
				case 'C':
					transcribedSeq += 'G';
					break;
				default:
					System.out.println("Ignoring unidentified character " + base + " at position " + (i + 1));
			}
		}
		
		return transcribedSeq;
	}
	
	public String toRNA() // Returns an RNA string that is equivalent to seq
	{
		String rnaString = "";
		
		for (int i = 0; i < seq.length(); i++)
		{
			char base = seq.charAt(i);
			
			if (base == 'T')
			{
				rnaString += 'U';
			}
			else if (base == 'A' || base == 'G' || base == 'C')
			{
				rnaString += base;
			}
			else
			{
				System.out.println("Ignoring unidentified character " + base + " at position " + (i + 1));
			}
		}
		
		return rnaString;
	}
	
	public String complement() // Returns a DNA string complementary to seq
	{
		String complmntSeq = "";
		String str = transcribe();
		
		for (int i = 0; i < str.length(); i++)
		{
			char base = str.charAt(i);
			
			if (base == 'U')
			{
				complmntSeq += 'T';
			}
			else
			{
				complmntSeq += base;
			}
		}
		
		return complmntSeq;
	}
	
	public String reverseComplement() // Returns a DNA string that is the reverse complement of seq
	{
		String reverseComplmntSeq = "";
		String str = complement();
		
		for (int i = str.length() - 1; i >= 0; i--)
		{
			reverseComplmntSeq += str.charAt(i);
		}
		
		return reverseComplmntSeq;
	}
	
	private String translateFrame(String dnaSequence, int startPos) // Method that returns a translated sequence for any frame of a DNA string
	{
		String proteinSequence = "";
		
		for (int i = startPos; i <= dnaSequence.length() - 3; i += 3)
		{
			String codon = dnaSequence.substring(i, i + 3);
			String aminoAcid = codonTable.get(codon);
			
			if (aminoAcid == null)
			{
				aminoAcid = "X"; // unknown codon
			}
			
			proteinSequence += aminoAcid;
		}
		
		return proteinSequence;
	}
	
	public ArrayList<String> translate(int choice) // Method that returns a ArrayList of protein sequences that are translated from the six reading frames of seq
	{		
		ArrayList<String> proteinSeq = new ArrayList<String>(6);
		
		for (int i = 0; i < 6; i++) // Initialising the proteinSeq ArrayList
		{
			proteinSeq.add("");
		}
		//ArrayList<Integer> stopCodonPos = new ArrayList<Integer>(6);
		
		switch (choice)
		{
			case 1:					
				for (int i = 0; i < 3; i++) // Catching the first three frames
				{
					proteinSeq.set(i, translateFrame(seq, i));
				}
				
				for (int i = 3; i < 6; i++) // Catching the last three frames (Reverse complement frames)
				{
					proteinSeq.set(i, translateFrame(reverseComplement(), i - 3));
				}
				
				/*if ("*".equals(aminoAcid))
				{
					stopCodonPos.add(i + 1);
				}*/
				
				//System.out.println("Stop codons marked by *, encountered at positions: " + stopCodonPos);
				break;
			default:
				System.out.println("\nWork in Progress!");
		}
		
		return proteinSeq;
	}
}
