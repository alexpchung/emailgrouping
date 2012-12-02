import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Set;

public class EmailGrouping {

	/**
	 * Parse mail server log and create email groups
	 * 	1. If Person X sent an email to Person Y, they are in a same group. 
	 * 		Similarly, if Y also sent an email to Z, Z is in the same group too.
	 *	2. If Person A didn't send any email to any person in a group or didn’t 
	 *		receive any email from any person in a group, A shouldn’t be in the group.
	 */
	public static void main(String[] args) {
		//Input mail server log  
		String logFilename = "emailServerLog.txt";
		ArrayList<String> emailLogEntries = FileReader.readLines(logFilename);
		
		//Declare an array of email groups (as a set) using the email entry as keys
		ArrayList<Set<String>> groupList = new ArrayList<Set<String>>();
		
		//Exit if the log file is empty
		if (emailLogEntries.size() == 0) {
			System.out.println("Error: there is no entry in the given log file");
			return;
		}
		
		//Log entry delimiter
		String delim = "\\s+";
		 
		for (String emailLogEntry : emailLogEntries)
		{
			//Assuming the entry is NOT case-sensitive
			//convert all inputs into upper-case
			emailLogEntry = emailLogEntry.toUpperCase();
			
			/*1. Screen input
			*/
			Set<String> emailSet;
			
			if (emailLogEntry.isEmpty()) {
				//Skip empty line
				continue;
			} else {
				String[] emails = emailLogEntry.split(delim);
						
				if (emails.length == 1) {
					//Skip single email entry
					continue;
				} else {
					//Check if the entries are identical
					emailSet = new HashSet<String>(Arrays.asList(emails));
					if (emailSet.size() == 1) {
						//Skip if all email entries are identical
						continue;
					}
				}
			}
			
			/*
			 * 2. Find email groups 
			 * Generalize to accept more than two entries per line
			 */
			if (groupList.size() == 0) {
				//No existing group, create new one with the given set of email entries
				groupList.add(emailSet);
			} else {
				//Find all matching group for each email entry
				
				//Hold the indexes of groups that contain the email entries
				Set<Set<String>> matchGroupSet = new HashSet<Set<String>>();
				
				//Iterate thru group list to find one that contains the email entry
				Iterator<String> itrTS = emailSet.iterator();
				while (itrTS.hasNext()) {
					String emailEntry = itrTS.next();
					
					int matchGroupIndex;
					try {
						matchGroupIndex = findMatchingEmailGroup(emailEntry, groupList);
					}
					catch(Exception e) {
						//No matching group is found
						System.out.println("Cannot find matching group for item: " + emailEntry + 
								" with error message: " + e.getMessage());
						continue;
					}
					
					if (matchGroupIndex != -1) {
						//Email group found with matching email signature
						matchGroupSet.add(groupList.get(matchGroupIndex));
					}
				}
				
				/* 
				 *	Check if there is > 1 returned groups
				 */
				if (matchGroupSet.isEmpty()) {
					//Create new group
					groupList.add(emailSet);
				} else if (matchGroupSet.size() == 1) {
					//Add email entries to the matching group.  First item in set.
					matchGroupSet.iterator().next().addAll(emailSet);
				} else {
					//Merge groups by moving all entries to a single group 
					//and deleting the extra entry
					mergeGroups(groupList, matchGroupSet);
				}
			}
		}
		
		// 3. Report Results
		reportGroups(groupList);
	}

	private static void reportGroups(ArrayList<Set<String>> groupList) {
		/*	Print the items within each group
		 * 	@param: final list of email groups
		 */
		ListIterator<Set<String>> itrGL = groupList.listIterator();
		System.out.println("Total Number of group(s): " + groupList.size());
		while (itrGL.hasNext()) {
			System.out.println("Group " + (itrGL.nextIndex() + 1) + ": " + Arrays.toString(itrGL.next().toArray()));
		}
	}

	private static void mergeGroups(ArrayList<Set<String>> groupList,
			Set<Set<String>> matchGroupSet) {
		/* Merge groups by moving all entries from one to another
		 * and then delete the empty set
		 * @param: original list of groups, an index of groups that need to combine
		 */
		Iterator<Set<String>> itrMGIS = matchGroupSet.iterator();
		Set<String> combineSet = new HashSet<String>();
		//Copy set items to a single combined set
		while (itrMGIS.hasNext()) {			
			Set<String> matchGroup = itrMGIS.next();
			//Copy items to combined set
			combineSet.addAll(matchGroup);
			//Remove the group
			groupList.remove(matchGroup);
		}
		
		//Add the combined set to the final list of groups
		groupList.add(combineSet);
	}

	private static int findMatchingEmailGroup(String target, ArrayList<Set<String>> groupList) {
		/* Iterate through existing email groups to find a possible match
		 * @param: search target, existing email groups
		 * @return: index of matching group in a list (>=0)
		 * 			-1 if there is no match
		 */
		ListIterator<Set<String>> itrGL = groupList.listIterator();
		while (itrGL.hasNext()) {
			int currentIndex = itrGL.nextIndex();
			Set<String> group = itrGL.next();
			//Search for email entry in each group
			if (group.contains(target)) {
				//Return matching group index
				return currentIndex;
			}
		}	
		//No match
		return -1;
	}
}



