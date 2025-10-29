import java.util.*;
import bioseqtool.DNAAnalyzer;
import bioseqtool.SeqManip;

public class App
{
	public static void main(String[] args)
	{
		Scanner sc = new Scanner(System.in);
		DNAAnalyzer obj1 = null;
		String seq = "";
		
		// First Page starts...
		System.out.println("Welcome to BioSeqTool.");
		
		System.out.println("\n1. Upload Sequence File");
		System.out.println("2. Enter Sequence Manually");
		System.out.print("\nChoose option: ");
		int option = sc.nextInt();
		
		switch (option)
		{
			case 1:
				System.out.print("\nEnter filename: ");
				String filename = sc.next();
				
				obj1 = new DNAAnalyzer(filename, true);
				
				obj1.readSeqFromFile();
				break;
				
			case 2:
				System.out.print("\nEnter sequence: ");
				seq = sc.next();
				
				obj1 = new DNAAnalyzer(seq);
				break;
				
			default:
				System.out.println("Enter valid option!");
		}
		
		obj1.stats();
		
		SeqManip obj2 = new SeqManip(seq);
		boolean running = true;
		
		// Second page starts...
		while(running) 
		{
			System.out.println("\nWhat do you want to do?");
			System.out.println("\n1. Transcribe sequence");
			System.out.println("2. Convert to RNA string");
			System.out.println("3. Find Complement");
			System.out.println("4. Find Reverse Complement");
			System.out.println("5. Translate");
			System.out.println("6. Find Palindrome");
			System.out.println("7. Find motif");
			System.out.println("8. Find Distance with another sequence");
			System.out.println("8. Exit");
			System.out.print("\nChoose option: ");
			option = sc.nextInt();
			
			String transcribedSeq, rnaString, complmntSeq, reverseComplmntSeq;
			ArrayList<String> translatedSeq = new ArrayList<String>(6);
			ArrayList<ArrayList<Integer>> palindromePosLength = new ArrayList<ArrayList<Integer>>();
			
			switch (option)
			{
				case 1:
					String transcribedSeq = obj2.transcribe();
					System.out.println("Transcribed Sequence: " + transcribedSeq);
					break;
					
				case 2:
					String rnaString = obj2.toRNA();
					System.out.println("Converted RNA Sequence: " + rnaString);
					break;
					
				case 3:
					String complmntSeq = obj2.complement();
					System.out.println("Complementary Sequence: " + complmntSeq);
					break;
					
				case 4:
					String reverseComplmntSeq = obj2.reverseComplement();
					System.out.println("Reverse Complement: " + reverseComplmntSeq);
					break;
					
				case 5:
					int translateChoice;
					// sub-page for translate
					System.out.println("\nSpecify translation type");
					System.out.println("\n1. Direct Translation (Disrespect Coding Regions)");
					System.out.println("2. Translate only coding region");
					System.out.print("\nChoose option: ");
					translateChoice = sc.nextInt();
					
					translatedSeq = obj2.translate(translateChoice);
					
					System.out.println("Translated Sequences ('*' for stop codons)\n");
					for (int i = 0; i < 6; i++)
					{
						if (i <= 2)
						{
							System.out.println("Reading Frame " + (i + 1) + ": " + translatedSeq.get(i));
						}
						else
						{
							System.out.println("Reading Frame " + (i + 1) + "(Reverse Complement): " + translatedSeq.get(i));
						}
					}
					break;
					
				case 6:
					// sub-page for palindrome
					System.out.print("Enter minimum length of palindrome: ");
					int minLength = sc.nextInt();
					System.out.print("Enter maximum length of palindrome: ");
					int maxLength = sc.nextInt();
					System.out.print("\n");
					
					palindromePosLength = obj1.getPalindrome(minLength, maxLength);
					
					for (int i = 0; i < palindromePosLength.size(); i++)
					{
						ArrayList<Integer> pal = palindromePosLength.get(i);
						int startPos = pal.get(0);
						int len = pal.get(1);
						int stopPos = startPos + len;
						String palindrome = seq.substring(startPos - 1, stopPos - 1);
						
						System.out.println("Palindrome at position " + startPos + " with length " + len + ": " + palindrome);
					}
					break;
					
				case 7:
					// sub-page for motif
					System.out.println("\nSpecify motif action");
					System.out.println("\n1. Search loaded sequence for a known motif");
					System.out.println("2. Find shared motifs among a set of sequences (Common Substrings)");
					System.out.print("\nChoose Option: ");
					int motifChoice = sc.nextInt();
					
					switch (motifChoice)
					{
						case 1:
							// sub-page for choice 1
							System.out.print("\nEnter motif: ");
							String motif = sc.next();
							motif.toUpperCase();
							
							ArrayList<Integer> motifPos = obj1.getMotifPos(motif);
							
							System.out.print("\nMotif " + motif + " found at positions: ");
							for (int i = 0; i < motifPos.size(); i++)
							{
								System.out.print(motifPos.get(i) + " ");
							}
							System.out.print("\n");
							break;
							
						case 2:
							System.out.println("Work in Progress");
							break;
							
						default:
							System.out.println("Enter valid option!");
					}
					break;
					
				case 8:
					//sub-page for distance
					System.out.println("\nSpecify type of distance");
					System.out.println("\n1. Hamming Distance");
					System.out.println("2. Edit Distance");
					System.out.println("3. Find transition/transversion ratio with another sequence");
					System.out.print("\nChoose Option: ");
					int distanceChoice = sc.nextInt();
					String sequence;
					
					switch (distanceChoice)
					{
						case 1:
						// sub-page for choice 1
							System.out.print("\nEnter second sequence: ");
							sequence = sc.next().toUpperCase();
							
							while (sequence.length() != seq.length())
							{
								System.out.println("\nLengths of sequences do not match!");
								System.out.print("Enter second sequence: ");
								sequence = sc.next().toUpperCase();
							}
							
							int hammDist = obj1.getHammingDistance(sequence);
							
							System.out.println("\nHamming Distance: " + hammDist);
							break;
							
						case 2:
							System.out.println("Work in Progress");
							break;
						
						case 3:
							// sub-page for choice 3
							System.out.print("\nEnter second sequence: ");
							sequence = sc.next().toUpperCase();
							
							while (sequence.length() != seq.length())
							{
								System.out.println("\nLengths of sequences do not match!");
								System.out.print("Enter second sequence: ");
								sequence = sc.next().toUpperCase();
							}
							
							double ratio = obj1.getTransitTransverseRatio(sequence);
							
							if (ratio == Double.POSITIVE_INFINITY)
							{
								System.out.println("No transversions detected. Ratio: Infinity!");
							}
							else
							{
								System.out.println("\nTransition/Transversion Ratio: " + ratio);
							}
							break;
							
						default:
							System.out.println("Enter valid option!");
					}
					break;
				
				case 9:
					System.out.println("Exiting...");
					running = false;
					break;
					
				default:
					System.out.println("Enter valid option!");
			}
		}
	}
}
