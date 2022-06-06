package com.sparrow.orm.type;

import com.sparrow.protocol.enums.PLATFORM;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author by harry
 */
public class PlatformTypeHandler implements TypeHandler<PLATFORM> {
    @Override public void setParameter(PreparedStatement ps, int i, PLATFORM parameter) throws SQLException {
        ps.setInt(i, parameter.getPlatform());
    }

    @Override public PLATFORM getResult(ResultSet rs, String columnName) throws SQLException {
        return PLATFORM.getByPlatform(rs.getInt(columnName));
    }

    @Override public PLATFORM getResult(ResultSet rs, int columnIndex) throws SQLException {
        return PLATFORM.getByPlatform(rs.getInt(columnIndex));
    }

    @Override public PLATFORM getResult(CallableStatement cs, int columnIndex) throws SQLException {
        return PLATFORM.getByPlatform(cs.getInt(columnIndex));
    }


}
