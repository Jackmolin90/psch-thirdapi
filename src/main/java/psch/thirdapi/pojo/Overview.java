package psch.thirdapi.pojo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Overview {
    private Long blocknumber;
    private Long tx_count;
    private Long storage_count;
    private Long node_count;
    private Long addr_count;
    private BigDecimal total_storage;
    private BigDecimal total_rent;
    private BigDecimal total_pledge;
    private BigDecimal total_locked;
}
