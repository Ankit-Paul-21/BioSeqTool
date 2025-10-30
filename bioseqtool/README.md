# BioSeqTool

A simple yet powerful DNA sequence analysis toolkit written in Java.

---

## Overview

**BioSeqTool** is a Java-based command-line application for essential DNA sequence manipulation and analysis.  
It performs transcription, translation, motif search, restriction site and palindrome detection, GC content calculation, and comparative analyses such as Hamming distance and transition/transversion ratio estimation.

The project is implemented as a reusable Java package (`bioseqtool`) and integrates R for generating ready-to-use visualisations of sequence statistics.  
It aims to provide biologists, students, and developers with a lightweight, extensible, and efficient toolkit for exploring genetic data.

---

## Features

- Transcription and translation of DNA sequences  
- Complement and reverse complement generation  
- Motif and palindrome detection  
- Hamming distance and transition/transversion ratio computation  
- GC content and base composition analysis  
- Integration with R for visualisation  
- FASTA file parsing and intuitive menu-driven interface  

---

## Code Structure

### `App.java`
The main entry point of the program.  
Provides a menu-based command-line interface allowing users to upload a FASTA file or enter a sequence manually.  
Interacts with the core classes (`DNAAnalyzer` and `SeqManip`) to perform analyses and display results.  
`App.java` also handles user input, error validation, and menu-driven navigation for motif searches, palindrome detection, translation frame selection, and comparative metrics.

### `DNAAnalyzer.java`
Acts as the analytical engine of BioSeqTool.  
Processes DNA sequences to compute GC content, molecular weight, and nucleotide frequencies, and identifies motifs, palindromes, and substitution patterns.  
Performs comparative sequence analysis through Hamming distance and transition/transversion ratio calculations.  
Interfaces with R to generate base composition plots, bridging numerical computation and visual interpretation.

### `SeqManip.java`
Implements all sequence manipulation operations, including transcription, RNA conversion, complement and reverse complement generation, and translation across six reading frames using an internal codon table.  
Ensures clean separation between logic and biological interpretation for clarity and reusability.

---

## R Visualisation Utility – `plot_base_comp.R`

This R script visualises base composition data exported from Java.  
It reads `base_composition.txt` and generates `base_composition.png`, displaying the percentage of A, T, G, and C bases.  
This demonstrates the Java–R integration, transforming raw numerical data into clear, publication-ready plots for rapid biological insights.

---

## utils Package

The `utils` directory contains lightweight helper classes that improve modularity and readability.

### `InputHandler.java`
Handles and sanitises user input.  
Trims whitespace, merges fragmented entries, and ensures robustness in command-line input, simplifying parsing and preventing downstream errors.

### `Decimal.java`
Encapsulates floating-point rounding functionality.  
Ensures precision and consistent numerical output for computed values such as GC percentages or transition/transversion ratios.

---

## Test File – `seq.fasta`

The `seq.fasta` file provides a sample DNA sequence used to verify the functionality of BioSeqTool.  
Users can modify or replace this sequence to test additional features or validate new implementations.

---

## Future Work

- Implement edit distance computation using dynamic programming  
- Add local alignment and substitution matrix support  
- Enable multi-sequence motif comparison  
- Improve error handling and file I/O efficiency  
- Integrate an optional graphical interface  

---

