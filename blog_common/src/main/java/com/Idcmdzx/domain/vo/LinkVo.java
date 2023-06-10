package com.Idcmdzx.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LinkVo {

    private Long id;

    private String name;

    private String logo;

    private String description;

    /**
     * 网站地址
     */
    private String address;

}
