import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Firewall {

	TrieNode root = new TrieNode();
	
	/*
	 * We shall implement a trie data structure to store our 
	 * Direction -> Protocol -> Port Range -> IP Range
	 * endOfRule signifies end of the above mentioned combination
	 * HashMap will store a dictionary at each level for all the combinations below
	 */
	
	class TrieNode {
		
		HashMap<String, TrieNode> children = new HashMap<>();
		
		boolean endOfRule;
		
		TrieNode() {
			
			children = new HashMap<>();
			
			endOfRule = false;
		
		}
	
	}
	
	Firewall(String filename) throws IOException {
		
		//Get absolute path to the PWD
		Path currentRelativePath = Paths.get("");
		String currentAbsolutePath = currentRelativePath.toAbsolutePath().toString();
		String fileName = currentAbsolutePath+"/"+filename;
		File inputFile = new File(fileName);
		
		BufferedReader br = new BufferedReader(new FileReader(inputFile));
		
		//We iterate until the entire input is read
		
		String currentLine="";
		
		while(((currentLine = br.readLine()) != null)) {
			if(currentLine.length()!=0)
				processRule(currentLine);
			
		}
	
	}
	
	/*
	 * This processes each line of the file containing the rules
	 * For each part, we add a new node in the Trie
	 */
	private void processRule(String currentLine) {
		
		//Split the current line to process each segment separately
		
		String rule[] = currentLine.split(",");
		
		//Initially we take the root as head
		
		TrieNode temp = root;
		
		for(int i=0; i<4; i++) {
			
			//when we process the last part, we must mention that the enfOfRule = true
			if(i==3)
	
				temp = insertWord(rule[i], temp, true);
			
			else {
				
				temp = insertWord(rule[i], temp, false);
				
				//Still we use , as a separator in our trie
				
				temp = insertWord(",", temp, false); 
			
			}
		}
	}
	
	/*
	 * If the new word is not present as a Node under the current Trie Node,
	 * create a new Node for this, add this to the HashMap of the current TrieNode
	 * and return the newly created Node, or the previously existing Node
	 */
	
	public TrieNode insertWord(String word, TrieNode start, boolean isLast) {
		
        TrieNode parent = start;
        
        TrieNode cur = parent.children.get(word);
        
        if(cur==null) {
        
        	cur = new TrieNode();
        
        	parent.children.put(word, cur);
        
        }
        
        parent = cur;
    
	    if(isLast) parent.endOfRule = true;
	    
	    return parent;
    
	}

	/*
	 * This function checks whether the accepted parameters
	 * fall part of any of the rules mentioned in the list of rules
	 */
	boolean accept_packet(String direction, String protocol, int port, String ip_address) {
		
		TrieNode temp = root;
		
		//Search the direction
		
		temp = searchTrie(temp, direction);
		if(temp==null) return false;
		
		//This must be there so no need to check for null
		
		temp = searchTrie(temp, ",");
		
		//Search the protocol
		
		temp = searchTrie(temp, protocol);
		if(temp==null) return false;
		
		temp = searchTrie(temp, ",");
		
		//Now here we have an issue of range. We might have to check multiple combinations of rules
		//so we use a Queue to save the TrieNodes which pass the Port criteria
		
		Queue<TrieNode> queue = new LinkedList<>();
		
		for(String key:temp.children.keySet()) {
			
			//If its a single port, we can directly check it
			
			if(!key.contains("-")) {
				if(key.equals(String.valueOf(port))) 
					queue.add(temp.children.get(key));
			}
			
			//If it's a range, we must check in the range
			
			else {
				int startPort = Integer.parseInt(key.split("-")[0]);
				int endPort = Integer.parseInt(key.split("-")[1]);
				
				if(port>=startPort&&port<=endPort)
					queue.add(temp.children.get(key));
			}
			
		}
		
		//If queue comes out empty that means none of the ports matched
		
		if(queue.isEmpty()) return false;
		
		//For each correct Port entry, we must check the IP 
		
		while(!queue.isEmpty()) {
			
			//Poll each entry from queue and process it
			
			temp = queue.poll();
			temp = searchTrie(temp, ",");

			for(String key: temp.children.keySet()) {
				
				//If its a single IP, we can directly check it
				
				if(!key.contains("-")) {
					if(key.equals(ip_address))
						return true;
				}
				
				//If it's a range, we must check in the range
				
				else {
					if(isValidRange(key.split("-")[0], key.split("-")[1], ip_address))
						return true;
				}
			}
		}
		
		return false;
	}
	
	/*
	 * These 2 functions help to determine if the IP is in range 
	 */
	private static long ipToLong(InetAddress ip) {
		
		byte[] octets = ip.getAddress();
		long result = 0;
		
		for (byte octet : octets) {
		
			result <<= 8;
			
			result |= octet & 0xff;
		}
		
		return result;
	}

	private boolean isValidRange(String ipStart, String ipEnd, String ipToCheck) {
		
		try {
			long ipLo = ipToLong(InetAddress.getByName(ipStart));
			long ipHi = ipToLong(InetAddress.getByName(ipEnd));
			long ipToTest = ipToLong(InetAddress.getByName(ipToCheck));
			
			return (ipToTest >= ipLo && ipToTest <= ipHi);
		
		} catch (UnknownHostException e) {
			
			e.printStackTrace();
			
			return false;
		}
	}
	
	
	/*
	 * This will get the next child of the current Trie Node
	 */
	
	private TrieNode searchTrie(TrieNode temp, String searchString) {
		
		return temp.children.get(searchString);
		
	}

	public static void main(String[] args) throws IOException {
		
		if(args.length!=1) {
			System.out.println("Invalid number of arguments. Enter the csv file when you run the program!");
			return;
		}
		
		String filename = args[0];
		
		Firewall firewall = new Firewall(filename);
		
		boolean result = firewall.accept_packet("inbound", "udp", 24, "52.12.48.92");

		System.out.println(result);
	}

	
}
