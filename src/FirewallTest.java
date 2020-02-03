import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.Test;

class FirewallTest {

	@Test
	void test() throws IOException {
		
		Firewall firewall = new Firewall("src/fw.csv");
		
		assertEquals(true, firewall.accept_packet("inbound", "udp", 53, "192.168.2.1")); 
		assertEquals(true, firewall.accept_packet("inbound", "tcp", 80, "192.168.1.2")); 
		assertEquals(true, firewall.accept_packet("outbound", "tcp", 10234, "192.168.10.11")); 
		assertEquals(false, firewall.accept_packet("inbound", "tcp", 81, "192.168.1.2")); 
		assertEquals(false, firewall.accept_packet("inbound", "udp", 24, "52.12.48.92"));
		assertEquals(false, firewall.accept_packet("outbound", "udp", 0, "52.12.48.92"));
		
	}

}
