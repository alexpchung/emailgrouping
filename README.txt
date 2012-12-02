This is a Java program that put connected emails into a group

Problem:
Imagine you have a mail server log. It has many lines. Each line tells a Person A sent an email to Person B. There are two rules:1. If Person X sent an email to Person Y, they are in a same group. Similarly, if Y also sent an email to Z, Z is in the same group too.2. If Person A didn't send any email to any person in a group or didn’t receive any email from any person in a group, A shouldn’t be in the group.So, there may be many groups from the server log. Please implement a Java program to process the log and eventually for every group, print out every member in the group.

Assumptions:
1) The program reads a sample server log file=emailServerLog.txt in the local directory
2) It assumes the email list is separated by space
3) It also assumes that the emails are NOT case-sensitive
4) There can be any number of email entry in each input line
5) It does not count as a group if the sender and recipient is the same person

Overview: 
Reading the inputs from the server log line-by-line in linear time.  After splitting the email addresses by space, iterate through the email addresses to search if there is an existing group containing the same signature (case-insensitive).  If different addresses have mapped to different email groups, merge them into a single group and remove the sub-groups.  Email addresses (believe to be String datatype) are stored in a HashSet with constant time in search.  However, the list of existing email groups can grow with the worst case of O(n) when there is no overlap of email addresses between different rows.  Leading to the worst case time complexity of O(n^2) for the solution.  A suffix tree could be used in place of the ArrayList of email groups for faster lookup.             

Running the Program:  	
>java -jar EmailGrouping.jar

Example File Input:
Y Z
X Y
K X
A B
C A
M L
D
Q

E F
F F
Y L
S E D
a q 
alexpchung@gmail.com achung@ischool.berkeley.edu
1 3 4 5

Example Output:
Total Number of group(s): 5
Group 1: [Q, A, B, C]
Group 2: [D, E, F, S]
Group 3: [L, M, Y, X, Z, K]
Group 4: [ALEXPCHUNG@GMAIL.COM, ACHUNG@ISCHOOL.BERKELEY.EDU]
Group 5: [3, 1, 5, 4]



