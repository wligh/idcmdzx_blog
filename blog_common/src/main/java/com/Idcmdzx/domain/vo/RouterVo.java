package com.Idcmdzx.domain.vo;

import com.Idcmdzx.domain.entity.Menu;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RouterVo {

    private List<MenuVo> menus;

}
