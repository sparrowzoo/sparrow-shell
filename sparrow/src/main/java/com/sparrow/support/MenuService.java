package com.sparrow.support;

import com.sparrow.protocol.BusinessException;
import com.sparrow.protocol.dto.SimpleItemDTO;

import java.util.List;

public interface MenuService {
    List<SimpleItemDTO> menu() throws BusinessException;
}
