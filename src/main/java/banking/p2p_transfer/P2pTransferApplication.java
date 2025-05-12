package banking.p2p_transfer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class P2pTransferApplication {

	public static void main(String[] args) {
		SpringApplication.run(P2pTransferApplication.class, args);
	}

}
