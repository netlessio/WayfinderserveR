#!/bin/bash


for i in `find . -name *.java`
do
wc -l $i
 num=`cat $i|wc -l | awk -F\  '{print $NF}'`
 let s