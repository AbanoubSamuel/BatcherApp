package org.abg.batcher.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BatchFileUser {
    private Long id;
    private String name;
    private String email;
    private String birthDate;
}
