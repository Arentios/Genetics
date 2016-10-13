'''
Tool to take a GBF data file, parse it, and then send similar sequence blocks to seperate alignment service
Created on October 11, 2016

CURRENTLY SUPER KLUDGY AS A PROOF OF CONCEPT

@author: Arentios
'''
import re
import sys
import requests
import constants

if __name__ == '__main__':
    filecontents = open(sys.argv[1], 'r')
    x = 0
    keys = []
    organisms = {}
    translations = {}
    multiline = False
    translation = ""
    organism = ""
    product = ""
    #Loop through the GBF and extract data to build a three level structure of organism : product : translation
    #This makes assumptions of file ordering and format found in the data dictionary at https://www.ncbi.nlm.nih.gov/Sitemap/samplerecord.html
    #Which is to say, data integrity is not currently checked
    for line in filecontents:
       
        if multiline is True:
            pattern = re.compile('^ +([A-Z]+)(")?$')
            symbol = pattern.match(line)
            if symbol != None:
                translation += symbol.group(1)
                #If there's a closing quote we're at the end of the current multiline block
                if symbol.group(2) != None:
                    translations[product] = translation
                    translation = ""
                    multiline = False
        else: 
            pattern = re.compile('^ +/organism="(.+)"$')
            symbol = pattern.match(line)             
            if symbol != None:
                #If we're already processing a organism then we're now on a new one, take the old one and put it in the dictionary
                if organism != "": 
                    #It's possible to have multiple entries for the same organism, so we need to check and append if one already exists
                    if organism in organisms:
                       organisms[organism].update(translations)
                    else:
                        organisms[organism] = dict(translations)
                    translations = {}
                organism = symbol.group(1)
                print str(x) + " " +str(organism)
            else:    
                pattern = re.compile('^ +/translation="([A-Z]+)(")?$') #Match for translation and optionally a closing quote, if there's no closing quote the sequence is split across multiple lines
                symbol = pattern.match(line)
                if symbol != None:
                    translation += symbol.group(1)
                    if symbol.group(2) == None:
                        multiline = True
                else:
                    pattern = re.compile('^ +/product="(.+)"$')
                    symbol = pattern.match(line)
                    if symbol != None:
                        product = symbol.group(1)
                        
                  
        x+=1
        
    #Now, very kludgely, go through and select the first two sequences from different organisms and send them to the alignment service as a proof of concept   
    x = 0
    sequences = []
    for i in organisms.keys():
        for j in organisms[i].keys():
            sequences.append(organisms[i][j])
            x+=1
            break
        if x == 2:
            response = requests.post(constants.ALIGNMENT_URL, json={'requestType': 'Needleman-Wunsch',   'sequences' : sequences, 'options' : [{ 'option' : 'singleTrack', 'value' : 'false'}, {'option' : 'SubstitutionMatrix', 'value' : 'blosum62'}]}, headers={'Content-type': 'application/json', 'Accept': 'text/plain'})
            print  response.request.headers
            print response.text
            break       
            