# GenBank Extractor

Python script to parse through a GBF (GenBank Format) file, pull out sequences and attempt to align them using java webservice

Currently in pure proof of concept mode. Full version will do better metadata extracting and tagging and will attempt to intelligently pick similar sequences across different but related organisms

Full GBF files are too large to upload to github and can be found at ftp://ftp.ncbi.nlm.nih.gov/ncbi-asn1

Usage
'python genbankextractor.py filename'
