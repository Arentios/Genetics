# Substitution Matrix Converter

Simple Python script to take in a tsv file representing a substitution matrix and generate Java code to add all entries in that matrix to a Substitution Matrix object used in other code

Very messy, should probably be in Java to allow for direct serialization of generated matrix

Usage
'python converter.py filename'

Sample using provided matrix file:
'python converter.py pam250raw.tsv'