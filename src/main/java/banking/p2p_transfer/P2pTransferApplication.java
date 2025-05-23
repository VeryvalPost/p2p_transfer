package banking.p2p_transfer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@EnableCaching
@EnableRetry
public class P2pTransferApplication {

	public static void main(String[] args) {
		SpringApplication.run(P2pTransferApplication.class, args);
	}

}
