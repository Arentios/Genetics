'''
Created on Aug 9, 2016

@author: Arentios
'''
import re
import sys


if __name__ == '__main__':
    filecontents = open(sys.argv[1], 'r')
    x = 0
    keys = []
    for line in filecontents:
        if(x == 0):
            keys = line.split('\t')          
        else:
            split = line.split('\t')
            pattern = re.compile('(\-?[A-Z|0-9]+)')
            y = 0
            symbol = ''
            for substr in split:       
                if(y == 0):
                    symbol = pattern.match(substr).group(1)
                else:
                    if len(substr) > 0:
                        print('matrix.addSubstitutionMatrixValue(\''+keys[y]+'\',\''+symbol+'\',\''+pattern.match(substr).group(1)+'\');')
                y+=1
        x+=1