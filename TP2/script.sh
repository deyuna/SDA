#!/bin/bash
# Copyright (C) 2021 Muhammad Athallah

# This free document is distributed in the hope that it will be
# useful, but WITHOUT ANY WARRANTY; without even the implied
# warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

# START Wed 10 Nov 2021 07:48:45 WIB

# Taken and modified from https://github.com/bhupixb/Stress-Testing-bash-script
# Please place test cases under the folder of "testcases"

# to color the output text in different colours
green=$(tput setaf 71);
red=$(tput setaf 1);
blue=$(tput setaf 32);
orange=$(tput setaf 178);
bold=$(tput bold);
reset=$(tput sgr0);

# Interactive input for filenames
read -p "Please enter the code filename (excluding the extension file): " CODEFILE

# You can change the version of the programming language or add the compiler flags as you wish
javac $CODEFILE.java || { echo ${bold}${orange}Compilation Error in ${reset} $CODEFILE.java; exit 1; }

diff_found=0
i=0

# Load test case (usually from Nuel, wkwk)
# Use https://stackoverflow.com/a/7993081 to sort in order of numbers
ls testcases/in_* | sed 's/^\([^0-9]*\)\([0-9]*\)/\1 \2/' | sort -k2,2n | tr -d ' ' |
while read file
do
    # Extract the number
    
    if [ $i -lt 100 ]
    then
        numname=${file:13:2}
    elif [ $i -lt 1000 ]
    then
        numname=${file:13:3}
    elif [ $i -lt 10000 ]
    then
        numname=${file:13:4}
    else
        numname=${file:13:5}
    fi
    
    # run the code solution, take input from test case
    # and save it in generated_output.txt
    java $CODEFILE < $file > generated_output.txt #|| {echo failed; exit 1;}
    
    # check if generated output and expected output differs
    # (we are ignoring spaces and then comparing files)
    if diff -F --label --side-by-side --ignore-space-change generated_output.txt testcases/out_$numname.txt > dont_show_on_terminal.txt; then
        echo "${orange}Test case #$i: ${bold}${green}Accepted${reset}"
    else
        echo "${orange}Test case #$i: ${bold}${red}Wrong Answer${reset}"
        diff_found=1
    fi
    
    if [ $diff_found -eq 1 ]
    then
        echo "${blue}Input: ${file}${reset}"
        cat $file
        echo ""

        echo "${blue}(User) Output: ${reset}"
        cat generated_output.txt
        echo ""

        echo "${blue}Expected Output: out_${numname}.txt${reset}"
        cat testcases/out_$numname.txt
        echo ""
        
        break
    fi
    
    i=$((i+1))
done

rm generated_output.txt
rm dont_show_on_terminal.txt