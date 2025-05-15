package banking.p2p_transfer.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class PhoneDTO implements Serializable {
    private Long id;
    private String phone;
}