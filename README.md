# FireWallAutomation

## Problem Statement

Real-world firewalls support both “allow” and “block” rules, and their ordering is important in
determining the fate of a packet. In this coding exercise, we will greatly simplify this model by
only supporting “allow” rules. If a packet does not match any “allow” rule, then we assume it
will be blocked.

<b>Input</b>

The provided input will be a CSV file in which each line contains exactly four columns: direction,
protocol, ports, and IP address:

*<b>direction</b>* :  Either “inbound” or “outbound”, corresponding to whether the
traffic is entering or leaving the machine.

*<b>protocol</b>* : Either “tcp” or “udp”, all lowercase – we will just implement two
of the most common protocols.

*<b>port</b>* :  Either (a) an integer in the range [1, 65535] or (b) a port range,
containing two integers in the range [1, 65535] separated by a
dash (no spaces).
Port ranges are inclusive, i.e. the port range “80-85” contains
ports 80 and 85. Given a port range, you may assume that the
range is well-formed i.e. the start of the range is strictly less than
the end.

*<b>IP address</b>* :  Either (a) an IPv4 address in dotted notation, consisting of 4
octets, each an integer in the range [0, 255], separated by periods
or (b) an IP range containing two IPv4 addresses, separated by a
dash (no spaces).
Like port ranges, IP ranges are inclusive. Given an IP range, you
may assume that the range is well-formed i.e. when viewed as a
number, the starting address is strictly less than the ending
address. 

<h3> Test Inputs </h3>
For example, the following are all valid inputs: <br></br>

inbound,tcp,80,192.168.1.2 <br>
outbound,tcp,10000-20000,192.168.10.11 <br>
inbound,udp,53,192.168.1.1-192.168.2.5 <br>
outbound,udp,1000-2000,52.12.48.92 

## Test the code

I've made a class for mentioning the test cases. It is called FireWallTest.java.
Here I have written all the test cases. So directly running this file will give the output of the test cases.
According to my code, the file must be kept in the same folder or any folder down from the current folder.
Mention that in the path to the file to run the src file FireWall.java

## Code Design

I have used Trie Data structure to store the rules. So we simply go down the Trie based on our search.

Trie : https://en.wikipedia.org/wiki/Trie

I've taken reference from https://gist.github.com/madan712/6651967. This is a great gist to check if an IP Address is in the range of IP Addresses mentioned in the rule.

The trie holds ranges wherever possible. If there is an assumption that the rules won't be updated often, but we need to check very fast that the IP is in range, we can have a separate tree for every port and not ranges like my structure.

## Future Improvements

This assigment was finished in just over 1.5 hours as per the mail instructions. Had I more time, I would find a way to better the IP Address range checker. I'd try to see if making a mask of the 
addresses is possible. I've also assumed that the test cases and rules mentioned in the files will all be valid inputs. Had I more times, I would write checks to catch invalid inputs. My code also has some redundancy in the Port level. There will be separate entries for suppose 50 and 0-100. It might be better to have some sort of Range Tree data structure to handle these cases. 

## Team Selection

Among all the teams, I'm really interested to join the <b>Platform Team</b> as it aligns with my skillset the most! I'd also like to join the <b>Data Team</b> as a second option.
