package com.seminar.easyCookWeb.pojo.information;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Bank {
    @Schema(description = "The code of the bank",
            example = "700", required = true)
    @Id
    @NotEmpty
    private String code;

    @Schema(description = "The name of the bank")
    @Column(columnDefinition = "nvarchar(256)")
    @NotEmpty
    private String name;

    @Schema(description = "The branch name of the bank")
    @Column(columnDefinition = "nvarchar(256)")
    private String branch;
}

